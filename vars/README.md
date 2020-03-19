## Shared Libraries

If you have  complex Buildpipelines the Jenkinsfile gets bigger and bigger with all the steps. In most of the cases the steps are similar and redudant.In order to reuse the code and make the file shorter we came up with the concept of Shared Libraries

# Scenario 1

We live in a world where micro-services are actively replacing monolithic applications, obviously there is a huge change in the way we build and deploy micro-services. Let us assume you have 40 micro-services, few of them backend , a few frontend that means you need 40 Jenkinsfiles in each but the steps to build them can be categorized into 3 or 4.

