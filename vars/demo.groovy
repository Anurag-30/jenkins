def call(Map pipelineParams) {

pipeline {
  agent {
    docker { image 'docker:latest' }
  }
  
    stages {
        stage("Env Variables") {
            steps {
                sh "printenv | sort "
                echo "${pipelineParams.service}"
                echo " docker pull ${env.SERVICE}:${env.GIT_COMMIT.substring(0,5)}"
            }
        }
    
        stage("test") {
            steps {
                sh ''' 
                pwd
                ls
                '''
                echo $env.SERVICE}
            }
    }
  }
    }   
}

