# 🚗 PitStop Clean Car — API

API RESTful em **Java + Spring Boot** para gerenciar um lava-rápido: cadastro de clientes e veículos, abertura e acompanhamento de ordens de serviço, e um dashboard com o resultado do dia. Dois tipos de usuário: **ADMIN** e **FUNCIONARIO**.

---

## 🛠 Tecnologias

- Java 21
- Spring Boot 3.5 / Spring Security 6
- Spring Web, Spring Data JPA (Hibernate)
- JWT (autenticação stateless)
- PostgreSQL
- Maven
- Lombok
- Swagger / OpenAPI (springdoc)

---

## ▶️ Como rodar

### Pré-requisitos
- JDK 21+
- Maven (ou `./mvnw` incluso)
- PostgreSQL rodando localmente na porta `5432`

### 1. Criar o banco de dados
```sql
CREATE DATABASE "CleanCar";
```
As tabelas são criadas automaticamente (`ddl-auto: update`).

### 2. Ajustar credenciais, se necessário
Em `src/main/resources/application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/CleanCar
    username: postgres
    password: SuaSenha
```

### 3. Subir a aplicação
```bash
./mvnw spring-boot:run
```
A API sobe em `http://localhost:8080`.

### 4. Usuário admin inicial
Criado automaticamente na primeira execução:
- **E-mail:** `admin@pitstop.com`
- **Senha:** `Admin@134`

Use-o para logar e, a partir dele, cadastrar funcionários em `/api/usuarios`.

---

## 📖 Swagger

```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Papéis de usuário

| Papel        | Pode fazer |
|--------------|------------|
| **ADMIN**       | Tudo que o FUNCIONARIO pode, além de gerenciar contas (`/api/usuarios`) |
| **FUNCIONARIO** | Cadastrar clientes e veículos, abrir/atualizar ordens de serviço, ver o dashboard do dia |

---

## 🔌 Endpoints

| Recurso   | Método | Rota                              | Acesso            |
|-----------|--------|------------------------------------|-------------------|
| Auth      | POST   | `/api/auth/login`                  | Público           |
| Usuários  | GET/POST/DELETE | `/api/usuarios...`         | ADMIN             |
| Clientes  | GET/POST/PUT/DELETE | `/api/clientes...`    | Autenticado       |
| Veículos  | GET/POST/PUT/DELETE | `/api/veiculos...`    | Autenticado       |
| Veículos  | GET    | `/api/veiculos?clienteId=`        | Autenticado       |
| Ordens    | GET/POST/DELETE | `/api/ordens...`           | Autenticado       |
| Ordens    | PATCH  | `/api/ordens/{id}/status`         | Autenticado       |
| Dashboard | GET    | `/api/dashboard` ou `?data=`      | Autenticado       |

Fluxo de status de uma ordem de serviço: `RECEBIDO` → `EM_LAVAGEM` → `FINALIZADO` → `ENTREGUE`.

---

## 📊 Dashboard ("resultado do dia")

`GET /api/dashboard` retorna, para o dia atual (ou para a data passada em `?data=AAAA-MM-DD`):
- total de ordens abertas no dia
- quantas estão em cada status
- faturamento total do dia
- faturamento e quantidade por tipo de serviço

> O front-end que vai consumir esse endpoint ainda não foi criado — o endpoint já está pronto para quando ele existir.

---

## 📂 Estrutura

```
src/main/java/br/com/pitstop/spring_boot_clean_car
├── controller
├── service
├── repository
├── entity
│   └── enums
├── dto
│   ├── request
│   └── response
├── security      # JWT, UserDetails, filtro
├── config        # segurança, swagger, dados iniciais
└── exception     # exceções + tratamento global
```

---

