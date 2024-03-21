package com.learn.learnreact.controller;

import com.learn.learnreact.util.CustomJWTException;
import com.learn.learnreact.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class APIRefreshController {

  @RequestMapping("/api/member/refresh")
  public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {
    System.out.println(refreshToken);
    if (refreshToken == null) {
      throw new CustomJWTException("NULL_REFRESH");
    }

    if (authHeader == null || authHeader.length() < 7) {
      throw new CustomJWTException("INVALID_STRING");
    }

    String accessToken = authHeader.substring(7);

    // Access Token이 만료되지 않았다면
    if (checkExpiredToken(accessToken) == false) {
      return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    // RefreshToken 검증
    Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
    log.info("APIRefreshController refresh claims ... {}", claims);

    String newAccessToken = JWTUtil.generateToken(claims, 10);
    String newRefreshToken = checkTime((Integer) claims.get("exp")) ? JWTUtil.generateToken(claims, 60 * 24) : refreshToken;

    return Map.of("accessToken", newAccessToken, "refreshToken", refreshToken);
  }

  // refresh token이 1시간 미만이면
  private boolean checkTime(Integer exp) {
    // JWT exp를 날짜로 변환
    Date expDate = new Date((long) exp * 1000);
    // 현재 시간과의 차이 계산 - 밀리세컨즈
    long gap = expDate.getTime() - System.currentTimeMillis();
    // 분단위 계산
    long leftMin = gap / (1000 * 60);
    // 1시간도 안남았는지 확인
    return leftMin < 60;
  }

  private boolean checkExpiredToken(String accessToken) {
    try {
      JWTUtil.validateToken(accessToken);
    } catch (CustomJWTException e) {
      if (e.getMessage().equals("Expired")) {
        return true;
      }
    }
    return false;
  }

}
