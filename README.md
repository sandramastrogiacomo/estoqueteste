# 📦 Sistema de Controle de Estoque

Este projeto é um sistema simples de controle de estoque desenvolvido em **Java Spring Boot**, com operações CRUD, testes unitários e relatório de cobertura de código com JaCoCo.

## 🛠 Tecnologias

- Java 17
- Spring Boot 3.4.4
- JUnit 5
- Maven
- JaCoCo
- IntelliJ IDEA

## 📂 Estrutura

```
src
├── main
│   └── java
│       └── com.example.estoque
│           ├── controller
│           ├── domain
│           ├── entity
│           ├── exception
│           ├── repository
│           └── service
└── test
    └── java
        └── com.example.estoque
```

## ✅ Funcionalidades

- Cadastro de produtos
- Atualização de estoque
- Exclusão de produtos
- Consulta por nome e ID
- Listagem de produtos
- Validação de quantidade insuficiente
- Tratamento de exceções personalizadas

## 🧪 Testes

Foram implementados testes unitários e de integração com **JUnit 5**, cobrindo os seguintes cenários:

- Criação e atualização de produtos
- Atualização de estoque com quantidade válida e inválida
- Exclusão e busca por ID ou nome
- Listagem geral
- Exceções personalizadas

### 📈 Cobertura de Código

- Linhas cobertas: **64%**
- Métodos cobertos: **74%**
- Branches cobertos: **66%**

### 🔍 Relatório HTML (JaCoCo)

Para visualizar o relatório de cobertura de testes:

1. Execute:

```bash
mvn clean test
mvn jacoco:report
```

2. Abra o arquivo:

```bash
target/site/jacoco/index.html
```

## 🚀 Como Executar

1. Clone o projeto:

```bash
git clone https://github.com/seu-usuario/estoqueteste.git
cd estoqueteste
```

2. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

## 👩‍💻 Desenvolvido por

Sandra Mastrogiacomo  
[LinkedIn](https://www.linkedin.com/in/sandramastrogiacomo) | [GitHub](https://github.com/seu-usuario)

## 📄 Licença

Este projeto está sob a licença MIT.
