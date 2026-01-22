# PlayWrightAutomation Project - Architecture & Flow Explanation

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Project Structure](#project-structure)
3. [Dependencies & Their Roles](#dependencies--their-roles)
4. [Architecture Patterns](#architecture-patterns)
5. [Test Execution Flow](#test-execution-flow)
6. [Component Interactions](#component-interactions)
7. [Data Flow](#data-flow)
8. [Build & Execution Process](#build--execution-process)

---

## 🎯 Project Overview

This is a **Java-based test automation framework** using **Playwright** for browser automation, implementing the **Page Object Model (POM)** design pattern. The project uses **TestNG** as the test framework and **Allure** for reporting.

---

## 📁 Project Structure

```
PlayWrigthAutomation/
├── pom.xml                          # Maven configuration & dependencies
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/
│   │   │       ├── pages/           # Page Object Model classes
│   │   │       │   ├── BasePage.java
│   │   │       │   ├── GooglePage.java
│   │   │       │   ├── AmazonHomePage.java
│   │   │       │   ├── AmazonSearchResultsPage.java
│   │   │       │   └── AmazonProductPage.java
│   │   │       └── utils/           # Utility classes
│   │   │           ├── ConfigReaderUtils.java
│   │   │           └── ExcelReaderUtils.java
│   │   └── resources/
│   │       └── config.properties   # Configuration data
│   └── test/
│       ├── java/
│       │   └── PlayWrightTest.java # Test class
│       └── resources/
│           └── allure.properties   # Allure configuration
└── target/                          # Build output & reports
    ├── allure-results/              # Allure JSON results
    └── site/allure-maven-plugin/    # Allure HTML report
```

---

## 🔗 Dependencies & Their Roles

### 1. **TestNG (v7.11.0)**
- **Purpose**: Test framework for Java
- **Role**: 
  - Manages test execution lifecycle (`@BeforeMethod`, `@Test`, `@AfterMethod`)
  - Provides assertions (`Assert.assertEquals`, `Assert.assertTrue`)
  - Handles test organization and execution

### 2. **Playwright (v1.57.0)**
- **Purpose**: Browser automation library
- **Role**:
  - Controls browser instances (Chrome, Firefox, Safari)
  - Interacts with web elements (click, fill, navigate)
  - Handles page navigation and waiting mechanisms
  - Provides `Page`, `Browser`, `Playwright` classes

### 3. **Apache POI (v5.2.5)**
- **Purpose**: Excel file reading/writing
- **Components**:
  - `poi`: Core library for Excel operations
  - `poi-ooxml`: Support for .xlsx files
- **Role**: Enables data-driven testing by reading test data from Excel files

### 4. **Allure TestNG (v2.24.0)**
- **Purpose**: Test reporting framework
- **Role**:
  - Captures test execution details
  - Generates beautiful HTML reports
  - Integrates with TestNG via listener (`AllureTestNg`)
  - Provides annotations for test metadata (`@Epic`, `@Feature`, `@Story`, `@Severity`)

### 5. **Maven Plugins**
- **maven-compiler-plugin**: Compiles Java source code
- **maven-surefire-plugin**: Executes TestNG tests
- **allure-maven-plugin**: Generates Allure reports

---

## 🏗️ Architecture Patterns

### 1. **Page Object Model (POM)**
- **Purpose**: Separates page logic from test logic
- **Structure**:
  ```
  BasePage (Abstract)
    ├── GooglePage
    ├── AmazonHomePage
    ├── AmazonSearchResultsPage
    └── AmazonProductPage
  ```

### 2. **Singleton Pattern**
- **Used in**: `ConfigReaderUtils`
- **Purpose**: Ensures single instance of configuration reader
- **Benefit**: Efficient memory usage, consistent configuration access

### 3. **Inheritance**
- **BasePage**: Contains common methods (navigate, wait, getTitle)
- **Page Objects**: Extend BasePage and add page-specific methods

### 4. **Fluent Interface**
- **Example**: `googlePage.navigate().enterSearchQuery("test").clickSearchButton()`
- **Benefit**: Readable, chainable method calls

---

## 🔄 Test Execution Flow

### **Complete Test Lifecycle:**

```
1. Maven Build Phase
   └──> Compiles Java code
   └──> Copies resources (config.properties)

2. Test Execution Phase
   │
   ├──> @BeforeMethod (setUp)
   │    ├──> Create Playwright instance
   │    ├──> Launch Chrome browser
   │    └──> Create new page
   │
   ├──> @Test (testGoogleTitle / testAddMobileToCart)
   │    ├──> Initialize Page Objects
   │    ├──> Execute test steps
   │    ├──> Perform assertions
   │    └──> Allure captures steps
   │
   └──> @AfterMethod (tearDown)
        ├──> Close browser (optional)
        └──> Close Playwright instance

3. Report Generation Phase
   └──> Allure generates JSON results
   └──> mvn allure:report generates HTML
```

### **Detailed Test Flow Example: testAddMobileToCart**

```
Step 1: Setup (BeforeMethod)
   Playwright.create()
   └──> browser = playwright.chromium().launch()
   └──> page = browser.newPage()

Step 2: Test Execution
   ConfigReaderUtils.getInstance()
   └──> Loads config.properties
   
   AmazonHomePage(page)
   └──> Extends BasePage
   └──> Gets URL from ConfigReaderUtils
   └──> navigate() → Opens Amazon
   
   search("motorola")
   └──> enterSearchQuery() → Types in search box
   └──> clickSearchButton() → Clicks search
   └──> Returns AmazonSearchResultsPage
   
   clickFirstSearchResult()
   └──> Waits for results
   └──> Clicks first product
   └──> Returns AmazonProductPage
   
   addProductToCart()
   └──> handlePopups()
   └──> handleProductOptions()
   └──> addToCart()
   
   verifyItemAddedToCart()
   └──> Checks for success message
   └──> Assert.assertTrue()

Step 3: Cleanup (AfterMethod)
   playwright.close()
```

---

## 🔀 Component Interactions

### **Interaction Diagram:**

```
┌─────────────────┐
│  PlayWrightTest │ (Test Class)
└────────┬────────┘
         │
         ├─────────────────────────────────────┐
         │                                     │
         ▼                                     ▼
┌─────────────────┐                  ┌──────────────────┐
│   Page Objects  │                  │ ConfigReaderUtils │
│  (GooglePage,   │                  │   (Singleton)     │
│  AmazonHomePage)│                  └─────────┬──────────┘
└────────┬────────┘                            │
         │                                     │
         │ extends                            │ reads
         │                                     │
         ▼                                     ▼
┌─────────────────┐                  ┌──────────────────┐
│   BasePage      │                  │ config.properties│
│  (Common Logic) │                  │  (Static Data)   │
└────────┬────────┘                  └──────────────────┘
         │
         │ uses
         │
         ▼
┌─────────────────┐
│  Playwright API │
│  (Page, Browser)│
└─────────────────┘
```

### **Key Interactions:**

1. **Test Class → Page Objects**
   - Test creates page object instances
   - Calls page object methods
   - Receives page objects as return values (fluent interface)

2. **Page Objects → ConfigReaderUtils**
   - Page objects get URLs, timeouts, test data
   - Singleton pattern ensures single config instance

3. **Page Objects → BasePage**
   - Inheritance provides common functionality
   - All pages share navigation, waiting methods

4. **BasePage → Playwright API**
   - Direct interaction with browser
   - Uses Playwright's Page object for all operations

5. **TestNG → Allure**
   - Allure listener captures test execution
   - Annotations provide metadata
   - Steps are logged automatically

---

## 📊 Data Flow

### **Configuration Data Flow:**

```
config.properties
    │
    │ (loaded once)
    │
    ▼
ConfigReaderUtils (Singleton)
    │
    │ (accessed by)
    │
    ├──> GooglePage
    │    └──> getGoogleUrl()
    │    └──> getGoogleExpectedTitle()
    │
    ├──> AmazonHomePage
    │    └──> getAmazonUrl()
    │
    └──> PlayWrightTest
         └──> getMotorolaSearchQuery()
```

### **Test Data Flow:**

```
Test Method
    │
    │ (creates)
    │
    ▼
Page Object (e.g., AmazonHomePage)
    │
    │ (uses)
    │
    ├──> ConfigReaderUtils → Test Data
    ├──> BasePage → Common Methods
    └──> Playwright Page → Browser Actions
    │
    │ (returns)
    │
    ▼
Next Page Object (e.g., AmazonSearchResultsPage)
    │
    │ (continues flow)
    │
    ▼
Final Assertion → Test Result
```

---

## 🛠️ Build & Execution Process

### **Maven Build Lifecycle:**

```
1. Validate
   └──> Checks project structure

2. Compile
   └──> Compiles src/main/java → target/classes
   └──> Compiles src/test/java → target/test-classes

3. Test
   └──> Runs TestNG tests
   └──> Allure captures results → target/allure-results/
   └──> Surefire generates reports → target/surefire-reports/

4. Package
   └──> Creates JAR file

5. Report (optional)
   └──> mvn allure:report
   └──> Generates HTML → target/site/allure-maven-plugin/
```

### **Command Flow:**

```bash
# Run tests and generate reports
mvn clean test allure:report

# View report
mvn allure:serve
```

---

## 🎨 Design Principles Applied

1. **Separation of Concerns**
   - Tests: Business logic validation
   - Page Objects: UI interaction logic
   - Utils: Configuration and data management

2. **DRY (Don't Repeat Yourself)**
   - BasePage eliminates code duplication
   - ConfigReaderUtils centralizes configuration

3. **Single Responsibility**
   - Each page object handles one page
   - ConfigReaderUtils only manages configuration
   - ExcelReaderUtils only handles Excel operations

4. **Maintainability**
   - Locators in page objects (easy to update)
   - Configuration in properties file
   - Clear structure and naming

5. **Reusability**
   - Page objects can be used across multiple tests
   - BasePage methods available to all pages
   - Utils classes are independent

---

## 🔍 Key Features

1. **Page Object Model**: Clean separation of test and page logic
2. **Configuration Management**: Centralized config via properties file
3. **Data-Driven Testing**: Excel support for test data
4. **Allure Reporting**: Rich HTML reports with step-by-step execution
5. **Fluent Interface**: Readable, chainable method calls
6. **Singleton Pattern**: Efficient configuration access
7. **Inheritance**: Code reuse through BasePage

---

## 📝 Summary

This framework follows **industry best practices** for test automation:

- ✅ **Modular**: Clear separation of concerns
- ✅ **Maintainable**: Easy to update and extend
- ✅ **Scalable**: Can add new pages/tests easily
- ✅ **Reportable**: Rich Allure reports
- ✅ **Data-Driven**: Support for external test data
- ✅ **Well-Structured**: Follows design patterns

The flow is: **Test → Page Objects → BasePage → Playwright → Browser**, with **ConfigReaderUtils** providing configuration data throughout the execution.

