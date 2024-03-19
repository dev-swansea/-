package com.learn.learnreact.security.handler;

import com.google.gson.Gson;
import com.learn.learnreact.dto.MemberDTO;
import com.learn.learnreact.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class APILoginSuccessHandler implements ServerAuthenticationSuccessHandler {
  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    log.info("===========================");
    log.info("Authentication Handler");
    log.info("===========================");

    MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();

    Map<String, Object> claims = memberDTO.getClaims();

    String accessToken = JWTUtil.generateToken(claims, 60 * 24);
    String refreshToken = JWTUtil.generateToken(claims, 60 * 24);

    claims.put("AccessToken", accessToken);
    claims.put("RefreshToken", refreshToken);

    Gson gson = new Gson();

    String json = gson.toJson(claims);

    ServerWebExchange exchange = webFilterExchange.getExchange();
    exchange.getResponse()
            .getHeaders()
            .set(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");

    return webFilterExchange.getExchange()
            .getResponse()
            .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(json.getBytes())));
  }

}
