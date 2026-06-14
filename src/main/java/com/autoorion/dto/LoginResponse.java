package com.autoorion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {
    private UserInfo user;
    private String token;
    private String refreshToken;
    private List<String> screens;
    /** Permissões granulares por tela. Ex: {"veiculos":["ver","criar","editar","excluir"]} */
    private Map<String, List<String>> permissoes;

    @Data
    @Builder
    @AllArgsConstructor
    public static class UserInfo {
        private String id;
        private String nome;
        private String email;
        private String cargo;
        private String perfil;
        private String status;
        private String telefone;
        private String avatarUrl; // ← foto do usuário
    }
}
