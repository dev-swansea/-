package com.learn.learnreact.service;

import com.learn.learnreact.dto.CartItemDTO;
import com.learn.learnreact.dto.CartItemListDTO;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public interface CartService {

  // 장바구니 아이템 추가 혹은 변경
  List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

  // 모든 장바구니 아이템 목록
  List<CartItemListDTO> getCartItem(String email);

  // 아이템 삭제
  List<CartItemListDTO> remove(Long cino);

}
