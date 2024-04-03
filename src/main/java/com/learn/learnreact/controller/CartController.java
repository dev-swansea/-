package com.learn.learnreact.controller;

import com.learn.learnreact.dto.CartItemDTO;
import com.learn.learnreact.dto.CartItemListDTO;
import com.learn.learnreact.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/cart")
public class CartController {

  private final CartService cartService;

  @PreAuthorize("#itemDTO.email == authentication.name")
  @PostMapping("/change")
  public List<CartItemListDTO> changeCart(@RequestBody CartItemDTO itemDTO) {
    log.info("item DTO => {}", itemDTO);

    if (itemDTO.getQty() <= 0) {
      return cartService.remove(itemDTO.getCino());
    }

    return cartService.addOrModify(itemDTO);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER')")
  @GetMapping("/items")
  public List<CartItemListDTO> getCartItems(Principal principal) {
    String email = principal.getName();
    log.info("=================================");
    log.info("email => {}", email);

    return cartService.getCartItem(email);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER')")
  @DeleteMapping("/{cino}")
  public List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino) {
    log.info("cart item no => {}", cino);

    return cartService.remove(cino);
  }

}
