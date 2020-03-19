pipeline {
    agent {
     kubernetes {
      yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    some-label: some-label-value
spec:
  containers:
  - name: gradle
    image: gradle:latest
    command:
    - cat
    tty: true
  - name: busybox
    image: busybox
    command:
    - cat
    tty: true
"""
    }
}
stages {

    stage('build your app') {
        steps {
            container('gradle'){
            sh '''
            gradle clean build docker --stacktrace

            '''
            }
          }
        }

       stage('Push your docker image) {
        steps {
            container('gradle'){
            sh '''
            
            docker push <image name>

            '''
            }
          }
        }

      stage('Push your Helm chart ) {
        steps {
            container('gradle'){}
            
            sh "sed -ri \"s/tag: latest/tag: ${env.Version}/\" helm/${env.SERVICE}/values.yaml"
            sh "helm lint helm/${env.SERVICE}"
            sh "helm package helm/${env.SERVICE} --version ${env.Version}"
            sh "helm repo add stable https://artifactory.test.cicd.com/artifactory/anurag-helm-virtual"
            sh 'curl -k -u ${ARTIFACTORY_CREDENTIALS_USR}:${ARTIFACTORY_CREDENTIALS_PSW} -T ${SERVICE}-${Version}.tgz https://artifactory.test.cicd.com/artifactory/anurag-helm/maverick/${SERVICE}/${SERVICE}-${Version}.tgz '

            '''
            }
          }
        }

    stage('trigger deploy') {
        steps {
            
            build job: 'deploy-job'
                
            }
        }
        
    }
 }
} 