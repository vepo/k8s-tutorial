# Expondo um POD como Service

[Anterior](/03-criando-pods.md)

PODs não tem conexão com o mundo externo. Qualquer interface (TCP ou UDP) deve ser declarada como serviço. Isso pode ser feito através de um YAML ou pela linha de comando. É preferível que seja feito por um arquivo YAML e versionado. Mas vamos começar expondo nosso serviço?

Primeiro vamos verificar se nossos PODs estão rodando, mas dessa vez vamos usar os _labels_ associados ao POD. Porque usar labels? Porque as associações no Kubernetes são feita através de consulta a _labels_. Um serviço não irá conhecer o nome dos PODs que ele está referenciando, apenas os labels. Na maioria dos casos o POD foi criado automaticamente por um Deployment com um nome gerado automaticamente, por isso o label é a melhor referência. Tente executar o comando abaixo usando outro labels.

```bash
$ kubectl get pods -l example=k8s
NAME       READY   STATUS    RESTARTS   AGE
backend    1/1     Running   0          2m21s
frontend   1/1     Running   0          2m21s

```

Podemos criar o serviçi usando o nome do POD, mas o Kubernetes não vai associar diretamente ao nome, mas aos labels.

```bash
$ kubectl expose pod frontend --type NodePort --name frontend-svc
service/frontend-svc exposed

```

Abaixo vemos a descrição do serviço, observe que no campo Selector temos todos os labels do POD.

```bash
$ kubectl describe service frontend-svc
Name:                     frontend-svc
Namespace:                default
Labels:                   app=k8s-tutorial
                          example=k8s
                          role=frontend
                          version=v1
Annotations:              <none>
Selector:                 app=k8s-tutorial,example=k8s,role=frontend,version=v1
Type:                     NodePort
IP Family Policy:         SingleStack
IP Families:              IPv4
IP:                       10.103.41.208
IPs:                      10.103.41.208
Port:                     <unset>  8080/TCP
TargetPort:               8080/TCP
NodePort:                 <unset>  30910/TCP
Endpoints:                172.17.0.4:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   <none>

```

Agora como acessamos o serviço? Se você está rodando um cluster kubernetes usando o kublet diretamente no Linux, é só usar a porta escolhida pelo serviço `30910`. Mas se você está usando o minikube em qualquer outra máquina ainda é preciso um passo a mais. O minikube é um sistema que cria um cluster kubernetes dentro de um container, por isso nem todas as portas são abertas. Para expor serviço, você pode requerer a URL dele através da linha de comando:

```cmd
C:\Users\vepo> minikube service frontend-svc --url
* Starting tunnel for service frontend-svc.
|-----------|--------------|-------------|-----------------------|
| NAMESPACE |     NAME     | TARGET PORT |          URL          |
|-----------|--------------|-------------|-----------------------|
| default   | frontend-svc |             | http://127.0.0.1:4443 |
|-----------|--------------|-------------|-----------------------|
http://127.0.0.1:4443
! Because you are using a Docker driver on windows, the terminal needs to be open to run it.

```

> **Aviso**
>
> O minikube não deve ser usado em desenvolvimento e o Kubernetes deve rodar nativamente para melhor performance.

Ao se criar um serviço, o mesmo é exposto em todos os nós do cluster. Se desejar que seja exposto em apenas alguns nós, procure por _node selector_ ou _node affinity_ na documentação, isso pode ser bastante útil em ambientes de produção. 

Se estiver em um cluster, para acessar é preciso conhecer o cluster, para isso execute o comando abaixo. Você pode faze a seleção dos nodes usando os labels.

```
$ kubectl get nodes --show-labels
NAME       STATUS   ROLES                  AGE   VERSION   LABELS
minikube   Ready    control-plane,master   17h   v1.21.2   beta.kubernetes.io/arch=amd64,beta.kubernetes.io/os=linux,kubernetes.io/arch=amd64,kubernetes.io/hostname=minikube,kubernetes.io/os=linux,minikube.k8s.io/commit=76b94fb3c4e8ac5062daf70d60cf03ddcc0a741b,minikube.k8s.io/name=minikube,minikube.k8s.io/updated_at=2021_11_22T15_08_34_0700,minikube.k8s.io/version=v1.24.0,node-role.kubernetes.io/control-plane=,node-role.kubernetes.io/master=,node.kubernetes.io/exclude-from-external-load-balancers=

```

Agora limpe todos os recursos usando

```bash
kubectl delete svc frontend-svc
kubectl delete -f k8s/pods/all.yaml
```

E depois dê uma olhada no YAML [k8s/services/all.yaml](/k8s/services/all.yaml) que define todos os serviços. Repare que existe dois tipos de serviço, o `NodePort` e o `ClusterIP`. O NodePort é usado para expor o serviço através de uma porta do cluster. Já o ClusterIP é usado para expor o serviço através de um IP apenas dentro do cluster. Em ambos os casos o serviço pode ser acessível por um nome DNS, verifique a documentação para conhecer a [regra de nomenclatura do DNS](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/#a-aaaa-records).


```bash
$ kubectl apply -f ./k8s/services/all.yaml
pod/backend created
pod/frontend created
service/frontend-svc created
service/backend-svc created

```