package com.Kilari.Controller;

import com.Kilari.modal.Cart;
import com.Kilari.modal.CartItem;
import com.Kilari.modal.Product;
import com.Kilari.modal.User;
import com.Kilari.request.AddItemRequest;
import com.Kilari.response.ApiResponse;
import com.Kilari.services.CartItemService;
import com.Kilari.services.CartService;
import com.Kilari.services.ProductService;
import com.Kilari.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private  final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String Jwt) throws Exception {
        User user = userService.findUserByJwtToken(Jwt);

        Cart cart = cartService.findUserCart(user);
        return new ResponseEntity<>(cart,org.springframework.http.HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem>addItemToCart(@RequestBody AddItemRequest req, @RequestHeader("Authorization")String jwt) throws Exception {
        User user  = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(req.getProductId());

        CartItem item = cartService.addCartItem(user,product, req.getSize(),req.getQuantity());

        ApiResponse res = new ApiResponse();
        res.setMessage("Item added to cart Successfully");

        return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(),cartItemId);

        ApiResponse res = new ApiResponse();
        res.setMessage("Item removed from cart Successfully");
        return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem>updateCartItemHandler(@PathVariable Long cartItemId, @RequestBody CartItem cartItem, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItem updatedCartItem = null;
        if(cartItem.getQuantity() > 0){
            updatedCartItem = cartItemService.updateCartItem(user.getId(),cartItemId,cartItem);
        }
        return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
    }
}
