pipeline {
    agent any
    
    // Using system-installed tools (Maven and Java must be in PATH)
    // Alternative: If you configure tools in Jenkins, uncomment the tools section below
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
    }
    
    environment {
        HEADLESS = 'true'
        MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=256m'
        // Uncomment and set if Java is not in PATH
        // JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        // PATH = "${JAVA_HOME}/bin:${PATH}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.GIT_COMMIT_SHORT = sh(
                        script: 'git rev-parse --short HEAD',
                        returnStdout: true
                    ).trim()
                    env.BUILD_DISPLAY_NAME = "#${env.BUILD_NUMBER} - ${env.GIT_COMMIT_SHORT}"
                }
            }
        }
        
        stage('Verify Tools') {
            steps {
                echo 'Verifying required tools are available...'
                sh '''
                    echo "Java version:"
                    java -version || echo "ERROR: Java not found in PATH"
                    echo ""
                    echo "Maven version:"
                    mvn -version || echo "ERROR: Maven not found in PATH"
                    echo ""
                    echo "Node version:"
                    node -v || echo "WARNING: Node.js not found (needed for Playwright browsers)"
                '''
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building project with Maven...'
                sh 'mvn clean compile test-compile'
            }
        }
        
        stage('Install Playwright Browsers') {
            steps {
                echo 'Installing Playwright browsers...'
                sh '''
                    mvn dependency:resolve || true
                    npx playwright install --with-deps chromium || true
                '''
            }
        }
        
        stage('Run Tests') {
            steps {
                echo 'Running Playwright tests...'
                sh 'mvn test'
            }
            post {
                always {
                    // Archive test results
                    junit 'target/surefire-reports/TEST-*.xml'
                }
            }
        }
        
        stage('Generate Allure Report') {
            steps {
                echo 'Generating Allure report...'
                sh 'mvn allure:report'
            }
        }
        
        stage('Publish Allure Report') {
            steps {
                echo 'Publishing Allure report...'
                script {
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                    ])
                }
            }
        }
    }
    
    post {
        always {
            echo 'Cleaning up workspace...'
            // Archive artifacts
            archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/allure-results/**/*', allowEmptyArchive: true
            
            // Clean workspace (optional)
            // cleanWs()
        }
        success {
            echo 'Pipeline succeeded! ✅'
            emailext(
                subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' succeeded.\n\nCheck console output at ${env.BUILD_URL}",
                to: "${env.CHANGE_AUTHOR_EMAIL ?: 'your-email@example.com'}"
            )
        }
        failure {
            echo 'Pipeline failed! ❌'
            emailext(
                subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' failed.\n\nCheck console output at ${env.BUILD_URL}",
                to: "${env.CHANGE_AUTHOR_EMAIL ?: 'your-email@example.com'}"
            )
        }
        unstable {
            echo 'Pipeline unstable! ⚠️'
        }
    }
}

