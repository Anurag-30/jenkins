# Useful Reminders

1. The slave pod automatically attaches itself a container(jnlp), the image name has to be configured in pod template in Jenkins UI. This container takes the responsibility of maintaining connectivity between Jenkins master and Slave pod.

2. ScriptedPipeline picks the image from the pod template but there is an issue with Declarative pipeline as it is unable to pick custom images for jnlp that are configured in pod template. 

   **Solution**: You can override the jnlp to custom by adding the jnlp container in yaml file of the pod.

3. A seed job can be used to set up a folder in jenkins and a pipeline job in it.

4. While Acceesing an environment variable use env when you are using double quotes.
   
   **Example** :
   
   sh 'echo ${service}'
   sh "echo ${env.service}"
