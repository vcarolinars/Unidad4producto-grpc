# Unidad 4 -Producto gRPC

Backend desarrollado con Java 21 y Spring Boot que implementa servicios **gRPC*z para gestionar productos mediante operaciones CRUD.

El proyecto utiliza servicios, **Spring Data JPA / Hibernate** como ORM y **PostgreSQL** como base de datos.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- gRPC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Postman

## Arquitectura

El proyecto está organizado en capas para separar las responsabilidades de la aplicación:

```text
Cliente gRPC -> ProductoGrpcService-> ProductoService -> ProductoRepository ->PostgreSQL
