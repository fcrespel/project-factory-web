pipeline {
  agent any
  parameters {
    string(name: 'mvn_opts', defaultValue: '-U', description: 'Maven build options')
    string(name: 'mvn_goals', defaultValue: 'clean install', description: 'Maven build goals')
  }
  stages {
    stage('Build') {
      steps {
        withMaven() {
          sh "mvn ${params.mvn_opts} ${params.mvn_goals}"
        }
      }
    }
  }
}