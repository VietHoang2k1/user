package com.example.user02.controller;
import com.example.user02.model.Role;
import com.example.user02.model.User;
import com.example.user02.repository.RoleRepository;
import com.example.user02.repository.UserRepository; // Thêm import cho UserRepository
import com.example.user02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Thêm khai báo UserRepository và Autowire vào
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        // Tìm vai trò USER trong cơ sở dữ liệu (hoặc tạo mới nếu không tồn tại)
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role USER không tồn tại"));

        // Thêm vai trò vào danh sách roles của người dùng
        user.setRoles(Collections.singletonList(userRole));

        userService.saveUser(user);
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/welcome")
    public String welcomePage(Authentication authentication, Model model) {
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("username", username);
        model.addAttribute("isAdmin", isAdmin);

        return "auth/welcome";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("identifier") String identifier, Model model) {
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(identifier, identifier);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("userId", user.getId());
            return "auth/reset-password";
        }

        model.addAttribute("error", "Không tìm thấy người dùng với thông tin đã cung cấp.");
        return "auth/forgot-password";
    }
    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    // Phương thức cập nhật thông tin người dùng
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User user, Authentication authentication) {
        // Mã hóa mật khẩu nếu người dùng thay đổi
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(userService.encodePassword(user.getPassword()));
        }
        userService.updateUser(user);
        return "redirect:/welcome";
    }
}
