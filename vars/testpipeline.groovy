def deleteExistingHelmChart() {
    container('jenkins-agent') {
        withFolderProperties {
            sh '''
            helm repo add stable https://artifactory.dev.maximus.axisb.com/artifactory/forex-helm-virtual
            if [ $(helm list -n ${TASK_NAMESPACE} | grep seed-${ENVIRONMENT_NAME} >/dev/null; echo $?) -eq 0 ];
            then
                helm delete seed-${ENVIRONMENT_NAME} -n ${TASK_NAMESPACE};
            fi
            '''
        }
    }
}

def setSeedVersion(String upstreamEnv) {
    withFolderProperties {
        container('jenkins-agent') {
            script {
                if("${ENVIRONMENT_NAME}" == "sit") {
                    def app = sh(returnStdout: true, script: "curl -k 'https://artifactory.dev.maximus.axisb.com/artifactory/forex-generic/services-build-versions/seed/version.txt'")
                    env.SEED_VERSION = app.split('=')[1]
                }
                else {
                    def upstream_build_number = UPSTREAM_BUILD.split('#')[1]
                    def app = sh(returnStdout: true, script: "curl -k https://artifactory.dev.maximus.axisb.com/artifactory/forex-generic/${upstreamEnv}-verified/seed/version-${upstream_build_number}.txt")
                    env.SEED_VERSION = app.split('=')[1]
                }
            }
        }
    }
}

def installHelmChart() {
    withFolderProperties {
        container('jenkins-agent') {
            sh '''
            echo "### Installing seed helm chart with version: ${SEED_VERSION}"
            helm repo add stable https://artifactory.dev.maximus.axisb.com/artifactory/forex-helm-virtual
            helm upgrade seed-${ENVIRONMENT_NAME} stable/seed  --install --version ${SEED_VERSION} --set environment.name=${ENVIRONMENT_NAME} --namespace ${TASK_NAMESPACE}
            '''
        }
    }
}

def publishVersion() {
    withFolderProperties {
        container('jenkins-agent') {
            sh '''
            echo "### Publishing seed helm chart version: ${SEED_VERSION} in file version-${BUILD_NUMBER}.txt"
            printf "seed_version=%s\n" ${SEED_VERSION} >> version-${BUILD_NUMBER}.txt
            cp version-${BUILD_NUMBER}.txt version-latest.txt
            curl -k -u ${ARTIFACTORY_CREDENTIALS_USR}:${ARTIFACTORY_CREDENTIALS_PSW} -T version-latest.txt https://artifactory.dev.maximus.axisb.com/artifactory/forex-generic/${ENVIRONMENT_NAME}-verified/seed/version-latest.txt
            curl -k -u ${ARTIFACTORY_CREDENTIALS_USR}:${ARTIFACTORY_CREDENTIALS_PSW} -T version-${BUILD_NUMBER}.txt https://artifactory.dev.maximus.axisb.com/artifactory/forex-generic/${ENVIRONMENT_NAME}-verified/seed/version-${BUILD_NUMBER}.txt
            '''
        }
    }
}

def triggerDownstreamSeed(String downstreamEnv) {
    container('jenkins-agent') {
        build job: "${downstreamEnv}/seed-deploy-${downstreamEnv}", propagate: false, parameters: [string(name: 'UPSTREAM_BUILD', value: "${BUILD_NUMBER}")]
    }
}
