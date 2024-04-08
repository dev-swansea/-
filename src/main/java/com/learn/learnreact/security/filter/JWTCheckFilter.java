package com.learn.learnreact.security.filter;

import com.google.gson.Gson;
import com.learn.learnreact.dto.MemberDTO;
import com.learn.learnreact.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();

    if (request.getMethod().equals("OPTIONS")) {
      return true;
    }

    if (path.startsWith("/api/member")) {
      return true;
    }

    if (path.startsWith("/api/products/view")) {
      return true;
    }

    return false;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    log.info("-----------------------JWTCheckFilter-----------------------");
    String authorization = request.getHeader("Authorization");
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

      UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.info("JWT Check Error..........");
      log.info("{}", e.getMessage());

      Gson gson = new Gson();
      String json = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

      response.setContentType("application/json");
      PrintWriter pw = response.getWriter();
      pw.println(json);
      pw.close();

    }
  }

}
