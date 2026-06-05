ackage com.autoorion.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
    public UnauthorizedException() {
        super("NÃ£o autenticado ou sessÃ£o expirada.", HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
}
