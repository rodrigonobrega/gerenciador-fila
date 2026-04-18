# Gerenciador de Fila

API REST para gerenciar fila de atendimento com prioridade, usando Spring Boot e persistencia em banco H2 em memoria.

## Tecnologias utilizadas

- Java 21
- Spring Boot 3.5.13
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Cache
- Banco H2 (runtime)
- Lombok
- Maven

## Pre-requisitos

- JDK 21 instalado
- Maven instalado (versao compativel com Java 21)

## Como rodar

### 1) Rodar em modo desenvolvimento

```bash
mvn clean spring-boot:run
```

A aplicacao sobe por padrao em:

- API: `http://localhost:8080`
- Console H2: `http://localhost:8080/h2-console`

Configuracoes do H2 (conforme `application.properties`):

- JDBC URL: `jdbc:h2:mem:filadb`
- Usuario: `sa`
- Senha: (vazia)

### 2) Gerar jar e executar

```bash
mvn clean package
java -jar target/gerenciador-fila-1.0.jar
```

## Collection Postman

O projeto inclui uma collection pronta para importacao no Postman:

- Arquivo: `gerenciador-fila.postman_collection.json`

### Como importar

1. Abra o Postman e clique em **Import**.
2. Selecione **Upload Files**.
3. Escolha o arquivo `gerenciador-fila.postman_collection.json` na raiz do projeto.

### Variaveis da collection

- `baseUrl`: `http://localhost:8080`
- `pessoaId`: `1`

## Dados iniciais

Ao iniciar a aplicacao, o arquivo `src/main/resources/data.sql` popula a base em memoria com registros iniciais para testes.

## Estrutura do projeto

```text
gerenciador-fila/
|-- pom.xml
|-- README.md
|-- gerenciador-fila.postman_collection.json
|-- src/
|   |-- main/
|   |   |-- java/
|   |   |   `-- br/gov/caixa/gerenciadorfila/
|   |   |       |-- Main.java
|   |   |       |-- controller/
|   |   |       |-- service/
|   |   |       |-- repository/
|   |   |       |-- entity/
|   |   |       |-- dto/
|   |   |       |-- enums/
|   |   |       `-- exception/
|   |   `-- resources/
|   |       |-- application.properties
|   |       `-- data.sql
|   `-- test/
`-- target/
```

## Camadas

- `controller`: endpoints REST
- `service`: regras de negocio da fila
- `repository`: acesso a dados via Spring Data JPA
- `entity`: entidades persistidas
- `dto`: contratos de entrada e saida da API
- `exception`: tratamento global e excecoes de dominio
- `enums`: tipos e status usados no fluxo de atendimento
