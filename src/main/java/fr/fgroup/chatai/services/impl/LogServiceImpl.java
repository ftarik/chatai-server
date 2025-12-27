package fr.fgroup.chatai.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fgroup.chatai.dao.services.LogDaoService;
import fr.fgroup.chatai.entities.LogEntity;
import fr.fgroup.chatai.resources.LogResource;
import fr.fgroup.chatai.services.LogService;
import fr.fgroup.chatai.utils.CryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class LogServiceImpl implements LogService {

  private final LogDaoService logDaoService;
  @Value("${encryption.key}")
  private String secretKey;

  public LogServiceImpl(LogDaoService logDaoService) {
    this.logDaoService = logDaoService;
  }

  @Override
  public ResponseEntity<Void> create(String data) {
    String encodedBase64Key = CryptUtil.encodeKey(secretKey);
    String json = CryptUtil.decrypt(data, encodedBase64Key);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      LogEntity logEntity = objectMapper.readValue(json, LogEntity.class);
      logDaoService.save(logEntity);
    } catch (JsonProcessingException e) {
      log.error("json processing failed : ", e);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> logs(LogResource resource) {
    log.info("Front logs: {}", resource);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
