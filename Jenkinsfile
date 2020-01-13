def label = "worker-${UUID.randomUUID().toString()}"

podTemplate(label: label, containers: [
  containerTemplate(name: 'gradle', image: 'gradle:latest', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'git', image: 'alpine/git:latest', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.8', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:latest', command: 'cat', ttyEnabled: true)
],
volumes: [
  hostPathVolume(mountPath: '/home/gradle/.gradle', hostPath: '/tmp/jenkins/.gradle'),
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]) {
  node(label) {
 
    stage('git clone') {
      
        container('git') {
          sh """
            echo "cloning the git repo"
            git clone https://github.com/Anurag-30/MediaWiki.git
            ls -ltra
            
            """
        
      }
    }
   
    stage('gradle build') {
      
        container('docker') {
          sh """
            echo "This is step 2"
            docker --version
            ls -ltra
            echo "testing auto"
            """
        
      }
    }
 
     stage('helm build') {
      
        container('helm') {
          sh """
            
            helm version
            echo " this is auto builld"
            """
        
      }
    }

}

  }
