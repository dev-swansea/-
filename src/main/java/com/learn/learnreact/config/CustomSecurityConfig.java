package com.learn.learnreact.config;

import com.learn.learnreact.security.filter.JWTCheckFilter;
import com.learn.learnreact.security.handler.APILoginFailHandler;
import com.learn.learnreact.security.handler.APILoginSuccessHandler;
import com.learn.learnreact.security.handler.CustomAccessDenieHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity // 메서드별 권한 체크를 위한 애너테이션
public class CustomSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    log.info("==============security config==============");

    http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
    http.csrf(csrf -> csrf.disable());

    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.formLogin(formLogin -> {
      formLogin.loginPage("/api/member/login");
      formLogin.successHandler(new APILoginSuccessHandler());
      formLogin.failureHandler(new APILoginFailHandler());
    });

    http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling(exception -> {
      exception.accessDeniedHandler(new CustomAccessDenieHandler());
    });

    return http.build();
  }


  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();

    corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
    corsConfiguration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
    corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    corsConfiguration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}

