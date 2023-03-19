package com.shopee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopee.enumerations.RoleUser;
import com.shopee.enumerations.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "user_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private RoleUser role;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant lastLogin;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    @JsonBackReference
    private List<ProductRateEntity> rates;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<SaleOrderEntity> saleOrders;


    @PrePersist
    public void prePersist() {
        if (this.role == null)
            this.role = RoleUser.Member;
        if (this.status == null)
            this.status = UserStatus.NOT_ACTIVE;
        if (this.createdAt == null)
            this.createdAt = Instant.now();
        this.password = new BCryptPasswordEncoder().encode(this.password);
    }
}
