# Usando a imagem oficial do OpenJDK 17 como base
FROM gradle:8.2.1-jdk17 AS builder

# Definindo o diretório de trabalho para /home/gradle
WORKDIR /home/gradle

# Copiando os arquivos de configuração Gradle
COPY build.gradle settings.gradle ./

# Copiando o diretório gradle
COPY gradle/ gradle/

# Copiando o wrapper Gradle
COPY gradlew ./

# Copiando o diretório de código-fonte
COPY src/ src/

# Construindo o projeto usando o Gradle
RUN ./gradlew clean build

# Usando a imagem oficial do OpenJDK 17 como base
FROM openjdk:17-jdk-slim

# Definindo variáveis de ambiente para a conexão com o MongoDB
ENV MONGODB_URI=mongodb://mongo:27017/wishlist_db

# Definindo o diretório de trabalho para /app
WORKDIR /app

# Copiando o jar construído a partir do estágio de construção anterior
COPY --from=builder /home/gradle/build/libs/wishlist-api.jar ./

# Copiando o arquivo application.properties modificado com as variáveis de ambiente
COPY ./src/main/resources/application.properties ./

# Expondo a porta 5000
EXPOSE 5000

# Comando de inicialização da aplicação
CMD ["java", "-jar", "wishlist-api.jar"]
