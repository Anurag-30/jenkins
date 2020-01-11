pipeline {
  agent any
  stages {
    stage('test') {
      steps {
        sh '''pwd;
ls -ltra'''
      }
    }

    stage('step2') {
      steps {
        echo 'this is step 2'
      }
    }

  }
}