package com.example.user02.config;

import org.springframework.security.authentication.LockedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public String handleLockedAccount(LockedException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "login";  // Chuyển về trang login với thông báo lỗi cụ thể
    }
}
