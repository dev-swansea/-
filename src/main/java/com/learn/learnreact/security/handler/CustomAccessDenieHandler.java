package com.learn.learnreact.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CustomAccessDenieHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

    Gson gson = new Gson();

    String json = gson.toJson(Map.of("error", "ERROR_ACCESSDEFIND"));

    response.setContentType("application/json");
    PrintWriter pw = response.getWriter();
    pw.println(json);
    pw.close();
  }
}
