package com.learn.learnreact.security;

import com.learn.learnreact.domain.Member;
import com.learn.learnreact.dto.MemberDTO;
import com.learn.learnreact.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("============findByUsername==============");
    Member member = memberRepository.getWithRoles(username);
    if (member == null) {
      throw new UsernameNotFoundException("없는 유저입니다.");
    }
    MemberDTO memberDTO = new MemberDTO(
            member.getEmail(),
            member.getPw(),
            member.getNickname(),
            member.isSocial(),
            member.getMemberRoleList().stream().map(role -> role.name()).collect(Collectors.toList()));

    log.info("member dto => {}", memberDTO);
    return memberDTO;
  }

}
