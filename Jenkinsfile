pipeline {
    agent any

    tools {
        // Maven version configured in Jenkins Global Tool Configuration (commented for now)
        maven "M3"

    }

    environment {
        // SonarQube scanner installed in Jenkins Global Tool Configuration
        scannerHome = tool 'sonarqubeCommunity'
        APP_NAME = "CsvAnalyser" 
        RUN_MODE = "none"
        ANSIBLE_HOST_KEY_CHECKING = 'False'
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
                    sh 'mvn clean package -DskipTests'
                }
            }
            post {
                success {
                    // Archive the built JAR
                    archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
                    // Optionally, record test results if tests exist
                    //junit 'backend/target/surefire-reports/TEST-*.xml'
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


        stage('Copy jar into artifacts folder') {
            steps {
                sh """

                mkdir -p artifacts
                cp backend/target/${APP_NAME}-0.0.1-SNAPSHOT.jar artifacts/${APP_NAME}-0.0.1-SNAPSHOT.jar
                """
            }
        }

        stage('Deploy using Ansible'){
            steps{

                ansiblePlaybook(
                    credentialsId: 'sshIntoPrivateInstances', // SSH key or user
                    installation: 'ansible',                  // Name of Ansible installed in Jenkins Tools
                    inventory: 'ansible/inventories/inventory.ini',
                    playbook: 'ansible/playbook/main.yml',
                    vaultTmpPath: '',
                )

            }
        }


    }
}


