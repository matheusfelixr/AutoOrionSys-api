package com.autoorion.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * UserDetails customizado que carrega o nome do usuário para uso
 * no AuditorAware (criadoPor / atualizadoPor).
 */
@Getter
public class CustomUserDetails extends User {

    private final String nome;

    public CustomUserDetails(String userId,
                              String senha,
                              String nome,
                              Collection<? extends GrantedAuthority> authorities) {
        super(userId, senha, authorities);
        this.nome = nome;
    }
}
