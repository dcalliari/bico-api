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

## Setup

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

## Configuração de Variáveis de Ambiente

Copie o arquivo de exemplo e preencha com suas credenciais:
```bash
cp .env.example .env
