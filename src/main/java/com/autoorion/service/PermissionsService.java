package com.autoorion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionsService {

    private final PerfilAcessoService perfilAcessoService;

    /**
     * Retorna as telas permitidas para um perfil.
     * Busca do banco de dados via PerfilAcessoService â€” escalÃ¡vel e configurÃ¡vel.
     */
    public List<String> getPermissions(Object perfil) {
        String codigoPerfil = perfil != null ? perfil.toString() : "visualizador";
        return perfilAcessoService.getScreensForPerfil(codigoPerfil);
    }

    /** MantÃ©m compatibilidade com cÃ³digo legado que passa enum */
    public List<String> getPermissions(com.autoorion.entity.Usuario.PerfilUsuario perfil) {
        return getPermissions((Object) perfil.name());
    }
}
