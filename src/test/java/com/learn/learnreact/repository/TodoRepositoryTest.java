package com.learn.learnreact.repository;

import com.learn.learnreact.domain.Todo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
public class TodoRepositoryTest {

  @Autowired
  private TodoRepository todoRepository;

  @Test
  public void test1(){
    log.info("========================");
    log.info("todo repository => {}",todoRepository);
  }

  @Test
  @DisplayName("JPA Insert test")
  public void testInsert(){
    for (int i = 0; i < 100; i++) {
      Todo todo = Todo.builder()
              .title("title ... " + i)
              .dueDate(LocalDate.of(2024, 01, 21))
              .writer("user0" + i)
              .build();
      todoRepository.save(todo);
    }
  }

  @Test
  @DisplayName("Jpa findById test")
  public void testFindById(){
    // 존재하는 번호로 확인
    Long id= 22L;
    Todo todo = todoRepository.findById(id).orElseThrow();
    log.info("찾았다 22번 => {}", todo.getTno());
  }

  @Test
  @DisplayName("Jpa 수정 테스트")
  public void testModify(){
    // 우선 변경할 엔티티를 id를 이용해 찾는다.
    // 필요한 내용을 변경한다. 여기선 title, complete, dueDate
    Long id = 22L;
    Todo todo22 = todoRepository.findById(id).orElseThrow();
    todo22.setTitle("수정된 22번");
    todo22.setComplete(true);
    todo22.setDueDate(LocalDate.of(2024,1,25));

    todoRepository.save(todo22);

    log.info("수정된 22번 => {}", todo22.toString());
  }

  @Test
  @DisplayName("JPA 삭제 테스트")
  public void testDelete(){
    todoRepository.deleteById(23L);
    Assertions.assertEquals(todoRepository.findAll().size(), 98);
  }

  @Test
  @DisplayName("페이징 처리")
  public void testPaging(){
    // org.springframework.data.domain.Pageable
    Pageable pageable = PageRequest.of(0,10, Sort.by("tno").descending());

   Page<Todo> result = todoRepository.findAll(pageable);

    log.info("페이징 => {}", result.getTotalElements());
    result.getContent().stream().forEach(e -> log.info("content => {}",e));
  }

}
