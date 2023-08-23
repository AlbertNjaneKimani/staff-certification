pipeline {
    agent any

    stages {
        stage('Clone Git Repo') {
            steps {
                // Clone the Git repository
                git 'https://github.com/AlbertNjaneKimani/staff-certification.git'
            }
        }

        stage('Maven Build') {
            steps {
                script {
                    // Build the Spring Boot application using Maven
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                // Build the Docker image using the Dockerfile
                script {
                    def dockerImage = docker.build("auth-service:${env.BUILD_NUMBER}")
                }
            }
        }

        stage('Docker Push') {
            steps {
                // Push the Docker image to Docker Hub
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                        def dockerImage = docker.build("auth-service:${env.BUILD_NUMBER}")
                        dockerImage.push()
                    }
                }
            }
        }
    }
}
