def call(Map pipelineParams) {

pipeline {
  agent {
    docker { image 'centos:latest' }
  }
  
    stages {
        stage("Env Variables") {
            steps {
                sh "printenv"
            }
        }
    
        stage("test") {
            steps {
                echo "hello"
            }
    }
  }
    }   
}

