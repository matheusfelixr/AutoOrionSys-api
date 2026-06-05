package com.autoorion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Envelope padrão para todas as respostas da API autoorion.
 *
 * Sucesso:  { success: true,  data: T,    message: "...", timestamp }
 * Erro:     { success: false, data: null, message: "...", errors: [...], timestamp }
 * Lista:    { success: true,  data: [...], total: N, page: P, pageSize: S }
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;

    /** Erros de validação por campo: [{ field, message }] */
    private List<FieldError> errors;

    /** Para listas paginadas */
    private Long total;
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;

    private LocalDateTime timestamp;

    // ── Factories ──────────────────────────────────────────────────────────

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return ok(data, message);
    }

    public static <T> ApiResponse<List<T>> list(List<T> items) {
        return ApiResponse.<List<T>>builder()
                .success(true)
                .data(items)
                .total((long) items.size())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<List<T>> page(Page<T> page) {
        return ApiResponse.<List<T>>builder()
                .success(true)
                .data(page.getContent())
                .total(page.getTotalElements())
                .page(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<List<T>> list(List<T> items, long total, int page, int pageSize) {
        return ApiResponse.<List<T>>builder()
                .success(true)
                .data(items)
                .total(total)
                .page(page)
                .pageSize(pageSize)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> validationError(String message, List<FieldError> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ── Inner types ────────────────────────────────────────────────────────

    @Data
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
