Dear Future me here is the list of learnings and observations from your past, please keep revisitng this and make it better not worse:

There are two ways you write a Jenkinsfile
a) scripted pipeline - using node
b) declarative pipeline - using pipeline

Go for declarative approach as it is better approach , says this guy here - [StackOverflow](https://stackoverflow.com/questions/44657896/jenkins-pipeline-jenkinsfile-node-and-pipeline-directives).You can find an example for both of them under 

**JenkinsPipelineAsCode**

# Jenkins Pipeline As Code

## Setup Jenkins In Kubernetes

Connect to you kubernetes cluster and run the **jenkins-setup.sh** script. This will deploy jenkins into your cluster with blueocean installed in it.

## Observations With Blueocean Plugin


1. Blueocean plugin looks for a file with the name "Jenkinsfile" to create a pipeline 
2. If the pipeline is manaully created from the UI it creates the jenkinsfile(code) in the repo with the exact code that matches the steps.
3. Don't make the changes to the jenkinsfile that is created by the pipeline in UI, it corrupts.
4. The pipeline is specific to the repo used to create it. All the contents of the repo are available. ---> Doesn't require a git clone


## Missing Features That You Were Looking For

1. Grouping the jobs in blueocean UI under a folder.

2. create pipeline automation --> the initial step

This can be automated using the `bitbucket team` plugin.The inital job will scan the repos periodically looks for Jenkinsfile and create the pipelines. This job has to be configured from the normal jenkins UI. It also builds the jobs automatically when you push the code to that repository.

About Kubernetes Plugin for Jenkins --> https://github.com/jenkinsci/kubernetes-plugin

Templating the `Jenkins pipeline` --> https://jenkins.io/blog/2017/10/02/pipeline-templates-with-shared-libraries/

Shared Library concepts --> https://tomd.xyz/jenkins-shared-library/

## Note:

1. Ideally your shared libraries will be in common repository, your application repos will be loading them at the run time.
2. Build.Jenkinsfile belongs in your code repository of your apllication ,in order to access the common repo we have written a small code that fetches the git repo where the common libraries
3. For some reason if your shared library and Jenkinsfile is in the same repo you don't need that piece of code as we did in Commons.Jenkinsfile
