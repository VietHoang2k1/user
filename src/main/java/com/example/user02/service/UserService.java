package com.example.user02.service;

import com.example.user02.model.Role;
import com.example.user02.model.User;
import com.example.user02.repository.RoleRepository;
import com.example.user02.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // Mã hóa mật khẩu trước khi lưu
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    // Phương thức để lấy người dùng theo ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
    @PostConstruct
    public void initRoles() {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_USER", null));
        }
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_ADMIN", null));
        }
    }

    // Tạo tài khoản admin duy nhất nếu chưa tồn tại
    @PostConstruct
    public void initAdmin() {
        Optional<User> admin = userRepository.findByUsername("Hoang");

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN", null)));

        if (admin.isEmpty()) {
            User user = new User();
            user.setUsername("Hoang");
            user.setPassword(passwordEncoder.encode("27102001"));
            user.setRoles(Collections.singletonList(adminRole));
            userRepository.save(user);
        } else {
            User existingAdmin = admin.get();
            if (!passwordEncoder.matches("27102001", existingAdmin.getPassword())) {
                existingAdmin.setPassword(passwordEncoder.encode("27102001"));
            }
            if (!existingAdmin.getRoles().contains(adminRole)) {
                existingAdmin.getRoles().add(adminRole);
            }
            userRepository.save(existingAdmin);
        }
    }

    // Xóa người dùng theo ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
