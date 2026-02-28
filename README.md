# Company Manager - Fullstack

Aplicacao fullstack para gerenciamento de empresas e funcionarios, construida com **Spring Boot**, **Angular** e **PostgreSQL**, orquestrada com **Docker Compose**.

## Tecnologias

### Backend

- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- Flyway (migracao de banco de dados)
- PostgreSQL 16

### Frontend

- Angular 18 (Standalone Components)
- TypeScript 5.4
- RxJS

### Infraestrutura

- Docker & Docker Compose
- Nginx (servidor do frontend)
- pgAdmin 4

## Arquitetura

### Backend - Clean Architecture

O backend segue os principios de Clean Architecture, separando responsabilidades em camadas:

```
vellenich.joao.companyManager
├── application/
│   ├── service/          → Controllers REST
│   └── useCases/         → Interfaces (*UseCase) e implementacoes (*UseCaseHandler)
├── domain/
│   ├── entity/           → Entidades JPA (Company, Employee, CompanyEmployee)
│   ├── enums/            → EmployeeType (Pessoa Física, Pessoa Jurídica)
│   ├── exception/        → Excecoes de dominio
│   └── repository/       → Interfaces de repositorio Spring Data JPA
├── infrastructure/
│   ├── config/           → CORS, tratamento global de excecoes, filtro de logs
│   └── externalservices/ → Integracao com APIs externas (ViaCEP)
└── interfaces/
    └── rest/             → DTOs agrupados por dominio (company/, employee/)
```

### Frontend - Angular 18 Standalone

```
src/app/
├── components/
│   ├── company/           → Listagem de empresas
│   ├── company-create/    → Formulario de criacao de empresa
│   ├── company-detail/    → Detalhes da empresa e gerenciamento de funcionarios
│   ├── employee/          → Listagem de funcionarios
│   ├── employee-create/   → Formulario de criacao de funcionario
│   ├── employee-detail/   → Detalhes do funcionario
│   └── home/              → Pagina inicial
├── models/                → Interfaces TypeScript (Company, Employee, CepResponse)
├── services/              → Servicos HTTP (CompanyService, EmployeeService, CepService)
└── utils/                 → Utilitarios (formatacao de CPF e CNPJ)
```

### Banco de Dados

O esquema e gerenciado pelo Flyway com tres migracoes:

- `V001` - Tabela `companies` (id, cnpj, company_name, cep, state)
- `V002` - Tabela `employees` com ENUM `employee_type` (Pessoa Física / Pessoa Jurídica)
- `V003` - Tabela de relacionamento `company_employees` (N:N)

## Regras de Negocio

- Funcionarios do tipo **Pessoa Física** (Pessoa Fisica) exigem CPF, RG e data de nascimento
- Funcionarios do tipo **Pessoa Jurídica** (Pessoa Juridica) exigem apenas CNPJ
- Funcionarios do tipo **Pessoa Física** com menos de 18 anos nao podem ser adicionados a empresas no estado do **PR (Parana)**

## Funcionalidades

- CRUD completo de empresas e funcionarios
- Relacionamento N:N entre empresas e funcionarios
- Busca de endereco por CEP com preenchimento automatico
- Paginacao e filtros nas listagens
- Validacao de documentos (CPF/CNPJ)

## Como Executar com Docker

### Pre-requisitos

- [Docker](https://www.docker.com/) instalado
- [Docker Compose](https://docs.docker.com/compose/) instalado

### Subindo toda a stack

```bash
docker compose up -d
```

Isso ira iniciar os seguintes servicos:

| Servico    | URL                   | Descricao                           |
| ---------- | --------------------- | ----------------------------------- |
| Frontend   | http://localhost:4200 | Aplicacao Angular                   |
| Backend    | http://localhost:5001 | API Spring Boot                     |
| PostgreSQL | http://localhost:5432 | Banco de dados                      |
| pgAdmin    | http://localhost:5050 | Interface de gerenciamento do banco |

### Credenciais

**PostgreSQL:**

- Banco: `company_db`
- Usuario: `postgres`
- Senha: `postgres`

**pgAdmin:**

- Email: `admin@admin.com`
- Senha: `admin`

### Subindo apenas o banco de dados

Se quiser rodar o backend e frontend localmente e usar apenas o PostgreSQL via Docker:

```bash
docker compose up -d postgres
```

Depois, para rodar o backend e frontend separadamente:

```bash
# Backend (porta 8080)
cd backend
./mvnw spring-boot:run

# Frontend (porta 4200)
cd frontend
npm install
npm start
```

### Parando os servicos

```bash
docker compose down
```

Para remover tambem os volumes (dados do banco):

```bash
docker compose down -v
```

## API Endpoints

### Empresas (`/companyManager/api/companies`)

- `GET /` - Listar empresas (paginado, filtravel por nome/cnpj)
- `GET /{companyId}` - Detalhes da empresa
- `POST /` - Criar empresa
- `DELETE /{companyId}` - Deletar empresa
- `PUT /{companyId}/employee/add` - Adicionar funcionario a empresa
- `PUT /{companyId}/employee/remove` - Remover funcionario da empresa

### Funcionarios (`/companyManager/api/employees`)

- `GET /` - Listar funcionarios (paginado, filtravel por nome/documento)
- `GET /{employeeId}` - Detalhes do funcionario
- `POST /` - Criar funcionario
- `DELETE /{employeeId}` - Deletar funcionario
- `PUT /{employeeId}/company/add` - Adicionar empresa ao funcionario

### CEP (`/companyManager/api/cep`)

- `GET /{cep}` - Buscar endereco pelo CEP

## Sobre a Integracao de CEP: de cep.la para ViaCEP

Inicialmente, a busca de CEP foi implementada utilizando a API do **cep.la**. Porem, durante o desenvolvimento, a API do cep.la apresentou instabilidade e nao retornava respostas de forma consistente, impossibilitando o uso em producao.

Por esse motivo, a integracao foi migrada para a API do **[ViaCEP](https://viacep.com.br/)**, que oferece:

- **Estabilidade** - servico gratuito e confiavel, amplamente utilizado no mercado brasileiro
- **Simplicidade** - endpoint direto (`https://viacep.com.br/ws/{cep}/json/`) sem necessidade de autenticacao ou headers especiais
- **Resposta completa** - retorna CEP, UF, cidade, bairro e logradouro em formato JSON

A migracao envolveu a alteracao do `CepExternalService` no backend para apontar para a nova API e o ajuste dos DTOs para mapear os campos retornados pelo ViaCEP.
