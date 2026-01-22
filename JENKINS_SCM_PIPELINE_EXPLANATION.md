# Jenkins SCM Pipeline - Complete Explanation

## 📚 What is Jenkins SCM Pipeline?

**SCM Pipeline** (Source Control Management Pipeline) is a Jenkins feature that allows you to:
- Store your pipeline definition (`Jenkinsfile`) directly in your Git repository
- Automatically trigger builds when code changes
- Version control your CI/CD configuration alongside your code
- Enable "Pipeline as Code" - infrastructure defined in code

---

## 🔄 How It Works - Step by Step

### **1. Initial Setup (One-Time)**

```
Developer → Git Repository → Jenkins Job Configuration
```

1. **Developer creates `Jenkinsfile`** in the repository root
2. **Jenkins Job is created** with "Pipeline script from SCM" option
3. **Jenkins is configured** to:
   - Connect to Git repository (GitHub in your case)
   - Monitor specific branch (e.g., `main`)
   - Use `Jenkinsfile` as the pipeline definition

### **2. Continuous Workflow (Every Push)**

```
┌─────────────────────────────────────────────────────────────┐
│                    JENKINS SCM PIPELINE FLOW                 │
└─────────────────────────────────────────────────────────────┘

1. DEVELOPER COMMITS CODE
   │
   ├─> git add .
   ├─> git commit -m "Update tests"
   └─> git push origin main
       │
       ▼
2. GIT REPOSITORY (GitHub)
   │
   ├─> Code stored in repository
   ├─> Jenkinsfile updated
   └─> Webhook triggered (if configured)
       │
       ▼
3. JENKINS DETECTS CHANGE
   │
   ├─> Polls Git repository (or receives webhook)
   ├─> Detects new commit
   └─> Starts new build
       │
       ▼
4. JENKINS FETCHES JENKINSFILE
   │
   ├─> Checks out code from Git
   ├─> Reads Jenkinsfile from repository
   └─> Parses pipeline definition
       │
       ▼
5. JENKINS EXECUTES PIPELINE
   │
   ├─> Stage 1: Checkout (already done)
   ├─> Stage 2: Verify Tools
   ├─> Stage 3: Build
   ├─> Stage 4: Install Playwright Browsers
   ├─> Stage 5: Run Tests
   ├─> Stage 6: Generate Allure Report
   └─> Stage 7: Publish Allure Report
       │
       ▼
6. RESULTS & ARTIFACTS
   │
   ├─> Test results archived
   ├─> Allure report published
   ├─> Build status updated
   └─> Email notification sent (if configured)
```

---

## 🔍 Detailed Breakdown of Your Pipeline

### **Stage 1: Checkout**

```groovy
stage('Checkout') {
    steps {
        checkout scm  // ← This is the magic!
    }
}
```

**What `checkout scm` does:**
- `scm` = Source Control Management (Git in your case)
- Jenkins automatically uses the repository URL configured in the job
- Checks out the code to Jenkins workspace
- Uses the same branch/commit that triggered the build

**In your case:**
- Repository: `https://github.com/Priyada1/PlayWrightAutomationAndSeleniumComparison.git`
- Branch: `main` (or branch specified in job config)
- Workspace: `/Users/chakrapanipriyadarshi/.jenkins/workspace/playWrightTest`

### **Stage 2: Verify Tools**

```groovy
stage('Verify Tools') {
    steps {
        sh '''
            java -version
            mvn -version
            node -v
        '''
    }
}
```

**Purpose:** Ensures required tools are available before proceeding

### **Stage 3: Build**

```groovy
stage('Build') {
    steps {
        sh 'mvn clean compile test-compile'
    }
}
```

**Purpose:** Compiles Java code and prepares test classes

### **Stage 4: Install Playwright Browsers**

```groovy
stage('Install Playwright Browsers') {
    steps {
        sh 'npx playwright install --with-deps chromium'
    }
}
```

**Purpose:** Downloads browser binaries needed for tests

### **Stage 5: Run Tests**

```groovy
stage('Run Tests') {
    steps {
        sh 'mvn test'
    }
    post {
        always {
            junit 'target/surefire-reports/TEST-*.xml'
        }
    }
}
```

**Purpose:** 
- Executes TestNG tests
- Archives JUnit XML results for Jenkins

### **Stage 6 & 7: Allure Reports**

```groovy
stage('Generate Allure Report') {
    steps {
        sh 'mvn allure:report'
    }
}

stage('Publish Allure Report') {
    steps {
        allure([results: [[path: 'target/allure-results']]])
    }
}
```

**Purpose:** Generates and publishes beautiful HTML test reports

---

## 🔑 Key Concepts

### **1. `checkout scm` - The Magic Command**

```groovy
checkout scm
```

This single line tells Jenkins:
- ✅ Use the Git repository configured in the job
- ✅ Check out the branch that triggered the build
- ✅ Get the latest commit
- ✅ Place files in the workspace

**No need to specify:**
- ❌ Repository URL (already in job config)
- ❌ Branch name (uses trigger branch)
- ❌ Credentials (already configured)

### **2. Jenkinsfile Location**

```
Your Repository:
├── Jenkinsfile          ← Pipeline definition (must be in root)
├── pom.xml
├── src/
│   ├── main/
│   └── test/
└── ...
```

**Important:** `Jenkinsfile` must be in the repository root!

### **3. Two Types of Checkout**

**Automatic Checkout (Declarative Pipeline):**
- Jenkins automatically checks out code before pipeline starts
- This is why you see "Declarative: Checkout SCM" in logs

**Manual Checkout (in your pipeline):**
```groovy
stage('Checkout') {
    steps {
        checkout scm  // Explicit checkout (redundant but shows intent)
    }
}
```

**Note:** The manual checkout is technically redundant because Jenkins already checked out the code, but it's good practice to be explicit.

### **4. How Jenkins Finds Your Jenkinsfile**

**Job Configuration:**
```
Pipeline Definition: Pipeline script from SCM
SCM: Git
Repository URL: https://github.com/.../PlayWrightAutomationAndSeleniumComparison.git
Branches to build: */main
Script Path: Jenkinsfile  ← This tells Jenkins where to find it
```

**Jenkins Process:**
1. Checks out code from Git
2. Looks for `Jenkinsfile` in repository root
3. Reads and parses the Groovy script
4. Executes the pipeline stages

---

## 🔄 Trigger Mechanisms

### **1. Polling (Automatic)**

Jenkins periodically checks Git for changes:

```groovy
// In job configuration:
Poll SCM: H/5 * * * *  // Every 5 minutes
```

**How it works:**
- Jenkins runs `git fetch` every 5 minutes
- Compares commit hashes
- If new commit found → triggers build

### **2. Webhook (Real-time)**

GitHub sends notification to Jenkins on push:

```
Developer pushes → GitHub → Webhook → Jenkins → Build starts
```

**Setup:**
- GitHub: Settings → Webhooks → Add webhook
- URL: `http://your-jenkins:8080/github-webhook/`
- Events: Push, Pull Request

### **3. Manual Trigger**

User clicks "Build Now" in Jenkins UI

---

## 📊 Your Specific Pipeline Flow

### **What Happens When You Push Code:**

```
1. You: git push origin main
   │
   ▼
2. GitHub: Receives push, updates repository
   │
   ▼
3. Jenkins: (Polling or Webhook detects change)
   │
   ├─> Checks out: https://github.com/Priyada1/PlayWrightAutomationAndSeleniumComparison.git
   ├─> Branch: main
   ├─> Reads: Jenkinsfile
   └─> Starts build
       │
       ▼
4. Pipeline Execution:
   │
   ├─> [Checkout] Already done by Jenkins
   ├─> [Verify Tools] Checks Java, Maven, Node
   ├─> [Build] mvn clean compile test-compile
   ├─> [Install Browsers] npx playwright install chromium
   ├─> [Run Tests] mvn test (runs PlayWrightTest.java)
   ├─> [Generate Report] mvn allure:report
   └─> [Publish Report] Publishes Allure to Jenkins UI
       │
       ▼
5. Results:
   │
   ├─> Build Status: SUCCESS/FAILURE
   ├─> Test Results: JUnit XML archived
   ├─> Allure Report: Available in Jenkins UI
   └─> Email: Sent (if configured)
```

---

## 🎯 Advantages of SCM Pipeline

### **✅ Version Control**
- Pipeline definition is in Git
- Changes are tracked
- Can rollback to previous pipeline version

### **✅ Code Review**
- Pipeline changes go through PR process
- Team reviews CI/CD changes
- Better collaboration

### **✅ Consistency**
- Same pipeline for all branches
- No manual configuration drift
- "Pipeline as Code" principle

### **✅ Automatic Updates**
- Update Jenkinsfile → Push → Pipeline updates automatically
- No need to manually edit Jenkins job

### **✅ Branch-Specific Pipelines**
- Different branches can have different pipelines
- Feature branches can run different tests

---

## 🔧 Configuration in Jenkins UI

### **Job Configuration:**

```
Pipeline Job Settings:
├── General
│   ├── Description
│   └── Build triggers
│
├── Pipeline
│   ├── Definition: Pipeline script from SCM  ← KEY SETTING
│   ├── SCM: Git
│   ├── Repository URL: https://github.com/.../repo.git
│   ├── Credentials: github-credentials
│   ├── Branches to build: */main
│   └── Script Path: Jenkinsfile
│
└── Build Triggers
    ├── Poll SCM: H/5 * * * *  (every 5 minutes)
    └── GitHub hook trigger for GITScm polling
```

---

## 🆚 SCM Pipeline vs Scripted Pipeline

### **SCM Pipeline (Your Setup):**
```groovy
// Jenkinsfile stored in Git
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean test'
            }
        }
    }
}
```

**Pros:**
- ✅ Version controlled
- ✅ Automatic updates
- ✅ Code review possible

### **Scripted Pipeline (In Jenkins UI):**
```groovy
// Pipeline script typed directly in Jenkins UI
// Not stored in Git
```

**Cons:**
- ❌ Not version controlled
- ❌ Manual updates required
- ❌ No code review

---

## 🐛 Common Issues & Solutions

### **Issue 1: "Jenkinsfile not found"**

**Cause:** Jenkinsfile not in repository root

**Solution:**
```
Repository structure:
✅ Correct: /Jenkinsfile
❌ Wrong: /src/Jenkinsfile
❌ Wrong: /scripts/Jenkinsfile
```

### **Issue 2: "checkout scm fails"**

**Cause:** Repository URL or credentials incorrect

**Solution:**
- Check job configuration → Repository URL
- Verify credentials in Jenkins → Credentials
- Test connection: "Test Connection" button

### **Issue 3: "Pipeline doesn't trigger on push"**

**Cause:** Polling not configured or webhook missing

**Solution:**
- Enable "Poll SCM" in job configuration
- Or set up GitHub webhook
- Or use manual trigger

### **Issue 4: "Wrong branch checked out"**

**Cause:** Branch specification incorrect

**Solution:**
- Check "Branches to build" in job config
- Use `*/main` for main branch
- Use `*/${env.BRANCH_NAME}` for current branch

---

## 📝 Summary

**Jenkins SCM Pipeline = Pipeline Definition in Git**

**Key Points:**
1. ✅ `Jenkinsfile` stored in Git repository
2. ✅ Jenkins reads `Jenkinsfile` from Git
3. ✅ `checkout scm` uses job-configured repository
4. ✅ Automatic builds on code changes
5. ✅ Version controlled CI/CD configuration

**Your Workflow:**
```
Code Change → Git Push → Jenkins Detects → Reads Jenkinsfile → Executes Pipeline → Results
```

**Benefits:**
- 🎯 Infrastructure as Code
- 🔄 Automatic updates
- 📊 Version control
- 👥 Team collaboration
- 🚀 Consistent builds

---

## 🔗 Related Files in Your Project

- **`Jenkinsfile`** - Main pipeline definition
- **`JENKINS_SETUP.md`** - Setup instructions
- **`.github/workflows/ci-cd.yml`** - GitHub Actions (alternative CI/CD)

---

## 📚 Additional Resources

- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [Pipeline Syntax Reference](https://www.jenkins.io/doc/book/pipeline/syntax/)
- [SCM Step Documentation](https://www.jenkins.io/doc/pipeline/steps/workflow-scm-step/)

---

**Last Updated:** Based on your current Jenkinsfile configuration

