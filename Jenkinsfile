pipeline {
  agent any

  tools {
    // These must exist in Jenkins Global Tool Configuration
    jdk 'jdk17'
    maven 'maven3'
  }

  triggers {
    // Schedule: run once a day at a hashed time
    cron('H H * * *')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Test') {
      steps {
        sh 'mvn -v'
        sh 'mvn test'
      }
      post {
        always {
          // Publish Allure report if the Jenkins Allure plugin is installed
          allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
        }
      }
    }
  }
}
