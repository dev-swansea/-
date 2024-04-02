package com.learn.learnreact.service;

import com.learn.learnreact.domain.Member;
import com.learn.learnreact.dto.MemberDTO;
import com.learn.learnreact.dto.MemberModifyDTO;
import jakarta.transaction.Transactional;

import java.util.stream.Collectors;

@Transactional
public interface MemberService {

  void modifyMember(MemberModifyDTO memberModifyDTO);

  MemberDTO getKakaoMember(String accessToken);

  default MemberDTO entityToDTO(Member member) {
    MemberDTO memberDTO = new MemberDTO(
            member.getEmail(),
            member.getPw(),
            member.getNickname(),
            member.isSocial(),
            member.getMemberRoleList().stream().map(role -> role.name()).collect(Collectors.toList()));

    return memberDTO;
  }
}
