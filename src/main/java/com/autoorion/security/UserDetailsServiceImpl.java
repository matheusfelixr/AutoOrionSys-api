package com.autoorion.security;

import com.autoorion.entity.Usuario;
import com.autoorion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + userId));

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name().toUpperCase()));
        return new CustomUserDetails(usuario.getId(), usuario.getSenha(), usuario.getNome(), authorities);
    }
}
