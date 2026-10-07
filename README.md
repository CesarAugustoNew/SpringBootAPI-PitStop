# PitStop Clean Car — API REST

API Back-end desenvolvida em Java com Spring Boot para o gerenciamento de um lava-rápido, com o objetivo de centralizar clientes, veículos, funcionários e ordens de serviço em um único sistema, no lugar de cadernos e planilhas.

A API registra cada lavagem desde a **entrada do veículo até a entrega**, guarda qual funcionário fez o atendimento, calcula o **resultado do dia** (ordens, status e faturamento) e protege tudo com **autenticação JWT** e controle de acesso por perfil. Ela é consumida pelo Front-end em React do projeto **Projeto-PitStop**.

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" alt="Spring Security">
  <img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white" alt="JWT">
  <img src="https://img.shields.io/badge/JPA%20%2F%20Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white" alt="JPA Hibernate">
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/Swagger%20%2F%20OpenAPI-85EA2D?style=flat-square&logo=swagger&logoColor=black" alt="Swagger">
</p>

---

## 🚀 Funcionalidades

### 🔐 Autenticação

- Login com e-mail e senha em `POST /api/auth/login`
- Devolve um **token JWT** (válido por 24 horas) e os dados do usuário
- Senhas armazenadas com **BCrypt**
- Sessão **stateless**: nenhuma sessão fica guardada no servidor

### 👤 Usuários

Gerenciamento das contas de acesso da equipe, **somente para ADMIN**:

- Listagem, busca por ID, criação e remoção
- Cargos **ADMIN** e **FUNCIONARIO**
- E-mail único e senha com no mínimo 6 caracteres
- Na primeira execução, a API cria sozinha o **administrador inicial**

### 👥 Clientes

- Cadastro com nome, telefone e endereço
- Listagem, busca por ID, atualização e remoção

### 🚘 Veículos

- Cadastro com placa, modelo, marca e **cliente dono do veículo**
- Placa **única** e sempre salva em maiúsculas
- Listagem completa ou **filtrada por cliente** (`?clienteId=`)
- Atualização e remoção

### 🧽 Ordens de serviço

O registro do dia a dia do lava-rápido:

- Abertura de uma ordem escolhendo cliente, veículo, tipo de serviço, valor e (opcionalmente) os objetos de valor deixados no carro
- O **funcionário responsável é o usuário logado**, extraído do token. O Front-end não precisa enviá-lo, o que mantém o registro confiável
- Toda ordem nasce com o status `RECEBIDO` e a data/hora de entrada é gravada automaticamente
- Atualização do status ao longo da lavagem
- Listagem completa ou **filtrada por status** (`?status=`)

### 📊 Resultado do dia

Endpoint de resumo de uma data (padrão: hoje):

- Total de ordens do dia
- Quantidade de ordens em cada status
- **Faturamento total** do dia
- Quantidade e faturamento **agrupados por tipo de serviço**

---

## 🧮 Regras de negócio

### Ciclo de uma ordem de serviço

```text
RECEBIDO  →  EM_LAVAGEM  →  FINALIZADO  →  ENTREGUE
```

| Status | Significado |
| --- | --- |
| `RECEBIDO` | Veículo chegou e aguarda |
| `EM_LAVAGEM` | Serviço em execução |
| `FINALIZADO` | Serviço concluído, aguardando retirada |
| `ENTREGUE` | Veículo entregue ao cliente |

### Tipos de serviço

`LAVAGEM_SIMPLES` · `LAVAGEM_COMPLETA` · `HIGIENIZACAO` · `POLIMENTO` · `CRISTALIZACAO`

### Perfis de acesso

| Perfil | Permissões |
| --- | --- |
| **ADMIN** | Acesso total, incluindo o gerenciamento de usuários (`/api/usuarios`) |
| **FUNCIONARIO** | Clientes, veículos, ordens de serviço e resultado do dia |

### Validações

- Uma ordem só é aberta se o **veículo pertencer ao cliente informado**
- Não é possível cadastrar duas vezes a mesma **placa** nem o mesmo **e-mail** de usuário
- O valor da ordem não pode ser negativo
- O e-mail de login precisa ter formato válido
- Campos obrigatórios em branco retornam erro `400` indicando cada campo inválido

### Indicadores do resultado do dia

| Indicador | Como é calculado |
| --- | --- |
| Total de ordens | Ordens com data de entrada entre 00:00 do dia escolhido e 00:00 do dia seguinte |
| Por status | Contagem das ordens do dia em cada um dos quatro status |
| Faturamento total | Soma do valor de todas as ordens do dia |
| Por tipo de serviço | Quantidade e soma do valor, agrupadas por tipo |

---

## 🏗️ Arquitetura

A API segue uma arquitetura em camadas:

```text
                    ┌───────────────────┐
                    │  Front-end React  │
                    └─────────┬─────────┘
                              │  HTTP / REST + JWT
                              ▼
                    ┌───────────────────┐
                    │    JwtFilter      │  valida o token
                    │ Spring Security   │  e as permissões
                    └─────────┬─────────┘
                              ▼
                    ┌───────────────────┐
                    │    Controller     │  rotas e validação (DTOs)
                    └─────────┬─────────┘
                              ▼
                    ┌───────────────────┐
                    │      Service      │  regras de negócio
                    └─────────┬─────────┘
                              ▼
                    ┌───────────────────┐
                    │    Repository     │  Spring Data JPA
                    └─────────┬─────────┘
                              │  Hibernate
                              ▼
                    ┌───────────────────┐
                    │    PostgreSQL     │
                    └───────────────────┘
```

### Fluxo de autenticação

```text
E-mail + senha
      │
      ▼
POST /api/auth/login
      │
      ▼
Validação das credenciais (BCrypt)
      │
      ▼
Geração do JWT (24h)
      │
      ▼
Front-end guarda o token
      │
      ▼
Authorization: Bearer <token>
      │
      ▼
JwtFilter → Spring Security → endpoint protegido
```

| Tabela | Principais campos |
| --- | --- |
| `usuarios` | id, nome, email (único), senha (BCrypt), role |
| `clientes` | id, nome, telefone, endereco |
| `veiculos` | id, placa (única), modelo, marca, cliente_id |
| `ordens_servico` | id, cliente_id, veiculo_id, funcionario_id, tipo_servico, status, objetos_valor, valor, data_entrada |

Os identificadores são **UUID**, e o esquema é criado e atualizado automaticamente pelo Hibernate.

### Estrutura de pastas

```text
SpringBootAPI-PitStop/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/main/
    ├── resources/
    │   └── application.yaml
    └── java/br/com/pitstop/spring_boot_clean_car/
        ├── SpringBootCleanCarApplication.java
        ├── config/
        │   ├── SecurityConfig.java      # rotas públicas, perfis e CORS
        │   ├── SwaggerConfig.java       # documentação OpenAPI
        │   └── DataInitializer.java     # cria o ADMIN inicial
        ├── controller/                  # endpoints REST
        ├── service/                     # regras de negócio
        ├── repository/                  # acesso ao banco (JPA)
        ├── entity/                      # Usuario, Cliente, Veiculo, OrdemServico
        ├── dto/
        │   ├── request/                 # dados de entrada validados
        │   └── response/                # dados de saída
        ├── enums/                       # Role, StatusOrdem, TipoServico
        ├── security/                    # JWT, filtro e UserDetails
        └── exception/                   # tratamento global de erros
```


## 🛠️ Tecnologias

- Java 21
- Spring Boot 3.5 (Spring Web)
- Spring Security + JWT (jjwt)
- Spring Data JPA + Hibernate
- PostgreSQL
- Bean Validation
- Lombok
- Maven
- Swagger / OpenAPI (springdoc)
- Spring Boot DevTools

- O CORS está liberado para qualquer origem (`*`). Em produção, restrinja ao endereço do Front-end.
- O Swagger é público. Se necessário, restrinja ou desative em produção.
- Os dados ficam no PostgreSQL. Em planos gratuitos, confira a política de expiração do banco do provedor.
