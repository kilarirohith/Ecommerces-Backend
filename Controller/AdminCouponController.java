package com.Kilari.Controller;

import com.Kilari.modal.Cart;
import com.Kilari.modal.Coupon;
import com.Kilari.modal.User;
import com.Kilari.services.CartService;
import com.Kilari.services.CouponService;
import com.Kilari.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class AdminCouponController {

    private final CouponService couponService;
    private final UserService userService;
    private final CartService cartService;

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(@RequestParam String apply,
                                            @RequestParam double orderValue,
                                            @RequestParam String code,
                                            @RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart;

        if(apply.equals("true")){
            cart = couponService.applyCoupon(code, orderValue, user);
        }else{
            cart = couponService.removeCoupon(code, user);
        }

        return ResponseEntity.ok(cart);
    }

    @PostMapping("admin/create")
    public  ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon){
        Coupon couponCreated = couponService.createCoupon(coupon);
        return ResponseEntity.ok(couponCreated);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?>deleteCoupon(@PathVariable Long id) throws Exception {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon deleted successfully");
    }
}
