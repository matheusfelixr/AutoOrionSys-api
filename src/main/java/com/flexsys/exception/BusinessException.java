package com.flexsys.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * Exceção de regra de negócio.
 * Lançada pelos services quando uma regra é violada.
 * Resulta em HTTP 422 (Unprocessable Entity) por padrão.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String code;
    private final String field; // campo específico que causou o erro (opcional)

    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.UNPROCESSABLE_ENTITY;
        this.code = "BUSINESS_ERROR";
        this.field = null;
    }

    public BusinessException(String message, String field) {
        super(message);
        this.status = HttpStatus.UNPROCESSABLE_ENTITY;
        this.code = "BUSINESS_ERROR";
        this.field = field;
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.code = "BUSINESS_ERROR";
        this.field = null;
    }

    public BusinessException(String message, HttpStatus status, String code) {
        super(message);
        this.status = status;
        this.code = code;
        this.field = null;
    }
}
