package com.autoorion.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Fornece o nome do usuário autenticado para o Spring Data JPA Auditing.
 * Alimenta os campos @CreatedBy e @LastModifiedBy no Auditable.
 */
@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return Optional.of("sistema");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return Optional.of(userDetails.getNome());
        }

        // Fallback: usa o username (pode ser email ou ID)
        return Optional.of(auth.getName());
    }
}
