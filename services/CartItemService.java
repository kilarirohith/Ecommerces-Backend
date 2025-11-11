package com.Kilari.services;

import com.Kilari.modal.CartItem;

public interface CartItemService {

    CartItem updateCartItem(Long userId,Long id,CartItem cartItem) throws Exception;
    void removeCartItem(Long userId,Long cartItemId) throws Exception;
    CartItem   findcartItemById(Long userId) throws Exception;

}
