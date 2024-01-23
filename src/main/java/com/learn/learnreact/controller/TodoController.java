package com.learn.learnreact.controller;

import com.learn.learnreact.dto.PageRequestDTO;
import com.learn.learnreact.dto.PageResponseDTO;
import com.learn.learnreact.dto.TodoDTO;
import com.learn.learnreact.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/todo")
public class TodoController {

  private final TodoService todoService;

  @GetMapping("/{tno}")
  public TodoDTO get(@PathVariable(name = "tno") Long tno) {
    return todoService.get(tno);
  }

  @GetMapping("/list")
  public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
    log.info("page request dto => {}", pageRequestDTO);
    return todoService.list(pageRequestDTO);
  }

  @PostMapping("/register")
  public Map<String, Long> register(@RequestBody TodoDTO todoDTO) {
    log.info("TodoDTO => {}", todoDTO);
    Long tno = todoService.register(todoDTO);
    return Map.of("tno", tno);
  }

  @PutMapping("/{tno}")
  public Map<String, String> modify(@PathVariable("tno") Long tno, @RequestBody TodoDTO todoDTO) {
    todoDTO.setTno(tno);
    log.info("Modify => {}", todoDTO.getTno());

    todoService.modify(todoDTO);
    return Map.of("Result", "SUCCESS");
  }

  @DeleteMapping("/{tno}")
  public Map<String, String> remove(@PathVariable("tno") Long tno) {
    log.info("tno => {}", tno);
    todoService.remove(tno);
    return Map.of("Result", "Success");
  }
}
