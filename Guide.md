# Kubernetes Guide

# Naming
| Name | Definition |
|------|------------|
| K8s | Kubernetes |
| Pod | Pods are the smallest deployable units of computing that can be created and managed in Kubernetes. |
| Service | An abstract way to expose an application running on a set of Pods as a network service. |
| Deployments | A Deployment provides declarative updates for Pods and ReplicaSets. |
| ReplicaSet | A ReplicaSet's purpose is to maintain a stable set of replica Pods running at any given time. As such, it is often used to guarantee the availability of a specified number of identical Pods. |
| DaemonSet | A DaemonSet ensures that all (or some) Nodes run a copy of a Pod. |
| Ingress | An API object that manages external access to the services in a cluster, typically HTTP. |

# 1. Create Docker Image

Kubernetes will always look at an Image Registry. So, for this example I will upload to my Docker Hub (https://hub.docker.com/u/vepo). If you want to rebuilt it, replace "vepo" by your Docker Hub user.

```bash
docker build backend/ -t backend
docker tag backend vepo/backend:1.0.0
docker push vepo/backend:1.0.0

docker build frontend/ -t frontend
docker tag frontend vepo/frontend:1.0.0
docker push vepo/frontend:1.0.0
```


# 2. Deploy PODs

The script [`k8s/pods/all.yaml`](./k8s/pods/all.yaml) contains a definition to both Containers as POD.

```bash
$ kubectl apply -f k8s/pods/all.yaml
pod/backend created
pod/frontend created

```

We can query it by filtering the label `example=k8s`:

```bash
$ kubectl get pods -l example=k8s
NAME       READY   STATUS    RESTARTS   AGE
backend    1/1     Running   0          2m21s
frontend   1/1     Running   0          2m21s

```

## Problem!

PODs cannot be accessed externally! We need services

Expose Pod as a Service:

```bash
$ kubectl expose pod frontend --type NodePort --name frontend-svc
service/frontend-svc exposed

```

Describe the service:

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

We can access the service by `http://<Node IP>:<NodePort>`. Now just access through localhost:30066.

## Cleanup

```bash
kubectl delete svc frontend-svc
kubectl delete -f k8s/pods/all.yaml
```

# 3. Deploy Services

To deploy all using PODs and Services:

```bash
kubectl apply -f k8s/services/all.yaml
```

Then you have to inspect the service to find which port use to access it.

```bash
kubectl describe svc frontend-svc
``

## Cleanup

```bash
kubectl delete -f k8s/services/all.yaml
```

# 4. Deploy Deployments

To deploy all using PODs, Services and Deployments

```bash
kubectl apply -f k8s/deployments/all.yaml
```

Then you have to inspect the service to find which port use to access it.

```bash
kubectl describe svc frontend-svc
```

To check all running pods:

```bash
kubectl get pods -l app=k8s-tutorial
```

## Cleanup

```bash
kubectl delete -f k8s/deployments/all.yaml
```

# Helm

Configure the repo:

```bash
helm repo add jaegertracing https://jaegertracing.github.io/helm-charts
```

Search for versions:

```bash
helm search repo jaeger
```

Install:

```bash
helm install tracing  jaegertracing/jaeger --version 0.32.0
```

Problem! Jaeger is configure to be exposed as Cluster IP!

Just upgrade using the values

```bash
helm upgrade tracing jaegertracing/jaeger  --version 0.25 --values helm/jaeger-node-port.yaml
```

## Cleanup

```bash
helm delete tracing
```