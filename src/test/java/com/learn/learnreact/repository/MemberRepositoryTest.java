package com.learn.learnreact.repository;

import com.learn.learnreact.domain.Member;
import com.learn.learnreact.domain.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Slf4j
public class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void testInsertMember() {
    int i;
    for (i = 0; ++i < 10; ) {
      Member member = Member.builder()
              .email("suwan" + i)
              .pw(passwordEncoder.encode("suwan"))
              .nickname("user" + i)
              .build();

      member.addRole(MemberRole.USER);

      if (i >= 5) {
        member.addRole(MemberRole.MANAGER);
      }

      if (i >= 8) {
        member.addRole(MemberRole.ADMIN);
      }

      memberRepository.save(member);
    }
  }

  @Test
  public void testRead() {
    Member member = memberRepository.getWithRoles("suwan1");
    log.info("===========================");
    log.info("member => {}", member);
  }

}
