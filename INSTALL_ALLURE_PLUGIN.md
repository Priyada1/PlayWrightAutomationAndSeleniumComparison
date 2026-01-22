# How to Install Allure Jenkins Plugin

## 📦 Step-by-Step Installation Guide

### **Step 1: Access Jenkins Plugin Manager**

1. **Open Jenkins Dashboard:**
   ```
   http://localhost:8080
   ```

2. **Navigate to Plugin Manager:**
   - Click **"Manage Jenkins"** (left sidebar)
   - Click **"Manage Plugins"**

### **Step 2: Install Allure Plugin**

1. **Go to Available Plugins Tab:**
   - Click on **"Available"** tab at the top

2. **Search for Allure Plugin:**
   - In the search box, type: `Allure` or `allure-jenkins-plugin`
   - Look for: **"Allure Plugin"** or **"allure-jenkins-plugin"**

3. **Select and Install:**
   - ✅ Check the box next to **"Allure Plugin"**
   - Click **"Install without restart"** or **"Download now and install after restart"**

4. **Wait for Installation:**
   - Jenkins will download and install the plugin
   - You'll see a progress bar

5. **Restart Jenkins (if required):**
   - If prompted, click **"Restart Jenkins when installation is complete"**
   - Or manually restart: **Manage Jenkins** → **Restart Jenkins**

### **Step 3: Verify Installation**

1. **Check Installed Plugins:**
   - Go to **Manage Jenkins** → **Manage Plugins** → **Installed** tab
   - Search for "Allure"
   - You should see **"Allure Plugin"** listed

2. **Verify in Pipeline:**
   - After updating Jenkinsfile, run a build
   - You should see **"Allure Report"** link in the build page

---

## 🔧 Alternative: Install via Jenkins CLI

If you prefer command line:

```bash
# Download plugin
wget https://updates.jenkins.io/download/plugins/allure-jenkins-plugin/latest/allure-jenkins-plugin.hpi

# Install via Jenkins CLI
java -jar jenkins-cli.jar -s http://localhost:8080 install-plugin allure-jenkins-plugin.hpi

# Restart Jenkins
java -jar jenkins-cli.jar -s http://localhost:8080 restart
```

---

## ✅ After Installation

### **1. Update Jenkinsfile**

Your Jenkinsfile has already been updated to use the `allure()` step:

```groovy
stage('Publish Allure Report') {
    steps {
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
```

### **2. Run a Build**

1. Go to your Jenkins job: `http://localhost:8080/job/playWrightTest/`
2. Click **"Build Now"**
3. Wait for build to complete

### **3. View Allure Report**

After the build completes:

1. **Open the build page:**
   ```
   http://localhost:8080/job/playWrightTest/[BUILD_NUMBER]/
   ```

2. **Look for "Allure Report" link:**
   - In the left sidebar
   - Or in the build summary at the top
   - It will look like: **"Allure Report"** or **"Allure"**

3. **Click the link:**
   - The report opens directly in Jenkins UI
   - No need to download files!

---

## 🎯 What You'll See

### **Before Plugin (Current):**
```
Build Page
├── [Build Artifacts] ← Need to download files
├── [Console Output]
└── [Test Results]
```

### **After Plugin (With Integration):**
```
Build Page
├── [Allure Report] ← Click to view directly! ✨
├── [Build Artifacts]
├── [Console Output]
└── [Test Results]
```

---

## 📊 Allure Report Features in Jenkins

Once installed, you'll get:

- ✅ **Direct Link:** "Allure Report" link in every build
- ✅ **In-Browser View:** View report without downloading
- ✅ **Trend Charts:** See test trends across builds
- ✅ **History:** Compare reports from different builds
- ✅ **Integration:** Seamless integration with Jenkins UI

---

## 🔍 Troubleshooting

### **Issue: "No such DSL method 'allure' found"**

**Solution:**
- Plugin not installed or not restarted
- Go to **Manage Plugins** → **Installed** → Check if "Allure Plugin" is listed
- If not listed, install it (see Step 2)
- If listed, restart Jenkins

### **Issue: "Allure Report" link not appearing**

**Solution:**
1. Check if `target/allure-results/` directory exists after test run
2. Verify Jenkinsfile has `allure()` step
3. Check build console for errors
4. Restart Jenkins after plugin installation

### **Issue: Plugin installation fails**

**Solution:**
1. Check Jenkins version compatibility
2. Try installing from **Available** tab instead of **Updates** tab
3. Check Jenkins logs: `~/.jenkins/logs/`
4. Ensure you have admin permissions

---

## 📝 Plugin Configuration (Optional)

You can configure Allure plugin globally:

1. **Go to:** **Manage Jenkins** → **Configure System**
2. **Scroll to:** "Allure" section
3. **Configure:**
   - Allure installation path (if custom)
   - Default report settings

**Note:** For most cases, default settings work fine!

---

## 🚀 Quick Start Checklist

- [ ] Install Allure Plugin from Plugin Manager
- [ ] Restart Jenkins (if required)
- [ ] Verify plugin in Installed plugins list
- [ ] Jenkinsfile already updated with `allure()` step
- [ ] Run a build
- [ ] Check for "Allure Report" link in build page
- [ ] Click link to view report

---

## 📚 Additional Resources

- **Allure Plugin Documentation:** https://plugins.jenkins.io/allure/
- **Allure Framework:** https://docs.qameta.io/allure/
- **Jenkins Plugin Wiki:** https://wiki.jenkins.io/display/JENKINS/Allure+Plugin

---

**Last Updated:** Based on your current Jenkinsfile configuration

