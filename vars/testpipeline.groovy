
def call(Map pipelineParams) {

    pipeline {
        agent any
        stages {

            stage('build') {
                steps {
                    sh 'pwd'
                }
            }
        }
        
        }
    }
}