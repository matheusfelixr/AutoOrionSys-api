package com.autoorion.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN, "FORBIDDEN");
    }
    public ForbiddenException() {
        super("Você não tem permissão para realizar esta ação.", HttpStatus.FORBIDDEN, "FORBIDDEN");
    }
}
