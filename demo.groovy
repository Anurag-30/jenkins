def call(Map pipelineParams) {

    pipeline {
        agent {
               kubernetes {
     
    }
             
 
    }



        stages {

            stage('build') {
                steps {
                    container('gradle'){
                    sh '''
                    ls;
                    pwd
                    gradle build
                    '''
                }
            }
        }

            stage('trigger deploy') {
                steps {
                    
              build job: 'deploy-job'
                
            }
        }
        
    }
 }
} 