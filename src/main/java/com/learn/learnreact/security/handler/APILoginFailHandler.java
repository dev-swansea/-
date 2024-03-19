package com.learn.learnreact.security.handler;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
public class APILoginFailHandler implements ServerAuthenticationFailureHandler {

  @Override
  public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {

    log.info("Login fail => {}", exception);
    System.out.println(exception);

    Gson gson = new Gson();
    String json = gson.toJson(Map.of("error", "LOGIN_ERROR"));

    ServerWebExchange exchange = webFilterExchange.getExchange();
    exchange.getResponse()
            .getHeaders()
            .set(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");

    return webFilterExchange.getExchange()
            .getResponse()
            .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(json.getBytes())));
  }
}
