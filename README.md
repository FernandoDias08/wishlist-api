# Wishlist-API
Sistema para armazenamento e gestão de Lista de Desejos de produtos.
O sistema foi escrito em JAVA 17 com Spring 3, utilizando como banco principal o MongoDB e um banco em memória Redis para armazenamento e controle de cache de consultas.

## Autores
![My Skills](https://skillicons.dev/icons?i=github)
- [@fernandoDias08](https://github.com/FernandoDias08) 


## Features
![My Skills](https://skillicons.dev/icons?i=java,spring,mongo,gradle,docker,redis)
- JAVA 17
- JUnit5
- Testcontainers
- JaCoCo
- Gradle
- Springboot 3.0.1
- MongoDB
- OpenAPI
- Docker
- Redis

## Padrão de Commits
![My Skills](https://skillicons.dev/icons?i=git)
- Por padrão, o Wishlist-API adota a convenção de commits descrita no link:
 https://www.conventionalcommits.org/en/v1.0.0/

## Testes
![My Skills](https://skillicons.dev/icons?i=java)
- Para realização de testes unitários, foram utilizados o JUnit 5, Mockito, Testcontainers e JaCoCo, cobrindo 100% da implementação das classes de Service.

## Executando o sistema local - Opção 1
![My Skills](https://skillicons.dev/icons?i=eclipse,java,docker)
- Para executar o projeto localmente, será necessário ter instalado o JAVA versão 17 ou superior e Docker para subir uma imagem de banco MongoDB e um servidor Redis.
- Com o JAVA e Docker já instalados, basta entrar na pasta raiz do projeto e executar o seguinte comando  para subir uma instância de banco MongoDB e uma instância de servidor Redis:
```docker-compose -f docker-compose-dev.yml up -d```

- Após execução do arquivo Docker, importar o projeto na IDE de sua preferência e executar como um projeto Java Application.
  
## Executando o sistema local - Opção 2
![My Skills](https://skillicons.dev/icons?i=docker)
- É possível executar o sistema localmente apenas através do Docker. Basta entrar na pasta raiz do projeto e executar o comando
 ```docker-compose build```. 

- Após criação da imagem, executar o comando ```docker-compose up``` para subir tanto a instância de banco MongoDB, o servidor Redis e a aplicação.

## Documentação OpenAPI - Swagger
![My Skills](https://skillicons.dev/icons?i=spring)
- Com a aplicação em execução, é possível acessar a documentação de API's via OpenAPI/Swagger através do link http://localhost:5000/wishlistapi/swagger-ui/index.html#/

## CI - CD 
![My Skills](https://skillicons.dev/icons?i=github,githubactions)
- Para os processos de CI - CD foi utilizado o Github Actions, criando a pipeline com 3 stages: test -> build -> deploy.
- O estágio de build depende da execução bem sucedida do stage de test. E para execução do estágio de deploy, o estágio de build precisa ser bem sucedido.
- O estágio de test irá executar todos os testes unitários do sistema.

## Infraestrutura
![My Skills](https://skillicons.dev/icons?i=docker,kubernetes)
- Ao executar o stage de build, uma imagem Docker com o sistema é gerada e armazenada no meu repositório Dockerhub.
- Em seguida, ao executar o stage de Deploy, o sistema irá buscar esta imagem no Dockerhub e executá-la em um cluster Kubernetes hospedado na Digital Ocean, onde permanecerá em execução.

## Cache
![My Skills](https://skillicons.dev/icons?i=redis)
- O sistema Wishlist-API conta com um banco em memória Redis, para armazenamento de cache de consultas recorrentes, com duração de 24 horas.
- Este cache é limpo sempre que um produto é adicionado ou removido da lista do usuário ou após o período de 24 horas sem alteração.
