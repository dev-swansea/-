package com.learn.learnreact.service;

import com.learn.learnreact.dto.TodoDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class TodoServiceImpl implements TodoService{
  @Override
  public Long register(TodoDTO todoDTO) {
    log.info("....");
    return null;
  }
}
