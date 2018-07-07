pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        withMaven() {
          sh 'mvn -U clean install'
        }
      }
    }
  }
}