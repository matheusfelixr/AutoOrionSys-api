package com.autoorion.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
    public UnauthorizedException() {
        super("Não autenticado ou sessão expirada.", HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
}
