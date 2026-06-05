package com.autoorion.service;

import com.autoorion.entity.RefreshToken;
import com.autoorion.exception.BusinessException;
import com.autoorion.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    @Value("${autoorion.jwt.refresh-expiration:604800000}") // 7 dias em ms
    private long refreshExpiration;

    private final RefreshTokenRepository repository;

    public RefreshToken createRefreshToken(String usuarioId) {
        // Remove tokens antigos do usuário
        repository.deleteByUsuarioId(usuarioId);

        var token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .usuarioId(usuarioId)
                .expiraEm(Instant.now().plusMillis(refreshExpiration))
                .criadoEm(Instant.now())
                .build();

        return repository.save(token);
    }

    public RefreshToken verifyExpiration(String tokenStr) {
        var token = repository.findByToken(tokenStr)
                .orElseThrow(() -> new BusinessException("Refresh token inválido ou não encontrado.", HttpStatus.UNAUTHORIZED));

        if (token.getExpiraEm().isBefore(Instant.now())) {
            repository.delete(token);
            throw new BusinessException("Refresh token expirado. Faça login novamente.", HttpStatus.UNAUTHORIZED);
        }
        return token;
    }

    @Transactional
    public void deleteByUsuarioId(String usuarioId) {
        repository.deleteByUsuarioId(usuarioId);
        log.info("[RefreshTokenService] Tokens revogados para usuário: {}", usuarioId);
    }
}
