FROM eclipse-temurin:11.0.12_7-jdk
COPY ./build/libs/pgtask-0.1-all.jar /app/
ENTRYPOINT ["java"]
CMD ["-jar", "/app/pgtask-0.1-all.jar"]
