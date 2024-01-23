package com.learn.learnreact.service;

import com.learn.learnreact.domain.Todo;
import com.learn.learnreact.dto.PageRequestDTO;
import com.learn.learnreact.dto.PageResponseDTO;
import com.learn.learnreact.dto.TodoDTO;
import com.learn.learnreact.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class TodoServiceImplTest {

  @Autowired
  private TodoService todoService;

  @Test
  @DisplayName("서비스 레지스터 테스트")
  public void testRegister() {
    TodoDTO todoDTO = TodoDTO.builder()
            .title("서비스 테스트")
            .writer("tester")
            .dueDate(LocalDate.of(2024, 1, 23))
            .build();

    Long tno = todoService.register(todoDTO);

    log.info("tno => {}", tno);
  }

  @Test
  @DisplayName("서비스 조회 테스트")
  public void testGet() {
    TodoDTO todoDTO = todoService.get(102L);
    assertEquals(todoDTO.getTno(), 102L);
    log.info("todoDTO 객체 => {}", todoDTO.getTno());
  }

  @Test
  @DisplayName("서비스 수정 테스트")
  public void testModify() {

    TodoDTO todoDTO = todoService.get(102L);
    todoDTO.setTitle("수정한다 제목");
    todoDTO.setDueDate(LocalDate.of(2024, 1, 25));
    todoDTO.setComplete(true);

    todoService.modify(todoDTO);

    log.info("수정 확인 => {}", todoDTO
            .getTitle());
  }

  @Test
  @DisplayName("서비스 삭제 테스트")
  public void testDelete() {
    todoService.remove(102L);
  }

  @Test
  @DisplayName("서비스 페이징 테스트")
  public void testList() {
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .page(2)
            .size(5)
            .build();

    PageResponseDTO<TodoDTO> list = todoService.list(pageRequestDTO);

    log.info("페이징 => {}", list);
  }
}