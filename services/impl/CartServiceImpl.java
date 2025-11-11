package com.Kilari.services.impl;

import com.Kilari.modal.Cart;
import com.Kilari.modal.CartItem;
import com.Kilari.modal.Product;
import com.Kilari.modal.User;
import com.Kilari.repository.CartItemRepository;
import com.Kilari.repository.CartRepository;
import com.Kilari.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);
        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product,size);
        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);
            

            int totalPrice = quantity*product.getSellingPrice();
            cartItem.setSellingprice(totalPrice);
            cartItem.setMrpPrice(quantity*product.getMrpPrice());
            cart.getCartItems().add(cartItem);


            return cartItemRepository.save(cartItem);
        }

        return isPresent;

    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        int totalPrice = 0;
        int totalDiscountPrice = 0;
        int totalItem = 0;

        for(CartItem cartItem : cart.getCartItems()) {
            totalPrice +=cartItem.getMrpPrice();
            totalDiscountPrice  += cartItem.getSellingprice();
            totalItem +=cartItem.getQuantity();
        }

        cart.setTotalMrpItems(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalsellingprice(totalDiscountPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountPrice));
        cart.setTotalItem(totalItem);
        return cart;
    }

    private int calculateDiscountPercentage(Integer mrprice, Integer sellingprice) {

        if (mrprice <= 0) {
//            throw new IllegalArgumentException("mrpprice must be greater than 0");
            return 0;
        }
        double discount = mrprice - sellingprice;
        double discountPercentage = (discount / mrprice) * 100;
        return (int) discountPercentage;
    }
}
