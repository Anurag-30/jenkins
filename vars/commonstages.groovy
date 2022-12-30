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
import groovyx.net.http.HTTPBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

def setServiceVersion() {
    // Create an HTTPBuilder instance
    def http = new HTTPBuilder()

    try {
        // Check if the environment is "sit"
        if("${ENVIRONMENT_NAME}" == "sit") {
            // If it is, get the service version from a URL
            def response = http.get("https://artifactory.test.cicd.com/artifactory/anurag-generic/services-build-versions/service/version.txt")

            // Convert the response to a JSON string
            def jsonString = JsonOutput.toJson(response)

            // Write the JSON string to a file
            def file = new File("response.json")
            file.write(jsonString)

            // Parse the JSON string and get the service version
            def slurper = new JsonSlurper()
            def json = slurper.parseText(jsonString)
            env.SERVICE_VERSION = json.serviceVersion
        }
        else {
            // If it's not "sit", get the upstream build number and use it to get the service version from a different URL
            def upstream_build_number = UPSTREAM_BUILD.split('#')[1]
            def response = http.get("https://artifactory.test.cicd.com/artifactory/anurag-generic/${upstreamEnv}-verified/service/version-${upstream_build_number}.txt")

            // Convert the response to a JSON string
            def jsonString = JsonOutput.toJson(response)

            // Write the JSON string to a file
            def file = new File("response.json")
            file.write(jsonString)

            // Parse the JSON string and get the service version
            def slurper = new JsonSlurper()
            def json = slurper.parseText(jsonString)
            env.SERVICE_VERSION = json.serviceVersion
        }
    }
    catch (Exception e) {
        // If an exception is thrown, print the error message and set the SERVICE_VERSION variable to "error"
        println(e.getMessage())
        error("An error occurred with the above exeception and the service version couldn't be set", 1)
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
