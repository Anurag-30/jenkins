def call(Map pipelineParams) {

pipeline {
  agent {
    docker { image 'centos:latest' }
  }

  environment {
        SERVICE_NAME = "bar"
    }
  
    stages {
        stage("Env Variables") {
            steps {
                sh "printenv"
                sh ''' env.SERVICE_NAME= '''
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

