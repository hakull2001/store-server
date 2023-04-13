package com.shopee.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "order_items", uniqueConstraints = @UniqueConstraint(columnNames = {"clothes_id", "sale_order_id"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clothes_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "sale_order_id")
    @JsonBackReference
    private SaleOrderEntity saleOrder;

    @Column(nullable = false)
    private Integer quantity;

    @Column
    private String color;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}