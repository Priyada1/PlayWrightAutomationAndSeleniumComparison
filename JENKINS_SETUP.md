# Jenkins Pipeline Setup Guide

This guide explains how to set up Jenkins Pipeline from SCM for the Playwright Automation project.

## 📋 Prerequisites

1. **Jenkins Server** installed and running
2. **Required Jenkins Plugins** installed:
   - Pipeline
   - Allure Plugin
   - Maven Integration
   - Git Plugin
   - Email Extension Plugin (optional, for email notifications)

## 🔧 Jenkins Configuration

### Step 1: Install Required Plugins

1. Go to **Jenkins Dashboard** → **Manage Jenkins** → **Manage Plugins**
2. Install the following plugins:
   - **Allure Plugin** (`allure-jenkins-plugin`)
   - **Pipeline** (usually pre-installed)
   - **Maven Integration**
   - **Git Plugin**
   - **Email Extension Plugin** (optional)

### Step 2: Configure Global Tools

1. Go to **Manage Jenkins** → **Global Tool Configuration**

2. **Configure JDK:**
   - Name: `jdk-17` (IMPORTANT: Use this exact name, or update Jenkinsfile)
   - JAVA_HOME: `/path/to/jdk-17` (or select "Install automatically")
   - Version: `17`

3. **Configure Maven:**
   - Name: `maven3` (IMPORTANT: Use this exact name, or update Jenkinsfile)
   - MAVEN_HOME: `/path/to/maven` (or select "Install automatically")
   - Version: `3.9.x` or latest

4. **Configure Allure:**
   - Name: `Allure`
   - Installation directory: `/path/to/allure` (or select "Install automatically")
   - Version: `2.24.0`

### Step 3: Create Jenkins Pipeline Job

1. **Create New Item:**
   - Go to **Jenkins Dashboard** → **New Item**
   - Enter job name: `PlaywrightAutomation`
   - Select **Pipeline**
   - Click **OK**

2. **Configure Pipeline:**
   - **Pipeline Definition**: Select **Pipeline script from SCM**
   - **SCM**: Select **Git**
   - **Repository URL**: `https://github.com/Priyada1/PlayWrightAutomationAndSeleniumComparison.git`
   - **Credentials**: Add your GitHub credentials (if private repo)
   - **Branches to build**: `*/main` (or your branch name)
   - **Script Path**: `Jenkinsfile` (this is the default)
   - Click **Save**

## 🚀 Running the Pipeline

### Automatic Trigger:
- Pipeline runs automatically on every push to the configured branch

### Manual Trigger:
1. Go to your Jenkins job
2. Click **Build Now**

### View Results:
- Click on a build number to see details
- **Allure Report** link will appear in the build page
- **Test Results** will be displayed in the build summary

## 📊 Jenkinsfile Features

### Stages:
1. **Checkout** - Gets code from repository
2. **Build** - Compiles the project
3. **Install Playwright Browsers** - Installs required browsers
4. **Run Tests** - Executes test suite
5. **Generate Allure Report** - Creates HTML report
6. **Publish Allure Report** - Publishes to Jenkins

### Post Actions:
- **Always**: Archives test results and artifacts
- **Success**: Sends success email notification
- **Failure**: Sends failure email notification

## 🔧 Customization

### Change Java Version:
```groovy
jdk 'jdk-17'  // Change to 'jdk-21' or your configured JDK name
```

### Change Maven Version:
```groovy
maven 'maven3'  // Change to your configured Maven name
```

### Alternative: Use System Tools (No Jenkins Tool Configuration)
If you don't want to configure tools in Jenkins, use `Jenkinsfile.without-tools`:
- Rename it to `Jenkinsfile`
- Ensure Maven and Java are installed on Jenkins agent
- Set JAVA_HOME in environment if needed

### Run Specific Tests:
Edit `testng.xml` or add test groups:
```groovy
stage('Run Tests') {
    steps {
        sh 'mvn test -Dtest=PlayWrightTest#testGoogleTitle'
    }
}
```

### Parallel Execution:
```groovy
stage('Run Tests') {
    parallel {
        stage('Chrome Tests') {
            steps {
                sh 'mvn test -Dbrowser=chrome'
            }
        }
        stage('Firefox Tests') {
            steps {
                sh 'mvn test -Dbrowser=firefox'
            }
        }
    }
}
```

## 📧 Email Configuration

To enable email notifications:

1. **Configure SMTP:**
   - Go to **Manage Jenkins** → **Configure System**
   - Under **Extended E-mail Notification**, configure SMTP settings

2. **Update Email Recipients:**
   - Edit `Jenkinsfile`
   - Change `your-email@example.com` to actual email addresses

## 🔍 Troubleshooting

### Issue: "Maven not found"
- **Solution**: Configure Maven in Global Tool Configuration

### Issue: "JDK not found"
- **Solution**: Configure JDK 17 in Global Tool Configuration

### Issue: "Allure not found"
- **Solution**: Install Allure Plugin and configure in Global Tool Configuration

### Issue: "Playwright browsers not installing"
- **Solution**: Ensure Node.js is installed on Jenkins agent, or use `continue-on-error: true` (already in Jenkinsfile)

### Issue: "Tests fail in Jenkins but pass locally"
- **Solution**: 
  - Check if running in headless mode (already configured)
  - Verify timeouts are sufficient
  - Check Jenkins agent has required permissions

## 📝 Jenkinsfile Location

The `Jenkinsfile` is located at the root of the repository:
```
PlayWrigthAutomation/
└── Jenkinsfile
```

Jenkins will automatically detect and use this file when you configure "Pipeline script from SCM".

## 🎯 Quick Start Checklist

- [ ] Install required Jenkins plugins
- [ ] Configure JDK 17 in Global Tools
- [ ] Configure Maven in Global Tools
- [ ] Configure Allure in Global Tools
- [ ] Create Pipeline job
- [ ] Configure Git repository URL
- [ ] Set branch to build
- [ ] Save and run first build
- [ ] Verify Allure report is generated
- [ ] Configure email notifications (optional)

## 🔗 Additional Resources

- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [Allure Jenkins Plugin](https://plugins.jenkins.io/allure-jenkins-plugin/)
- [Maven Plugin for Jenkins](https://plugins.jenkins.io/maven-plugin/)

