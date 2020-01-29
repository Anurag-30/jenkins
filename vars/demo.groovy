def call(Map pipelineParams) {

    pipeline {
        agent {
              docker { image 'centos:latest' }
     
    }
             
  stages {
          environment {
                SERVICE_CREDS = credentials('my-prefined-username-password')
            }
        stage("Env Variables") {
            steps {
                sh "printenv"
            }
        }
           stage("Env Variables") {
            steps {
              echo "$SERVICE_NAME"
            }
        }
    
    }
 } 
}
