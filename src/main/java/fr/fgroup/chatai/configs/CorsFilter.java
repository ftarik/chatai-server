package fr.fgroup.chatai.configs;

/**
 * @author Tarik FAMIL (contact: tarikfamil@gmail.com or tarik.famil@acensi.fr)
 * <p>
 * Copyright (C) ACENSI, Inc - All Rights Reserved Unauthorized copying of this file, via any medium
 * is strictly prohibited Proprietary and confidential
 * <p>
 * Created 12/03/2023
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CorsFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(CorsFilter.class);

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
          throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;

    HttpServletResponse resp = (HttpServletResponse) servletResponse;
    logger.info("[method:{}]-> Request from [{}] to [{}] by [{}]", request.getMethod(),
            request.getHeader("Origin"), request.getRequestURI(), request.getRemoteAddr());
    resp.addHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
    resp.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    resp.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    resp.addHeader("Access-Control-Allow-Credentials", "true");

    // Just ACCEPT and REPLY OK if OPTIONS
    if (request.getMethod().equals("OPTIONS")) {
      resp.setStatus(HttpServletResponse.SC_OK);
      return;
    }
    chain.doFilter(request, resp);
  }

}
