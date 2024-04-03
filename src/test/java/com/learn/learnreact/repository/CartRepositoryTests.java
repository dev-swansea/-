package com.learn.learnreact.repository;

import com.learn.learnreact.domain.Cart;
import com.learn.learnreact.domain.CartItem;
import com.learn.learnreact.domain.Member;
import com.learn.learnreact.domain.Product;
import com.learn.learnreact.dto.CartItemListDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class CartRepositoryTests {

  @Autowired
  CartRepository cartRepository;

  @Autowired
  CartItemRepository cartItemRepository;

  @Transactional
  @Commit
  @Test
  public void testInsertByProduct() {
    log.info("test1-----------------");

    // 사용자가 전송하는 정보
    String email = "suwan1";
    Long pno = 3L;
    int qty = 2;

    // 만일 기존에 사용자의 장바구니 아이템이 있었다면
    CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

    if (cartItem != null) {
      cartItem.changeQty(qty); // ?? 더하지 않는다?
      cartItemRepository.save(cartItem);

      return;
    }

    // 장바구니에 아이템이 없었다면 장바구니로부터 확인 필요
    // 사용자가 장바구니를 만든적이 있는지 확인 => 이게 왜 필요한 작업일까?
    Optional<Cart> result = cartRepository.getCartOfMember(email);
    Cart cart = null;

    // 사용자의 장바구니가 존재하지 않으면 장바구니 생성
    if (result.isEmpty()) {
      log.info("Member cart is not exist!!");
      Member member = Member.builder().email(email).build();
      Cart tempCart = Cart.builder().owner(member).build();

      cart = cartRepository.save(tempCart);
    } else {
      cart = result.get();
    }

    log.info("cart => {}", cart);

    if (cartItem == null) {
      Product product = Product.builder().pno(pno).build();
      cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
    }

    // 상품 아이템 저장
    cartItemRepository.save(cartItem);
  }

  @Test
  @Commit
  public void testUpdateByCino() {
    Long cino = 1L;
    int qty = 4;
    CartItem cartItem = cartItemRepository.findById(cino).orElseThrow();
    cartItem.changeQty(qty);

    cartItemRepository.save(cartItem);
  }

  @Test
  @DisplayName("장바구니 목록 확인")
  public void testListOfMember() {
    String email = "suwan1";

    List<CartItemListDTO> itemsOfCartDTOByEmail = cartItemRepository.getItemsOfCartDTOByEmail(email);

    log.info("아이템 목록 => {}", itemsOfCartDTOByEmail);
  }

  @Test
  @DisplayName("삭제 후 다시 반환")
  public void testDeleteThenList() {
    Long cino = 3L;

    // 장바구니 번호
    Long cno = cartItemRepository.getCartFromItem(3L);
    log.info("삭제 전 장바구니 => {}", cartItemRepository.getItemsOfCartDTOByCart(cno));
    cartItemRepository.deleteById(cino);
    log.info("삭제 후 장바구니 => {}", cartItemRepository.getItemsOfCartDTOByCart(cno));
  }

}
