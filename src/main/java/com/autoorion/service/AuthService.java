package com.autoorion.service;

import com.autoorion.dto.LoginRequest;
import com.autoorion.dto.LoginResponse;
import com.autoorion.dto.RefreshTokenResponse;
import com.autoorion.entity.Usuario;
import com.autoorion.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final PermissionsService permissionsService;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse login(LoginRequest request) {
        Usuario usuario;
        try {
            usuario = usuarioService.findByEmail(request.getEmail());
        } catch (Exception e) {
            throw new BadCredentialsException("E-mail ou senha invÃ¡lidos");
        }

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new BadCredentialsException("E-mail ou senha invÃ¡lidos");
        }

        if (usuario.getStatus() == Usuario.StatusUsuario.inativo) {
            throw new DisabledException("UsuÃ¡rio inativo. Entre em contato com o administrador.");
        }

        usuarioService.updateUltimoAcesso(usuario.getId());

        String token      = tokenProvider.generateToken(usuario.getId(), usuario.getEmail(), usuario.getPerfil().name());
        var refreshToken  = refreshTokenService.createRefreshToken(usuario.getId());
        var screens       = permissionsService.getPermissions(usuario.getPerfil());

        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .screens(screens)
                .user(LoginResponse.UserInfo.builder()
                        .id(usuario.getId())
                        .nome(usuario.getNome())
                        .email(usuario.getEmail())
                        .cargo(usuario.getCargo())
                        .perfil(usuario.getPerfil().name())
                        .status(usuario.getStatus().name())
                        .telefone(usuario.getTelefone())
                        .avatarUrl(usuario.getAvatarUrl())
                        .build())
                .build();
    }

    public RefreshTokenResponse refreshToken(String refreshTokenStr) {
        var refreshToken = refreshTokenService.verifyExpiration(refreshTokenStr);
        var usuario = usuarioService.findById(refreshToken.getUsuarioId());
        String newJwt = tokenProvider.generateToken(usuario.getId(), usuario.getEmail(), usuario.getPerfil().name());
        return new RefreshTokenResponse(newJwt, refreshTokenStr);
    }

    public void logout(String usuarioId) {
        refreshTokenService.deleteByUsuarioId(usuarioId);
    }
}
