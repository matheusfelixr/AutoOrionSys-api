package com.autoorion.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN, "FORBIDDEN");
    }
    public ForbiddenException() {
        super("VocÃª nÃ£o tem permissÃ£o para realizar esta aÃ§Ã£o.", HttpStatus.FORBIDDEN, "FORBIDDEN");
    }
}
