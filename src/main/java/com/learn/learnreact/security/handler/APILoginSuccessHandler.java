package com.learn.learnreact.security.handler;

import com.google.gson.Gson;
import com.learn.learnreact.dto.MemberDTO;
import com.learn.learnreact.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    log.info("===========================");
    log.info("Authentication Handler");
    log.info("===========================");

    MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();

    Map<String, Object> claims = memberDTO.getClaims();

    //String accessToken = JWTUtil.generateToken(claims, 1);
    String accessToken = JWTUtil.generateToken(claims, 60 * 24);
    String refreshToken = JWTUtil.generateToken(claims, 60 * 24);

    claims.put("accessToken", accessToken);
    claims.put("refreshToken", refreshToken);

    Gson gson = new Gson();

    String json = gson.toJson(claims);

    response.setContentType("application/json");

    PrintWriter pw = response.getWriter();
    pw.println(json);
    pw.close();
  }


}
