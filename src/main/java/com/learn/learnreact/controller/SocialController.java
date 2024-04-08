package com.learn.learnreact.controller;

import com.learn.learnreact.dto.MemberDTO;
import com.learn.learnreact.dto.MemberModifyDTO;
import com.learn.learnreact.service.MemberService;
import com.learn.learnreact.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SocialController {

  private final MemberService memberService;

  @GetMapping("/api/member/kakao")
  public Map<String, Object> getMemberFromKakao(String accessToken) {
    log.info("access Token ");
    log.info("{}", accessToken);

    MemberDTO MemberDTO = memberService.getKakaoMember(accessToken);

    Map<String, Object> claims = MemberDTO.getClaims();

    log.info("social member claims => {}", claims);

    String jwtAccessToken = JWTUtil.generateToken(claims, 10);
    String jwtRefreshToken = JWTUtil.generateToken(claims, 60 * 24);

    claims.put("accessToken", jwtAccessToken);
    claims.put("refreshToken", jwtRefreshToken);

    return claims;
  }

  @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, path = "/api/member/modify")
  public Map<String, String> modify(@RequestBody MemberModifyDTO memberModifyDTO) {
    memberService.modifyMember(memberModifyDTO);
    return Map.of("result: ", "modified");
  }
}
