def call(Map pipelineParams) {

pipeline {
  agent {
    docker { image 'centos:latest' }
  }
  
    stages {
        stage("Env Variables") {
            steps {
                sh "printenv"
                sh ""
            }
        }
    
        stage("test") {
            steps {
                echo "hello ${service}"
            }
    }
  }
    }   
}

