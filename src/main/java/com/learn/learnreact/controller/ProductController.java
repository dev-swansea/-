package com.learn.learnreact.controller;

import com.learn.learnreact.dto.ProductDTO;
import com.learn.learnreact.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products")
public class ProductController {

  private final CustomFileUtil customFileUtil;

  @GetMapping
  public void test(String name) {
    System.out.println(name);
  }

  @PostMapping
  public Map<String, String> register(ProductDTO productDTO) {
    log.info("register => {}", productDTO);
    List<FilePart> fileList = productDTO.getFiles();
    List<String> uploadFileNames = customFileUtil.saveFiles(fileList);
    productDTO.setUploadFileNames(uploadFileNames);
    log.info("upload file name => {}", uploadFileNames);
    return Map.of("result", "success");
  }

}
