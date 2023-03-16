package com.shopee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "carts")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserShopEntity user;

    @OneToMany(mappedBy = "cart")
    private List<CartDetailEntity> cartDetails;
}
