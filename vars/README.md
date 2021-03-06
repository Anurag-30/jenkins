## Shared Libraries

If you have  complex Buildpipelines the Jenkinsfile gets bigger and bigger with all the steps. In most of the cases the steps are similar and redudant.In order to reuse the code and make the file shorter we came up with the concept of Shared Libraries

# Scenario 1

We live in a world where micro-services are actively replacing monolithic applications, obviously there is a huge change in the way we build and deploy micro-services. Let us assume you have 40 micro-services, few of them backend ,a few frontend having their own code repositiories in this case git. This means you need to have a Jenkinsfile in all the 40 repos which has the steps to build just like the one in **JenkinsPipelineAsCode** folder. 

1. The problem with this is if I have to change or add a stage in my pipeline I have to push the code to 40 repos and there is a high chance of making a mistake.

2. The code gets messier and you would loose track of it even before you realize.

3. The code is redundant.

## Assumption

Let us assume that we have 40 services but steps to build them can be categorized into 3 or 4.

## Solution

Take a look at BuildPipeline.groovy, It consists of all the stages that are requrired to build your services. We will see how we can use this in the Jenkinsfile, look for **Build.Jenkinsfile**. we can identify services which use the same steps and make them use one common shared library.

# Scenario 2

Now we have successfully made all the 40 repos to use 3-4 common Shared library. However we now have identified that even the 3-4 shared library scripts have some redudant code. There are few stages that are common among them. In order to drill down further we have made one file which has all the common stages , look at **commonstages.groovy**. These are like functions that you can use in your Jenkinsfile to do a certain job. You can either directlty invoke them or pass parameters.We can have a look at **Commons.Jenkinsfile** to see how it works. 





