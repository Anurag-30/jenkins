def call(Map pipelineParams) {

pipeline {
  agent {
    docker { image 'centos:latest' }
  }
  
    stages {
        stage("Env Variables") {
            steps {
                sh "printenv"
                echo "${pipelineParams.service}"
                docker 
            }
        }
    
        stage("test") {
            steps {
                sh ''' 
                echo ${pipelineParams.service}
                whoami
                echo "hello" 
                '''
            }
    }
  }
    }   
}

