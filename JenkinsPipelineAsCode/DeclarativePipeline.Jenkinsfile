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

    stage('trigger deploy') {
        steps {
            
            build job: 'deploy-job'
                
            }
        }
        
    }
 }
} 