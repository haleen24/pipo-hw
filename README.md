

`openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048`

`openssl rsa -pubout -in private.pem -out public.pem`



prometheus-operator: kubectl apply --server-side
-f  https://github.com/prometheus-operator/prometheus-operator/releases/download/v0.74.0/bundle.yaml

kubectl port-forward -n monitoring svc/grafana 3000:80

minikube service user-service -n pipo --url
