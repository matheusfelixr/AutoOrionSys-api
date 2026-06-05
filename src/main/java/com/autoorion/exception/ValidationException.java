package com.autoorion.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import java.util.List;
import java.util.Map;

@Getter
public class ValidationException extends BusinessException {
    private final List<Map<String, String>> fieldErrors;

    public ValidationException(List<Map<String, String>> fieldErrors) {
        super("Dados inválidos. Verifique os campos e tente novamente.", HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
        this.fieldErrors = fieldErrors;
    }
}
