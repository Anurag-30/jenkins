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
            steps {
                sh "printenv"
                echo "$pipelineParams.service"
            }
        }
    
        stage("test") {
            steps {
                sh ' echo "$hello pipelineParams.service" '
            }
    }
  }
    }   
}

