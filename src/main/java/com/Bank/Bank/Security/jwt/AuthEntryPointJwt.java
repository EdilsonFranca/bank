package com.Bank.Bank.Security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		       logger.error("error Não autorizado: {}", authException.getMessage());

			   Map<String, Object> map = new HashMap<>();
               map.put("error", "Email ou Senha invalida");

               response.setContentType("application/json");
               response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			 try {
                 ObjectMapper mapper = new ObjectMapper();
                 mapper.writeValue(response.getOutputStream(), map);
              } catch (Exception e) {
                  throw new ServletException();
              }
	}

}
