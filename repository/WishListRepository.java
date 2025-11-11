package com.Kilari.repository;

import com.Kilari.modal.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<Wishlist,Long> {

    Wishlist findByUserId(Long UserId);
}
