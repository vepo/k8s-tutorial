# Pods

Simplest unit from Kubernetes.

To deploy `frontend` and `backend` pods:

```bash
$ kubectl apply -f k8s/pods/all.yaml
pod/backend created
pod/frontend created

```

To get the running pods, filtering by label `example=k8s`:

```bash
$ kubectl get pods -l example=k8s
NAME       READY   STATUS    RESTARTS   AGE
backend    1/1     Running   0          2m21s
frontend   1/1     Running   0          2m21s

```

Expose Pod as a Service:

```bash
$ kubectl expose pod frontend --type NodePort --name frontend-svc
service/frontend-svc exposed

```

Get Service Port:

```bash
$ kubectl describe svc frontend-svc
Name:                     frontend-svc
Namespace:                default
Labels:                   app=k8s-tutorial
                          example=k8s
                          role=frontend
                          version=v1
Annotations:              <none>
Selector:                 app=k8s-tutorial,example=k8s,role=frontend,version=v1
Type:                     NodePort
IP:                       10.102.163.164
LoadBalancer Ingress:     localhost
Port:                     <unset>  8080/TCP
TargetPort:               8080/TCP
NodePort:                 <unset>  30066/TCP
Endpoints:                10.1.0.34:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   <none>

```

Now just access through localhost:30066.

There is a problem here! Frontend cannot find backend, so we need to expose backend and set it address as frontend environment variable.

Expose backend as ClusterIP.

```bash
$ kubectl expose pod backend --type ClusterIP --name backend-svc
service/backend-svc exposed

```

Find the DNS rule for Backend Service:

```bash
$ kubectl apply -f k8s/utils/dnsutils.yaml 
pod/dnsutils created

$ kubectl exec -ti dnsutils -- nslookup backend-svc
Unable to use a TTY - input is not a terminal or the right kind of file
Server:         10.96.0.10
Address:        10.96.0.10#53

Name:   backend-svc.default.svc.cluster.local
Address: 10.103.171.66


```

Problem here! You cannot update the environment variables of a runnig POD, so you need to delete the old pods, create a new one and retrieve the new node port.

```bash
$ kubectl delete -f k8s/pods/all.yaml 
pod "backend" deleted
pod "frontend" deleted

$ kubectl apply -f k8s/pods/all-with-env.yaml 
pod/backend created
pod/frontend created

$ kubectl get svc
NAME           TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
backend-svc    ClusterIP   10.103.171.66    <none>        8080/TCP         47m
frontend-svc   NodePort    10.102.163.164   <none>        8080:30066/TCP   60m
kubernetes     ClusterIP   10.96.0.1        <none>        443/TCP          5d7h

```

Now just access localhost:30066! 
