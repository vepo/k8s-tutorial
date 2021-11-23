# Criando Deployments

[Anterior](/04-expondo-servico.md) [Próximo](/06-usando-o-helm.md)


Quando falamos de Deployment, estamos falando de estado desejável. Um POD é uma instância de algo rodando, um Deployment vai definir o quanto desejamos de um POD. O Deployment vai declarar como deve ser os PODs, quantas instâncias teremos dele. O Deplyoment não escala automáticamente, quem faz isso é o [Horizontal Pod Autoscaler](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/), mas o Deployment que será atualizado quando se precisa escalar.


A definição do deployment deve conter a especificação dos PODs que ele vai controlar. No exemplo abaixo, já a definição do POD em `spec.template.spec` que deve ser compatível com o selector.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend-deployment
spec:
  selector:
    matchLabels:
      app: k8s-tutorial
      role: backend
  replicas: 10
  template:
    metadata:
      labels:
        app: k8s-tutorial
        role: backend
    spec:
      containers:
      - name: backend
        image: vepo/backend:1.0.0
        ports:
        - containerPort: 8080
```

No arquivo [k8s/deployments/all.yaml](/k8s/deployments/all.yaml) temos nossa aplicação definida usando dois Deployment e dois serviços. Para aplicar:


```bash
$ kubectl apply -f ./k8s/deployments/all.yaml
deployment.apps/backend-deployment created
service/backend-svc created
deployment.apps/frontend-deployment created
service/frontend-svc created

```

Observe que foram criados um POD de frontend e dois PODs para a aplicação backend;

```bash
$ kubectl get pods
NAME                                  READY   STATUS    RESTARTS   AGE
backend-deployment-f68cbd9f4-gfpjq    1/1     Running   0          6s
backend-deployment-f68cbd9f4-jw768    1/1     Running   0          6s
frontend-deployment-6cd9f8d5f-7gwrt   1/1     Running   0          6s

```

Se quisermos escalar ela, podemos usar o comando scale do kubectl.

```bash
$ kubectl scale deployment backend-deployment --replicas=5
deployment.apps/backend-deployment scaled

```

Com isso ficamos com 5 instâncias do mesmo serviço. 

```bash
$ kubectl get pods
NAME                                  READY   STATUS    RESTARTS   AGE
backend-deployment-f68cbd9f4-cb2f2    1/1     Running   0          39s
backend-deployment-f68cbd9f4-gfpjq    1/1     Running   0          2m3s
backend-deployment-f68cbd9f4-jw768    1/1     Running   0          2m3s
backend-deployment-f68cbd9f4-sfth8    1/1     Running   0          39s
backend-deployment-f68cbd9f4-t4qt9    1/1     Running   0          39s
frontend-deployment-6cd9f8d5f-7gwrt   1/1     Running   0          2m3s

```

Como usamos o protocolo HTTP, cada requisição será [distribuida](https://kubernetes.io/docs/concepts/services-networking/service/#proxy-mode-ipvs) entre todos os PODs.