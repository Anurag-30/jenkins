def call(Map pipelineParams) {
podTemplate(label: label, containers: [
  containerTemplate(name: 'gradle', image: 'gradle:latest', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'git', image: 'alpine/git:latest', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.8', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:latest', command: 'cat', ttyEnabled: true)
],
volumes: [
  hostPathVolume(mountPath: '/home/gradle/.gradle', hostPath: '/tmp/jenkins/.gradle'),
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
])
    pipeline {
        agent any
        stages {

            stage('build') {
                container('gradle'){
                steps {
                    sh 'mvn clean package -DskipTests=true'
                }
            }
        }
    }
 }
}