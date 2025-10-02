pipeline {
    agent any

    tools {
        // Maven version configured in Jenkins Global Tool Configuration (commented for now)
        maven "M3"
    }

    environment {
        // SonarQube scanner installed in Jenkins Global Tool Configuration
        scannerHome = tool 'sonarqubeCommunity'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/masteradios/CsvAnalyser.git'
            }
            post {
                success {
                    echo 'Git checkout successful!'
                }
            }
        }

        stage('Build') {
            steps {
                dir('backend') {
                    // Build the project so that target/classes exists for SonarQube
                    sh 'mvn clean package'
                }
            }
            post {
                success {
                    // Archive the built JAR
                    archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
                    // Optionally, record test results if tests exist
                    junit 'backend/target/surefire-reports/TEST-*.xml'
                }
            }
        }

        stage('Run SonarQube') {
            steps {
                withSonarQubeEnv(credentialsId: 'jenkinsForSonar', installationName: 'sonarqube-community') {
                    sh """
                        ${scannerHome}/bin/sonar-scanner \
                          -Dsonar.projectKey=csv-analyser \
                          -Dsonar.projectName=csv-analyser \
                          -Dsonar.projectVersion=1.0 \
                          -Dsonar.sources=backend/src/main/java \
                          -Dsonar.java.binaries=backend/target/classes
                    """
                }
            }
        }
    }
}
