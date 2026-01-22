# Maven - Complete Guide

## 🎯 What is Maven?

What is Maven?
- Maven is a build automation and project management tool for Java projects. 
It handles:
. Building your project (compile, test, package)
. Managing dependencies (downloads libraries automatically)
. Running tests
. Generating reports
Standardizing project structure-
Think of it as a build system that automates common tasks.

Key concepts
1. POM (Project Object Model)
The pom.xml file that describes your project
Contains dependencies, build config, project info
2. Dependencies
External libraries your project needs
Declare in pom.xml, Maven downloads them automatically
3. Build Lifecycle
validate → compile → test → package → install → deploy
4. Plugins
Extensions that add functionality (compiler, test runner, reports)


**Apache Maven** is a **build automation and project management tool** primarily used for Java projects. It helps you:

- ✅ **Build** your project (compile, test, package)
- ✅ **Manage dependencies** (download and include libraries)
- ✅ **Run tests** automatically
- ✅ **Package** your application (JAR, WAR files)
- ✅ **Generate reports** (test reports, code coverage)
- ✅ **Deploy** to repositories

Think of Maven as a **"smart build system"** that knows how to:
- Download libraries you need
- Compile your code
- Run tests
- Package everything together
- Generate documentation

---

## 📦 Key Concepts

### **1. POM (Project Object Model)**

**POM** = `pom.xml` file that describes your project

**What it contains:**
- Project information (name, version, group)
- Dependencies (libraries your project needs)
- Build configuration (plugins, goals)
- Project structure

**Example from your project:**
```xml
<project>
    <groupId>org.example</groupId>
    <artifactId>PlaywrightAutomation</artifactId>
    <version>1.0-SNAPSHOT</version>
    
    <dependencies>
        <dependency>
            <groupId>com.microsoft.playwright</groupId>
            <artifactId>playwright</artifactId>
            <version>1.57.0</version>
        </dependency>
    </dependencies>
</project>
```

### **2. Dependencies**

**Dependencies** = External libraries your project needs

**How Maven handles them:**
1. You declare what you need in `pom.xml`
2. Maven downloads from Maven Central Repository
3. Stores in local repository (`~/.m2/repository`)
4. Includes in your project automatically

**Example:**
```xml
<!-- You need Playwright for browser automation -->
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>1.57.0</version>
</dependency>
```

Maven automatically:
- Downloads Playwright library
- Downloads its dependencies (transitive dependencies)
- Makes it available in your project

### **3. Build Lifecycle**

Maven has **predefined phases** that run in order:

```
validate → compile → test → package → install → deploy
```

**What each phase does:**

| Phase | Description | What Happens |
|-------|-------------|--------------|
| **validate** | Validate project | Checks if project is correct |
| **compile** | Compile source code | Converts `.java` → `.class` files |
| **test** | Run unit tests | Executes test classes |
| **package** | Package compiled code | Creates JAR/WAR file |
| **install** | Install to local repo | Copies to `~/.m2/repository` |
| **deploy** | Deploy to remote repo | Uploads to remote repository |

**Example:**
```bash
mvn compile    # Runs: validate → compile
mvn test       # Runs: validate → compile → test-compile → test
mvn package    # Runs: validate → compile → test → package
```

### **4. Plugins**

**Plugins** = Extensions that add functionality to Maven

**Common plugins:**
- `maven-compiler-plugin` - Compiles Java code
- `maven-surefire-plugin` - Runs tests
- `allure-maven-plugin` - Generates Allure reports

**Example from your project:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
</plugin>
```

---

## 🎯 Use Cases

### **1. Dependency Management**

**Problem:** Manually downloading and managing JAR files is tedious

**Maven Solution:**
```xml
<!-- Just declare what you need -->
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.11.0</version>
</dependency>
```

Maven automatically:
- Downloads TestNG
- Downloads all its dependencies
- Manages versions
- Resolves conflicts

**Without Maven:**
```
❌ Download testng.jar
❌ Download all its dependencies manually
❌ Add to classpath
❌ Update versions manually
❌ Handle conflicts manually
```

**With Maven:**
```
✅ Declare in pom.xml
✅ Maven handles everything automatically
```

### **2. Standardized Build Process**

**Problem:** Different developers use different build commands

**Maven Solution:**
```bash
# Everyone uses the same commands
mvn clean compile    # Clean and compile
mvn test             # Run tests
mvn package          # Create JAR file
```

**Benefits:**
- ✅ Consistent builds across team
- ✅ Same commands work everywhere
- ✅ No need to remember complex build scripts

### **3. Automated Testing**

**Problem:** Running tests manually is error-prone

**Maven Solution:**
```bash
mvn test  # Automatically:
          # 1. Compiles source code
          # 2. Compiles test code
          # 3. Runs all tests
          # 4. Generates reports
```

**In your project:**
```bash
mvn test
# Runs:
# - PlayWrightTest.java
# - Generates test reports
# - Creates JUnit XML files
```

### **4. Project Structure Standardization**

**Maven enforces standard directory structure:**

```
project/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/          # Source code
│   │   └── resources/     # Config files
│   └── test/
│       ├── java/          # Test code
│       └── resources/     # Test config
└── target/                 # Build output
    ├── classes/           # Compiled classes
    └── test-classes/      # Compiled tests
```

**Benefits:**
- ✅ Everyone knows where files go
- ✅ Tools work automatically
- ✅ Easy to navigate projects

### **5. Report Generation**

**Maven can generate various reports:**

```bash
mvn test                    # Test reports
mvn site                    # Project site
mvn allure:report           # Allure test reports
mvn jacoco:report           # Code coverage
```

**In your project:**
```bash
mvn allure:report
# Generates beautiful HTML test reports
```

### **6. Multi-Module Projects**

**Maven supports large projects with multiple modules:**

```
parent-project/
├── pom.xml (parent)
├── module1/
│   └── pom.xml
├── module2/
│   └── pom.xml
└── module3/
    └── pom.xml
```

**Benefits:**
- ✅ Build all modules together
- ✅ Share dependencies
- ✅ Manage versions centrally

---

## 🔧 How Maven is Used in Your Project

### **Your `pom.xml` Structure:**

```xml
<project>
    <!-- 1. Project Information -->
    <groupId>org.example</groupId>
    <artifactId>PlaywrightAutomation</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!-- 2. Dependencies -->
    <dependencies>
        <!-- TestNG for testing -->
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>7.11.0</version>
        </dependency>
        
        <!-- Playwright for browser automation -->
        <dependency>
            <groupId>com.microsoft.playwright</groupId>
            <artifactId>playwright</artifactId>
            <version>1.57.0</version>
        </dependency>
        
        <!-- Apache POI for Excel reading -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>5.2.5</version>
        </dependency>
        
        <!-- Allure for reporting -->
        <dependency>
            <groupId>io.qameta.allure</groupId>
            <artifactId>allure-testng</artifactId>
            <version>2.24.0</version>
        </dependency>
    </dependencies>

    <!-- 3. Build Plugins -->
    <build>
        <plugins>
            <!-- Compiler plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
            </plugin>
            
            <!-- Test runner plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.5.0</version>
            </plugin>
            
            <!-- Allure report plugin -->
            <plugin>
                <groupId>io.qameta.allure</groupId>
                <artifactId>allure-maven</artifactId>
                <version>2.12.0</version>
            </plugin>
        </plugins>
    </build>
</project>
```

### **Common Maven Commands in Your Project:**

```bash
# Clean previous builds
mvn clean

# Compile source code
mvn compile

# Compile tests
mvn test-compile

# Run tests
mvn test

# Generate Allure report
mvn allure:report

# Clean, compile, test, and generate report
mvn clean test allure:report

# Package into JAR
mvn package
```

---

## 📊 Maven vs Alternatives

### **Maven vs Ant**

| Feature | Maven | Ant |
|---------|-------|-----|
| **Configuration** | Declarative (XML) | Imperative (XML) |
| **Dependencies** | Automatic | Manual |
| **Convention** | Standard structure | Custom structure |
| **Learning Curve** | Medium | Low |

**Maven:** "Tell me what you want" (declarative)  
**Ant:** "Tell me how to do it" (imperative)

### **Maven vs Gradle**

| Feature | Maven | Gradle |
|---------|-------|--------|
| **Language** | XML | Groovy/Kotlin DSL |
| **Speed** | Slower | Faster (incremental builds) |
| **Flexibility** | Less flexible | More flexible |
| **Learning Curve** | Medium | Medium-High |

**Maven:** XML-based, more established  
**Gradle:** DSL-based, more modern

---

## 🎯 Real-World Use Cases

### **1. Java Web Application**

```bash
# Build WAR file for deployment
mvn clean package
# Creates: target/myapp.war
```

### **2. Library Development**

```bash
# Build and install to local repository
mvn clean install
# Other projects can now use your library
```

### **3. Continuous Integration**

```bash
# In CI/CD pipeline (Jenkins/GitHub Actions)
mvn clean test
# Automatically runs all tests
```

**In your Jenkinsfile:**
```groovy
stage('Run Tests') {
    steps {
        sh 'mvn test'  // Maven runs all tests
    }
}
```

**In your GitHub Actions:**
```yaml
- name: Run tests
  run: mvn clean test
```

### **4. Multi-Environment Builds**

```bash
# Build for different environments
mvn clean package -Pproduction
mvn clean package -Pdevelopment
```

### **5. Dependency Updates**

```bash
# Check for outdated dependencies
mvn versions:display-dependency-updates
```

---

## 🔍 Maven Repository

### **Local Repository**

**Location:** `~/.m2/repository/`

**What it is:**
- Cache of downloaded dependencies
- Stores JAR files locally
- Speeds up builds (no re-download)

**Structure:**
```
~/.m2/repository/
└── com/
    └── microsoft/
        └── playwright/
            └── playwright/
                └── 1.57.0/
                    └── playwright-1.57.0.jar
```

### **Central Repository**

**Location:** https://repo.maven.apache.org/maven2/

**What it is:**
- Public repository
- Contains millions of libraries
- Maven downloads from here automatically

### **Private Repositories**

**Use cases:**
- Company internal libraries
- Proprietary code
- Custom builds

---

## 📝 Common Maven Commands

### **Basic Commands:**

```bash
# Clean build directory
mvn clean

# Compile source code
mvn compile

# Run tests
mvn test

# Package project
mvn package

# Install to local repository
mvn install

# Deploy to remote repository
mvn deploy
```

### **Combined Commands:**

```bash
# Clean and compile
mvn clean compile

# Clean, compile, and test
mvn clean test

# Clean, test, and package
mvn clean package

# Full build cycle
mvn clean install
```

### **Plugin Commands:**

```bash
# Generate Allure report
mvn allure:report

# Show dependency tree
mvn dependency:tree

# Show effective POM
mvn help:effective-pom

# Check for updates
mvn versions:display-dependency-updates
```

### **Debug Commands:**

```bash
# Run with debug output
mvn -X test

# Skip tests
mvn package -DskipTests

# Run specific test
mvn test -Dtest=PlayWrightTest#testGoogleTitle
```

---

## 🎓 Maven in Your Project Workflow

### **Development Workflow:**

```
1. Write code
   └─> src/main/java/PlayWrightTest.java

2. Write tests
   └─> src/test/java/PlayWrightTest.java

3. Run Maven
   └─> mvn test
       ├─> Compiles code
       ├─> Runs tests
       └─> Generates reports

4. View results
   └─> target/surefire-reports/
       └─> TEST-*.xml
```

### **CI/CD Workflow:**

```
Jenkins/GitHub Actions
    │
    ├─> mvn clean compile
    │   └─> Validates and compiles
    │
    ├─> mvn test
    │   └─> Runs all tests
    │
    └─> mvn allure:report
        └─> Generates test reports
```

---

## 💡 Key Benefits

### **1. Dependency Management**
- ✅ No manual JAR file management
- ✅ Automatic version resolution
- ✅ Transitive dependency handling

### **2. Standardization**
- ✅ Consistent project structure
- ✅ Standard build commands
- ✅ Works across different machines

### **3. Automation**
- ✅ Automatic test execution
- ✅ Automatic report generation
- ✅ Automatic packaging

### **4. Integration**
- ✅ Works with IDEs (IntelliJ, Eclipse)
- ✅ Works with CI/CD (Jenkins, GitHub Actions)
- ✅ Works with other tools

### **5. Extensibility**
- ✅ Plugin ecosystem
- ✅ Custom plugins
- ✅ Flexible configuration

---

## 🚀 Quick Start

### **1. Install Maven:**

**macOS:**
```bash
brew install maven
```

**Linux:**
```bash
sudo apt-get install maven
```

**Windows:**
Download from https://maven.apache.org/

### **2. Verify Installation:**
```bash
mvn -version
```

### **3. Create Project:**
```bash
mvn archetype:generate
```

### **4. Build Project:**
```bash
mvn clean package
```

---

## 📚 Summary

**Maven is:**
- 🎯 **Build tool** - Compiles, tests, packages
- 📦 **Dependency manager** - Downloads and manages libraries
- 📋 **Project manager** - Standardizes project structure
- 🔧 **Automation tool** - Automates repetitive tasks

**Key Use Cases:**
1. ✅ Managing dependencies automatically
2. ✅ Standardizing build process
3. ✅ Running tests automatically
4. ✅ Generating reports
5. ✅ Packaging applications
6. ✅ CI/CD integration

**In Your Project:**
- ✅ Manages Playwright, TestNG, Allure dependencies
- ✅ Compiles Java code
- ✅ Runs Playwright tests
- ✅ Generates Allure reports
- ✅ Integrates with Jenkins and GitHub Actions

**Maven = Your project's build automation engine!** 🚀

---

**Last Updated:** Based on your current `pom.xml` configuration

