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
                echo https://jenkins.io/blog/2017/10/02/pipeline-templates-with-shared-libraries/
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

