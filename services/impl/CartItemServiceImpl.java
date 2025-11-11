package com.Kilari.services.impl;

import com.Kilari.modal.CartItem;
import com.Kilari.modal.User;
import com.Kilari.repository.CartItemRepository;
import com.Kilari.services.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;


    @Override
    public CartItem updateCartItem(Long userId,Long CartItemId, CartItem newItem) throws Exception {
       CartItem item = findcartItemById(CartItemId);

        User cartItemUser = item.getCart().getUser();
        if(cartItemUser.getId().equals(userId)){
            item.setQuantity(newItem.getQuantity());
            item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
            return cartItemRepository.save(item);
        }
        throw new Exception("You Can't Udate this cartItem");

    }


    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
   CartItem item = findcartItemById(cartItemId);
   User cartItemUser = item.getCart().getUser();

   if(cartItemUser.getId().equals(userId)){
       cartItemRepository.delete(item);
   }else {


       throw new Exception("you an't delete this Item");
   }
    }

    @Override
    public CartItem findcartItemById(Long id) throws Exception {
        return cartItemRepository.findById(id).orElseThrow(() ->
                new Exception("Cart Item not Found with Id" + id));
    }
}
