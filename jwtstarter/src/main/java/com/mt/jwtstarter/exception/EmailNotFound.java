package com.mt.jwtstarter.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailNotFound extends AuthenticationException {
    public EmailNotFound(String msg) {
        super(msg);
    }
}
