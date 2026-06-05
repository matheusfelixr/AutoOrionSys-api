package com.autoorion.exception;

import org.springframework.http.HttpStatus;

/**
 * Lançada quando um recurso não é encontrado.
 * Resulta em HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resource, String id) {
        super(resource + " não encontrado(a) com id: " + id, HttpStatus.NOT_FOUND, "NOT_FOUND");
    }

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "NOT_FOUND");
    }
}
