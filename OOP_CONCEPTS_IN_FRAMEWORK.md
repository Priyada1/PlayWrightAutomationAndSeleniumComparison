# OOP Concepts in Automation Framework - Detailed Explanation with Examples

## 📚 Table of Contents
1. [Encapsulation](#1-encapsulation)
2. [Inheritance](#2-inheritance)
3. [Polymorphism](#3-polymorphism)
4. [Abstraction](#4-abstraction)
5. [Composition](#5-composition)
6. [Additional OOP Concepts](#6-additional-oop-concepts)

---

## 1. Encapsulation

**Definition**: Encapsulation is the bundling of data (fields) and methods that operate on that data within a single unit (class), and restricting direct access to some of the object's components.

### Example 1: Encapsulation in BasePage

```java
public class BasePage {
    protected Page page;  // Protected field - accessible to subclasses only
    
    public BasePage(Page page) {
        this.page = page;  // Data is set through constructor
    }
    
    // Public methods to interact with the page
    public void navigate(String url) {
        page.navigate(url);
        page.waitForLoadState();
    }
    
    public String getTitle() {
        return page.title();
    }
}
```

**Explanation**:
- The `page` field is `protected`, meaning it's not directly accessible from outside the class hierarchy
- External classes can only interact with the page through public methods like `navigate()` and `getTitle()`
- This protects the internal state and ensures controlled access

### Example 2: Encapsulation in GooglePage

```java
public class GooglePage extends BasePage {
    // Private constants - cannot be accessed from outside
    private static final String SEARCH_BOX = "textarea[name='q']";
    private static final String SEARCH_BUTTON = "input[name='btnK']";
    
    // Private field - encapsulated
    private final ConfigReaderUtils config;
    
    public GooglePage(Page page) {
        super(page);
        this.config = ConfigReaderUtils.getInstance();  // Internal initialization
    }
    
    // Public method - controlled access to search functionality
    public GooglePage enterSearchQuery(String query) {
        page.locator(SEARCH_BOX).fill(query);  // Internal implementation hidden
        return this;
    }
}
```

**Explanation**:
- **Locators are private**: The actual CSS selectors are hidden from external classes
- **Config is private**: The configuration object is encapsulated within the class
- **Public interface**: Only `enterSearchQuery()` is exposed, hiding the internal implementation
- **Data hiding**: External classes don't need to know about `SEARCH_BOX` or `SEARCH_BUTTON` locators

### Example 3: Encapsulation in ConfigReaderUtils (Singleton Pattern)

```java
public class ConfigReaderUtils {
    // Private static instance - cannot be accessed directly
    private static ConfigReaderUtils instance;
    
    // Private field - properties are encapsulated
    private Properties properties;
    
    // Private constructor - prevents direct instantiation
    private ConfigReaderUtils() {
        properties = new Properties();
        loadProperties();  // Internal initialization
    }
    
    // Public static method - controlled access to instance
    public static ConfigReaderUtils getInstance() {
        if (instance == null) {
            synchronized (ConfigReaderUtils.class) {
                if (instance == null) {
                    instance = new ConfigReaderUtils();
                }
            }
        }
        return instance;
    }
    
    // Private method - internal implementation hidden
    private void loadProperties() {
        // Implementation details hidden
    }
    
    // Public methods - controlled access to properties
    public String getGoogleUrl() {
        return getProperty("google.url");
    }
}
```

**Explanation**:
- **Private constructor**: Prevents creating multiple instances directly
- **Private fields**: `instance` and `properties` are encapsulated
- **Public getter methods**: Controlled access to configuration values
- **Internal logic hidden**: The `loadProperties()` method is private

### Benefits of Encapsulation in Framework:
1. **Security**: Prevents unauthorized access to internal data
2. **Maintainability**: Changes to internal implementation don't affect external code
3. **Flexibility**: Can modify internal logic without breaking tests
4. **Data Integrity**: Ensures data is accessed and modified correctly

---

## 2. Inheritance

**Definition**: Inheritance allows a class (child/subclass) to inherit properties and methods from another class (parent/superclass), promoting code reusability.

### Example 1: BasePage as Parent Class

```java
// Parent Class (BasePage)
public class BasePage {
    protected Page page;
    
    public BasePage(Page page) {
        this.page = page;
    }
    
    // Common methods available to all child classes
    public void navigate(String url) {
        page.navigate(url);
        page.waitForLoadState();
    }
    
    public String getTitle() {
        return page.title();
    }
    
    public String getCurrentUrl() {
        return page.url();
    }
    
    public void waitForPageLoad() {
        page.waitForLoadState();
    }
}
```

### Example 2: Child Classes Inheriting from BasePage

```java
// Child Class 1: GooglePage
public class GooglePage extends BasePage {
    public GooglePage(Page page) {
        super(page);  // Calls parent constructor
    }
    
    // Can use inherited methods
    public GooglePage navigate() {
        super.navigate(config.getGoogleUrl());  // Uses parent's navigate() method
        return this;
    }
    
    public String getPageTitle() {
        return getTitle();  // Uses inherited getTitle() method
    }
}

// Child Class 2: AmazonHomePage
public class AmazonHomePage extends BasePage {
    public AmazonHomePage(Page page) {
        super(page);  // Calls parent constructor
    }
    
    public AmazonHomePage navigate() {
        super.navigate(config.getAmazonUrl());  // Uses parent's navigate() method
        return this;
    }
    
    // Can also use inherited methods directly
    public void someMethod() {
        waitForPageLoad();  // Uses inherited waitForPageLoad() method
        String url = getCurrentUrl();  // Uses inherited getCurrentUrl() method
    }
}

// Child Class 3: AmazonSearchResultsPage
public class AmazonSearchResultsPage extends BasePage {
    public AmazonSearchResultsPage(Page page) {
        super(page);  // Calls parent constructor
    }
    
    public int getSearchResultsCount() {
        // Can access protected 'page' field from parent
        return page.locator(SEARCH_RESULT_ITEM).count();
    }
}
```

### Inheritance Hierarchy in Framework:

```
                    BasePage (Parent)
                         |
        ┌────────────────┼────────────────┐
        |                |                |
   GooglePage    AmazonHomePage    AmazonSearchResultsPage
                                           |
                                    AmazonProductPage
```

### Key Points:

1. **Code Reusability**: Common methods like `navigate()`, `getTitle()`, `waitForPageLoad()` are defined once in `BasePage` and reused by all child classes

2. **`super` Keyword**: Used to call parent class methods and constructor
   ```java
   super(page);  // Calls parent constructor
   super.navigate(url);  // Calls parent method
   ```

3. **`protected` Access Modifier**: The `page` field is `protected`, making it accessible to child classes but not to external classes

4. **Method Overriding**: Child classes can override parent methods (see Polymorphism section)

### Benefits of Inheritance in Framework:
1. **DRY Principle**: Don't Repeat Yourself - common code written once
2. **Consistency**: All page objects have the same base functionality
3. **Maintainability**: Changes to common methods only need to be made in BasePage
4. **Extensibility**: Easy to add new page objects by extending BasePage

---

## 3. Polymorphism

**Definition**: Polymorphism allows objects of different types to be accessed through the same interface. In Java, this is achieved through method overriding and method overloading.

### Example 1: Method Overriding (Runtime Polymorphism)

**Parent Class Method:**
```java
public class BasePage {
    public void navigate(String url) {
        page.navigate(url);
        page.waitForLoadState();
    }
}
```

**Child Class Overriding:**
```java
// GooglePage overrides navigate() with different behavior
public class GooglePage extends BasePage {
    public GooglePage navigate() {  // Different signature - method overloading
        super.navigate(config.getGoogleUrl());  // Calls parent method
        return this;  // Returns self for fluent interface
    }
}

// AmazonHomePage also overrides navigate()
public class AmazonHomePage extends BasePage {
    public AmazonHomePage navigate() {  // Same method name, different implementation
        super.navigate(config.getAmazonUrl());  // Different URL
        return this;
    }
}
```

**Usage in Test:**
```java
@Test
public void testGoogleTitle() {
    GooglePage googlePage = new GooglePage(page);
    googlePage.navigate();  // Calls GooglePage's navigate() method
    // Even though both GooglePage and AmazonHomePage have navigate(),
    // each executes its own implementation
}

@Test
public void testAddMobileToCart() {
    AmazonHomePage amazonHomePage = new AmazonHomePage(page);
    amazonHomePage.navigate();  // Calls AmazonHomePage's navigate() method
    // Same method name, different behavior
}
```

### Example 2: Method Overloading (Compile-time Polymorphism)

**In ConfigReaderUtils:**
```java
public class ConfigReaderUtils {
    // Method 1: Get property without default
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    // Method 2: Get property with default value (overloaded)
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    // Method 3: Get property as integer
    public int getIntProperty(String key) {
        String value = getProperty(key);
        return Integer.parseInt(value);
    }
    
    // Method 4: Get property as integer with default (overloaded)
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }
}
```

**Usage:**
```java
ConfigReaderUtils config = ConfigReaderUtils.getInstance();

// Calls getProperty(String key)
String url = config.getProperty("google.url");

// Calls getProperty(String key, String defaultValue) - different method
String url = config.getProperty("google.url", "https://www.google.com");

// Calls getIntProperty(String key)
int timeout = config.getIntProperty("timeout.short");

// Calls getIntProperty(String key, int defaultValue) - different method
int timeout = config.getIntProperty("timeout.short", 1000);
```

### Example 3: Polymorphism with Fluent Interface

**All page objects return themselves, allowing method chaining:**
```java
// GooglePage returns GooglePage
public GooglePage enterSearchQuery(String query) {
    page.locator(SEARCH_BOX).fill(query);
    return this;  // Returns GooglePage instance
}

// AmazonHomePage returns AmazonSearchResultsPage (different return type)
public AmazonSearchResultsPage search(String query) {
    enterSearchQuery(query);
    return clickSearchButton();  // Returns different type
}

// Usage - same pattern, different implementations
GooglePage googlePage = new GooglePage(page);
googlePage.navigate().enterSearchQuery("test").clickSearchButton();

AmazonHomePage amazonPage = new AmazonHomePage(page);
AmazonSearchResultsPage results = amazonPage.navigate().search("motorola");
```

### Benefits of Polymorphism in Framework:
1. **Flexibility**: Same interface, different implementations
2. **Code Reusability**: Write generic code that works with different page types
3. **Maintainability**: Easy to modify behavior in child classes without affecting parent
4. **Extensibility**: Can add new page types without changing existing code

---

## 4. Abstraction

**Definition**: Abstraction hides the complex implementation details and shows only the essential features to the user.

### Example 1: BasePage as Abstraction

```java
// BasePage provides abstract interface for all page operations
public class BasePage {
    protected Page page;
    
    // Abstract interface - hides Playwright implementation details
    public void navigate(String url) {
        page.navigate(url);  // Implementation hidden
        page.waitForLoadState();  // Internal details hidden
    }
    
    public String getTitle() {
        return page.title();  // How title is retrieved is abstracted
    }
    
    public void waitForPageLoad() {
        page.waitForLoadState();  // Waiting mechanism is abstracted
    }
}
```

**Usage - Test doesn't need to know Playwright details:**
```java
@Test
public void testGoogleTitle() {
    GooglePage googlePage = new GooglePage(page);
    googlePage.navigate();  // Test doesn't know about page.navigate() or waitForLoadState()
    String title = googlePage.getPageTitle();  // Simple interface, complex implementation hidden
}
```

### Example 2: Abstraction in ConfigReaderUtils

```java
public class ConfigReaderUtils {
    private Properties properties;  // Internal data structure hidden
    
    // Simple interface - hides file reading complexity
    public String getGoogleUrl() {
        return getProperty("google.url");  // How it's retrieved is abstracted
    }
    
    // Internal implementation hidden
    private void loadProperties() {
        // Complex file reading logic hidden from users
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config.properties file", e);
        }
    }
}
```

**Usage - Simple interface, complex implementation:**
```java
// Test code - simple and clean
ConfigReaderUtils config = ConfigReaderUtils.getInstance();
String url = config.getGoogleUrl();  // Don't need to know about Properties, InputStream, etc.

// Without abstraction, you would need:
Properties props = new Properties();
InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");
props.load(is);
String url = props.getProperty("google.url");
// Much more complex!
```

### Example 3: Abstraction in Page Objects

```java
public class AmazonProductPage extends BasePage {
    // Complex locator logic abstracted
    private static final String[] ADD_TO_CART_SELECTORS = {
        "#add-to-cart-button",
        "input#add-to-cart-button",
        // ... multiple selectors
    };
    
    // Simple interface hides complex implementation
    public AmazonProductPage addProductToCart() {
        handlePopups();  // Internal complexity hidden
        handleProductOptions();  // Internal complexity hidden
        addToCart();  // Complex selector logic hidden
        return this;
    }
    
    // Internal methods - implementation details hidden
    private AmazonProductPage handlePopups() {
        // Complex popup handling logic
        // Test doesn't need to know about this
    }
    
    private AmazonProductPage addToCart() {
        // Complex logic to try multiple selectors
        // All hidden from test code
    }
}
```

**Usage - Simple and clean:**
```java
@Test
public void testAddMobileToCart() {
    AmazonProductPage productPage = new AmazonProductPage(page);
    productPage.addProductToCart();  // Simple! Complex logic hidden
    // Test doesn't need to know about:
    // - Multiple selectors
    // - Popup handling
    // - Product options
    // - Retry logic
}
```

### Benefits of Abstraction in Framework:
1. **Simplicity**: Test code is clean and easy to read
2. **Maintainability**: Changes to implementation don't affect test code
3. **Focus**: Tests focus on business logic, not technical details
4. **Reusability**: Abstract methods can be reused across different scenarios

---

## 5. Composition

**Definition**: Composition is a "has-a" relationship where a class contains an instance of another class as a member variable.

### Example 1: Page Objects Composing ConfigReaderUtils

```java
public class GooglePage extends BasePage {
    // Composition: GooglePage "has-a" ConfigReaderUtils
    private final ConfigReaderUtils config;
    
    public GooglePage(Page page) {
        super(page);
        this.config = ConfigReaderUtils.getInstance();  // Composed object
    }
    
    public GooglePage navigate() {
        // Uses composed ConfigReaderUtils object
        super.navigate(config.getGoogleUrl());
        return this;
    }
    
    public String getExpectedTitle() {
        // Uses composed object
        return config.getGoogleExpectedTitle();
    }
}
```

**Explanation**:
- `GooglePage` **has-a** `ConfigReaderUtils` (composition)
- `GooglePage` **is-a** `BasePage` (inheritance)
- The `config` object is composed into `GooglePage` to provide configuration functionality

### Example 2: BasePage Composing Playwright Page

```java
public class BasePage {
    // Composition: BasePage "has-a" Playwright Page
    protected Page page;  // Composed object
    
    public BasePage(Page page) {
        this.page = page;  // Page object is composed
    }
    
    public void navigate(String url) {
        page.navigate(url);  // Uses composed Page object
        page.waitForLoadState();
    }
}
```

**Explanation**:
- `BasePage` **has-a** `Playwright Page` object
- All page objects inherit this composition through inheritance
- The `Page` object provides browser interaction capabilities

### Example 3: Test Class Composing Multiple Objects

```java
public class PlayWrightTest {
    // Composition: Test class "has-a" Playwright, Browser, and Page
    private Playwright playwright;  // Composed object
    private Browser browser;        // Composed object
    private Page page;             // Composed object
    
    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();  // Create composed objects
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }
    
    @Test
    public void testGoogleTitle() {
        // Creates page object which internally composes Page and ConfigReaderUtils
        GooglePage googlePage = new GooglePage(page);
        // googlePage internally has:
        // - Page object (from BasePage)
        // - ConfigReaderUtils object
    }
}
```

### Composition vs Inheritance:

| Relationship | Example | Type |
|-------------|---------|------|
| **Inheritance (is-a)** | `GooglePage extends BasePage` | GooglePage **is-a** BasePage |
| **Composition (has-a)** | `GooglePage has ConfigReaderUtils` | GooglePage **has-a** ConfigReaderUtils |
| **Composition (has-a)** | `BasePage has Page` | BasePage **has-a** Page |

### Benefits of Composition in Framework:
1. **Flexibility**: Can change composed objects without affecting the class
2. **Loose Coupling**: Classes depend on interfaces, not concrete implementations
3. **Reusability**: Composed objects can be reused across different classes
4. **Testability**: Can easily mock composed objects for testing

---

## 6. Additional OOP Concepts

### 6.1. Constructor Chaining

**Example:**
```java
public class GooglePage extends BasePage {
    private final ConfigReaderUtils config;
    
    public GooglePage(Page page) {
        super(page);  // Calls parent constructor first (constructor chaining)
        this.config = ConfigReaderUtils.getInstance();
    }
}
```

**Flow:**
1. `new GooglePage(page)` is called
2. `super(page)` calls `BasePage(Page page)` constructor
3. `BasePage` constructor initializes `this.page = page`
4. Control returns to `GooglePage` constructor
5. `GooglePage` initializes `this.config`

### 6.2. Method Chaining (Fluent Interface)

**Example:**
```java
public class GooglePage extends BasePage {
    public GooglePage enterSearchQuery(String query) {
        page.locator(SEARCH_BOX).fill(query);
        return this;  // Returns self for chaining
    }
    
    public GooglePage clickSearchButton() {
        page.locator(SEARCH_BUTTON).click();
        return this;  // Returns self for chaining
    }
}

// Usage - method chaining
GooglePage googlePage = new GooglePage(page);
googlePage.enterSearchQuery("test")
          .clickSearchButton();  // Chained method calls
```

### 6.3. Static Members

**Example:**
```java
public class GooglePage extends BasePage {
    // Static final - class-level constant, shared by all instances
    private static final String SEARCH_BOX = "textarea[name='q']";
    
    // Static method in ConfigReaderUtils
    public static ConfigReaderUtils getInstance() {
        // Static method can be called without creating object
        return ConfigReaderUtils.getInstance();
    }
}
```

### 6.4. Access Modifiers

| Modifier | Visibility | Example in Framework |
|----------|-----------|---------------------|
| **private** | Same class only | `private Properties properties` in ConfigReaderUtils |
| **protected** | Same class + subclasses | `protected Page page` in BasePage |
| **public** | Everywhere | `public void navigate()` in BasePage |
| **default** | Same package | (Not used in framework) |

---

## 📊 Summary: OOP Concepts in Framework

| OOP Concept | Example | Purpose |
|------------|---------|---------|
| **Encapsulation** | Private locators, protected page field | Data hiding, controlled access |
| **Inheritance** | `GooglePage extends BasePage` | Code reusability, DRY principle |
| **Polymorphism** | Overridden `navigate()` methods | Same interface, different behavior |
| **Abstraction** | Simple public methods hiding complex logic | Simplifies test code |
| **Composition** | Page objects containing ConfigReaderUtils | "Has-a" relationship, flexibility |

---

## 🎯 Interview Answer: "How are OOP concepts used in your framework?"

**2-3 Minute Answer:**

"I've extensively used OOP concepts in my framework to ensure maintainability, reusability, and scalability.

**First, Encapsulation**: All page objects encapsulate their locators as private constants, and internal implementation details are hidden. For example, in `GooglePage`, the `SEARCH_BOX` locator is private, and tests interact only through public methods like `enterSearchQuery()`. The `ConfigReaderUtils` class uses encapsulation with a private constructor and static getter method to implement the Singleton pattern.

**Second, Inheritance**: All page objects like `GooglePage`, `AmazonHomePage`, and `AmazonProductPage` extend `BasePage`, which contains common methods like `navigate()`, `getTitle()`, and `waitForPageLoad()`. This follows the DRY principle - common code is written once in the parent class and reused by all child classes.

**Third, Polymorphism**: I've implemented method overriding where each page object overrides the `navigate()` method with its own implementation. For instance, `GooglePage.navigate()` navigates to Google, while `AmazonHomePage.navigate()` navigates to Amazon. I also use method overloading in `ConfigReaderUtils` with multiple `getProperty()` methods that accept different parameters.

**Fourth, Abstraction**: The framework abstracts complex Playwright operations behind simple interfaces. Tests don't need to know about `page.navigate()` or `waitForLoadState()` - they just call `googlePage.navigate()`. The `addProductToCart()` method in `AmazonProductPage` abstracts complex logic like popup handling, product option selection, and multiple selector attempts.

**Finally, Composition**: Page objects use composition to include utility objects. For example, `GooglePage` has-a `ConfigReaderUtils` object for configuration management, and `BasePage` has-a `Playwright Page` object for browser interactions.

These OOP principles make the framework maintainable - when UI changes, I only update the page object class. They also make it scalable - adding new pages is as simple as extending `BasePage`. The abstraction layer ensures test code remains clean and focused on business logic rather than technical implementation details."

---

## 📝 Key Takeaways for Interview

1. ✅ **Encapsulation**: Private fields, public methods, data hiding
2. ✅ **Inheritance**: BasePage → Child pages, code reusability
3. ✅ **Polymorphism**: Method overriding and overloading
4. ✅ **Abstraction**: Simple interfaces hiding complex implementations
5. ✅ **Composition**: "Has-a" relationships for flexibility

**Remember**: Always provide concrete examples from your codebase when explaining OOP concepts in interviews!

