# Kafka - Avro- Order System


## What this repo contains
- Producer that emits Avro `Order` messages every second
- Consumer that updates a running average and simulates transient failures
- Retry consumer that retries up to 3 times, then forwards to DLQ
- DLQ consumer that logs failed messages


## Requirements
- Java 17+
- Maven
- Docker & Docker Compose (to run Kafka & Schema Registry)


## Run
1. Start Kafka + Schema Registry


```bash
docker-compose up -d
```


2. Build and run the Spring Boot app


```bash
mvn clean package
java -jar target/kafka-avro-sample-0.0.1-SNAPSHOT.jar
```


3. Watch logs: you will see produced messages, consumed messages, retries and DLQ transfers.


## Notes
- Avro classes are generated at `target/generated-sources/avro` using the Avro Maven Plugin.
- Adjust schema registry URL in `application.yml` if needed.
