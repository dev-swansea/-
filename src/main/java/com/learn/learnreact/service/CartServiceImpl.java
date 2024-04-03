package com.learn.learnreact.service;

import com.learn.learnreact.domain.Cart;
import com.learn.learnreact.domain.CartItem;
import com.learn.learnreact.domain.Member;
import com.learn.learnreact.domain.Product;
import com.learn.learnreact.dto.CartItemDTO;
import com.learn.learnreact.dto.CartItemListDTO;
import com.learn.learnreact.repository.CartItemRepository;
import com.learn.learnreact.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  @Override
  public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {
    String email = cartItemDTO.getEmail();
    Long cino = cartItemDTO.getCino();
    Long pno = cartItemDTO.getPno();
    int qty = cartItemDTO.getQty();

    if (cino != null) { // 장바구니에 이미 아이템이 있어서 수량만 변경하는 경우
      CartItem cartItemResult = cartItemRepository.findById(cino).orElseThrow();
      cartItemResult.changeQty(qty);
      cartItemRepository.save(cartItemResult);

      return getCartItem(email);
    }

    // 장바구니 아이템 번호 cino가 없는 경우
    // 사용자의 카트
    Cart cart = getCart(email);
    CartItem cartItem = null;

    // 이미 동일한 상품이 담긴적이 있을 수 있으므로
    cartItem = cartItemRepository.getItemOfPno(email, pno);
    if (cartItem == null) {
      Product product = Product.builder().pno(pno).build();
      cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
    } else {
      cartItem.changeQty(qty);
    }

    // 상품 아이템 저장
    cartItemRepository.save(cartItem);

    return getCartItem(email);
  }

  private Cart getCart(String email) {
    Cart cart = null;
    Optional<Cart> result = cartRepository.getCartOfMember(email);

    if (result.isEmpty()) {
      log.info("Cart of the member is not exist!! ");

      Member member = Member.builder().email(email).build();
      Cart tempCart = Cart.builder().owner(member).build();
      cart = cartRepository.save(tempCart);
    } else {
      cart = result.get();
    }

    return cart;
  }

  @Override
  public List<CartItemListDTO> getCartItem(String email) {
    return cartItemRepository.getItemsOfCartDTOByEmail(email);
  }

  @Override
  public List<CartItemListDTO> remove(Long cino) {
    Long cno = cartItemRepository.getCartFromItem(cino);
    log.info("cart no => {}", cno);

    cartItemRepository.deleteById(cino);

    return cartItemRepository.getItemsOfCartDTOByCart(cno);
  }
}
