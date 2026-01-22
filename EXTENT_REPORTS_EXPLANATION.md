# ExtentReports - Complete Step-by-Step Guide

## 🎯 What is ExtentReports?

**ExtentReports** is a **test reporting library** that generates beautiful, interactive HTML test reports. It provides:

- ✅ **Rich HTML reports** with charts and graphs
- ✅ **Test execution details** (pass/fail/skip)
- ✅ **Screenshots** and media attachments
- ✅ **Timeline** of test execution
- ✅ **Categories and tags** for organizing tests
- ✅ **Dashboard** with statistics

---

## 📊 How ExtentReports Works - Step-by-Step

### **Step 1: Initialize ExtentReports**

**What happens:**
- Create an `ExtentReports` instance
- Configure report settings (path, name, theme)
- This is the **main report object**

**In your project (`ExtentManager.java`):**
```java
public class ExtentManager {
    private static ExtentReports extent;
    
    public static ExtentReports getExtentReports() {
        if (extent == null) {
            extent = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReports/ExtentReport.html");
            extent.attachReporter(spark);
        }
        return extent;
    }
}
```

**What this does:**
1. Creates a singleton `ExtentReports` instance
2. Creates a `ExtentSparkReporter` (HTML reporter)
3. Sets report output path: `target/ExtentReports/ExtentReport.html`
4. Attaches reporter to ExtentReports instance

---

### **Step 2: Create Test in Report**

**What happens:**
- For each test method, create an `ExtentTest` object
- This represents a single test in the report
- Each test gets its own entry in the report

**In your project (`ExtentReportListeners.java`):**
```java
@Override
public void onTestStart(ITestResult result) {
    ExtentTest test = extent.createTest(result.getMethod().getMethodName());
    extentTest.set(test);
}
```

**What this does:**
1. TestNG calls `onTestStart()` when a test begins
2. Creates a new `ExtentTest` with test method name
3. Stores it in ThreadLocal (for thread safety)
4. This test will appear in the report

**Example:**
```java
// When testGoogleTitle() starts:
ExtentTest test = extent.createTest("testGoogleTitle");
// Creates a new test entry in report
```

---

### **Step 3: Log Test Steps**

**What happens:**
- Add detailed steps/logs to the test
- Each step shows what happened during test execution
- Steps can have status: PASS, FAIL, INFO, WARNING, SKIP

**In your project:**
```java
public static void addStep(String stepDescription, String status) {
    ExtentTest test = extentTest.get();
    test.log(Status.valueOf(status), stepDescription);
}
```

**Usage example:**
```java
ExtentReportListeners.addStep("Navigate to Google homepage", "PASS");
ExtentReportListeners.addStep("Verify page title", "PASS");
ExtentReportListeners.addStep("Click on search button", "INFO");
```

**What appears in report:**
```
testGoogleTitle
  ✓ Navigate to Google homepage
  ✓ Verify page title
  ℹ Click on search button
```

---

### **Step 4: Mark Test Result**

**What happens:**
- When test completes, mark it as PASS, FAIL, or SKIP
- This updates the test status in the report

**In your project (`ExtentReportListeners.java`):**
```java
@Override
public void onTestSuccess(ITestResult result) {
    extentTest.get().pass("Test passed");
}

@Override
public void onTestFailure(ITestResult result) {
    extentTest.get().fail("Test failed");
}

@Override
public void onTestSkipped(ITestResult result) {
    extentTest.get().skip("Test skipped");
}
```

**What this does:**
1. TestNG calls appropriate method based on test result
2. Updates ExtentTest status (pass/fail/skip)
3. Adds final message to report

**Example:**
```java
// If testGoogleTitle() passes:
extentTest.get().pass("Test passed");
// Report shows: testGoogleTitle ✓ PASSED
```

---

### **Step 5: Flush Report**

**What happens:**
- Write all test data to HTML file
- Generate the final report
- Close the report

**In your project:**
```java
@Override
public void onFinish(ITestContext context) {
    extent.flush();
}
```

**What this does:**
1. TestNG calls `onFinish()` when all tests complete
2. `extent.flush()` writes all data to HTML file
3. Report is saved to `target/ExtentReports/ExtentReport.html`
4. Report is ready to view

---

## 🔄 Complete Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│              EXTENTREPORTS EXECUTION FLOW                    │
└─────────────────────────────────────────────────────────────┘

1. INITIALIZATION (Before tests run)
   │
   ├─> ExtentManager.getExtentReports()
   │   ├─> Creates ExtentReports instance
   │   ├─> Creates ExtentSparkReporter
   │   ├─> Sets output path: target/ExtentReports/ExtentReport.html
   │   └─> Attaches reporter
   │
   ▼
2. TEST START (For each test method)
   │
   ├─> onTestStart(ITestResult)
   │   ├─> extent.createTest("testGoogleTitle")
   │   ├─> Creates ExtentTest object
   │   └─> Stores in ThreadLocal
   │
   ▼
3. TEST EXECUTION (During test run)
   │
   ├─> Test code executes
   │   ├─> ExtentReportListeners.addStep("Step 1", "PASS")
   │   ├─> ExtentReportListeners.addStep("Step 2", "INFO")
   │   └─> Logs are added to ExtentTest
   │
   ▼
4. TEST COMPLETE (After test finishes)
   │
   ├─> onTestSuccess() OR onTestFailure() OR onTestSkipped()
   │   ├─> extentTest.get().pass("Test passed")
   │   └─> Marks test status in report
   │
   ▼
5. ALL TESTS COMPLETE
   │
   ├─> onFinish(ITestContext)
   │   ├─> extent.flush()
   │   ├─> Writes all data to HTML
   │   └─> Report generated: ExtentReport.html
   │
   ▼
6. VIEW REPORT
   │
   └─> Open target/ExtentReports/ExtentReport.html in browser
```

---

## 📝 Detailed Code Walkthrough

### **1. ExtentManager.java - Report Initialization**

```java
public class ExtentManager {
    private static ExtentReports extent;
    
    public static ExtentReports getExtentReports() {
        // Singleton pattern - only one instance
        if (extent == null) {
            // Step 1: Create ExtentReports instance
            extent = new ExtentReports();
            
            // Step 2: Create HTML reporter
            ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReports/ExtentReport.html");
            
            // Step 3: Configure reporter (optional)
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Playwright Test Report");
            spark.config().setReportName("Automation Test Report");
            
            // Step 4: Attach reporter to ExtentReports
            extent.attachReporter(spark);
            
            // Step 5: Set system information (optional)
            extent.setSystemInfo("OS", "Mac OS");
            extent.setSystemInfo("Java Version", "17");
        }
        return extent;
    }
}
```

**What each part does:**
- `new ExtentReports()` - Creates main report object
- `ExtentSparkReporter` - HTML reporter (generates HTML file)
- `attachReporter()` - Connects reporter to ExtentReports
- `setSystemInfo()` - Adds environment info to report

---

### **2. ExtentReportListeners.java - Test Lifecycle**

```java
public class ExtentReportListeners implements ITestListener {
    
    // Get ExtentReports instance
    private static ExtentReports extent = ExtentManager.getExtentReports();
    
    // ThreadLocal for thread-safe test storage
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    // Called when test starts
    @Override
    public void onTestStart(ITestResult result) {
        // Create test entry in report
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }
    
    // Called when test passes
    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().pass("Test passed");
    }
    
    // Called when test fails
    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail("Test failed");
        // Can also add screenshot:
        // extentTest.get().fail(result.getThrowable()).addScreenCaptureFromPath("screenshot.png");
    }
    
    // Called when test is skipped
    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().skip("Test skipped");
    }
    
    // Called when all tests finish
    @Override
    public void onFinish(ITestContext context) {
        extent.flush(); // Generate HTML report
    }
}
```

**Key Points:**
- `ThreadLocal<ExtentTest>` - Ensures each thread has its own test instance
- `onTestStart()` - Creates test entry
- `onTestSuccess/Failure/Skipped()` - Updates test status
- `onFinish()` - Generates final report

---

### **3. testng.xml - Register Listener**

```xml
<suite name="Playwright Automation Test Suite">
    <listeners>
        <listener class-name="org.example.listeners.ExtentReportListeners" />
    </listeners>
    
    <test name="Playwright Tests">
        <classes>
            <class name="PlayWrightTest">
                <methods>
                    <include name="testGoogleTitle"/>
                    <include name="testAddMobileToCart"/>
                </methods>
            </class>
        </classes>
    </test>
</suite>
```

**What this does:**
- Registers `ExtentReportListeners` with TestNG
- TestNG automatically calls listener methods during test execution
- No need to manually call ExtentReports methods in test code

---

## 🎯 Real Example: Your Test Execution

### **Test: testGoogleTitle()**

**Step-by-step what happens:**

```
1. TestNG starts testGoogleTitle()
   │
   ▼
2. TestNG calls: onTestStart()
   ├─> Creates ExtentTest: "testGoogleTitle"
   └─> Report now has: [testGoogleTitle] (pending)
   │
   ▼
3. Test code executes:
   ├─> googlePage.navigate()
   ├─> googlePage.getPageTitle()
   └─> Assert.assertEquals(...)
   │
   ▼
4. Test passes
   │
   ▼
5. TestNG calls: onTestSuccess()
   ├─> extentTest.get().pass("Test passed")
   └─> Report now shows: [testGoogleTitle] ✓ PASSED
   │
   ▼
6. All tests complete
   │
   ▼
7. TestNG calls: onFinish()
   ├─> extent.flush()
   └─> HTML report generated
```

**Final Report Shows:**
```
Dashboard:
├── Total Tests: 2
├── Passed: 2
├── Failed: 0
└── Skipped: 0

Test Results:
├── testGoogleTitle ✓ PASSED
└── testAddMobileToCart ✓ PASSED
```

---

## 📊 Report Structure

### **Generated HTML Report Contains:**

```
ExtentReport.html
├── Dashboard
│   ├── Test Summary (Total, Passed, Failed, Skipped)
│   ├── Charts (Pie chart, Bar chart)
│   └── System Information
│
├── Test Details
│   ├── testGoogleTitle
│   │   ├── Status: PASSED
│   │   ├── Duration: 2.5s
│   │   ├── Steps:
│   │   │   ├── Navigate to Google homepage
│   │   │   └── Verify page title
│   │   └── Screenshots (if any)
│   │
│   └── testAddMobileToCart
│       ├── Status: PASSED
│       ├── Duration: 15.3s
│       └── Steps: ...
│
└── Timeline
    └── Test execution order
```

---

## 🔧 Advanced Features

### **1. Adding Screenshots**

```java
@Override
public void onTestFailure(ITestResult result) {
    // Take screenshot
    String screenshotPath = takeScreenshot(result.getMethod().getMethodName());
    
    // Add to report
    extentTest.get().fail(result.getThrowable())
              .addScreenCaptureFromPath(screenshotPath);
}
```

### **2. Adding Categories/Tags**

```java
@Override
public void onTestStart(ITestResult result) {
    ExtentTest test = extent.createTest(result.getMethod().getMethodName());
    
    // Add category
    test.assignCategory("Smoke", "Regression");
    
    extentTest.set(test);
}
```

### **3. Adding Author Information**

```java
@Override
public void onTestStart(ITestResult result) {
    ExtentTest test = extent.createTest(result.getMethod().getMethodName());
    
    // Add author
    test.assignAuthor("John Doe");
    
    extentTest.set(test);
}
```

### **4. Adding Logs with Different Levels**

```java
// In your test code
ExtentReportListeners.addStep("Navigate to page", "PASS");
ExtentReportListeners.addStep("Click button", "INFO");
ExtentReportListeners.addStep("Verify element", "WARNING");
ExtentReportListeners.addStep("Test failed", "FAIL");
```

---

## 🎓 Key Concepts

### **1. ExtentReports (Main Object)**
- **Purpose:** Main report manager
- **Lifecycle:** Created once, used throughout test execution
- **Responsibility:** Manages all tests and generates final report

### **2. ExtentTest (Test Object)**
- **Purpose:** Represents a single test
- **Lifecycle:** Created when test starts, updated during execution
- **Responsibility:** Stores test steps, status, and details

### **3. ExtentSparkReporter (Reporter)**
- **Purpose:** Generates HTML report
- **Lifecycle:** Created once, attached to ExtentReports
- **Responsibility:** Converts data to HTML format

### **4. ThreadLocal**
- **Purpose:** Thread-safe storage
- **Why needed:** Multiple tests can run in parallel
- **How it works:** Each thread gets its own ExtentTest instance

---

## 📋 Complete Setup Checklist

### **1. Add Dependency (pom.xml)**
```xml
<dependency>
    <groupId>com.aventstack</groupId>
    <artifactId>extentreports</artifactId>
    <version>5.1.1</version>
</dependency>
```

### **2. Create ExtentManager**
- ✅ Singleton pattern
- ✅ Initialize ExtentReports
- ✅ Configure reporter

### **3. Create Listener**
- ✅ Implement ITestListener
- ✅ Handle test lifecycle methods
- ✅ Use ThreadLocal for ExtentTest

### **4. Register Listener**
- ✅ Add to testng.xml
- ✅ Or use @Listeners annotation

### **5. Run Tests**
- ✅ Execute tests normally
- ✅ Report generated automatically

### **6. View Report**
- ✅ Open `target/ExtentReports/ExtentReport.html`
- ✅ View in browser

---

## 🚀 Quick Reference

### **Common Methods:**

```java
// Create test
ExtentTest test = extent.createTest("testName");

// Log steps
test.pass("Step passed");
test.fail("Step failed");
test.info("Information");
test.warning("Warning");

// Add screenshot
test.addScreenCaptureFromPath("path/to/screenshot.png");

// Add category
test.assignCategory("Smoke");

// Add author
test.assignAuthor("John Doe");

// Flush report
extent.flush();
```

---

## 📊 Your Project's ExtentReports Flow

```
1. Maven runs: mvn test
   │
   ▼
2. TestNG reads: testng.xml
   │
   ▼
3. TestNG loads: ExtentReportListeners
   │
   ▼
4. ExtentManager.getExtentReports()
   ├─> Creates ExtentReports
   └─> Configures HTML reporter
   │
   ▼
5. TestNG starts: testGoogleTitle()
   │
   ▼
6. onTestStart() called
   ├─> Creates ExtentTest: "testGoogleTitle"
   └─> Stores in ThreadLocal
   │
   ▼
7. Test executes
   │
   ▼
8. onTestSuccess() called
   ├─> Marks test as PASSED
   └─> Updates report
   │
   ▼
9. All tests complete
   │
   ▼
10. onFinish() called
    ├─> extent.flush()
    └─> HTML report generated
    │
    ▼
11. Report available at:
    └─> target/ExtentReports/ExtentReport.html
```

---

## 💡 Summary

**ExtentReports Process:**
1. ✅ **Initialize** - Create ExtentReports instance
2. ✅ **Create Test** - Create ExtentTest for each test method
3. ✅ **Log Steps** - Add detailed steps during execution
4. ✅ **Mark Status** - Update test status (pass/fail/skip)
5. ✅ **Generate Report** - Flush data to HTML file
6. ✅ **View Report** - Open HTML in browser

**Key Components:**
- `ExtentReports` - Main report manager
- `ExtentTest` - Individual test entry
- `ExtentSparkReporter` - HTML generator
- `ITestListener` - TestNG integration

**Your Setup:**
- ✅ ExtentManager - Initializes report
- ✅ ExtentReportListeners - Handles test lifecycle
- ✅ testng.xml - Registers listener
- ✅ Automatic report generation

---

**Last Updated:** Based on your current ExtentReports implementation

