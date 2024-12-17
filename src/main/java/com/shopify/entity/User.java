package com.shopify.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends AbstractAuditingEntity<Long> {

    public User(String userName, UserRole role) {
        this.userName = userName;
        this.role = role;
    }

    public User(String userName, String password, UserRole role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public enum UserRole {
        MANAGER,
        USER
    }
}
