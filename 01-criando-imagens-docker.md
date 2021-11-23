# Criando imagens Docker

[Anterior](/README.md) [Próximo](/02-building-blocks.md)

Nesse tutorial vamos criar dois serviços, para isso, vamos criar duas imagens docker. O código deles será feito usando o Quarkus.io e um framework antigo chamado Thorntail, ambos usando as especificações Jakarta EE.

Você não precisa ter nada além do docker instalado na sua máquina para construir essas imagens. 

```bash
docker build backend/ -t backend
docker build frontend/ -t frontend
```

Com essas imagens contruidas, podemos agora ver se o serviço está funcionando corretamente: 

```bash
docker network create -d bridge network
docker run -d --rm --network network --name backend backend
docker run -d --rm --network network -p 8080:8080 --env BACKEND_URL="http://backend:8080" --name frontend frontend
```

Verifique o acesso com http://localhost:8080/index.xhtml

Agora com as imagens funcionando, é preciso carregar elas no hub.docker.com. Para isso execute os comandos abaixo:

```bash
docker build backend/ -t backend
docker tag backend vepo/backend:1.0.0
docker push vepo/backend:1.0.0

docker build frontend/ -t frontend
docker tag frontend vepo/frontend:1.0.0
docker push vepo/frontend:1.0.0
```

Observação: Caso queira executar os comandos acima, você precisa criar um usuário em hub.docker.com e usar seu nome de usuário ao invés `vepo`.