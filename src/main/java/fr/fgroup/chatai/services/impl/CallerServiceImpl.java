package fr.fgroup.chatai.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fgroup.chatai.dao.services.UserDaoService;
import fr.fgroup.chatai.dao.specifications.UserSpecifications;
import fr.fgroup.chatai.entities.UserEntity;
import fr.fgroup.chatai.exceptions.ExpectationFailedException;
import fr.fgroup.chatai.resources.KeyResource;
import fr.fgroup.chatai.resources.MessageResource;
import fr.fgroup.chatai.resources.post.ContinueMessageResourcePost;
import fr.fgroup.chatai.resources.post.MessageResourcePost;
import fr.fgroup.chatai.resources.RequestResponse;
import fr.fgroup.chatai.services.CallerService;
import fr.fgroup.chatai.utils.HasherUtil;
import fr.fgroup.chatai.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * CallerServiceImpl - Core service for handling ChatAI requests to OpenAI's GPT API.
 * 
 * This service is responsible for:
 * - Generating unique user keys for API access
 * - Managing user token quotas and consumption
 * - Forwarding chat requests to OpenAI's API
 * - Handling single-turn and multi-turn conversations
 * - URL encoding/decoding for request/response data
 * 
 * The service maintains token tracking to enforce user quotas and prevent abuse.
 * Each user has a maximum token limit (totalTokensAuthorized) and current token count.
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 1.0
 * @since 2023-03-05
 */
@Slf4j
@Service
public class CallerServiceImpl implements CallerService {

  /** OpenAI API key for authentication */
  private final String apiKey;
  
  /** OpenAI API base URL for making requests */
  private final String openaiBaseUrl;
  
  /** Data access service for UserEntity operations */
  private final UserDaoService userDaoService;

  /**
   * Constructor for dependency injection.
   * 
   * @param apiKey OpenAI API key (injected from application properties)
   * @param openaiBaseUrl OpenAI API base URL (injected from application properties)
   * @param userDaoService DAO service for user operations
   */
  public CallerServiceImpl(@Value("${openai.api-key}") String apiKey,
                           @Value("${openai.url}") String openaiBaseUrl,
                           UserDaoService userDaoService) {
    this.apiKey = apiKey;
    this.openaiBaseUrl = openaiBaseUrl;
    this.userDaoService = userDaoService;
  }

  /**
   * Generates a unique user key for API access.
   * 
   * If the user already has a key, it returns the existing key.
   * If the user is new, creates a new UserEntity with default token quota (500 tokens)
   * and generates a SHA-256 hashed key for that user.
   * 
   * @return ResponseEntity containing the generated/existing key
   * @throws ExpectationFailedException if key generation fails
   */
  @Override
  public ResponseEntity<KeyResource> generateKey() {
    log.debug("Start generating a new key");
    UserEntity userEntity = null;
    try {
      if (UserContextHolder.getContext().getKey() != null) {
        userEntity = userDaoService.findOne(Specification.where(UserSpecifications.withKey(UserContextHolder.getContext().getKey())));
      }
    } catch (Exception e) {
      log.error("No user found with key : {}", UserContextHolder.getContext().getKey());
    }
    if (userEntity == null) {
      userEntity = new UserEntity();
      userEntity.setTotalTokensAuthorized(500L);
      userEntity.setTotalTokens(0L);
      userEntity = userDaoService.save(userEntity);
      try {
        String key = HasherUtil.getHashedKey(userEntity.getId());
        log.info("Key generated : {}", key);

        userEntity.setKey(key);
        userDaoService.save(userEntity);
        return new ResponseEntity<>(new KeyResource(key), HttpStatus.OK);
      } catch (NoSuchAlgorithmException e) {
        throw new ExpectationFailedException("Failed to generate a key : {}", e);
      }
    }
    return new ResponseEntity<>(new KeyResource(userEntity.getKey()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<MessageResource> askRequest(MessageResourcePost message) {
    // Retrieve user entity to check token quota
    log.debug("Get user with key : {}", UserContextHolder.getContext().getKey());
    UserEntity userEntity;
    try {
      userEntity = userDaoService.findOne(Specification.where(
              UserSpecifications.withKey(UserContextHolder.getContext().getKey())));
      // Check if user has exceeded token quota
      if (userEntity.getTotalTokens() > userEntity.getTotalTokensAuthorized()) {
        log.warn("User token quota exceeded. Current: {}, Authorized: {}", 
                userEntity.getTotalTokens(), userEntity.getTotalTokensAuthorized());
        return new ResponseEntity<>(HttpStatus.CONFLICT);
      }
    } catch (Exception e) {
      log.error("No user found with key : {}", UserContextHolder.getContext().getKey());
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    // Prepare OpenAI API request
    String model = "gpt-3.5-turbo";
    String role = "user";
    String content = message.getContent();
    // URL encode content to handle special characters
    content = URLEncoder.encode(content, StandardCharsets.UTF_8);

    String body = String.format("{\"model\": \"%s\",\"messages\": [{\"role\": \"%s\", \"content\": \"%s\"}]}",
            model, role, content);

    // Configure HTTP client with timeouts
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    MediaType mediaType = MediaType.parse("application/json");

    RequestBody requestBody = RequestBody.Companion.create(body, mediaType);

    // Build and send request to OpenAI
    Request request = new Request.Builder()
            .url(openaiBaseUrl + "/chat/completions")
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + apiKey)
            .build();

    String responseBody = null;
    try {
      Response response = client.newCall(request).execute();
      if (response.body() != null && response.code() == 200) {
        responseBody = Objects.requireNonNull(response.body()).string();
        log.debug(responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        RequestResponse requestResponse = objectMapper.readValue(responseBody, RequestResponse.class);
        // Update user token consumption
        if (requestResponse != null && requestResponse.getUsage() != null) {
          userEntity.setTotalTokens(userEntity.getTotalTokens() + requestResponse.getUsage().getTotalTokens());
        } else {
          log.error("requestResponse or requestResponse.usage is null. billing is at risk");
        }
        // Extract and return the assistant's response
        if (requestResponse != null && requestResponse.getChoices() != null
                && !requestResponse.getChoices().isEmpty()) {
          MessageResource messageResponse = new MessageResource(
                  requestResponse.getChoices().get(0).getMessage().getRole(),
                  requestResponse.getChoices().get(0).getMessage().getContent());
          messageResponse.setContent(URLDecoder.decode(messageResponse.getContent()));
          return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } else {
          log.error("Error while getting data from requestResponse : {}", requestResponse);
        }
      } else {
        log.error("Error while communicating with chat gpt code : {}, responseBody : {}", response.code(), response);
      }
    } catch (JsonProcessingException e) {
      log.error("Error while parsing data to object : {}", responseBody, e);
    } catch (IOException e) {
      throw new ExpectationFailedException("Something went wrong while communicating with Openai", e);
    }
    return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
  }

  /**
   * Handles multi-turn conversations with OpenAI.
   * 
   * This method allows continuing a conversation by sending the full conversation history
   * (including previous user and assistant messages). The OpenAI API uses this context
   * to provide more coherent and contextual responses.
   * 
   * @param messages List of messages representing the conversation history
   *                 (includes both user and assistant messages)
   * @return ResponseEntity containing the assistant's response or appropriate HTTP status
   * @throws ExpectationFailedException if communication with OpenAI fails
   */
  @Override
  public ResponseEntity<MessageResource> continueConversation(List<ContinueMessageResourcePost> messages) {
    // Retrieve user entity to check token quota
    log.debug("Get user with key : {}", UserContextHolder.getContext().getKey());
    UserEntity userEntity;
    try {
      userEntity = userDaoService.findOne(Specification.where(
              UserSpecifications.withKey(UserContextHolder.getContext().getKey())));
      // Check if user has exceeded token quota
      if (userEntity.getTotalTokens() > userEntity.getTotalTokensAuthorized()) {
        log.warn("User token quota exceeded. Current: {}, Authorized: {}", 
                userEntity.getTotalTokens(), userEntity.getTotalTokensAuthorized());
        return new ResponseEntity<>(HttpStatus.CONFLICT);
      }
    } catch (Exception e) {
      log.error("No user found with key : {}", UserContextHolder.getContext().getKey());
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    String model = "gpt-3.5-turbo";

    // Build JSON body with conversation history
    StringBuilder body = new StringBuilder(String.format("{\"model\": \"%s\",\"messages\": [", model));
    for (int i = 0; i < messages.size(); i++) {
      String content = messages.get(i).getContent();
      // URL encode each message content
      content = URLEncoder.encode(content, StandardCharsets.UTF_8);
      body.append(String.format("{\"role\": \"%s\", \"content\": \"%s\"}" + (i < messages.size() - 1 ? "," : ""), messages.get(i).getRole(), content));
    }
    body.append("]}");

    // Configure HTTP client with timeouts
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    MediaType mediaType = MediaType.parse("application/json");

    RequestBody requestBody = RequestBody.Companion.create(body.toString(), mediaType);

    // Build and send request to OpenAI
    Request request = new Request.Builder()
            .url(openaiBaseUrl + "/chat/completions")
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + apiKey)
            .build();

    String responseBody = null;
    try {
      Response response = client.newCall(request).execute();
      if (response.body() != null && response.code() == 200) {
        responseBody = Objects.requireNonNull(response.body()).string();
        log.debug(responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        RequestResponse requestResponse = objectMapper.readValue(responseBody, RequestResponse.class);
        if (requestResponse != null && requestResponse.getUsage() != null) {
          userEntity.setTotalTokens(userEntity.getTotalTokens() + requestResponse.getUsage().getTotalTokens());
        } else {
          log.error("requestResponse or requestResponse.usage is null. billing is at risk");
        }
        if (requestResponse != null && requestResponse.getChoices() != null
                && !requestResponse.getChoices().isEmpty()) {
          MessageResource messageResponse = new MessageResource(
                  requestResponse.getChoices().get(0).getMessage().getRole(),
                  requestResponse.getChoices().get(0).getMessage().getContent());
          messageResponse.setContent(URLDecoder.decode(messageResponse.getContent()));
          return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } else {
          log.error("Error while getting data from requestResponse : {}", requestResponse);
        }
      } else {
        log.error("Error while communicating with chat gpt code : {}, responseBody : {}", response.code(), response);
      }
    } catch (JsonProcessingException e) {
      log.error("Error while parsing data to object : {}", responseBody, e);
    } catch (IOException e) {
      throw new ExpectationFailedException("Something went wrong while communicating with Openai", e);
    }
    return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
  }
}
