def call(Map pipelineParams) {

pipeline {
  agent {
    docker { image 'centos:latest' }
  }

  /* environment {
        SERVICE_NAME = "$(echo "$env.JOB_NAME" | awk -F[//] '{print $2}')"
    } */
  
    stages {
        stage("Env Variables") {
            script {
                    env.SERVICE_NAME = "(echo "$env.JOB_NAME" | awk -F[//] '{print $2}')" 
                }
            steps {
                sh "printenv"
                echo ${SERVICE_NAME}
            }
        }
    
        stage("test") {
            steps {
                sh ' echo "hello ${SERVICE_NAME}" '
            }
    }
  }
    }   
}

