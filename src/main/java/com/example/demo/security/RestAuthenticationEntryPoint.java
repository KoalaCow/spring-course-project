package com.example.demo.security;

import com.example.demo.constants.ErrorMessageConstants;
import com.example.demo.web.model.ErrorDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorDetails errorDetails = new ErrorDetails(ErrorMessageConstants.USER_UNAUTHORIZED);
        httpServletResponse.getOutputStream().println(new ObjectMapper().writeValueAsString(errorDetails));
    }
}
