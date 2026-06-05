ackage com.autoorion.exception;

import org.springframework.http.HttpStatus;

/**
 * LanÃ§ada quando um recurso nÃ£o Ã© encontrado.
 * Resulta em HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resource, String id) {
        super(resource + " nÃ£o encontrado(a) com id: " + id, HttpStatus.NOT_FOUND, "NOT_FOUND");
    }

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "NOT_FOUND");
    }
}
