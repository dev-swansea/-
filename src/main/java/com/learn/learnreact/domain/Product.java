package com.learn.learnreact.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 상품을 의미한다.
 * 일반 엔티티와 비슷하지만 ProductImage의 목록을 가지고 이를 관리하는 기능이 있게 작성한다.
 */
@Entity
@Table(name = "tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pno;
  private int price;
  private String pname;
  private String pdesc;
  private boolean delFlag;

  @ElementCollection
  @Builder.Default
  private List<ProductImage> imageList = new ArrayList();

  public void changePrice(int price) {
    this.price = price;
  }

  public void changeDesc(String pdesc) {
    this.pdesc = pdesc;
  }

  public void changeName(String pname) {
    this.pname = pname;
  }

  public void addImage(ProductImage image) {
    image.setOrd(this.imageList.size());
    imageList.add(image);
  }

  public void addImageString(String fileName) {
    ProductImage productImage = ProductImage.builder()
            .fileName(fileName)
            .build();
    addImage(productImage);
  }

  public void changeDel(boolean delFlag) {
    this.delFlag = delFlag;
  }

  public void clearList() {
    imageList.clear();
  }
}
