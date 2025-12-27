package fr.fgroup.chatai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.util.Objects;

/**
 * ChatAI Application - Main entry point for the ChatAI backend service.
 * 
 * This Spring Boot application provides a REST API gateway for interacting with OpenAI's GPT models.
 * It manages user tokens, handles conversations with AI, and maintains audit logs for all interactions.
 * 
 * Features:
 * - User key generation and token management
 * - OpenAI API integration for chat completions
 * - Request/response logging and auditing
 * - JWT-based authentication
 * - Multi-profile support (dev, staging, prod)
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 2.0.0-RELEASE
 * @since 2020-06-17
 */

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class ChatAIApplication {

  /**
   * Application entry point. Starts the Spring Boot application and logs startup information.
   * 
   * Displays the following information upon successful startup:
   * - Application name and active profile
   * - Local access URL
   * - External access URL (with IP address)
   * 
   * @param args Command line arguments passed to the application
   */
  public static void main(String[] args) {
    Environment environment =
            SpringApplication.run(ChatAIApplication.class, args).getEnvironment();

    String protocol = "http";
    if (environment.getProperty("server.ssl.key-store") != null) {
      protocol = "https";
    }
    String hostAddress = "localhost";
    try {
      hostAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (Exception e) {
      log.warn("The host name could not be determined, using `localhost` as fallback");
    }
    log.info("\n----------------------------------------------------------\n\t"
                    + "Application '{}' is running! Access URLs:\n\t"
                    + "Local: \t\t{}://localhost:{}\n\t"
                    + "External: \t{}://{}:{}\n\t"
                    + "Profile(s): \t{}\n----------------------------------------------------------",
            Objects.requireNonNull(environment.getProperty("spring.application.name")).toUpperCase(),
            protocol,
            environment.getProperty("server.port"),
            protocol,
            hostAddress,
            environment.getProperty("server.port"),
            environment.getActiveProfiles());
  }

}
