package com.learn.learnreact.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class APILoginFailHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    log.info("Login fail => {}", exception);
    System.out.println(exception);

    Gson gson = new Gson();
    String json = gson.toJson(Map.of("error", "LOGIN_ERROR"));

    response.setContentType("application/json");

    PrintWriter pw = response.getWriter();
    pw.println(json);
    pw.close();

  }

}
