pipeline {
  agent any
  parameters {
    string(name: 'maven_opts', defaultValue: '', description: 'Maven environment options')
    string(name: 'maven_cli_opts', defaultValue: '-U -B -s .mvn/settings.xml', description: 'Maven CLI options')
    string(name: 'maven_goals', defaultValue: 'clean install', description: 'Maven build goals')
    string(name: 'maven_repo_creds', defaultValue: 'project-factory-maven-repo-creds', description: 'Maven distribution repository credentials')
  }
  environment {
    MAVEN_OPTS = "${params.maven_opts}"
    MAVEN_REPO_CREDS = credentials("${params.maven_repo_creds}")
  }
  stages {
    stage('Build parent') {
      steps {
        sh "./mvnw -N ${params.maven_cli_opts} ${params.maven_goals}"
      }
    }
    stage('Build modules') {
      parallel {
        stage('Platform API') {
          steps {
            sh "./mvnw -f api/pom.xml ${params.maven_cli_opts} ${params.maven_goals}"
          }
          post {
            always {
              junit 'api/target/surefire-reports/**/*.xml'
              archiveArtifacts 'api/target/*.war'
            }
          }
        }
        stage('Platform Manager') {
          steps {
            sh "./mvnw -f manager/pom.xml ${params.maven_cli_opts} ${params.maven_goals}"
          }
          post {
            always {
              junit 'manager/target/surefire-reports/**/*.xml'
              archiveArtifacts 'manager/target/*.war'
            }
          }
        }
        stage('Portal') {
          steps {
            sh "./mvnw -f portal/pom.xml ${params.maven_cli_opts} ${params.maven_goals}"
          }
          post {
            always {
              archiveArtifacts 'portal/target/*.zip'
            }
          }
        }
      }
    }
  }
}
