package com.learn.learnreact.repository;

import com.learn.learnreact.domain.CartItem;
import com.learn.learnreact.dto.CartItemListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  @Query("""
            select new com.learn.learnreact.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName)
            from CartItem ci inner join Cart mc on ci.cart = mc
            left join Product p on ci.product = p
            left join p.imageList pi
            where
            mc.owner.email = :email and pi.ord = 0
            and p.delFlag = false
            order by ci desc
          """)
  public List<CartItemListDTO> getItemsOfCartDTOByEmail(@Param("email") String email);

  @Query("""
            select ci from CartItem ci inner join Cart c on ci.cart = c
            where 
            c.owner.email = :email and ci.product.pno = :pno
          """)
  public CartItem getItemOfPno(@Param("email") String email, @Param("pno") Long pno);

  @Query("""
            select c.cno from  Cart c inner join CartItem ci on ci.cart = c
            where
            ci.cino = :cino
          """)
  public Long getCartFromItem(@Param("cino") Long cino);

  @Query("""
            select
            new com.learn.learnreact.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName)
            from
            CartItem  ci inner join Cart mc on ci.cart = mc
            left join Product p on ci.product = p
            left join p.imageList pi
            where 
            mc.cno = :cno and pi.ord =0
            and p.delFlag = false
            order by ci desc
          """)
  public List<CartItemListDTO> getItemsOfCartDTOByCart(@Param("cno") Long cno);
}
