package com.learn.learnreact.service;

import com.learn.learnreact.domain.Product;
import com.learn.learnreact.domain.ProductImage;
import com.learn.learnreact.dto.PageRequestDTO;
import com.learn.learnreact.dto.PageResponseDTO;
import com.learn.learnreact.dto.ProductDTO;
import com.learn.learnreact.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Override
  public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

    log.info("getList -------------------------");

    Pageable pageable = PageRequest.of(
            pageRequestDTO.getPage() - 1,
            pageRequestDTO.getSize(),
            Sort.by("pno").descending());

    // Page<Object[]> 타입의 결과 데이터를 가진다.
    Page<Object[]> objects = productRepository.selectList(pageable);

    // 반복처리로 Product와 ProductImage를 ProductDTO로 만든다.
    List<ProductDTO> dtoList = objects.get().map(arr -> {
      Product product = (Product) arr[0];
      ProductImage productImage = (ProductImage) arr[1];

      ProductDTO productDTO = ProductDTO.builder()
              .pno(product.getPno())
              .pname(product.getPname())
              .price(product.getPrice())
              .pdesc(product.getPdesc())
              .build();

      String fileName = productImage.getFileName();
      productDTO.setUploadFileNames(List.of(fileName));

      return productDTO;
    }).collect(Collectors.toList());

    long totalCount = objects.getTotalElements();

    return PageResponseDTO.<ProductDTO>withAll()
            .dtoList(dtoList)
            .totalCnt((int) totalCount)
            .pageRequestDTO(pageRequestDTO)
            .build();
  }

  @Override
  public Long register(ProductDTO productDTO) {

    Product product = dtoToEntity(productDTO);
    Product result = productRepository.save(product);

    return result.getPno();
  }

  public Product dtoToEntity(ProductDTO productDTO) {
    Product product = Product.builder()
            .pno(productDTO.getPno())
            .pname(productDTO.getPname())
            .pdesc(productDTO.getPdesc())
            .price(productDTO.getPrice())
            .build();

    // 업로드 처리가 끝난 파일들의 이름 리스트
    List<String> uploadFileNames = productDTO.getUploadFileNames();

    if (uploadFileNames.isEmpty()) {
      return product;
    }

    uploadFileNames.stream().forEach(uploadName -> {
      product.addImageString(uploadName);
    });

    return product;
  }

  @Override
  public ProductDTO get(Long pno) {
    Product product = productRepository.selectOne(pno).orElseThrow();
    return entityToDTO(product);
  }

  private ProductDTO entityToDTO(Product product) {
    ProductDTO productDTO = ProductDTO.builder()
            .pno(product.getPno())
            .pname(product.getPname())
            .pdesc(product.getPdesc())
            .price(product.getPrice())
            .build();

    List<ProductImage> imageList = product.getImageList();
    if (imageList.isEmpty() || imageList.size() == 0) {
      return productDTO;
    }

    List<String> fileNameList = imageList.stream().map(productImage -> productImage.getFileName()).toList();

    productDTO.setUploadFileNames(fileNameList);

    return productDTO;
  }

  @Override
  public void modify(ProductDTO productDTO) {

    // step1 read
    Product product = productRepository.selectOne(productDTO.getPno()).orElseThrow();

    // change field
    product.changeName(productDTO.getPname());
    product.changeDesc(productDTO.getPdesc());
    product.changePrice(productDTO.getPrice());

    // upload file - - clear first
    product.clearList();

    List<String> uploadFileNames = productDTO.getUploadFileNames();
    if (!uploadFileNames.isEmpty()) {
      uploadFileNames.stream().forEach(fileName -> {
        product.addImageString(fileName);
      });
    }

    productRepository.save(product);
  }

  @Override
  public void delete(Long pno) {
    productRepository.updateToDelete(pno, true);
  }
}
