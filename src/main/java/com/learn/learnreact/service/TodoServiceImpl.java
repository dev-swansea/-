package com.learn.learnreact.service;

import com.learn.learnreact.domain.Todo;
import com.learn.learnreact.dto.PageRequestDTO;
import com.learn.learnreact.dto.PageResponseDTO;
import com.learn.learnreact.dto.TodoDTO;
import com.learn.learnreact.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

  private final ModelMapper modelMapper;
  private final TodoRepository todoRepository;

  @Override
  public Long register(TodoDTO todoDTO) {
    Todo todo = modelMapper.map(todoDTO, Todo.class);
    return todoRepository.save(todo).getTno();
  }

  @Override
  public TodoDTO get(Long tno) {
    Todo todo = todoRepository.findById(tno).orElseThrow();
    return modelMapper.map(todo, TodoDTO.class);
  }

  @Override
  public void modify(TodoDTO todoDTO) {
    Todo todo = todoRepository.findById(todoDTO.getTno()).orElseThrow();

    todo.setTitle(todoDTO.getTitle());
    todo.setDueDate(todoDTO.getDueDate());
    todo.setComplete(todoDTO.isComplete());

    todoRepository.save(todo);
  }

  @Override
  public void remove(Long tno) {
    todoRepository.deleteById(tno);
  }

  @Override
  public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
    // 1페이지가 0이므로 getPage() -1을 해준다.
    Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
            pageRequestDTO.getSize(),
            Sort.by("tno").descending());

    Page<Todo> result = todoRepository.findAll(pageable);
    List<TodoDTO> dtoList = result.getContent().stream()
            .map(todo -> modelMapper.map(todo, TodoDTO.class))
            .collect(Collectors.toList());

    long totalCnt = result.getTotalElements();

    return PageResponseDTO.<TodoDTO>withAll()
            .dtoList(dtoList)
            .pageRequestDTO(pageRequestDTO)
            .totalCnt((int) totalCnt)
            .build();
  }
}
