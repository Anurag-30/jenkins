def call(Map pipelineParams) {

pipeline {
  agent {
    docker { image 'centos:latest' }
  }

  environment {
        SERVICE_NAME = "$(echo "$JOB_NAME" | awk -F[//] '{print $2}')"
    }
  
    stages {
        stage("Env Variables") {
            script {
                    env.FOO = "IT DOES NOT WORK!" // it can't override env.FOO declared at the pipeline (or stage) level
                    env.SOMETHING = "2" // it can override env variable created imperatively
                }
            steps {
                sh "printenv"
                echo ${SERVICE_NAME}
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

