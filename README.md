# Dinning Organizer Application

**Guia completo para uso local (modo _dev_)**

## Visão geral

O **Dinning Organizer** é uma API REST que realiza CRUDs em um banco de dados Postgres, criando um usuário, atualizando o usuário, atuilzando senha de usuário, realizando logins e removendo o usuário.

## Pré-requisitos

| Ferramenta           | Observações                                                                                                             |
|----------------------|-------------------------------------------------------------------------------------------------------------------------|
| **Arquivo JAR**      | Compilar código e gerar arquivo JAR                                                                                     |
| **Portas livres**    | - 5437 para acesso externo ao Postgres (localhost:5437 → container:5432); <br/>- 8080 para a sua aplicação Spring Boot. |

## Passo a passo de execução
* Subir banco de dados primeiro:
  * `docker-compose up -d postgres`
---  
* Build do JAR
  * `mvn clean package`
---  
* Subir e buildar containers
  * `docker-compose up --build -d`
---
* Checar logs:
  * `docker-compose logs -f app`

## Link para testes no Swagger:
* http://localhost:8080/swagger-ui/index.html
* Para testar as requisições via postman, as collections estão em:
  * ./postman-collection/dining-organizer.postman_collection.json

