package com.Kilari.modal;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private  User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CartItem> cartItems = new HashSet<>();

    private double totalsellingprice;

    @Min(value = 0, message = "Total items cannot be negative")
    private int totalItem;

    @Min(value = 0, message = "Total MRP items cannot be negative")
    private int totalMrpItems;

    @Min(value = 0, message = "Discount cannot be negative")
    private int discount;

    private String couponCode;

}
