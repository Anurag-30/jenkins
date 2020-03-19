# Useful Reminders

1. The slave pod automatically attaches itself a container(jnlp), the image name has to be configured in pod template in Jenkins UI. This container takes the responsibility of maintaining connectivity between Jenkins master and Slave pod.

2. ScriptedPipeline picks the image from the pod template