pipeline {
  agent any
  stages {
    stage('step 1') {
      steps {
        sh '''pwd;
ls'''
      }
    }

    stage('step 2') {
      steps {
        echo 'hello this is step 2'
        echo "hello this is modified"
      }
    }

  }
}