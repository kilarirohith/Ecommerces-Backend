package com.Kilari.repository;

import com.Kilari.modal.Product;
import com.Kilari.modal.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long product);

}
