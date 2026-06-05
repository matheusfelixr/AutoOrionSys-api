ackage com.autoorion.exception;

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

    // â”€â”€ ExceÃ§Ãµes de negÃ³cio customizadas â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(ValidationException ex, HttpServletRequest req) {
        log.debug("ValidaÃ§Ã£o falhou em {}: {}", req.getRequestURI(), ex.getMessage());
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
        log.debug("Recurso nÃ£o encontrado em {}: {}", req.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex, HttpServletRequest req) {
        log.warn("Erro de negÃ³cio em {}: {}", req.getRequestURI(), ex.getMessage());

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

    // â”€â”€ Spring Validation (@Valid) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        log.debug("ValidaÃ§Ã£o de campos falhou em {}", req.getRequestURI());
        List<ApiResponse.FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> ApiResponse.FieldError.builder()
                        .field(f.getField())
                        .message(f.getDefaultMessage())
                        .build())
                .toList();
        return ResponseEntity.badRequest()
                .body(ApiResponse.validationError("Dados invÃ¡lidos. Verifique os campos destacados.", errors));
    }

    // â”€â”€ Spring Security â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        log.warn("Acesso negado em {} por usuÃ¡rio nÃ£o autorizado", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("VocÃª nÃ£o tem permissÃ£o para realizar esta aÃ§Ã£o."));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("E-mail ou senha incorretos."));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Void>> handleDisabled(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("UsuÃ¡rio inativo. Entre em contato com o administrador."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthentication(AuthenticationException ex, HttpServletRequest req) {
        log.warn("Falha de autenticaÃ§Ã£o em {}: {}", req.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("NÃ£o autenticado ou sessÃ£o expirada."));
    }

    // â”€â”€ HTTP / Request â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandler(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Endpoint nÃ£o encontrado: " + ex.getRequestURL()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Corpo da requisiÃ§Ã£o invÃ¡lido ou malformado."));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("ParÃ¢metro obrigatÃ³rio ausente: " + ex.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Valor invÃ¡lido para o parÃ¢metro '" + ex.getName() + "': " + ex.getValue()));
    }

    // â”€â”€ Banco de dados â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        log.warn("ViolaÃ§Ã£o de integridade em {}: {}", req.getRequestURI(), ex.getMostSpecificCause().getMessage());
        String msg = "OperaÃ§Ã£o nÃ£o permitida: dados duplicados ou referÃªncia invÃ¡lida.";
        String cause = ex.getMostSpecificCause().getMessage();
        if (cause != null) {
            if (cause.contains("email"))        msg = "JÃ¡ existe um cadastro com este e-mail.";
            else if (cause.contains("cpf"))     msg = "JÃ¡ existe um cadastro com este CPF.";
            else if (cause.contains("placa"))   msg = "JÃ¡ existe um veÃ­culo com esta placa.";
            else if (cause.contains("codigo"))  msg = "JÃ¡ existe um registro com este cÃ³digo.";
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(msg));
    }

    // â”€â”€ Fallback geral â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex, HttpServletRequest req) {
        log.error("Erro inesperado em {} [{}]: {}", req.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno do servidor. Nossa equipe foi notificada."));
    }
}
