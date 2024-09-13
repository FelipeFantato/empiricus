# Use uma imagem base com Java 17
FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR gerado da aplicação para o diretório de trabalho
COPY target/empiricus-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que sua aplicação está utilizando (modifique se necessário)
EXPOSE 8080

# Define o comando de inicialização do container, que irá rodar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]
