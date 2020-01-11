#! /bin/sh

kubectl create ns ci-cd
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash
helm repo add stable https://kubernetes-charts.storage.googleapis.com/
helm install jenkins stable/jenkins -f jenkins-values.yml --namespace ci-cd



