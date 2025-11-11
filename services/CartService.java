package com.Kilari.services;

import com.Kilari.modal.Cart;
import com.Kilari.modal.CartItem;
import com.Kilari.modal.Product;
import com.Kilari.modal.User;

public interface CartService {
    public CartItem addCartItem(User user, Product product,String data,int quantity);

    public Cart findUserCart(User user);
}
