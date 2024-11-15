package com.example.user02.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().and() // Nếu bạn cần bật CSRF, hãy xóa dòng này
                .authorizeHttpRequests()
                .requestMatchers("/users/**").hasRole("ADMIN") // Chỉ cho phép ADMIN truy cập vào /users/**
                .requestMatchers("/register", "/login", "/forgot-password", "/reset-password").permitAll() // Cho phép truy cập mà không cần xác thực
                .anyRequest().authenticated() // Các yêu cầu khác phải được xác thực
                .and()
                .formLogin()
                .loginPage("/login") // Trang đăng nhập tùy chỉnh
                .defaultSuccessUrl("/welcome", true) // Trang chuyển đến sau khi đăng nhập thành công
                .failureUrl("/login?error=true") // Trang khi đăng nhập thất bại
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // Đường dẫn đăng xuất
                .logoutSuccessUrl("/login?logout=true") // Trang chuyển đến sau khi đăng xuất thành công
                .invalidateHttpSession(true) // Hủy phiên sau khi đăng xuất
                .deleteCookies("JSESSIONID"); // Xóa cookie phiên

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Mã hóa mật khẩu với BCrypt
    }
}
