# 🔧 AutoOrionSys-api

**Seu carro. Nossa missão.**

API Backend para o sistema de gerenciamento de peças automotivas AutoOrion, construída com Spring Boot 3.4.4.

## 📋 Sobre

Sistema responsável por:
- ✅ Cadastro de peças automotivas desmontadas
- ✅ Gerenciamento de anúncios Mercado Livre e Shopee
- ✅ Controle de estoque e inventário
- ✅ Autenticação e autorização
- ✅ API REST

## Pré-requisitos

- Java 21+
- Maven 3.9+

## 🚀 Executando

`ash
mvn spring-boot:run
`

O servidor inicia em http://localhost:8080.

## 🔑 Credenciais Padrão

Todos os usuários seed têm a senha utoorion123.

| Nome           | Email                              | Perfil       |
|----------------|------------------------------------|--------------|
| Admin AutoOrion | admin@autoorion.com.br             | admin        |
| Gerente 1      | gerente1@autoorion.com.br          | gerente      |
| Gerente 2      | gerente2@autoorion.com.br          | gerente      |
| Técnico        | tecnico@autoorion.com.br           | tecnico      |
| Visualizador   | visualizador@autoorion.com.br      | visualizador |

## 🗄️ H2 Console (dev only)

URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:autooriondb
- Username: sa
- Password: *(empty)*

## 📡 API Endpoints

### Autenticação
| Method | Path              | Descrição          |
|--------|-------------------|--------------------|
| POST   | /api/auth/login   | Login, retorna JWT |

### Usuários
| Method | Path                        | Descrição                    |
|--------|-----------------------------|------------------------------|
| GET    | /api/usuarios               | Listar todos os usuários     |
| GET    | /api/usuarios/{id}          | Obter usuário por ID         |
| GET    | /api/usuarios/{id}/permissions | Obter permissões do usuário |
| POST   | /api/usuarios               | Criar usuário                |
| PUT    | /api/usuarios/{id}          | Atualizar usuário            |
| DELETE | /api/usuarios/{id}          | Deletar usuário              |

### Peças (em desenvolvimento)
| Method | Path               | Descrição                |
|--------|--------------------|-----------------------|
| GET    | /api/pecas         | Listar todas as peças    |
| GET    | /api/pecas/{id}    | Obter peça por ID        |
| POST   | /api/pecas         | Criar peça               |
| PUT    | /api/pecas/{id}    | Atualizar peça           |
| DELETE | /api/pecas/{id}    | Deletar peça             |

### Exemplo de Login

`ash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@autoorion.com.br","senha":"autoorion123"}'
`

Use o token retornado como Authorization: Bearer <token> em requisições autenticadas.

## 📊 Migrando para PostgreSQL

Ative o perfil prod:

`ash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
`

Defina variáveis de ambiente do banco:

`ash
export DB_USERNAME=autoorion
export DB_PASSWORD=autoorion123
`

Ou passe diretamente:

`ash
mvn spring-boot:run -Dspring-boot.run.profiles=prod \
  -Dspring-boot.run.jvmArguments="-DDB_USERNAME=autoorion -DDB_PASSWORD=suasenha"
`

O perfil de produção conecta a jdbc:postgresql://localhost:5432/autooriondb. Crie o banco antes de executar:

`sql
CREATE DATABASE autooriondb;
CREATE USER autoorion WITH PASSWORD 'autoorion123';
GRANT ALL PRIVILEGES ON DATABASE autooriondb TO autoorion;
`

## 🎨 Identidade Visual AutoOrion

**Cores Oficiais:**
- **Cinza Grafite:** #3a3a3a
- **Preto Fosco:** #1a1a1a
- **Laranja Queimado:** #d97f3e

**Slogan:** "Seu carro. Nossa missão."

**Tom de voz:** Profissional, direto, técnico e confiável.

---

**AutoOrion** © 2026 - Peças automotivas confiáveis | [Website](https://autoorion.com.br) | [@autoorion](https://instagram.com/autoorion)
