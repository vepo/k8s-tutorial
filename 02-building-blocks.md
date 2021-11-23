# Building Blocks

[Anterior](01-criando-imagens-docker.md) [Próximo](/03-criando-pods.md)

O Kubernete tem vários componente que devemos usar para contruir nosso cluster. Aqui vamos listar apenas os básicos, a documentação pode ser conferida no [documentação oficial](https://kubernetes.io/docs/home/).

| Nome | Definição |
|------|------------|
| POD | PODs são a menor peça de um deploy que pode ser criado e gerenciado pelo Kubernetes. |
| Service | Um serviço é uma abstração, significa que uma aplicação ou um conjunto de PODs está exposto como um serviço de rede. |
| Deployments | Um Deployment provê uma forma declarativa de controlar PODs e ReplicaSets. |
| ReplicaSet | O objetivo de um ReplicaSet é manter um conjunto estável réplicas de pods em execução a qualquer momento. Como tal, costuma ser usado para garantir a disponibilidade de um número especificado de pods idênticos. |
| DaemonSet | Garante a execução de um POD em todos (ou apenas alguns) nós. |
| Ingress | Um objeto API que gerencia o acesso externo aos serviços em um cluster, normalmente HTTP. |

Todos os componentes de um cluster Kubernetes são declarados através de arquivos YAML e ao serem aplicados ao cluster são transformados em serviços em execução.