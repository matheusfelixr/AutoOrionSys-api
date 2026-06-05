package com.autoorion.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuraçoes do Swagger UI / OpenAPI 3.
 *
 * Acesso: http://localhost:8080/swagger-ui.html
 * JSON:   http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI autoorionOpenAPI() {
        return new OpenAPI()
                // â”€â”€ InformaÃ§Ãµes gerais â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
                .info(new Info()
                        .title("autoorion API")
                        .description("""
                                API REST do sistema autoorion â€” plataforma modular de gestÃ£o empresarial.

                                ## AutenticaÃ§Ã£o
                                Todos os endpoints (exceto `/api/auth/login`) requerem um token JWT vÃ¡lido.

                                **Como usar:**
                                1. FaÃ§a `POST /api/auth/login` com e-mail e senha
                                2. Copie o campo `data.token` da resposta
                                3. Clique em **Authorize** acima e cole: `Bearer {token}`

                                ## Credenciais de teste
                                | E-mail | Perfil | Senha |
                                |---|---|---|
                                | ana.souza@autoorion.com.br | Admin | autoorion123 |
                                | carlos.mendes@autoorion.com.br | Gerente | autoorion123 |
                                | ricardo.alves@autoorion.com.br | TÃ©cnico | autoorion123 |

                                ## PaginaÃ§Ã£o
                                Endpoints de listagem aceitam: `?page=0&size=10&sortBy=nome&sortDir=asc`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("autoorion Team")
                                .email("dev@autoorion.com.br"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://autoorion.com.br")))

                // â”€â”€ Servidores â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Desenvolvimento"),
                        new Server().url("https://api.autoorion.com.br").description("ProduÃ§Ã£o")
                ))

                // â”€â”€ SeguranÃ§a global (JWT Bearer) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT obtido em POST /api/auth/login")));
    }
}
