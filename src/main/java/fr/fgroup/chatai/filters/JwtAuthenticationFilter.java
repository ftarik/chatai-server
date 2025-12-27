package fr.fgroup.chatai.filters;

import fr.fgroup.chatai.enums.AuthorityEnum;
import fr.fgroup.chatai.utils.SecurityUtils;
import fr.fgroup.chatai.utils.UserContext;
import fr.fgroup.chatai.utils.UserContextHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.impl.DefaultJwtParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JwtAuthenticationFilter - Filter for JWT-based authentication validation.
 * 
 * This filter is executed once per request and performs the following:
 * - Extracts the Authorization header from the request
 * - Creates a user context with the provided key
 * - Sets up Spring Security authentication
 * - Clears the context after request processing
 * 
 * The filter allows the application to use simple token-based authentication
 * without full JWT parsing complexity. The token is passed as the Authorization header
 * and stored in UserContextHolder for access throughout the request lifecycle.
 * 
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com)
 * @version 1.0
 * @since 2020-06-17
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  /**
   * Performs the JWT authentication filter logic for each request.
   * 
   * This method:
   * 1. Extracts the Authorization header
   * 2. Creates a UserContext with the provided key
   * 3. Stores the context in thread-local storage
   * 4. Creates a Spring Security authentication token
   * 5. Proceeds with the filter chain
   * 6. Clears the context after processing (in finally block)
   * 
   * @param httpServletRequest the HTTP request
   * @param httpServletResponse the HTTP response
   * @param filterChain the filter chain to continue processing
   * @throws ServletException if a servlet error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  FilterChain filterChain)
          throws ServletException, IOException {

    log.info("Processing authentication for request '{}'", httpServletRequest.getRequestURL());
    String key = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

    if (key != null) {
      log.debug("Set key of user in context holder : {}", key);

      // Create user context with the provided key
      UserContext userContext = new UserContext();
      userContext.setKey(key);
      UserContextHolder.setContext(userContext);

      // Create Spring Security authentication token
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
              key, null, null);

      authentication
              .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    try {
      filterChain.doFilter(httpServletRequest, httpServletResponse);
    } finally {
      // Clean up thread-local context to prevent memory leaks
      UserContextHolder.clear();
    }
  }

}
