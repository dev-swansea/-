package com.learn.learnreact.controller;

import com.learn.learnreact.dto.PageRequestDTO;
import com.learn.learnreact.dto.PageResponseDTO;
import com.learn.learnreact.dto.ProductDTO;
import com.learn.learnreact.service.ProductService;
import com.learn.learnreact.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;
  private final CustomFileUtil customFileUtil;


  @PostMapping
  public Map<String, Long> register(ProductDTO productDTO) {
    List<MultipartFile> fileList = productDTO.getFiles();
    List<String> uploadFileNames = customFileUtil.saveFiles(fileList);
    productDTO.setUploadFileNames(uploadFileNames);

    Long pno = productService.register(productDTO);
    return Map.of("result", pno);
  }

  @GetMapping("/view/{fileName}")
  public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {
    return customFileUtil.getFile(fileName);
  }

  @GetMapping("/list")
  public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
    log.info("pageRequest param .............. => {}", pageRequestDTO);

    return productService.getList(pageRequestDTO);
  }

  //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  @GetMapping("/{pno}")
  public ProductDTO read(@PathVariable("pno") Long pno) {
    return productService.get(pno);
  }

  @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, path = ("/{pno}"))
  public Map<String, String> modify(ProductDTO productDTO, @PathVariable("pno") Long pno) {
    productDTO.setPno(pno);
    ProductDTO originalProduct = productService.get(pno);

    // 기존의 파일들(db에 존재하는 파일들 - 수정 과정에서 삭제되었을 수 있음) ??
    List<String> originalFileNames = originalProduct.getUploadFileNames();

    // 새로 업로드 해야 하는 파일들
    List<MultipartFile> files = productDTO.getFiles();

    // 새로 업로드되어서 만들어진 파일 이름들
    List<String> updateFileNames = customFileUtil.saveFiles(files);

    // 화면에서 변화 없이 계속 유지된 파일들 => 수정되지 않은 파일들을 말하는건가?
    List<String> uploadFileNames = productDTO.getUploadFileNames();

    // 유지되는 파일들 + 새로 업로드된 파일 이름들이 저장해야 하는 파일 목록이 된다.
    if (!updateFileNames.isEmpty()) uploadFileNames.addAll(updateFileNames);

    // 수정 작업
    productService.modify(productDTO);
    if (!originalFileNames.isEmpty()) {
      // 지워야 하는 파일 목록 찾기 => filter 사용
      // 예전 파일들 중에서 지워져야 하는 파일이름들, filter로 새로 업로드되는 파일 이름이 기존의 파일 이름에 존재하지 않는 것들을 가져와 리스트로 만든다.
      List<String> removeFiles = originalFileNames.stream().filter(fileName -> updateFileNames.indexOf(fileName) == -1).collect(Collectors.toList());

      // 실제 파일 삭제
      customFileUtil.deleteFile(removeFiles);
    }
    return Map.of("RESULT", "SUCCESS");
  }

  @DeleteMapping("/{pno}")
  public Map<String, String> remove(@PathVariable("pno") Long pno) {
    // 삭제되어야할 파일 알아내기
    List<String> uploadFileNames = productService.get(pno).getUploadFileNames();

    productService.delete(pno);
    customFileUtil.deleteFile(uploadFileNames);

    return Map.of("RESULT", "SUCCESS");
  }

}
