def call(Map pipelineParams) {

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

            stage('build') {
                steps {
                    container('gradle'){
                    sh '''
                    ls;
                    pwd
                    gradle build
                    '''
                }
            }
        }

            stage('trigger deploy') {
                steps {
                    
             def job = build job: 'deploy-job'
                
            }
        }
        
    }
 }
} 
