package fr.fgroup.chatai.configs;

import fr.fgroup.chatai.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * SecurityConfig - Spring Security configuration for JWT-based authentication.
 * 
 * This configuration class sets up:
 * - JWT token validation through JwtAuthenticationFilter
 * - CSRF protection (disabled for stateless API)
 * - Session management policy (stateless for REST API)
 * - Authorization rules for endpoints
 * - HTTP firewall configuration
 * 
 * All requests to `/chatai/**` endpoints are permitted without authentication,
 * while all other endpoints require valid authentication.
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 1.0
 * @since 2020-06-17
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  /** JWT authentication filter for validating user tokens */
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * Constructor for dependency injection.
   * 
   * @param jwtAuthenticationFilter the JWT filter to validate tokens
   */
  public SecurityConfig(
          JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  /**
   * Configures HTTP security settings for the application.
   * 
   * - CSRF protection is disabled (API doesn't require CSRF tokens)
   * - Session creation policy is set to IF_REQUIRED (stateless)
   * - Public endpoints are configured to permit all
   * - JWT filter is added before UsernamePasswordAuthenticationFilter
   * 
   * @param http the HttpSecurity to modify
   * @throws Exception if there's an error during configuration
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/chatai/**", "/chatai/requests/**", "/chatai/requests", "/chatai/log").permitAll()
            .anyRequest().authenticated();

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }

  /**
   * Configures the HTTP firewall to allow encoded slashes in URLs.
   * 
   * This is necessary for handling URLs that might contain encoded double slashes.
   * 
   * @return the configured HttpFirewall bean
   */
  @Bean
  public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowUrlEncodedDoubleSlash(true);
    return firewall;
  }

}
