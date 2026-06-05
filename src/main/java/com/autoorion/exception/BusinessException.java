ackage com.autoorion.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * ExceÃ§Ã£o de regra de negÃ³cio.
 * LanÃ§ada pelos services quando uma regra Ã© violada.
 * Resulta em HTTP 422 (Unprocessable Entity) por padrÃ£o.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String code;
    private final String field; // campo especÃ­fico que causou o erro (opcional)

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
