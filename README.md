# Projeto de gerenciamento de cadastro de clientes e envio de e-mail.

Este projeto é uma aplicação desenvolvida com Spring Boot para gerenciar o cadastro de clientes. Ele utiliza um banco de dados relacional para armazenar os dados dos clientes e inclui funcionalidades de envio de e-mail assíncrono para notificações e confirmações.

## Índice

- [Sobre](#sobre)
- -[Requisitos](#requisitos)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Uso Endpoints](#uso)

## Sobre    

O projeto tem como objetivo fornecer uma solução para a gestão de clientes. As principais funcionalidades incluem:

- Cadastro e atualização de informações dos usuários.
- Armazenamento dos dados em um banco de dados relacional.
- Envio de e-mails assíncronos.

## Requisitos

Para inicializar o projeto é apenas necessário ter o Docker instalado em sua máquina(caso for no Windows, certifique-se que o Docker Desktop está aberto antes de subir a aplicação).

## Tecnologias Utilizadas

- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **Banco de Dados Relacional**: PostgreSQL
- **Spring Data JPA**: Para interação com o banco de dados.
- **Spring Mail**: Para envio de e-mails.
- **Mockito/Junit**: Para testes unitários, no qual atigiu a cobertura de 87%!
- **H2 Database**: [Se aplicável] Banco de dados em memória para testes.
- **Docker**: Para poder realizar a segmentação em containers da aplicação e do banco de dados, consequentemente facilitar a execução da aplicação em qualquer máquina.

  
## Instalação

Siga os passos abaixo para configurar o projeto localmente:

1. **Clone o repositório:**

    ```bash
    git clone https://github.com/FelipeFantato/empiricus.git
    ```

2. **Navegue até o diretório do projeto:**

    ```bash
    cd nome-do-repositorio
    ```

Para iniciar a aplicação, execute o seguinte comando:


```bash
docker-compose up
```

Após a inicialização, importe para o Postman o arquivo empiricus.postman_collection

## Uso Endpoints


Para poder utilizar os endpoints é necessário inserir a chave JWT gerada ao logar(/login),
enviando sempre a chave como um Header Authorization.

Demais instruções apenas seguir os modelos no collection no postman :)


Qualquer dúvida estou a disposição!



