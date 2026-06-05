ackage com.autoorion.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenResponse {
    private String token;        // novo JWT
    private String refreshToken; // mesmo refresh token (ou novo)
    private String type = "Bearer";

    public RefreshTokenResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
