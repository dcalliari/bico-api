# Bico Vagas API

Backend REST API para a plataforma Bico Vagas, desenvolvida com Spring Boot.

## Stack

- **Java 21** + **Spring Boot 3.2.5**
- **PostgreSQL 16** (via Docker)
- **Spring Security** + JWT para autenticação
- **Spring Data JPA** para persistência
- **Springdoc OpenAPI** para documentação da API

## Pré-requisitos

- Java 21+
- Docker e Docker Compose
- Maven (ou use o wrapper `./mvnw`)
- Opcional (recomendado): `mise` para gerenciar versões de runtime

## Setup

### Opção 1: Setup com `mise` (recomendado)

1. **Instale o mise (se ainda não tiver):**
   ```bash
   curl https://mise.jdx.dev/install.sh | sh
   ```

2. **Adicione ao `~/.bashrc`:**
   ```bash
   eval "$(~/.local/bin/mise activate bash)"
   ```

3. **Recarregue o shell:**
   ```bash
   source ~/.bashrc
   ```

4. **Suba o banco e rode a aplicação:**
   ```bash
   docker-compose up -d
   mise run api
   ```

O Java 21 será ativado automaticamente com base no `mise.toml`.
O comando `mise run api` usa a task `api` definida no projeto e executa `./mvnw spring-boot:run`.

### Opção 2: Setup manual

1. **Inicie o banco de dados:**
   ```bash
   docker-compose up -d
   ```

2. **Execute a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Acesse a documentação da API:**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - OpenAPI JSON: http://localhost:8080/v3/api-docs

## Estrutura do Projeto

```
src/main/java/com/example/demo/
├── BicosApplication.java       # Entry point
└── bicos/
    ├── controller/             # REST endpoints
    │   └── dto/                # Data Transfer Objects
    ├── service/                # Business logic
    ├── repo/                   # JPA repositories
    ├── models/                 # JPA entities
    └── infra/config/           # Security, Swagger config
```

## Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/register` | Registrar usuário |
| POST | `/auth/login` | Login (retorna JWT) |
| GET | `/api/bicos` | Listar todos os bicos |
| POST | `/api/bicos/{userId}` | Criar bico para usuário |
| GET | `/api/users` | Listar usuários |

## Variáveis de Ambiente (Produção)

Copie o arquivo de exemplo e preencha com suas credenciais:
```bash
cp .env.example .env

## Link da API

https://bico-api.hml.defensoria.pa.def.br/
