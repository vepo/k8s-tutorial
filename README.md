# Kubernetes & Helm Tutorial

## Requirements

* Java 8
* Maven
* Docker
* Minikube

## The idea

We should create two services.

Then create a Docker image for each service and run both in containers.

Then create PODs for each one.

Then create Services for both.

The create a Deployment for both.

## Executing each service

### Frontend

```bash
mvn clean thorntail:run
```

### Backend

```bash
mvn clean thorntail:run -Dthorntail.http.port=8081
```

Then access: http://localhost:8080/index.xhtml

## Building Docker Images

```bash
docker build backend/ -t backend
docker build frontend/ -t frontend
docker network create -d bridge network
docker run -d --rm --network network --name backend backend
docker run -d --rm --network network -p 8080:8080 --env BACKEND_URL="http://backend:8080" --name frontend frontend
```

Then access: http://localhost:8080/index.xhtml