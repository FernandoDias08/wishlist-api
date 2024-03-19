# Use a imagem oficial do OpenJDK 17 como base
FROM gradle:8.2.1-jdk17 AS builder

# Defina o diretório de trabalho para /home/gradle
WORKDIR /home/gradle

# Copie os arquivos de configuração Gradle
COPY build.gradle settings.gradle ./

# Copie o diretório gradle
COPY gradle/ gradle/

# Copie o wrapper Gradle
COPY gradlew ./

# Copie o diretório de código-fonte
COPY src/ src/

# Construa o projeto usando o Gradle
RUN ./gradlew clean build

# Use a imagem oficial do OpenJDK 17 como base
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho para /app
WORKDIR /app

# Copie o jar construído a partir do estágio de construção anterior
COPY --from=builder /home/gradle/build/libs/wishlist-api.jar ./

# Exponha a porta 5000
EXPOSE 5000

# Comando de inicialização da aplicação
CMD ["java", "-jar", "wishlist-api.jar"]
