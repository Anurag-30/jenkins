def call(Map pipelineParams) {
    def git_version_script = libraryResource 'git_version.sh' // Assigning a file to variable
    pipeline {
        agent {
            kubernetes {
                yaml libraryResource('jenkins-agent-pod.yaml') // Manifest file for pod. You will find all the common files under resources
            }
        }

        environment {
            ARTIFACTORY_CREDENTIALS = credentials('jfrog-user')
            GIT_AUTH = credentials('GIT_AUTH')
        }

        stages {
            stage('build app') {
                steps {
                    container('jenkins-agent') {
                        sh "gradle clean build docker --stacktrace"
                        writeFile file: 'git_version.sh', text: "${git_version_script}"
                        sh "git config --global http.postBuffer 524288000"
                        sh "sh git_version.sh -b gradle"
                        script {
                            env.Version = sh(returnStdout: true, script: 'sh git_version.sh -v gradle').trim()
                        }
                    }
                }
            }

            stage('build image and push') {
                steps {
                    container('jenkins-agent') {
                        echo "Version is ${env.Version}"
                        sh "docker login artifactory.test.cicd.com -u=${ARTIFACTORY_CREDENTIALS_USR} -p=${ARTIFACTORY_CREDENTIALS_PSW}"
                        sh "docker push artifactory.test.cicd.com/anurag-docker/${env.SERVICE}:latest"
                        sh "docker tag artifactory.test.cicd.com/anurag-docker/${env.SERVICE}:latest artifactory.test.cicd.com/anurag-docker/${env.SERVICE}:${env.Version}"
                        sh "docker push artifactory.test.cicd.com/anurag-docker/${env.SERVICE}:${env.Version}"
                    }
                }
            }

            stage('Helm package and push') {
                steps {
                    container('jenkins-agent') {
                        sh "sed -ri \"s/tag: latest/tag: ${env.Version}/\" helm/${env.SERVICE}/values.yaml"
                        sh "helm lint helm/${env.SERVICE}"
                        sh "helm package helm/${env.SERVICE} --version ${env.Version}"
                        sh "helm repo add stable https://artifactory.test.cicd.com/artifactory/anurag-helm-virtual"
                        sh 'curl -k -u ${ARTIFACTORY_CREDENTIALS_USR}:${ARTIFACTORY_CREDENTIALS_PSW} -T ${SERVICE}-${Version}.tgz https://artifactory.test.cicd.com/artifactory/anurag-helm/maverick/${SERVICE}/${SERVICE}-${Version}.tgz '
                    }
                }
            }

            stage('push service version') {
                steps {
                    container('jenkins-agent') {
                        sh "echo ${env.SERVICE}=${env.Version} >> version.txt"
                        sh 'curl -k -u ${ARTIFACTORY_CREDENTIALS_USR}:${ARTIFACTORY_CREDENTIALS_PSW} -T version.txt https://artifactory.test.cicd.com/artifactory/anurag-generic/services-build-versions/${SERVICE}/version.txt'
                    }
                }
            }

            stage('trigger generate_build_versions job') {
                steps{
                    container('jenkins-agent') {
                        build (job: 'Generate_Build_Version', propagate: false, wait: false)
                    }
                }
            }
        }

        post {
            always {
                // publish test reports
                publishHTML target:[
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'build/reports/tests/test',
                    reportFiles: 'index.html',
                    reportName: "Tests Report"
                ]
            }
        }
    }
}
