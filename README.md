# jenkins
Connect to you kubernetes cluster and run the `jenkins-setup.sh` script. This will deploy jenkins into your cluster with blueocean installed in it.

## OBSERVATIONS With Blueocean Plugin


1. Blueocean plugin looks for a file with the name "Jenkinsfile" to create a pipeline 
2. If the pipeline is manaully created from the UI it creates the jenkinsfile(code) in the repo with the exact code that matches the steps.
3. Don't make the changes to the jenkinsfile that is created by the pipeline in UI, it corrupts.
4. The pipeline is specific to the repo used to create it. All the contents of the repo are available. ---> Doesn't require a git clone

## About BuildPipeline.groovy

1. It is a shared library file which can be used by multiple jenkinsfile. ---> The groovy file has to be under vars directory.
2. It can have 'n' number of stages. The second stage here is the downstream job where it starts another job after the sucessful completion of before stages.

https://github.com/jenkinsci/kubernetes-plugin


## Missing Features

1. Grouping the jobs in blueocean UI under a folder.

2. create pipeline automation --> the initial step

This can be automated using the `bitbucket team` plugin.The inital job will scan the repos periodically looks for Jenkinsfile and create the pipelines. This job has to be configured from the normal jenkins UI. It also builds the jobs automatically when you push the code to that repository.




https://jenkins.io/blog/2017/10/02/pipeline-templates-with-shared-libraries/

https://tomd.xyz/jenkins-shared-library/
