package com.Kilari.repository;

import com.Kilari.modal.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryId(String CategoryId) ;
}
