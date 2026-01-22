# Allure Reports - Complete Step-by-Step Guide

## 🎯 What is Allure Reports?

**Allure Reports** is a **flexible, lightweight multi-language test reporting tool** that generates beautiful, interactive HTML test reports. It provides:

- ✅ **Rich HTML reports** with detailed test execution information
- ✅ **Test history** and trends across multiple runs
- ✅ **Attachments** (screenshots, logs, videos)
- ✅ **Test categorization** (Epic, Feature, Story)
- ✅ **Severity levels** (Blocker, Critical, Normal, Minor, Trivial)
- ✅ **Step-by-step execution** details
- ✅ **Integration** with TestNG, JUnit, Cucumber, etc.

---

## 📊 How Allure Reports Works - Step-by-Step

### **Step 1: Add Allure Dependencies**

**What happens:**
- Add Allure TestNG integration dependency
- Add Allure Maven plugin
- Configure in `pom.xml`

**In your project (`pom.xml`):**
```xml
<dependencies>
    <!-- Allure TestNG Integration -->
    <dependency>
        <groupId>io.qameta.allure</groupId>
        <artifactId>allure-testng</artifactId>
        <version>2.24.0</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Allure Maven Plugin -->
        <plugin>
            <groupId>io.qameta.allure</groupId>
            <artifactId>allure-maven</artifactId>
            <version>2.12.0</version>
        </plugin>
        
        <!-- Configure Surefire Plugin -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
                <properties>
                    <property>
                        <name>listener</name>
                        <value>io.qameta.allure.testng.AllureTestNg</value>
                    </property>
                </properties>
            </configuration>
        </plugin>
    </plugins>
</build>
```

**What this does:**
1. `allure-testng` - Integrates Allure with TestNG
2. `allure-maven` - Maven plugin to generate reports
3. `AllureTestNg` listener - Captures test execution data

---

### **Step 2: Register Allure Listener**

**What happens:**
- Register Allure listener with TestNG
- Listener automatically captures test execution data

**In your project (`testng.xml`):**
```xml
<suite name="Playwright Automation Suite">
    <listeners>
        <listener class-name="io.qameta.allure.testng.AllureTestNg"/>
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
- TestNG automatically calls Allure listener during test execution
- Allure captures test data and stores in `target/allure-results/`
- No manual code needed - automatic data collection

---

### **Step 3: Add Allure Annotations to Tests**

**What happens:**
- Add annotations to test methods
- Categorize and describe tests
- Set severity levels

**In your project (`PlayWrightTest.java`):**
```java
@Test
@Epic("Web Application Testing")
@Feature("Google Search")
@Story("Verify Google Page Title")
@Description("This test verifies that the Google homepage displays the correct page title")
@Severity(SeverityLevel.CRITICAL)
public void testGoogleTitle() {
    // Test code
}
```

**What each annotation does:**

| Annotation | Purpose | Example |
|------------|---------|---------|
| `@Epic` | High-level feature group | "Web Application Testing" |
| `@Feature` | Feature being tested | "Google Search" |
| `@Story` | User story or scenario | "Verify Google Page Title" |
| `@Description` | Detailed test description | "This test verifies..." |
| `@Severity` | Test importance level | `SeverityLevel.CRITICAL` |

**Severity Levels:**
- `BLOCKER` - Blocks development/testing
- `CRITICAL` - Critical functionality
- `NORMAL` - Normal functionality
- `MINOR` - Minor functionality
- `TRIVIAL` - Trivial functionality

---

### **Step 4: Add Allure Steps**

**What happens:**
- Wrap test steps with `Allure.step()`
- Creates detailed step-by-step execution log
- Each step appears in the report

**In your project:**
```java
@Test
public void testGoogleTitle() {
    // Step 1: Navigate
    Allure.step("Navigate to Google homepage", () -> {
        googlePage.navigate();
    });
    
    // Step 2: Get title
    String actualTitle = Allure.step("Get page title", () -> {
        return googlePage.getPageTitle();
    });
    
    // Step 3: Verify
    Allure.step("Verify page title matches expected value: " + expectedTitle, () -> {
        Assert.assertEquals(actualTitle, expectedTitle);
    });
}
```

**What this does:**
1. Each `Allure.step()` creates a step entry
2. Steps are nested and organized in the report
3. If step fails, Allure captures the error
4. Steps appear in chronological order

**Report shows:**
```
testGoogleTitle
  ├─ Navigate to Google homepage ✓
  ├─ Get page title ✓
  └─ Verify page title matches expected value ✓
```

---

### **Step 5: Test Execution**

**What happens:**
- Tests run normally
- Allure listener captures execution data
- Data stored in JSON format in `target/allure-results/`

**During test execution:**
```
1. Test starts
   └─> Allure captures: test name, start time

2. Steps execute
   └─> Allure captures: step name, duration, status

3. Assertions execute
   └─> Allure captures: pass/fail status

4. Test completes
   └─> Allure captures: end time, duration, status
```

**Data stored in:**
```
target/allure-results/
├── 1234567890-result.json  (Test result data)
├── 1234567891-result.json  (Step data)
└── ...
```

---

### **Step 6: Generate Allure Report**

**What happens:**
- Run Maven command to generate HTML report
- Allure reads JSON files from `target/allure-results/`
- Generates interactive HTML report

**Command:**
```bash
mvn allure:report
```

**What this does:**
1. Reads all JSON files from `target/allure-results/`
2. Processes test data
3. Generates HTML report in `target/site/allure-maven-plugin/`
4. Creates interactive dashboard

**Output:**
```
target/site/allure-maven-plugin/
├── index.html          (Main report - open this!)
├── app.js
├── data/
└── plugins/
```

---

### **Step 7: View Allure Report**

**What happens:**
- Open `index.html` in browser
- View interactive report with all test details

**Report sections:**
1. **Overview** - Test summary, statistics, trends
2. **Behaviors** - Tests grouped by Epic/Feature/Story
3. **Packages** - Tests organized by package
4. **Suites** - TestNG suites
5. **Graphs** - Pie charts, duration graphs, retry trends
6. **Timeline** - Test execution timeline

---

## 🔄 Complete Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│              ALLURE REPORTS EXECUTION FLOW                  │
└─────────────────────────────────────────────────────────────┘

1. SETUP (One-time)
   │
   ├─> Add dependencies to pom.xml
   │   ├─> allure-testng
   │   └─> allure-maven plugin
   │
   ├─> Register listener in testng.xml
   │   └─> io.qameta.allure.testng.AllureTestNg
   │
   └─> Configure Surefire plugin
       └─> Add AllureTestNg listener
   │
   ▼
2. TEST ANNOTATION (In test code)
   │
   ├─> Add @Epic, @Feature, @Story
   ├─> Add @Description
   ├─> Add @Severity
   └─> Wrap steps with Allure.step()
   │
   ▼
3. TEST EXECUTION
   │
   ├─> Run: mvn test
   │   │
   │   ├─> TestNG starts test
   │   │   └─> Allure listener activated
   │   │
   │   ├─> Test method executes
   │   │   ├─> Allure.step() captures steps
   │   │   └─> Annotations captured
   │   │
   │   └─> Test completes
   │       └─> Data saved to target/allure-results/*.json
   │
   ▼
4. REPORT GENERATION
   │
   ├─> Run: mvn allure:report
   │   │
   │   ├─> Reads JSON files from target/allure-results/
   │   ├─> Processes test data
   │   ├─> Generates HTML report
   │   └─> Saves to target/site/allure-maven-plugin/
   │
   ▼
5. VIEW REPORT
   │
   └─> Open: target/site/allure-maven-plugin/index.html
       └─> Interactive HTML report with all details
```

---

## 📝 Detailed Code Walkthrough

### **1. Test Method with Allure Annotations**

**Your project example:**
```java
@Test
@Epic("Web Application Testing")
@Feature("Google Search")
@Story("Verify Google Page Title")
@Description("This test verifies that the Google homepage displays the correct page title")
@Severity(SeverityLevel.CRITICAL)
public void testGoogleTitle() {
    // Using Page Object Model
    GooglePage googlePage = new GooglePage(page);
    
    // Navigate to Google and get title
    Allure.step("Navigate to Google homepage", () -> {
        googlePage.navigate();
    });
    
    String actualTitle = Allure.step("Get page title", () -> {
        return googlePage.getPageTitle();
    });

    // Validate title
    String expectedTitle = googlePage.getExpectedTitle();
    Allure.step("Verify page title matches expected value: " + expectedTitle, () -> {
        Assert.assertEquals(actualTitle, expectedTitle,
            "Page title does not match expected value. Actual: " + actualTitle);
    });
}
```

**What each part does:**

1. **Annotations:**
   - `@Epic("Web Application Testing")` - Groups related features
   - `@Feature("Google Search")` - Specific feature being tested
   - `@Story("Verify Google Page Title")` - User story/scenario
   - `@Description(...)` - Detailed description shown in report
   - `@Severity(SeverityLevel.CRITICAL)` - Test importance

2. **Allure.step():**
   - `Allure.step("Navigate to Google homepage", () -> { ... })`
   - Creates a step entry in the report
   - Lambda executes the action
   - Step appears in chronological order

3. **Return values:**
   - `String actualTitle = Allure.step("Get page title", () -> { return ... })`
   - Can return values from steps
   - Useful for capturing test data

---

### **2. Allure.step() Variations**

**Simple step (no return value):**
```java
Allure.step("Navigate to Google homepage", () -> {
    googlePage.navigate();
});
```

**Step with return value:**
```java
String title = Allure.step("Get page title", () -> {
    return googlePage.getPageTitle();
});
```

**Step with parameters:**
```java
Allure.step("Search for product: " + searchQuery, () -> {
    amazonHomePage.search(searchQuery);
});
```

**Nested steps:**
```java
Allure.step("Complete checkout process", () -> {
    Allure.step("Add product to cart", () -> {
        productPage.addToCart();
    });
    
    Allure.step("Proceed to checkout", () -> {
        cartPage.checkout();
    });
});
```

---

### **3. Allure Listener Configuration**

**In testng.xml:**
```xml
<suite name="Playwright Automation Suite">
    <listeners>
        <listener class-name="io.qameta.allure.testng.AllureTestNg"/>
    </listeners>
    ...
</suite>
```

**In pom.xml (Surefire plugin):**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <properties>
            <property>
                <name>listener</name>
                <value>io.qameta.allure.testng.AllureTestNg</value>
            </property>
        </properties>
    </configuration>
</plugin>
```

**What this does:**
- Registers Allure listener with TestNG
- Automatically captures test execution
- No manual code needed

---

## 🎯 Real Example: Your Test Execution

### **Test: testGoogleTitle()**

**Step-by-step what happens:**

```
1. Maven runs: mvn test
   │
   ▼
2. TestNG loads testng.xml
   │
   ├─> Registers AllureTestNg listener
   └─> Allure ready to capture data
   │
   ▼
3. TestNG starts: testGoogleTitle()
   │
   ├─> Allure captures:
   │   ├─> Test name: "testGoogleTitle"
   │   ├─> Epic: "Web Application Testing"
   │   ├─> Feature: "Google Search"
   │   ├─> Story: "Verify Google Page Title"
   │   ├─> Description: "This test verifies..."
   │   ├─> Severity: CRITICAL
   │   └─> Start time
   │
   ▼
4. Step 1 executes:
   │
   ├─> Allure.step("Navigate to Google homepage")
   │   ├─> Captures step name
   │   ├─> Executes lambda
   │   └─> Records step duration
   │
   ▼
5. Step 2 executes:
   │
   ├─> Allure.step("Get page title")
   │   ├─> Captures step name
   │   ├─> Executes lambda
   │   ├─> Captures return value
   │   └─> Records step duration
   │
   ▼
6. Step 3 executes:
   │
   ├─> Allure.step("Verify page title...")
   │   ├─> Captures step name
   │   ├─> Executes assertion
   │   └─> Records step duration
   │
   ▼
7. Test passes
   │
   ├─> Allure captures:
   │   ├─> Status: PASSED
   │   ├─> End time
   │   └─> Total duration
   │
   ▼
8. Data saved
   │
   └─> JSON files created in target/allure-results/
   │
   ▼
9. Generate report: mvn allure:report
   │
   ├─> Reads JSON files
   ├─> Processes data
   └─> Generates HTML report
   │
   ▼
10. View report
   │
   └─> Open target/site/allure-maven-plugin/index.html
```

---

## 📊 Report Structure

### **Generated HTML Report Contains:**

```
Allure Report (index.html)
├── Overview
│   ├── Test Summary
│   │   ├── Total: 2
│   │   ├── Passed: 2
│   │   ├── Failed: 0
│   │   └── Skipped: 0
│   ├── Statistics
│   │   ├── Duration: 15.3s
│   │   └── Average: 7.65s
│   └── Trends (if multiple runs)
│
├── Behaviors
│   ├── Epic: Web Application Testing
│   │   └── Feature: Google Search
│   │       └── Story: Verify Google Page Title
│   │           └── testGoogleTitle() ✓
│   └── Epic: E-commerce Testing
│       └── Feature: Shopping Cart
│           └── Story: Add Product to Cart
│               └── testAddMobileToCart() ✓
│
├── Suites
│   └── Playwright Automation Suite
│       └── Playwright Tests
│           ├── testGoogleTitle() ✓
│           └── testAddMobileToCart() ✓
│
├── Graphs
│   ├── Test Results (Pie chart)
│   ├── Duration (Bar chart)
│   └── Retry Trends
│
└── Timeline
    └── Test execution order
        ├── testGoogleTitle (2.5s)
        └── testAddMobileToCart (12.8s)
```

---

## 🔧 Advanced Features

### **1. Attach Screenshots**

```java
@Test
public void testGoogleTitle() {
    Allure.step("Navigate to Google", () -> {
        googlePage.navigate();
        
        // Attach screenshot
        byte[] screenshot = page.screenshot();
        Allure.addAttachment("Google Homepage", "image/png", 
            new ByteArrayInputStream(screenshot), ".png");
    });
}
```

### **2. Attach Text/Logs**

```java
Allure.step("Verify page title", () -> {
    String title = page.title();
    Allure.addAttachment("Page Title", "text/plain", title);
});
```

### **3. Link to Issues**

```java
@Test
@Issue("JIRA-123")
@TmsLink("TC-456")
public void testGoogleTitle() {
    // Test code
}
```

### **4. Owner Information**

```java
@Test
@Owner("John Doe")
public void testGoogleTitle() {
    // Test code
}
```

### **5. Flaky Tests**

```java
@Test
@Flaky
public void testGoogleTitle() {
    // Test code
}
```

---

## 🎓 Key Concepts

### **1. AllureTestNg Listener**
- **Purpose:** Captures test execution data
- **Lifecycle:** Active during entire test execution
- **Responsibility:** Converts TestNG events to Allure data

### **2. Allure.step()**
- **Purpose:** Creates detailed step-by-step log
- **Lifecycle:** Executes during test run
- **Responsibility:** Captures individual test actions

### **3. Annotations**
- **Purpose:** Categorize and describe tests
- **Types:** @Epic, @Feature, @Story, @Description, @Severity
- **Responsibility:** Organize tests in report

### **4. Allure Results (JSON)**
- **Purpose:** Store test execution data
- **Location:** `target/allure-results/`
- **Format:** JSON files
- **Responsibility:** Raw test data

### **5. Allure Report (HTML)**
- **Purpose:** Visual representation of test results
- **Location:** `target/site/allure-maven-plugin/`
- **Format:** Interactive HTML
- **Responsibility:** User-friendly test report

---

## 📋 Complete Setup Checklist

### **1. Add Dependencies (pom.xml)**
```xml
<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-testng</artifactId>
    <version>2.24.0</version>
</dependency>
```

### **2. Add Maven Plugin (pom.xml)**
```xml
<plugin>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-maven</artifactId>
    <version>2.12.0</version>
</plugin>
```

### **3. Configure Surefire Plugin**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <properties>
            <property>
                <name>listener</name>
                <value>io.qameta.allure.testng.AllureTestNg</value>
            </property>
        </properties>
    </configuration>
</plugin>
```

### **4. Register Listener (testng.xml)**
```xml
<listeners>
    <listener class-name="io.qameta.allure.testng.AllureTestNg"/>
</listeners>
```

### **5. Add Annotations to Tests**
```java
@Test
@Epic("Web Application Testing")
@Feature("Google Search")
@Story("Verify Google Page Title")
@Description("Detailed description")
@Severity(SeverityLevel.CRITICAL)
public void testGoogleTitle() {
    // Test code
}
```

### **6. Add Allure.step() to Test Code**
```java
Allure.step("Step description", () -> {
    // Step code
});
```

### **7. Run Tests**
```bash
mvn test
```

### **8. Generate Report**
```bash
mvn allure:report
```

### **9. View Report**
```bash
open target/site/allure-maven-plugin/index.html
```

---

## 🚀 Quick Reference

### **Common Annotations:**

```java
@Epic("Epic Name")
@Feature("Feature Name")
@Story("Story Name")
@Description("Test description")
@Severity(SeverityLevel.CRITICAL)
@Owner("John Doe")
@Issue("JIRA-123")
@TmsLink("TC-456")
@Flaky
```

### **Allure.step() Usage:**

```java
// Simple step
Allure.step("Step name", () -> {
    // Code
});

// Step with return value
String result = Allure.step("Get value", () -> {
    return someValue;
});

// Step with parameters
Allure.step("Search for: " + query, () -> {
    search(query);
});
```

### **Attachments:**

```java
// Screenshot
Allure.addAttachment("Screenshot", "image/png", 
    new ByteArrayInputStream(screenshot), ".png");

// Text/Log
Allure.addAttachment("Log", "text/plain", logContent);
```

---

## 📊 Your Project's Allure Flow

```
1. Maven runs: mvn test
   │
   ▼
2. TestNG reads: testng.xml
   │
   ├─> Loads AllureTestNg listener
   └─> Allure ready to capture
   │
   ▼
3. TestNG executes: PlayWrightTest
   │
   ├─> testGoogleTitle()
   │   ├─> Allure captures annotations
   │   ├─> Allure.step() captures steps
   │   └─> Data saved to target/allure-results/
   │
   └─> testAddMobileToCart()
       ├─> Allure captures annotations
       ├─> Allure.step() captures steps
       └─> Data saved to target/allure-results/
   │
   ▼
4. Generate report: mvn allure:report
   │
   ├─> Reads target/allure-results/*.json
   ├─> Processes test data
   └─> Generates HTML report
   │
   ▼
5. Report available at:
   └─> target/site/allure-maven-plugin/index.html
```

---

## 💡 Allure vs ExtentReports

| Feature | Allure Reports | ExtentReports |
|---------|---------------|---------------|
| **Setup** | Annotations + Listener | Manual listener code |
| **Steps** | `Allure.step()` | Manual logging |
| **Categorization** | @Epic/@Feature/@Story | Categories/Tags |
| **History** | Built-in trends | Limited |
| **Integration** | Multiple frameworks | TestNG focused |
| **Report Style** | Modern, interactive | Traditional |
| **Your Project** | ✅ Primary | ✅ Secondary |

**Both in your project:**
- **Allure** - Primary reporting (automatic, annotation-based)
- **ExtentReports** - Secondary reporting (manual, step-based)

---

## 🎓 Summary

**Allure Reports Process:**
1. ✅ **Setup** - Add dependencies and configure listener
2. ✅ **Annotate** - Add @Epic, @Feature, @Story, @Description, @Severity
3. ✅ **Step** - Wrap actions with Allure.step()
4. ✅ **Execute** - Run tests (data captured automatically)
5. ✅ **Generate** - Run `mvn allure:report`
6. ✅ **View** - Open HTML report in browser

**Key Components:**
- `allure-testng` - TestNG integration
- `AllureTestNg` - Listener that captures data
- `Allure.step()` - Creates detailed steps
- Annotations - Categorize and describe tests
- `allure-maven` - Generates HTML reports

**Your Setup:**
- ✅ Dependencies configured in pom.xml
- ✅ Listener registered in testng.xml
- ✅ Tests annotated with Allure annotations
- ✅ Steps wrapped with Allure.step()
- ✅ Automatic report generation

**Allure Reports = Beautiful, automatic, annotation-based test reporting!** 🎉

---

**Last Updated:** Based on your current Allure Reports implementation

