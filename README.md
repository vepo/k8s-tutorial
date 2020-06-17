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