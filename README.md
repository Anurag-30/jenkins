Dear Future me here is the list of learnings and observations from your past, please keep revisitng this and make it better not worse:

There are two ways you write a Jenkinsfile
a) scripted pipeline - using node
b) declarative pipeline - using pipeline

Go for declarative approach as it is better approach , says this guy here - https://stackoverflow.com/questions/44657896/jenkins-pipeline-jenkinsfile-node-and-pipeline-directives. You can find an example for both of them under `JenkinsPipelineAsCode`

# Jenkins Pipeline As Code

## Setup Jenkins In Kubernetes

Connect to you kubernetes cluster and run the `jenkins-setup.sh` script. This will deploy jenkins into your cluster with blueocean installed in it.

## Observations With Blueocean Plugin


1. Blueocean plugin looks for a file with the name "Jenkinsfile" to create a pipeline 
2. If the pipeline is manaully created from the UI it creates the jenkinsfile(code) in the repo with the exact code that matches the steps.
3. Don't make the changes to the jenkinsfile that is created by the pipeline in UI, it corrupts.
4. The pipeline is specific to the repo used to create it. All the contents of the repo are available. ---> Doesn't require a git clone

## About testpipeline.groovy

1. It is a shared library file which can be used by multiple jenkinsfile. ---> The groovy file has to be under vars directory.
2. It can have 'n' number of stages. The second stage here is the downstream job where it starts another job after the sucessful completion of before stages.
3. You can refer this file in the `Jenkinsfile` as shown in the example --> https://github.com/Anurag-30/gradle-demo.git

For more information on the kubernetes plugin  --> https://github.com/jenkinsci/kubernetes-plugin



## Missing Features That You Were Looking For

1. Grouping the jobs in blueocean UI under a folder.

2. create pipeline automation --> the initial step

This can be automated using the `bitbucket team` plugin.The inital job will scan the repos periodically looks for Jenkinsfile and create the pipelines. This job has to be configured from the normal jenkins UI. It also builds the jobs automatically when you push the code to that repository.


Templating the `Jenkins pipeline` --> https://jenkins.io/blog/2017/10/02/pipeline-templates-with-shared-libraries/

Shared Library concepts --> https://tomd.xyz/jenkins-shared-library/
