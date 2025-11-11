package com.Kilari.services.impl;

import com.Kilari.modal.Cart;
import com.Kilari.modal.Coupon;
import com.Kilari.modal.User;
import com.Kilari.repository.CartRepository;
import com.Kilari.repository.CouponRepository;
import com.Kilari.repository.UserRepository;
import com.Kilari.services.CartService;
import com.Kilari.services.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;


    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
       Coupon coupon = couponRepository.findByCode(code);
       Cart cart=cartRepository.findByUserId(user.getId());

       if(coupon==null){
           throw new Exception("coupon not valid");
       }
       if(user.getUsedCoupon().contains(coupon)){
           throw new Exception("coupon already used");
       }
       if(orderValue <= coupon.getMinimumOrderValue()){
           throw new Exception("coupon order value must be greater than minimum order value"+coupon.getMinimumOrderValue());
       }
       if(coupon.isActive() && LocalDate.now().isAfter(coupon.getValidateStartDate()) && LocalDate.now().isBefore(coupon.getValidityEndDate())){
           user.getUsedCoupon().add(coupon);
           userRepository.save(user);

           double discountedPrice = (cart.getTotalsellingprice()*coupon.getDiscount())/100;
           cart.setTotalsellingprice(cart.getTotalsellingprice()-discountedPrice);
           cart.setCouponCode(code);
           cartRepository.save(cart);
           return cart;
       }
        throw new Exception("coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        if(coupon==null){
            throw new Exception("coupon not found...");
        }
        Cart cart=cartRepository.findByUserId(user.getId());
        double discountedPrice = (cart.getTotalsellingprice()*coupon.getDiscount())/100;
        cart.setTotalsellingprice(cart.getTotalsellingprice()+discountedPrice);
        cart.setCouponCode(null);

        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id).orElseThrow(()->
                new Exception("coupon not found"));
    }

    @Override
    @PreAuthorize("hasRole('Admin')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('Admin')")
    public void deleteCoupon(Long id) throws Exception {
        findCouponById(id);
        couponRepository.deleteById(id);
    }
}
