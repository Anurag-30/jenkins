// Function to delete existing helm chart

def deleteExistingHelmChart() {              
    container('jenkins-agent') {
        withFolderProperties {
            sh '''
            helm repo add stable https://artifactory.test.cicd.com/artifactory/anuragok-helm-virtual
            if [ $(helm list -n ${TASK_NAMESPACE} | grep service-${ENVIRONMENT_NAME} >/dev/null; echo $?) -eq 0 ];
            then
                helm delete service-${ENVIRONMENT_NAME} -n ${TASK_NAMESPACE};
            fi
            '''
        }
    }
}
// Function to get the service version
def setServiceVersion(String upstreamEnv) {
    withFolderProperties {
        container('jenkins-agent') {
            script {
                if("${ENVIRONMENT_NAME}" == "sit") {
                    def app = sh(returnStdout: true, script: "curl -k 'https://artifactory.test.cicd.com/artifactory/anurag-generic/services-build-versions/service/version.txt'")
                    env.SERVICE_VERSION = app.split('=')[1]
                }
                else {
                    def upstream_build_number = UPSTREAM_BUILD.split('#')[1]
                    def app = sh(returnStdout: true, script: "curl -k https://artifactory.test.cicd.com/artifactory/anurag-generic/${upstreamEnv}-verified/service/version-${upstream_build_number}.txt")
                    env.SERVICE_VERSION = app.split('=')[1]
                }
            }
        }
    }
}

// Function Installing Helm chart 
def installHelmChart() {
    withFolderProperties {
        container('jenkins-agent') {
            sh '''
            echo "### Installing service helm chart with version: ${service_VERSION}"
            helm repo add stable https://artifactory.test.cicd.com/artifactory/anurag-helm-virtual
            helm upgrade service-${ENVIRONMENT_NAME} stable/service  --install --version ${service_VERSION} --set environment.name=${ENVIRONMENT_NAME} --namespace ${TASK_NAMESPACE}
            '''
        }
    }
}
def verifyDeployment(){
          sh ''' 
            SERVICE_NAMESPACE_COUNT=0;
            echo "#### FAILED DEPLOYMENTS IN ${SERVICE_NAMESPACE} ####"
            DEPLOYMENTS_LIST=$(kubectl get deployments -n ${SERVICE_NAMESPACE} -o name);
            for DEPLOYMENT in $DEPLOYMENTS_LIST;
            do
                kubectl rollout status $DEPLOYMENT -n ${SERVICE_NAMESPACE} --timeout=5m;
                ROLLOUT_STATUS=$?;
                SERVICE_NAMESPACE_COUNT=$((SERVICE_NAMESPACE_COUNT + ROLLOUT_STATUS));
            done;

            echo "#### Failed Deployment Count in $SERVICE_NAMESPACE is $SERVICE_NAMESPACE_COUNT ####";

            WEB_NAMESPACE_COUNT=0;

            echo "#### FAILED DEPLOYMENTS IN ${WEB_NAMESPACE} ####"
            DEPLOYMENTS_LIST=$(kubectl get deployments -n ${WEB_NAMESPACE} -o name);

            for DEPLOYMENT in $DEPLOYMENTS_LIST;
            do
                kubectl rollout status $DEPLOYMENT -n ${WEB_NAMESPACE} --timeout=5m;
                ROLLOUT_STATUS=$?;
                WEB_NAMESPACE_COUNT=$((WEB_NAMESPACE_COUNT + ROLLOUT_STATUS));
            done;

            echo "#### Failed Deployment Count in $WEB_NAMESPACE is $WEB_NAMESPACE_COUNT ####";

            [[ "${SERVICE_NAMESPACE_COUNT}" != "0" || "${WEB_NAMESPACE_COUNT}" != "0"  ]] && { echo "Deployment Failed"; exit 1; }

            echo "#### Deployment Successful ####"
            '''
}
def publishVersion() {
    withFolderProperties {
        container('jenkins-agent') {
            sh '''
            echo "### Publishing service helm chart version: ${service_VERSION} in file version-${BUILD_NUMBER}.txt"
            printf "service_version=%s\n" ${service_VERSION} >> version-${BUILD_NUMBER}.txt
            cp version-${BUILD_NUMBER}.txt version-latest.txt
            curl -k -u ${ARTIFACTORY_CREDENTIALS_USR}:${ARTIFACTORY_CREDENTIALS_PSW} -T version-latest.txt https://artifactory.test.cicd.com/artifactory/anurag-generic/${ENVIRONMENT_NAME}-verified/service/version-latest.txt
            curl -k -u ${ARTIFACTORY_CREDENTIALS_USR}:${ARTIFACTORY_CREDENTIALS_PSW} -T version-${BUILD_NUMBER}.txt https://artifactory.test.cicd.com/artifactory/anurag-generic/${ENVIRONMENT_NAME}-verified/service/version-${BUILD_NUMBER}.txt
            '''
        }
    }
}

// Function to trigger any downstream job
def triggerDownstreamJob(String downstreamEnv) {
    container('jenkins-agent') {
        build job: "${downstreamEnv}/service-deploy-${downstreamEnv}", propagate: false, parameters: [string(name: 'UPSTREAM_BUILD', value: "${BUILD_NUMBER}")]
    }
}

// Publishes all kinds of Artifacts
def PublishArtifacts() {
    container('jenkins-agent') {
            post {
             always {
                archiveArtifacts artifacts: 'smoke-test/cypress/screenshots/**/*.*', fingerprint: true, allowEmptyArchive: true
                archiveArtifacts artifacts: 'smoke-test/TestsReports/*.*', fingerprint: true, allowEmptyArchive: true
                archiveArtifacts artifacts: 'helm-charts/application-services/requirements.yaml', fingerprint: true, allowEmptyArchive: true
                }
            }
        }
    }
