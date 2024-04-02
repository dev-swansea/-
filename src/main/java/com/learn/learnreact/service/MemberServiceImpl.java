package com.learn.learnreact.service;

import com.learn.learnreact.domain.Member;
import com.learn.learnreact.domain.MemberRole;
import com.learn.learnreact.dto.MemberDTO;
import com.learn.learnreact.dto.MemberModifyDTO;
import com.learn.learnreact.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void modifyMember(MemberModifyDTO memberModifyDTO) {
    Member member = memberRepository.findById(memberModifyDTO.getEmail()).orElseThrow();
    member.changeNickname(memberModifyDTO.getNickname());
    member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));
    member.changeSocial(false);

    memberRepository.save(member);
  }

  @Override
  public MemberDTO getKakaoMember(String accessToken) {
    String email = getEmailFromKakaoAccessToken(accessToken);
    log.info("email from kakao => {}", email);

    Optional<Member> member = memberRepository.findById(email);

    // 기존 회원
    if (member.isPresent()) {
      MemberDTO memberDTO = entityToDTO(member.get());
      return memberDTO;
    }

    // 회원이 아닐때, 닉네임은 '소셜회원'으로, 패스워드는 임의로
    Member socialMember = makeSocialMember(email);
    memberRepository.save(socialMember);

    MemberDTO memberDTO = entityToDTO(socialMember);

    return memberDTO;
  }

  private String getEmailFromKakaoAccessToken(String accessToken) {
    final String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

    if (accessToken == null) {
      throw new RuntimeException("Access Token is null");
    }

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Content-Type", "application/x-www-form-urlencoded");
    HttpEntity<String> entity = new HttpEntity<>(headers);

    UriComponents uriBuilder = UriComponentsBuilder
            .fromHttpUrl(kakaoGetUserURL).build();

    ResponseEntity<LinkedHashMap> response =
            restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);

    log.info("get kakao member => {}", response);

    LinkedHashMap<String, LinkedHashMap> body = response.getBody();

    log.info("==========================================");
    log.info("get body map => {}", body);

    LinkedHashMap<String, String> kakaoAccount = body.get("kakao_account");

    log.info("kakao account: {}", kakaoAccount);

    return kakaoAccount.get("email");
  }

  private String makeTempPassword() {
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < 10; i++) {
      sb.append((char) ((int) (Math.random() * 55) + 65));
    }

    return sb.toString();
  }

  private Member makeSocialMember(String email) {
    String tempPw = makeTempPassword();
    log.info("temp password => {}", tempPw);

    String nickname = "소셜회원";

    Member member = Member.builder()
            .email(email)
            .pw(passwordEncoder.encode(tempPw))
            .nickname(nickname)
            .social(true)
            .build();

    member.addRole(MemberRole.USER);

    return member;
  }

}
