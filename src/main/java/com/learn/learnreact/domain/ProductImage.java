package com.learn.learnreact.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * 순서(ord)라는 속성을 가지도록 만든다.
 * 이것은 나중에 목록에서 각 이미지마다 번호를 지정하고 상품 목록을 출력할 때 ord 값이 0번인 이미지들만
 * 화면에서 볼 수 있도록 하기 위함이다.(대표 이미지 출력)
 */
@Embeddable // 이건 해당 클래스의 인스턴스가 값 타입 객체임을 명시한다. => 뭔소리지
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

  private String fileName;
  private int ord;

  public void setOrd(int ord) {
    this.ord = ord;
  }
}
