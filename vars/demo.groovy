def call(Map pipelineParams) {

pipeline {
  agent {
    docker { image 'docker:latest' }
  }
  
    stages {
        stage("Env Variables") {
            steps {
                sh "printenv"
                echo "${pipelineParams.service}"
                sh docker pull ${pipelineParams.service}
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

