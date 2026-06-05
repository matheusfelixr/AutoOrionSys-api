package com.autoorion.config;

import com.autoorion.entity.*;
import com.autoorion.repository.*;
import com.autoorion.entity.PerfilAcesso;
import com.autoorion.repository.PerfilAcessoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData(
            UsuarioRepository usuarios,
            MenuGrupoRepository menuGrupos,
            TelaSistemaRepository telas,
            GrupoParametroRepository gruposParametro,
            ParametroRepository parametros,
            NotificacaoRepository notificacoes,
            PerfilAcessoRepository perfilAcessoRepo) {
        return args -> {
            if (usuarios.count() > 0) return;
            log.info("Inicializando dados de demonstração...");

            String senhaDefault = passwordEncoder.encode("autoorion123");

            // ── Usuários ─────────────────────────────────────────────────────
            usuarios.save(Usuario.builder().nome("Ana Souza").email("ana.souza@autoorion.com.br")
                    .senha(senhaDefault).cargo("Gerente de Projetos").perfil(Usuario.PerfilUsuario.admin)
                    .status(Usuario.StatusUsuario.ativo).telefone("(48) 99101-2233")
                    .dataCadastro(LocalDateTime.now()).ultimoAcesso(LocalDateTime.now()).build());

            usuarios.save(Usuario.builder().nome("Carlos Mendes").email("carlos.mendes@autoorion.com.br")
                    .senha(senhaDefault).cargo("Engenheiro Civil").perfil(Usuario.PerfilUsuario.gerente)
                    .status(Usuario.StatusUsuario.ativo).telefone("(48) 99202-3344")
                    .dataCadastro(LocalDateTime.now()).ultimoAcesso(LocalDateTime.now()).build());

            usuarios.save(Usuario.builder().nome("Fernanda Lima").email("fernanda.lima@autoorion.com.br")
                    .senha(senhaDefault).cargo("Arquiteta").perfil(Usuario.PerfilUsuario.gerente)
                    .status(Usuario.StatusUsuario.ativo).dataCadastro(LocalDateTime.now()).build());

            usuarios.save(Usuario.builder().nome("Ricardo Alves").email("ricardo.alves@autoorion.com.br")
                    .senha(senhaDefault).cargo("Técnico de Campo").perfil(Usuario.PerfilUsuario.tecnico)
                    .status(Usuario.StatusUsuario.ativo).dataCadastro(LocalDateTime.now()).build());

            usuarios.save(Usuario.builder().nome("Camila Santos").email("camila.santos@autoorion.com.br")
                    .senha(senhaDefault).cargo("Analista").perfil(Usuario.PerfilUsuario.visualizador)
                    .status(Usuario.StatusUsuario.ativo).dataCadastro(LocalDateTime.now()).build());

            // ── Menus Grupos ─────────────────────────────────────────────────
            var mg1 = menuGrupos.save(MenuGrupo.builder().nome("Principal").icone("🏠").ordem(1).build());
            var mg2 = menuGrupos.save(MenuGrupo.builder().nome("Cadastros").icone("📋").ordem(2).build());
            var mg3 = menuGrupos.save(MenuGrupo.builder().nome("Configurações").icone("⚙️").ordem(3).build());
            var mg4 = menuGrupos.save(MenuGrupo.builder().nome("Conta").icone("👤").ordem(4).build());

            // ── Telas do Sistema ──────────────────────────────────────────────
            // Principal (mg1)
            telas.save(TelaSistema.builder().screenName("home").nome("Início")
                    .descricao("Tela inicial do sistema").menuId(mg1.getId())
                    .icone("🏠").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("notificacoes-group").nome("Notificações")
                    .descricao("Módulo de notificações (agrupador)").menuId(mg1.getId())
                    .icone("🔔").ordem(3).build());
            telas.save(TelaSistema.builder().screenName("notificacoes").nome("Minhas Notificações")
                    .descricao("Notificações do usuário logado").menuId(mg1.getId())
                    .parentScreenName("notificacoes-group").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("notificacoes.admin").nome("Gerenciar Notificações")
                    .descricao("Criar e gerenciar notificações do sistema").menuId(mg1.getId())
                    .parentScreenName("notificacoes-group").ordem(2).build());

            // Cadastros (mg2)
            telas.save(TelaSistema.builder().screenName("usuarios-group").nome("Usuários")
                    .descricao("Módulo de usuários (agrupador)").menuId(mg2.getId())
                    .icone("👥").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("usuarios").nome("Usuários")
                    .descricao("Cadastro e gestão de usuários").menuId(mg2.getId())
                    .parentScreenName("usuarios-group").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("perfis").nome("Perfis")
                    .descricao("Perfis de acesso ao sistema").menuId(mg2.getId())
                    .parentScreenName("usuarios-group").ordem(2).build());

            // Configurações (mg3)
            telas.save(TelaSistema.builder().screenName("config-group").nome("Telas")
                    .descricao("Configurações de telas (agrupador)").menuId(mg3.getId())
                    .icone("⚙️").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("config.telas").nome("Telas do Sistema")
                    .descricao("Cadastro das telas do sistema").menuId(mg3.getId())
                    .parentScreenName("config-group").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("config.menus").nome("Menus")
                    .descricao("Organização dos menus laterais").menuId(mg3.getId())
                    .parentScreenName("config-group").ordem(2).build());
            telas.save(TelaSistema.builder().screenName("parametros-group").nome("Parâmetros")
                    .descricao("Módulo de parâmetros (agrupador)").menuId(mg3.getId())
                    .icone("🔧").ordem(2).build());
            telas.save(TelaSistema.builder().screenName("parametros").nome("Parâmetros")
                    .descricao("Configurações parametrizáveis do sistema").menuId(mg3.getId())
                    .parentScreenName("parametros-group").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("parametros.grupos").nome("Grupos")
                    .descricao("Grupos de parâmetros").menuId(mg3.getId())
                    .parentScreenName("parametros-group").ordem(2).build());

            // Conta (mg4)
            telas.save(TelaSistema.builder().screenName("perfil").nome("Perfil")
                    .descricao("Dados pessoais do usuário logado").menuId(mg4.getId())
                    .icone("🧑").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("sair").nome("Sair")
                    .descricao("Sair do sistema").menuId(mg4.getId())
                    .icone("🚪").ordem(2).build());

            // ── Grupos de Parâmetros ─────────────────────────────────────────
            var gpGeral = gruposParametro.save(GrupoParametro.builder().nome("Geral")
                    .descricao("Configurações gerais do sistema").ordem(1).build());
            var gpFinanceiro = gruposParametro.save(GrupoParametro.builder().nome("Financeiro")
                    .descricao("Configurações financeiras").ordem(2).build());
            var gpObras = gruposParametro.save(GrupoParametro.builder().nome("Obras")
                    .descricao("Configurações de obras e projetos").ordem(3).build());
            var gpNotif = gruposParametro.save(GrupoParametro.builder().nome("Notificações")
                    .descricao("Configurações de notificações").ordem(4).build());
            var gpSeg = gruposParametro.save(GrupoParametro.builder().nome("Segurança")
                    .descricao("Configurações de segurança e acesso").ordem(5).build());

            // ── Parâmetros ───────────────────────────────────────────────────
            parametros.save(Parametro.builder().nome("prmNomeSistema").descricao("Nome exibido no sistema")
                    .grupoId(gpGeral.getId()).valor("autoorion Demo").tipo("texto").ordem(1).build());
            parametros.save(Parametro.builder().nome("prmVersao").descricao("Versão atual do sistema")
                    .grupoId(gpGeral.getId()).valor("1.0.0").tipo("texto").ordem(2).build());
            parametros.save(Parametro.builder().nome("prmItensPorPagina").descricao("Quantidade de itens por página nas listagens")
                    .grupoId(gpGeral.getId()).valor("10").tipo("numero").ordem(3).build());

            parametros.save(Parametro.builder().nome("prmArredondamento").descricao("Casas decimais para arredondamento financeiro")
                    .grupoId(gpFinanceiro.getId()).valor("2").tipo("numero").ordem(1).build());
            parametros.save(Parametro.builder().nome("prmMoeda").descricao("Moeda padrão do sistema")
                    .grupoId(gpFinanceiro.getId()).valor("BRL").tipo("lista")
                    .opcoes("[\"BRL\",\"USD\",\"EUR\"]").ordem(2).build());

            parametros.save(Parametro.builder().nome("prmUnidadeMedida").descricao("Unidade de medida padrão para obras")
                    .grupoId(gpObras.getId()).valor("m²").tipo("lista")
                    .opcoes("[\"m²\",\"m³\",\"un\",\"kg\"]").ordem(1).build());

            parametros.save(Parametro.builder().nome("prmNotifAtivas").descricao("Habilitar envio de notificações")
                    .grupoId(gpNotif.getId()).valor("true").tipo("booleano").ordem(1).build());

            parametros.save(Parametro.builder().nome("prmTentativasLogin").descricao("Número máximo de tentativas de login")
                    .grupoId(gpSeg.getId()).valor("5").tipo("numero").ordem(1).build());
            parametros.save(Parametro.builder().nome("prmTempoSessao").descricao("Tempo de sessão em minutos")
                    .grupoId(gpSeg.getId()).valor("60").tipo("numero").ordem(2).build());

            // ── Notificações demo (vinculadas ao admin Ana Souza) ────────────
            var adminDemo = usuarios.findByEmail("ana.souza@autoorion.com.br").orElse(null);
            if (adminDemo != null) {
                notificacoes.save(Notificacao.builder()
                        .usuarioId(adminDemo.getId())
                        .titulo("Bem-vindo ao autoorion!")
                        .mensagem("O sistema foi inicializado com sucesso. Explore os módulos no menu lateral.")
                        .tipo("success").lida(false)
                        .criadoEm(LocalDateTime.now().minusDays(2))
                        .build());

                notificacoes.save(Notificacao.builder()
                        .usuarioId(adminDemo.getId())
                        .titulo("Manutenção programada")
                        .mensagem("O sistema passará por manutenção preventiva neste fim de semana das 22h às 6h.")
                        .tipo("warning").lida(false)
                        .criadoEm(LocalDateTime.now().minusHours(5))
                        .build());

                notificacoes.save(Notificacao.builder()
                        .usuarioId(adminDemo.getId())
                        .titulo("Sistema configurado")
                        .mensagem("Os módulos do sistema foram configurados com sucesso.")
                        .tipo("info").lida(true)
                        .criadoEm(LocalDateTime.now().minusHours(1))
                        .build());
            }

            // ── Perfis de Acesso ─────────────────────────────────────────────
            if (perfilAcessoRepo.count() == 0) {
                perfilAcessoRepo.save(PerfilAcesso.builder()
                    .codigo("admin")
                    .nome("Administrador")
                    .descricao("Acesso total ao sistema")
                    .telasPermitidas("[\"home\",\"usuarios\",\"perfis\",\"perfil\",\"config.telas\",\"config.menus\",\"notificacoes\",\"notificacoes.admin\",\"parametros\",\"parametros.grupos\"]")
                    .totalUsuarios(2)
                    .build());

                perfilAcessoRepo.save(PerfilAcesso.builder()
                    .codigo("gerente")
                    .nome("Gerente")
                    .descricao("Gerencia recursos e equipes")
                    .telasPermitidas("[\"home\",\"usuarios\",\"perfis\",\"perfil\",\"notificacoes\",\"notificacoes.admin\",\"parametros\"]")
                    .totalUsuarios(4)
                    .build());

                perfilAcessoRepo.save(PerfilAcesso.builder()
                    .codigo("tecnico")
                    .nome("Técnico")
                    .descricao("Acesso operacional")
                    .telasPermitidas("[\"home\",\"perfil\",\"notificacoes\"]")
                    .totalUsuarios(7)
                    .build());

                perfilAcessoRepo.save(PerfilAcesso.builder()
                    .codigo("visualizador")
                    .nome("Visualizador")
                    .descricao("Somente visualização")
                    .telasPermitidas("[\"home\",\"perfil\",\"notificacoes\"]")
                    .totalUsuarios(2)
                    .build());

                log.info("✅ Perfis de acesso inicializados: {} perfis", perfilAcessoRepo.count());
            }

            log.info("Dados inicializados: {} usuários, {} menus, {} telas, {} grupos, {} parâmetros, {} notificações",
                    usuarios.count(), menuGrupos.count(),
                    telas.count(), gruposParametro.count(), parametros.count(), notificacoes.count());
        };
    }
}
