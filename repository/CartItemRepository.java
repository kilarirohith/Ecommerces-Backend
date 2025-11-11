package com.Kilari.repository;

import com.Kilari.modal.Cart;
import com.Kilari.modal.CartItem;
import com.Kilari.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository  extends JpaRepository<CartItem,Long> {

    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
