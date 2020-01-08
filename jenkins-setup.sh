#! /bin/sh

kubectl create ns ci-cd
helm repo add stable https://kubernetes-charts.storage.googleapis.com/
helm install jenkins stable/jenkins -f jenkins-values.yml --namespace ci-cd



