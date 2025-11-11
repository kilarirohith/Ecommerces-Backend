package com.Kilari.Controller;

import com.Kilari.modal.Product;
import com.Kilari.modal.User;
import com.Kilari.modal.Wishlist;

import com.Kilari.services.ProductService;
import com.Kilari.services.UserService;
import com.Kilari.services.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<Wishlist> getWishListByUserId(
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishListService.getWishListByUserId(user);

        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishList(@PathVariable Long productId,
                                                         @RequestHeader("Authorization")String jwt) throws Exception {
        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwt);
        Wishlist updatedWishlist = wishListService.addProductToWishList(user, product);
        return ResponseEntity.ok(updatedWishlist);

    }
}
