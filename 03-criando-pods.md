# Criando PODs

[Anterior](/02-building-blocks.md) [Próximo](/04-expondo-servico)

PODs são a unidade básica de um cluster Kubernetes. Eles são compostos um ou mais containers que compartilham os mesmos recursos.

No exemplo abaixo vemos os dois a definilão dos dois PODs que implementarão os nossos serviços.


```yaml
apiVersion: v1
kind: Pod
metadata:
  name: backend
  labels:
    app: k8s-tutorial
    version: v1
    role: backend
    example: k8s
spec:
  containers:
  - name: backend
    image: vepo/backend:1.0.0
    ports:
    - containerPort: 8080
---
apiVersion: v1
kind: Pod
metadata:
  name: frontend
  labels:
    app: k8s-tutorial
    version: v1
    role: frontend
    example: k8s
spec:
  containers:
  - name: frontend
    image: vepo/frontend:1.0.0
    ports:
    - containerPort: 8080
```

Para criar esses PODs execute:

```bash
$ kubectl apply -f ./k8s/pods/all.yaml
pod/backend created
pod/frontend created

```

Para verificar os PODs em execução:

```bash
$ kubectl get pods -A
NAMESPACE     NAME                               READY   STATUS    RESTARTS   AGE
default       backend                            1/1     Running   0          9m32s
default       frontend                           1/1     Running   0          9m32s
kube-system   coredns-558bd4d5db-72n9p           1/1     Running   0          41m
kube-system   etcd-minikube                      1/1     Running   0          41m
kube-system   kube-apiserver-minikube            1/1     Running   0          41m
kube-system   kube-controller-manager-minikube   1/1     Running   0          41m
kube-system   kube-proxy-m8wxg                   1/1     Running   0          41m
kube-system   kube-scheduler-minikube            1/1     Running   0          41m
kube-system   storage-provisioner                1/1     Running   1          41m

```

Observe que nossos PODs estão rodando no namespace `default`, enquanto outros PODs estão rodando no namespace `kube-system`. Cada um desses PODs tem função diferente, os pods do `kube-system` é o que chamamos de _Control Plane_, eles implementam a lógica de gerenciamento do cluster. Enquanto nossos PODs implementa a lógica da aplicação, por isso são chamados de _Application Plane_.

Os logs dos PODs estão acessíveis pelos comandos `kubectl logs`.

```bash
$ kubectl logs backend
__  ____  __  _____   ___  __ ____  ______
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/
2021-11-22 18:41:18,941 INFO  [io.quarkus] (main) backend 1.0.0-SNAPSHOT on JVM (powered by Quarkus 2.5.0.Final) started in 1.539s. Listening on: http://0.0.0.0:8080
2021-11-22 18:41:19,025 INFO  [io.quarkus] (main) Profile prod activated.
2021-11-22 18:41:19,026 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy, resteasy-jsonb, resteasy-mutiny, smallrye-context-propagation, vertx]

```

O POD front-end vai encontrar o POD back-end através de uma variável de ambiente, para declarar essa variável, é preciso definir ela na especificação do POD. Vamos supor que o pod back-end pode ser acessível através da URL `http://backend-svc.default.svc.cluster.local:8080` para isso altere o POD para:


```yaml
apiVersion: v1
kind: Pod
metadata:
  name: frontend
  labels:
    app: k8s-tutorial
    version: v1
    role: frontend
    example: k8s
spec:
  containers:
  - name: frontend
    image: vepo/frontend:1.0.0
    ports:
    - containerPort: 8080
    env:
    - name: BACKEND_URL
      value: "http://backend-svc.default.svc.cluster.local:8080"
```

Para aplicar essa alteração, é preciso primeiro remover os PODs antigos e depois aplicar novamente:

```
$ kubectl delete -f ./k8s/pods/all.yaml
pod "backend" deleted
pod "frontend" deleted

$ kubectl apply -f ./k8s/pods/all-with-env.yaml
pod/backend created
pod/frontend created
```

A remoção de qualquer recurso Kubernetes é feita usando apenas o nome e namespace do recurso, logo qualquer um dos arquivos acima poderiam ser utilizados.

Por fim, como acessamos os PODs? Podemos acessar via linha de comando, mas não conseguimos acessar a interface HTTP porque ela só é habilitada por um serviço.

```bash
$ kubectl exec -it frontend -- sh
/ # ls
app    bin    dev    etc    home   lib    media  mnt    opt    proc   root   run    sbin   srv    sys    tmp    usr    var
/ #
```