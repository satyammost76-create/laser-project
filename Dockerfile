FROM eclipse-temurin:21git add .
                       git commit -m "java 21 fix"
                       git push -f origin main

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean install

EXPOSE 8080

CMD ["java","-jar","target/laser-project-0.0.1-SNAPSHOT.jar"]
