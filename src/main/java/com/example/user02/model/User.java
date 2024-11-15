package com.example.user02.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone")
    private Integer phone;

    @Column(name = "password", nullable = false)
    private String password;
//    // Trạng thái kích hoạt của tài khoản
//    @Column(name = "active")
//    private Boolean active = true;

    // Thiết lập quan hệ Many-to-Many với Role
    @ManyToMany(fetch = FetchType.EAGER) // Sử dụng EAGER để tự động tải danh sách roles
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
