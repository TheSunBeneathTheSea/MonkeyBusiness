pipeline {
    agent any
    environment {
        DOCKERHUB_USERNAME = "slowlight50"
        APP_NAME = "backend-stock"
        IMAGE_NAME = "${DOCKERHUB_USERNAME}" + "/" + "${APP_NAME}"
    }

    stages {

        stage('Clean Workspace'){
            steps {
                script {
                    cleanWs()
                }
            }
        }

        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('build') {
            sh './gradlew clean build'
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker_image = docker.build("${IMAGE_NAME}")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('', 'docker-hub') {
                        docker_image.push("${env.BUILD_NUMBER}")
                        docker_image.push("latest")
                    }
                }
            }
        }
    }
}