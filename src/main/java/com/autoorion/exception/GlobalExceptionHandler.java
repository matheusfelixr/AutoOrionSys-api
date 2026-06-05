package com.autoorion.exception;

import com.autoorion.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ── Exceções de negócio customizadas ─────────────────────────────────────

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(ValidationException ex, HttpServletRequest req) {
        log.debug("Validação falhou em {}: {}", req.getRequestURI(), ex.getMessage());
        List<ApiResponse.FieldError> errors = ex.getFieldErrors().stream()
                .map(e -> ApiResponse.FieldError.builder()
                        .field(e.get("field"))
                        .message(e.get("message"))
                        .build())
                .toList();
        var response = ApiResponse.<Void>validationError(ex.getMessage(), errors);
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        log.debug("Recurso não encontrado em {}: {}", req.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex, HttpServletRequest req) {
        log.warn("Erro de negócio em {}: {}", req.getRequestURI(), ex.getMessage());

        if (ex.getField() != null) {
            var fieldError = ApiResponse.FieldError.builder()
                    .field(ex.getField())
                    .message(ex.getMessage())
                    .build();
            return ResponseEntity.status(ex.getStatus())
                    .body(ApiResponse.validationError(ex.getMessage(), List.of(fieldError)));
        }

        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(ex.getMessage()));
    }

    // ── Spring Validation (@Valid) ────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        log.debug("Validação de campos falhou em {}", req.getRequestURI());
        List<ApiResponse.FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> ApiResponse.FieldError.builder()
                        .field(f.getField())
                        .message(f.getDefaultMessage())
                        .build())
                .toList();
        return ResponseEntity.badRequest()
                .body(ApiResponse.validationError("Dados inválidos. Verifique os campos destacados.", errors));
    }

    // ── Spring Security ───────────────────────────────────────────────────────

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        log.warn("Acesso negado em {} por usuário não autorizado", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("Você não tem permissão para realizar esta ação."));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("E-mail ou senha incorretos."));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Void>> handleDisabled(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Usuário inativo. Entre em contato com o administrador."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthentication(AuthenticationException ex, HttpServletRequest req) {
        log.warn("Falha de autenticação em {}: {}", req.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Não autenticado ou sessão expirada."));
    }

    // ── HTTP / Request ────────────────────────────────────────────────────────

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandler(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Endpoint não encontrado: " + ex.getRequestURL()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Corpo da requisição inválido ou malformado."));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Parâmetro obrigatório ausente: " + ex.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Valor inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue()));
    }

    // ── Banco de dados ────────────────────────────────────────────────────────

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        log.warn("Violação de integridade em {}: {}", req.getRequestURI(), ex.getMostSpecificCause().getMessage());
        String msg = "Operação não permitida: dados duplicados ou referência inválida.";
        String cause = ex.getMostSpecificCause().getMessage();
        if (cause != null) {
            if (cause.contains("email"))        msg = "Já existe um cadastro com este e-mail.";
            else if (cause.contains("cpf"))     msg = "Já existe um cadastro com este CPF.";
            else if (cause.contains("placa"))   msg = "Já existe um veículo com esta placa.";
            else if (cause.contains("codigo"))  msg = "Já existe um registro com este código.";
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(msg));
    }

    // ── Fallback geral ────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex, HttpServletRequest req) {
        log.error("Erro inesperado em {} [{}]: {}", req.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor. Nossa equipe foi notificada."));
    }
}
