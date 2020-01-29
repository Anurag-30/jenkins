def call(Map pipelineParams) {

pipeline {
  agent {
    docker { image 'centos:latest' }
  }
  
    stages {
        stage("Env Variables") {
            steps {
                sh "printenv"
                sh ''' env.SERVICE_NAME=$(echo "$JOB_NAME" | awk -F[//] '{print $2}') '''
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

