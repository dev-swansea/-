package com.learn.learnreact.dto;

import lombok.*;

import java.util.*;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;


/**
 * 상품의 이름이나 설명 등과 같은 문자열과 함께 여러 개의 첨부파일을 의미하는
 * multipartFile의 리스트를 가지도록 설계한다.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

  private Long pno;
  private String pname;
  private String pdesc;
  private int price;
  // 삭제는 안한다. 이유는 외래키가 많이 걸릴텐데, 상품이 삭제되면 그거에 관한 구매 내역 관리에 있어 문제가 생긴다고 한다.
  private boolean delFlag;

  @Builder.Default // null 체크를 쉽게 하기 위해?
  private List<MultipartFile> files = new ArrayList<>();
  @Builder.Default
  private List<String> uploadFileNames = new ArrayList<>();

}
