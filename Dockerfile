# Usar uma imagem base com OpenJDK 17
FROM openjdk:17-jdk-alpine

# Instalar Maven
RUN apk add --no-cache maven

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos do projeto para o diretório de trabalho
COPY . .

# Executa o Maven para construir o JAR
RUN mvn clean package -DskipTests

# Copia o JAR gerado para a imagem final
COPY target/empiricus-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que sua aplicação está utilizando
EXPOSE 8080

# Define o comando de inicialização do container
ENTRYPOINT ["java", "-jar", "empiricus-0.0.1-SNAPSHOT.jar.jar"]
