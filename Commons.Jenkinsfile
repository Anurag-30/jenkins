env.currentEnv = "dev"

pipeline {
    agent {
        kubernetes {
            yaml libraryResource('jenkins-agent-pod.yaml')
        }
    }

    environment {
        ARTIFACTORY_CREDENTIALS = credentials('jfrog-user')
        GIT_AUTH = credentials('GIT_AUTH')
    }

    stages {
        stage('Setup Helm Client') {
            steps {
                script {
                    commonstages.deleteExistingHelmChart()
                }
            }
        }

        stage('Set service version to be used by later stages') {
            steps {
                script {
                    commonstages.setServiceVersion(null)
                }
            }
        }

        stage('Install services Helm Chart') {
            steps {
                script {
                    commonstages.installHelmChart()
                }
            }
        }

        stage('Publish service Version') {
            steps {
                script {
                    commonstages.publishVersion()
                }
            }
        }
        
         stage('Trigger downstream commonstages deployment') {
            steps {
                 script{
                    commonstages.triggerDownstreamJob('qa')
                }
            }
         }
    }
}