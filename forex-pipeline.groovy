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
"""
    }
             
 
    }



        stages {

            stage('build') {
                steps {
                    container(''){
                    sh '''
                    gradle build docker --stacktrace
                    '''
                }
            }
        }

            stage('t') {
                steps {
                    
              build job: 'deploy-job'
                
            }
        }
        
    }
 }
} 