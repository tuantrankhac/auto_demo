pipeline {
    agent any  // chạy trên bất kỳ agent nào (có thể chỉ định label nếu cần)

    environment {
        // Biến môi trường tùy chỉnh cho Maven (tăng heap, bỏ qua test fail để report Allure đầy đủ)
        MAVEN_OPTS = '-Xmx1024m -Dmaven.test.failure.ignore=true'
        // Biến browser mặc định là chrome, có thể override bằng tham số hoặc biến ENV ngoài
        BROWSER = "${env.BROWSER ?: 'chrome'}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm  // lấy code từ Git (Jenkins tự hiểu repo)
            }
        }

        stage('Build & Test') {
            steps {
                sh "mvn clean verify -Dbrowser=${BROWSER} -Dheadless=true"  // chạy test với browser
            }
            post {
                always {
                    // Lưu Allure results nếu dùng Allure
                    archiveArtifacts artifacts: 'target/allure-results/**', allowEmptyArchive: true
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                allure includeProperties: false,
                      jdk: '',
                      results: [[path: 'target/allure-results']]
            }
        }

        // (Tùy chọn) Stage deploy Docker nếu cần
        stage('Build Docker Image') {
            when {
                branch 'main'  // chỉ build Docker khi merge vào main
            }
            steps {
                script {
                    docker.build("auto-demo:${env.BUILD_NUMBER}")
                }
            }
        }
    }

    post {
        always {
            // Cleanup workspace nếu cần
            cleanWs()
        }
        success {
            echo 'Build & Test thành công!'
        }
        failure {
            echo 'Build hoặc Test fail!'
        }
    }
}