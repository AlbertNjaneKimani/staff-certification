pipeline {
    agent any

    stages {
        stage('Clone Git Repo') {
            steps {
                // Clone the Git repository
                echo 'Cloning Git repository...'
                git 'https://github.com/AlbertNjaneKimani/staff-certification.git'
            }
        }

        stage('Maven Build') {
            steps {
                script {
                    // Build the Spring Boot application using Maven
                    echo 'Building with maven...'
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                // Build the Docker image using the Dockerfile

                script {
                  echo 'Docker image build...'
                    def dockerImage = docker.build("auth-service:${env.BUILD_NUMBER}")
                }
            }
        }

        stage('Docker Push') {
            steps {
                // Push the Docker image to Docker Hub
                script {
                echo 'Docker image push....'
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                        def dockerImage = docker.build("auth-service:${env.BUILD_NUMBER}")
                        dockerImage.push()
                    }
                }
            }
        }
    }
}
