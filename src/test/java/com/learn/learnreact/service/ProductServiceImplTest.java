package com.learn.learnreact.service;

import com.learn.learnreact.dto.PageRequestDTO;
import com.learn.learnreact.dto.PageResponseDTO;
import com.learn.learnreact.dto.ProductDTO;
import com.learn.learnreact.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProductServiceImplTest {

  @Autowired
  ProductService productService;

  @Test
  public void testList() {

    // page:1 size:10 => 기본값
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

    PageResponseDTO<ProductDTO> result = productService.getList(pageRequestDTO);

    result.getDtoList().forEach(dto -> log.info("dto => {}", dto));
  }

  @Test
  public void testRegister() {

    ProductDTO productDTO = ProductDTO.builder()
            .pdesc("서비스 테스트")
            .price(1000)
            .pname("Service test")
            .build();

    productDTO.setUploadFileNames(
            List.of(UUID.randomUUID().toString().substring(0, 8) + "_" + "Test1.jpg", UUID.randomUUID().toString().substring(0, 8) + "_" + "Test2.jpg")
    );

    productService.register(productDTO);
  }

  @Test
  public void testRead() {

    ProductDTO productDTO = productService.get(1L);
    log.info("productDTO => {}", productDTO);
    log.info("imageNames => {}", productDTO.getUploadFileNames());

  }
}