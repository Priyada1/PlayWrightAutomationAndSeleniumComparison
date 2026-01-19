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
        // MaxPermSize was removed in Java 8+, using only Xmx for Java 23
        MAVEN_OPTS = '-Xmx1024m'
        // Add Homebrew bin to PATH for Maven and Node.js
        PATH = "/opt/homebrew/bin:${env.PATH}"
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
                    if command -v mvn &> /dev/null; then
                        mvn -version
                    elif [ -f "/Users/chakrapanipriyadarshi/.jenkins/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn" ]; then
                        /Users/chakrapanipriyadarshi/.jenkins/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn -version
                        echo "Using Jenkins Maven installation"
                    else
                        echo "ERROR: Maven not found in PATH or Jenkins tools"
                    fi
                    echo ""
                    echo "Node version:"
                    node -v || echo "WARNING: Node.js not found (needed for Playwright browsers)"
                '''
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building project with Maven...'
                script {
                    def mvnCmd = sh(
                        script: 'command -v mvn || echo "/Users/chakrapanipriyadarshi/.jenkins/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn"',
                        returnStdout: true
                    ).trim()
                    sh "${mvnCmd} clean compile test-compile"
                }
            }
        }
        
        stage('Install Playwright Browsers') {
            steps {
                echo 'Installing Playwright browsers...'
                sh '''
                    MVN_CMD=$(command -v mvn || echo "/Users/chakrapanipriyadarshi/.jenkins/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn")
                    $MVN_CMD dependency:resolve || true
                    npx playwright install --with-deps chromium || true
                '''
            }
        }
        
        stage('Run Tests') {
            steps {
                echo 'Running Playwright tests...'
                script {
                    def mvnCmd = sh(
                        script: 'command -v mvn || echo "/Users/chakrapanipriyadarshi/.jenkins/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn"',
                        returnStdout: true
                    ).trim()
                    sh "${mvnCmd} test"
                }
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
                script {
                    def mvnCmd = sh(
                        script: 'command -v mvn || echo "/Users/chakrapanipriyadarshi/.jenkins/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn"',
                        returnStdout: true
                    ).trim()
                    sh "${mvnCmd} allure:report"
                }
            }
        }
        
        stage('Publish Allure Report') {
            steps {
                echo 'Publishing Allure report...'
                script {
                    // Archive Allure report as artifact (works without Allure plugin)
                    // The report is already generated in target/site/allure-maven-plugin/
                    echo 'Allure report generated at: target/site/allure-maven-plugin/'
                    echo 'To view the report, install Allure Jenkins Plugin or download the artifact'
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
            // Archive Allure HTML report (can be downloaded and opened in browser)
            archiveArtifacts artifacts: 'target/site/allure-maven-plugin/**/*', allowEmptyArchive: true
            
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

