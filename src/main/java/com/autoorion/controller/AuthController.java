package com.autoorion.controller;

import com.autoorion.dto.ApiResponse;
import com.autoorion.dto.LoginRequest;
import com.autoorion.dto.LoginResponse;
import com.autoorion.dto.RefreshTokenRequest;
import com.autoorion.dto.RefreshTokenResponse;
import com.autoorion.entity.Usuario;
import com.autoorion.service.AuthService;
import com.autoorion.service.NotificacaoService;
import com.autoorion.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticação", description = "Endpoints de login e gestão de sessão")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final NotificacaoService notificacaoService;

    @PostMapping("/login")
    @Operation(
        summary = "Realizar login",
        description = """
            Autentica o usuário e retorna um token JWT válido por 24 horas.
            O token deve ser enviado no header `Authorization: Bearer {token}` em todas as demais requisições.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(
                name = "Login Admin",
                value = """
                    {
                      "email": "ana.souza@autoorion.com.br",
                      "senha": "autoorion123"
                    }
                    """
            ))
        )
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Usuário inativo"),
    })
    @SecurityRequirements  // Login não precisa de token
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        log.info("[Auth] Login realizado: email={}, perfil={}", request.getEmail(), response.getUser().getPerfil());
        return ResponseEntity.ok(ApiResponse.ok(response, "Login realizado com sucesso!"));
    }

    @PostMapping("/refresh")
    @SecurityRequirements
    @Operation(summary = "Renovar token JWT usando refresh token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        var response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.ok(response, "Token renovado com sucesso!"));
    }

    /**
     * Retorna dados do usuário logado — usado para validar sessão no startup do frontend.
     * Se o JWT for inválido ou o usuário não existir, retorna 401.
     */
    @GetMapping("/me")
    @Operation(summary = "Retorna o usuário autenticado atual")
    public ResponseEntity<ApiResponse<Usuario>> me(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Não autenticado"));
        }
        var usuario = usuarioService.findById(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(usuario));
    }

    /**
     * Permite ao usuário logado atualizar o próprio perfil (nome, cargo, telefone, avatarUrl).
     * Qualquer perfil pode usar este endpoint — sem restrição de ADMIN/GERENTE.
     */
    @PutMapping("/me")
    @Operation(summary = "Atualiza o perfil do usuário autenticado")
    public ResponseEntity<ApiResponse<Usuario>> updateMe(
            @RequestBody java.util.Map<String, Object> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Não autenticado"));
        }
        // Permite apenas campos pessoais — não permite mudar perfil/status via este endpoint
        body.remove("perfil");
        body.remove("status");
        body.remove("senha");
        var usuario = usuarioService.update(userDetails.getUsername(), body);
        notificacaoService.enviar(
            userDetails.getUsername(),
            "Perfil atualizado",
            "Seus dados de perfil foram atualizados com sucesso.",
            "success"
        );
        log.info("[Auth] Perfil próprio atualizado: id={}", userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(usuario, "Perfil atualizado com sucesso!"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Fazer logout e revogar refresh token")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            authService.logout(userDetails.getUsername());
        }
        return ResponseEntity.ok(ApiResponse.ok(null, "Logout realizado com sucesso!"));
    }
}
