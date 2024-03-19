package com.learn.learnreact.security.filter;

import com.google.gson.Gson;
import com.learn.learnreact.dto.MemberDTO;
import com.learn.learnreact.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTCheckFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();

    // Preflight는 체크하지 않음
/*    if (request.getMethod().equals("OPTIONS")) {
      return chain.filter(exchange);
    }

    // api/member 체크하지 않음
    String path = request.getURI().getPath();
    if (path.startsWith("/api/member")) {
      return chain.filter(exchange);
    }

    // 이미지 조회 경로는 체크하지 않음
    if (path.startsWith("/api/products/view/")) {
      return chain.filter(exchange);
    }*/

    log.info("-----------------------JWTCheckFilter-----------------------");
    String authorization = request.getHeaders().getFirst("Authorization");

    try {
      String accessToken = authorization.substring(7);
      Map<String, Object> claims = JWTUtil.validateToken(accessToken);
      log.info("JWT Claims => {}", claims);

      String email = claims.get("email").toString();
      String pw = claims.get("pw").toString();
      String nickname = claims.get("nickname").toString();
      Boolean social = (Boolean) claims.get("social");
      List<String> roleNames = (List<String>) claims.get("roleNames");

      MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(), roleNames);

      log.info("==============================");
      log.info("member dto => {}", memberDTO);
      log.info("getAuthorities() => {}", memberDTO.getAuthorities()); // getAuthorities는 User 클래스에 있음,

      UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());

      return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));

      //SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    } catch (Exception e) {
      log.info("JWT Check Error..........");
      log.info("{}", e.getMessage());

      Gson gson = new Gson();
      String json = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

      exchange.getResponse()
              .getHeaders()
              .set(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");

      return exchange
              .getResponse()
              .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(json.getBytes())));
    }

    //return chain.filter(exchange);
  }
}
