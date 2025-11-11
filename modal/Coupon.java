package com.Kilari.modal;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    private  double discount;

    private LocalDate validateStartDate;

    private LocalDate  validityEndDate;

    private  double  minimumOrderValue;

    private  boolean isActive = true;

    @ManyToMany(mappedBy = "UsedCoupon")
    private Set<User> usedByUsers = new HashSet<>();
}


