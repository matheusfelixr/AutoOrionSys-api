ackage com.autoorion.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token Ã© obrigatÃ³rio")
    private String refreshToken;
}
