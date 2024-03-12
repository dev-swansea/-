package com.learn.learnreact.service;

import com.learn.learnreact.dto.PageRequestDTO;
import com.learn.learnreact.dto.PageResponseDTO;
import com.learn.learnreact.dto.ProductDTO;

public interface ProductService {
  PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

  Long register(ProductDTO productDTO);

  ProductDTO get(Long pno);

  void modify(ProductDTO productDTO);

  void delete(Long pno);
}
