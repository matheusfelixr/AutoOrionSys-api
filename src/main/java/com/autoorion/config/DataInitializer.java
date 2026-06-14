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
            PerfilAcessoRepository perfilAcessoRepo,
            VeiculoRepository veiculos,
            MarcaRepository marcas,
            CorRepository cores) {
        return args -> {
            if (usuarios.count() > 0) return;
            log.info("Inicializando dados de demonstracao...");

            String senhaDefault = passwordEncoder.encode("autoorion123");

            usuarios.save(Usuario.builder().nome("Matheus Felix").email("matheus.felix@autoorion.com.br")
                    .senha(senhaDefault).cargo("Administrador do Sistema").perfil(Usuario.PerfilUsuario.admin)
                    .status(Usuario.StatusUsuario.ativo).telefone("(48) 99000-0001")
                    .dataCadastro(LocalDateTime.now()).ultimoAcesso(LocalDateTime.now()).build());
            usuarios.save(Usuario.builder().nome("Ana Souza").email("ana.souza@autoorion.com.br")
                    .senha(senhaDefault).cargo("Gerente Comercial").perfil(Usuario.PerfilUsuario.admin)
                    .status(Usuario.StatusUsuario.ativo).telefone("(48) 99101-2233")
                    .dataCadastro(LocalDateTime.now()).ultimoAcesso(LocalDateTime.now()).build());
            usuarios.save(Usuario.builder().nome("Carlos Mendes").email("carlos.mendes@autoorion.com.br")
                    .senha(senhaDefault).cargo("Consultor de Vendas").perfil(Usuario.PerfilUsuario.gerente)
                    .status(Usuario.StatusUsuario.ativo).telefone("(48) 99202-3344")
                    .dataCadastro(LocalDateTime.now()).ultimoAcesso(LocalDateTime.now()).build());
            usuarios.save(Usuario.builder().nome("Fernanda Lima").email("fernanda.lima@autoorion.com.br")
                    .senha(senhaDefault).cargo("Supervisora de Pos-Venda").perfil(Usuario.PerfilUsuario.gerente)
                    .status(Usuario.StatusUsuario.ativo).dataCadastro(LocalDateTime.now()).build());
            usuarios.save(Usuario.builder().nome("Ricardo Alves").email("ricardo.alves@autoorion.com.br")
                    .senha(senhaDefault).cargo("Tecnico Automotivo").perfil(Usuario.PerfilUsuario.tecnico)
                    .status(Usuario.StatusUsuario.ativo).dataCadastro(LocalDateTime.now()).build());
            usuarios.save(Usuario.builder().nome("Camila Santos").email("camila.santos@autoorion.com.br")
                    .senha(senhaDefault).cargo("Analista").perfil(Usuario.PerfilUsuario.visualizador)
                    .status(Usuario.StatusUsuario.ativo).dataCadastro(LocalDateTime.now()).build());

            var mg1 = menuGrupos.save(MenuGrupo.builder().nome("Principal").icone("HOME").ordem(1).build());
            var mg2 = menuGrupos.save(MenuGrupo.builder().nome("Cadastros").icone("LIST").ordem(2).build());
            var mg3 = menuGrupos.save(MenuGrupo.builder().nome("Configuracoes").icone("GEAR").ordem(3).build());
            var mg4 = menuGrupos.save(MenuGrupo.builder().nome("Conta").icone("USER").ordem(4).build());

            telas.save(TelaSistema.builder().screenName("home").nome("Inicio").descricao("Tela inicial").menuId(mg1.getId()).icone("HOME").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("notificacoes-group").nome("Notificacoes").descricao("Modulo de notificacoes").menuId(mg1.getId()).icone("BELL").ordem(3).build());
            telas.save(TelaSistema.builder().screenName("notificacoes").nome("Minhas Notificacoes").descricao("Notificacoes do usuario logado").menuId(mg1.getId()).parentScreenName("notificacoes-group").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("notificacoes.admin").nome("Gerenciar Notificacoes").descricao("Criar e gerenciar notificacoes").menuId(mg1.getId()).parentScreenName("notificacoes-group").ordem(2).build());

            telas.save(TelaSistema.builder().screenName("veiculos-group").nome("Veiculos").descricao("Modulo de veiculos").menuId(mg2.getId()).icone("CAR").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("veiculos").nome("Cadastro de Veiculos").descricao("Cadastro e gestao de veiculos").menuId(mg2.getId()).parentScreenName("veiculos-group").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("marcas").nome("Cadastro de Marcas").descricao("Cadastro de marcas de veiculos").menuId(mg2.getId()).parentScreenName("veiculos-group").ordem(2).build());
            telas.save(TelaSistema.builder().screenName("cores").nome("Cadastro de Cores").descricao("Cadastro de cores de veiculos").menuId(mg2.getId()).parentScreenName("veiculos-group").ordem(3).build());
            telas.save(TelaSistema.builder().screenName("usuarios-group").nome("Usuarios").descricao("Modulo de usuarios").menuId(mg2.getId()).icone("USERS").ordem(2).build());
            telas.save(TelaSistema.builder().screenName("usuarios").nome("Usuarios").descricao("Cadastro e gestao de usuarios").menuId(mg2.getId()).parentScreenName("usuarios-group").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("perfis").nome("Perfis").descricao("Perfis de acesso").menuId(mg2.getId()).parentScreenName("usuarios-group").ordem(2).build());

            telas.save(TelaSistema.builder().screenName("config-group").nome("Telas").descricao("Configuracoes de telas").menuId(mg3.getId()).icone("GEAR").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("config.telas").nome("Telas do Sistema").descricao("Cadastro das telas").menuId(mg3.getId()).parentScreenName("config-group").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("config.menus").nome("Menus").descricao("Organizacao dos menus").menuId(mg3.getId()).parentScreenName("config-group").ordem(2).build());
            telas.save(TelaSistema.builder().screenName("parametros-group").nome("Parametros").descricao("Modulo de parametros").menuId(mg3.getId()).icone("WRENCH").ordem(2).build());
            telas.save(TelaSistema.builder().screenName("parametros").nome("Parametros").descricao("Configuracoes parametrizaveis").menuId(mg3.getId()).parentScreenName("parametros-group").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("parametros.grupos").nome("Grupos").descricao("Grupos de parametros").menuId(mg3.getId()).parentScreenName("parametros-group").ordem(2).build());

            telas.save(TelaSistema.builder().screenName("perfil").nome("Perfil").descricao("Dados pessoais").menuId(mg4.getId()).icone("USER").ordem(1).build());
            telas.save(TelaSistema.builder().screenName("sair").nome("Sair").descricao("Sair do sistema").menuId(mg4.getId()).icone("DOOR").ordem(2).build());

            var gpGeral      = gruposParametro.save(GrupoParametro.builder().nome("Geral").descricao("Configuracoes gerais").ordem(1).build());
            var gpFinanceiro = gruposParametro.save(GrupoParametro.builder().nome("Financeiro").descricao("Configuracoes financeiras").ordem(2).build());
            var gpVeiculos   = gruposParametro.save(GrupoParametro.builder().nome("Veiculos").descricao("Configuracoes de veiculos").ordem(3).build());
            var gpNotif      = gruposParametro.save(GrupoParametro.builder().nome("Notificacoes").descricao("Configuracoes de notificacoes").ordem(4).build());
            var gpSeg        = gruposParametro.save(GrupoParametro.builder().nome("Seguranca").descricao("Configuracoes de seguranca").ordem(5).build());

            parametros.save(Parametro.builder().nome("prmNomeSistema").descricao("Nome exibido no sistema").grupoId(gpGeral.getId()).valor("AutoOrion").tipo("texto").ordem(1).build());
            parametros.save(Parametro.builder().nome("prmVersao").descricao("Versao atual").grupoId(gpGeral.getId()).valor("1.0.0").tipo("texto").ordem(2).build());
            parametros.save(Parametro.builder().nome("prmItensPorPagina").descricao("Itens por pagina").grupoId(gpGeral.getId()).valor("10").tipo("numero").ordem(3).build());
            parametros.save(Parametro.builder().nome("prmArredondamento").descricao("Casas decimais").grupoId(gpFinanceiro.getId()).valor("2").tipo("numero").ordem(1).build());
            parametros.save(Parametro.builder().nome("prmMoeda").descricao("Moeda padrao").grupoId(gpFinanceiro.getId()).valor("BRL").tipo("lista").opcoes("[\"BRL\",\"USD\",\"EUR\"]").ordem(2).build());
            parametros.save(Parametro.builder().nome("prmKmRevisao").descricao("Intervalo KM revisao").grupoId(gpVeiculos.getId()).valor("10000").tipo("numero").ordem(1).build());
            parametros.save(Parametro.builder().nome("prmStatusPadraoEntrada").descricao("Status padrao novo veiculo").grupoId(gpVeiculos.getId()).valor("disponivel").tipo("lista").opcoes("[\"disponivel\",\"reservado\",\"manutencao\"]").ordem(2).build());
            parametros.save(Parametro.builder().nome("prmNotifAtivas").descricao("Habilitar notificacoes").grupoId(gpNotif.getId()).valor("true").tipo("booleano").ordem(1).build());
            parametros.save(Parametro.builder().nome("prmTentativasLogin").descricao("Max tentativas de login").grupoId(gpSeg.getId()).valor("5").tipo("numero").ordem(1).build());
            parametros.save(Parametro.builder().nome("prmTempoSessao").descricao("Tempo de sessao em minutos").grupoId(gpSeg.getId()).valor("60").tipo("numero").ordem(2).build());

            if (perfilAcessoRepo.count() == 0) {
                perfilAcessoRepo.save(PerfilAcesso.builder()
                    .codigo("admin").nome("Administrador").descricao("Acesso total ao sistema")
                    .telasPermitidas("[\"home\",\"veiculos\",\"marcas\",\"cores\",\"usuarios\",\"perfis\",\"perfil\",\"config.telas\",\"config.menus\",\"notificacoes\",\"notificacoes.admin\",\"parametros\",\"parametros.grupos\"]")
                    .permissoes("{\"veiculos\":[\"ver\",\"criar\",\"editar\",\"excluir\"],\"marcas\":[\"ver\",\"criar\",\"editar\",\"excluir\"],\"cores\":[\"ver\",\"criar\",\"editar\",\"excluir\"],\"usuarios\":[\"ver\",\"criar\",\"editar\",\"excluir\"],\"perfis\":[\"ver\",\"criar\",\"editar\",\"excluir\"],\"parametros\":[\"ver\",\"criar\",\"editar\",\"excluir\"],\"notificacoes.admin\":[\"ver\",\"criar\",\"excluir\"]}")
                    .totalUsuarios(2).build());

                perfilAcessoRepo.save(PerfilAcesso.builder()
                    .codigo("gerente").nome("Gerente").descricao("Gerencia recursos e equipes")
                    .telasPermitidas("[\"home\",\"veiculos\",\"marcas\",\"cores\",\"usuarios\",\"perfis\",\"perfil\",\"notificacoes\",\"notificacoes.admin\",\"parametros\"]")
                    .permissoes("{\"veiculos\":[\"ver\",\"criar\",\"editar\",\"excluir\"],\"marcas\":[\"ver\",\"criar\",\"editar\"],\"cores\":[\"ver\",\"criar\",\"editar\"],\"usuarios\":[\"ver\",\"editar\"],\"perfis\":[\"ver\"],\"parametros\":[\"ver\"],\"notificacoes.admin\":[\"ver\",\"criar\"]}")
                    .totalUsuarios(2).build());

                perfilAcessoRepo.save(PerfilAcesso.builder()
                    .codigo("tecnico").nome("Tecnico").descricao("Acesso operacional")
                    .telasPermitidas("[\"home\",\"veiculos\",\"perfil\",\"notificacoes\"]")
                    .permissoes("{\"veiculos\":[\"ver\",\"criar\",\"editar\"],\"marcas\":[\"ver\"],\"cores\":[\"ver\"]}")
                    .totalUsuarios(1).build());

                perfilAcessoRepo.save(PerfilAcesso.builder()
                    .codigo("visualizador").nome("Visualizador").descricao("Somente visualizacao")
                    .telasPermitidas("[\"home\",\"veiculos\",\"perfil\",\"notificacoes\"]")
                    .permissoes("{\"veiculos\":[\"ver\"],\"marcas\":[\"ver\"],\"cores\":[\"ver\"]}")
                    .totalUsuarios(1).build());

                log.info("Perfis de acesso inicializados: {} perfis", perfilAcessoRepo.count());
            }

            String[] nomesMarcas = {
                "Acura","Alfa Romeo","Aston Martin","Audi","Bentley","BMW","Bugatti",
                "BYD","Cadillac","Chery","Chevrolet","Chrysler","Citroen","Dodge",
                "Ferrari","Fiat","Ford","Genesis","GMC","Honda","Hyundai","Infiniti",
                "JAC","Jaguar","Jeep","Kia","Lamborghini","Land Rover","Lexus","Lotus",
                "Maserati","Mazda","McLaren","Mercedes-Benz","Mini","Mitsubishi",
                "Nissan","Peugeot","Pontiac","Porsche","Ram","Renault","Rolls-Royce",
                "Subaru","Suzuki","Tesla","Toyota","Troller","Volkswagen","Volvo"
            };
            for (String nome : nomesMarcas) {
                marcas.save(Marca.builder().nome(nome).build());
            }
            log.info("Marcas inicializadas: {}", marcas.count());

            String[] nomesCores = {
                "Amarelo","Azul","Azul Celeste","Azul Escuro","Azul Marinho",
                "Bege","Bordo","Branco","Branco Perola","Bronze",
                "Cinza","Cinza Escuro","Cobre","Dourado","Grafite",
                "Laranja","Laranja Metalico","Marrom","Prata","Preto",
                "Rosa","Roxo","Verde","Verde Escuro","Verde Militar",
                "Vermelho","Vermelho Escuro","Vinho"
            };
            for (String nome : nomesCores) {
                cores.save(Cor.builder().nome(nome).build());
            }
            log.info("Cores inicializadas: {}", cores.count());

            veiculos.save(Veiculo.builder().placa("ABC-1234").modelo("Civic").marca("Honda")
                    .anoFabricacao(2019).anoModelo(2020).cor("Prata").km(45000L)
                    .chassi("1HGCM82633A004352").renavam("12345678901")
                    .descricao("Veiculo em otimo estado, unico dono.").build());
            veiculos.save(Veiculo.builder().placa("DEF-5678").modelo("Corolla").marca("Toyota")
                    .anoFabricacao(2021).anoModelo(2021).cor("Preto").km(22000L)
                    .chassi("JT2BF22K1W0083588").renavam("98765432100")
                    .descricao("Motor em excelente estado.").podeVenderMotor(true).build());
            veiculos.save(Veiculo.builder().placa("GHI-9012").modelo("HB20").marca("Hyundai")
                    .anoFabricacao(2020).anoModelo(2020).cor("Branco").km(61000L)
                    .chassi("9BWZZZ377VT004251").renavam("11122233344").build());
            veiculos.save(Veiculo.builder().placa("JKL-3456").modelo("Onix").marca("Chevrolet")
                    .anoFabricacao(2022).anoModelo(2022).cor("Vermelho").km(18000L)
                    .chassi("9BWZZZ377VT004252").renavam("55566677788")
                    .baixado(true).descricao("Veiculo baixado para desmanche.").build());
            veiculos.save(Veiculo.builder().placa("MNO-7890").modelo("Gol").marca("Volkswagen")
                    .anoFabricacao(2018).anoModelo(2019).cor("Cinza").km(92000L)
                    .chassi("9BWZZZ377VT004253").renavam("99988877766")
                    .podeVenderMotor(true).numeroMotor("EA111-ABC123").build());

            log.info("Dados inicializados: {} usuarios, {} menus, {} telas, {} grupos, {} parametros, {} veiculos",
                    usuarios.count(), menuGrupos.count(),
                    telas.count(), gruposParametro.count(), parametros.count(), veiculos.count());
        };
    }
}
