package com.example.user02.exception;

import org.springframework.security.core.AuthenticationException;

public class UserLockedException extends AuthenticationException {
    public UserLockedException(String message) {
        super(message);
    }
}
