# API RESTful feita em Spring Boot - algamoney-api -

## Requisitos e Ferramentas utilizadas

### Requisitos 

- Java Superior ***VERSION `8`***
- Spring Boot ***VERSION `2.2.6`***
- Banco de Dados MYSQL 80 
- Necessário preencher corretamente os dados do seu servidor SMTP
   ```properties
      algamoney.mail.host=SERVIDORSMTP
      algamoney.mail.port=PORTA
      algamoney.mail.username=USERNAME
      algamoney.mail.password=PASSWORD
    ```
- Necessário preencher corretamente as suas Creendências do AWS
  ```properties
    algamoney.s3.access-key-id=ID_ACCESS_KEY
    algamoney.s3.secret-access-key=ID_SECRET_KEY
    ```
 
 ### Tecnologias Utilizadas
 - Flyway
 - Thymeleaf
 - Hibermate
 - JasperSoft
 - Jackson
 - Swagger
 - JWT
 - Spring Security Oauth2

## Funcionalidades Desenvolvidas Durante o período do curso...

### Dashboard
  - Geração de dados de estásticos de acordo com a Categoria ou Dia.
  
### Relatórios
 - Geração de relatórios de acordo com intervalos de datas.
 
### Anexo de Documento
 - Anexação de documento na AWS.
 
### Seguança Oauth2 
  - Geração de access_token Token Jwt e validação do access_token pelo refresh_token.

## Funcionalidades Desenvolvidas com o objetivo de melhora do projeto...

### Swagger Ui
  - Criação da Documentação da API 
