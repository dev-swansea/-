package com.learn.learnreact.repository;

import com.learn.learnreact.domain.Product;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@Slf4j
public class ProductRepositoryTest {

  @Autowired
  ProductRepository productRepository;

  @Test
  public void testInsert() {
    for (int i = 0; i < 10; i++) {
      Product product = Product.builder()
              .pname("Product name " + i)
              .pdesc("Product description " + i)
              .price(100 * i)
              .build();
      // 2개의 이미지 파일 추가
      product.addImageString(UUID.randomUUID().toString() + "_" + "IMAGE1.jpg");
      product.addImageString(UUID.randomUUID().toString() + "_" + "IMAGE2.jpg");

      productRepository.save(product);
    }
  }

  @Test
  @Transactional
  public void testRead() {

    Product product = productRepository.findById(1L).orElseThrow();

    log.info("product => {}", product);
    log.info("product image list => {}", product.getImageList());
  }

  @Test
  public void testRead2() {
    Product product = productRepository.selectOne(1L).orElseThrow();

    log.info("product => {}", product);
    log.info("product image list => {}", product.getImageList());
  }

  @Test
  @Transactional
  @Commit
  public void testDelete() {
    productRepository.updateToDelete(2L, true);
  }

  @Test
  public void testUpdate() {
    Product product = productRepository.selectOne(10L).orElseThrow();

    product.changeName("10번 상품 수정");
    product.changeDesc("10번 상품을 수정하였습니다.");
    product.changePrice(5000);

    log.info("기존 이미지 => {}", product.getImageList());
    // 첨부파일 수정
    product.clearList();

    product.addImageString(UUID.randomUUID().toString().substring(0, 8) + "_" + "NEW IMAGE1.jpg");
    product.addImageString(UUID.randomUUID().toString().substring(0, 8) + "_" + "NEW IMAGE2.jpg");
    product.addImageString(UUID.randomUUID().toString().substring(0, 8) + "_" + "NEW IMAGE3.jpg");
    log.info("변경된 이미지 => {}", product.getImageList());

    productRepository.save(product);
  }

  @Test
  public void testList() {

    Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());
    Page<Object[]> objects = productRepository.selectList(pageable);

    objects.getContent().forEach(e -> log.info("select list => {}", Arrays.toString(e)));
  }

}