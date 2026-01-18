# Login Test Implementation Comparison

This document shows how the same login test scenario is implemented across all three frameworks, highlighting the differences in syntax, approach, and capabilities.

## 🎯 Test Scenario

**Objective**: Test user login functionality
**Steps**:
1. Navigate to login page
2. Enter username and password
3. Click login button
4. Verify successful login (redirect to dashboard)
5. Handle login failure scenarios

## 🔧 Selenium WebDriver Implementation

### Java with TestNG
```java
// LoginTest.java
package tests;

import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.DashboardPage;
import static org.testng.Assert.*;

public class LoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    
    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }
    
    @Test
    public void testSuccessfulLogin() {
        // Navigate to login page
        loginPage.navigateToLogin();
        
        // Enter credentials
        loginPage.enterUsername("testuser@example.com");
        loginPage.enterPassword("password123");
        
        // Click login button
        loginPage.clickLoginButton();
        
        // Verify successful login
        assertTrue(dashboardPage.isDashboardDisplayed(), 
                  "Dashboard should be displayed after successful login");
        assertEquals(dashboardPage.getWelcomeMessage(), 
                    "Welcome, Test User!");
    }
    
    @Test
    public void testInvalidLogin() {
        loginPage.navigateToLogin();
        loginPage.enterUsername("invalid@example.com");
        loginPage.enterPassword("wrongpassword");
        loginPage.clickLoginButton();
        
        // Verify error message
        assertTrue(loginPage.isErrorMessageDisplayed());
        assertEquals(loginPage.getErrorMessage(), 
                    "Invalid username or password");
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

// LoginPage.java (Page Object)
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(css = ".error-message")
    private WebElement errorMessage;
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    public void navigateToLogin() {
        driver.get("https://example.com/login");
    }
    
    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
    }
    
    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }
    
    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }
    
    public boolean isErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getErrorMessage() {
        return errorMessage.getText();
    }
}
```

### Python with pytest
```python
# test_login.py
import pytest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from pages.login_page import LoginPage
from pages.dashboard_page import DashboardPage

class TestLogin:
    
    @pytest.fixture(autouse=True)
    def setup(self):
        self.driver = webdriver.Chrome()
        self.wait = WebDriverWait(self.driver, 10)
        self.login_page = LoginPage(self.driver)
        self.dashboard_page = DashboardPage(self.driver)
        yield
        self.driver.quit()
    
    def test_successful_login(self):
        # Navigate and login
        self.login_page.navigate_to_login()
        self.login_page.enter_credentials("testuser@example.com", "password123")
        self.login_page.click_login()
        
        # Verify successful login
        assert self.dashboard_page.is_dashboard_displayed()
        assert self.dashboard_page.get_welcome_message() == "Welcome, Test User!"
    
    def test_invalid_login(self):
        self.login_page.navigate_to_login()
        self.login_page.enter_credentials("invalid@example.com", "wrongpassword")
        self.login_page.click_login()
        
        # Verify error message
        assert self.login_page.is_error_displayed()
        assert self.login_page.get_error_message() == "Invalid username or password"

# pages/login_page.py
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

class LoginPage:
    def __init__(self, driver):
        self.driver = driver
        self.wait = WebDriverWait(driver, 10)
        
        # Locators
        self.username_field = (By.ID, "username")
        self.password_field = (By.ID, "password")
        self.login_button = (By.ID, "login-button")
        self.error_message = (By.CSS_SELECTOR, ".error-message")
    
    def navigate_to_login(self):
        self.driver.get("https://example.com/login")
    
    def enter_credentials(self, username, password):
        username_element = self.wait.until(EC.visibility_of_element_located(self.username_field))
        username_element.clear()
        username_element.send_keys(username)
        
        password_element = self.driver.find_element(*self.password_field)
        password_element.clear()
        password_element.send_keys(password)
    
    def click_login(self):
        login_btn = self.wait.until(EC.element_to_be_clickable(self.login_button))
        login_btn.click()
    
    def is_error_displayed(self):
        try:
            self.wait.until(EC.visibility_of_element_located(self.error_message))
            return True
        except:
            return False
    
    def get_error_message(self):
        error_element = self.driver.find_element(*self.error_message)
        return error_element.text
```

## 🌲 Cypress Implementation

```javascript
// cypress/e2e/login.cy.js
describe('Login Tests', () => {
  beforeEach(() => {
    // Navigate to login page before each test
    cy.visit('/login');
  });

  it('should login successfully with valid credentials', () => {
    // Enter credentials
    cy.get('[data-cy=username]').type('testuser@example.com');
    cy.get('[data-cy=password]').type('password123');
    
    // Click login button
    cy.get('[data-cy=login-button]').click();
    
    // Verify successful login
    cy.url().should('include', '/dashboard');
    cy.get('[data-cy=welcome-message]').should('contain', 'Welcome, Test User!');
  });

  it('should show error message with invalid credentials', () => {
    // Enter invalid credentials
    cy.get('[data-cy=username]').type('invalid@example.com');
    cy.get('[data-cy=password]').type('wrongpassword');
    cy.get('[data-cy=login-button]').click();
    
    // Verify error message
    cy.get('[data-cy=error-message]')
      .should('be.visible')
      .and('contain', 'Invalid username or password');
    
    // Verify still on login page
    cy.url().should('include', '/login');
  });

  it('should handle network errors gracefully', () => {
    // Mock network failure
    cy.intercept('POST', '/api/auth/login', { forceNetworkError: true }).as('loginRequest');
    
    cy.get('[data-cy=username]').type('testuser@example.com');
    cy.get('[data-cy=password]').type('password123');
    cy.get('[data-cy=login-button]').click();
    
    // Wait for the request and verify error handling
    cy.wait('@loginRequest');
    cy.get('[data-cy=error-message]')
      .should('contain', 'Network error. Please try again.');
  });

  it('should validate required fields', () => {
    // Try to login without entering credentials
    cy.get('[data-cy=login-button]').click();
    
    // Verify validation messages
    cy.get('[data-cy=username]').should('have.class', 'error');
    cy.get('[data-cy=password]').should('have.class', 'error');
    cy.get('[data-cy=validation-message]').should('contain', 'Please fill in all fields');
  });
});

// cypress/support/commands.js - Custom Commands
Cypress.Commands.add('login', (username, password) => {
  cy.visit('/login');
  cy.get('[data-cy=username]').type(username);
  cy.get('[data-cy=password]').type(password);
  cy.get('[data-cy=login-button]').click();
});

// Usage in tests:
// cy.login('testuser@example.com', 'password123');
```

## 🎭 Playwright Implementation

### JavaScript/TypeScript
```typescript
// tests/login.spec.ts
import { test, expect } from '@playwright/test';
import { LoginPage } from '../pages/LoginPage';
import { DashboardPage } from '../pages/DashboardPage';

test.describe('Login Tests', () => {
  let loginPage: LoginPage;
  let dashboardPage: DashboardPage;

  test.beforeEach(async ({ page }) => {
    loginPage = new LoginPage(page);
    dashboardPage = new DashboardPage(page);
    await loginPage.goto();
  });

  test('should login successfully with valid credentials', async ({ page }) => {
    await loginPage.login('testuser@example.com', 'password123');
    
    // Verify successful login
    await expect(page).toHaveURL(/.*dashboard/);
    await expect(dashboardPage.welcomeMessage).toHaveText('Welcome, Test User!');
  });

  test('should show error message with invalid credentials', async ({ page }) => {
    await loginPage.login('invalid@example.com', 'wrongpassword');
    
    // Verify error message
    await expect(loginPage.errorMessage).toBeVisible();
    await expect(loginPage.errorMessage).toHaveText('Invalid username or password');
    
    // Verify still on login page
    await expect(page).toHaveURL(/.*login/);
  });

  test('should handle API errors gracefully', async ({ page }) => {
    // Mock API failure
    await page.route('**/api/auth/login', route => {
      route.fulfill({
        status: 500,
        contentType: 'application/json',
        body: JSON.stringify({ error: 'Internal server error' })
      });
    });

    await loginPage.login('testuser@example.com', 'password123');
    
    await expect(loginPage.errorMessage).toHaveText('Server error. Please try again later.');
  });

  test('should validate required fields', async ({ page }) => {
    await loginPage.clickLoginButton();
    
    // Verify validation
    await expect(loginPage.usernameField).toHaveClass(/error/);
    await expect(loginPage.passwordField).toHaveClass(/error/);
    await expect(page.locator('[data-testid=validation-message]'))
      .toHaveText('Please fill in all fields');
  });

  test('should support keyboard navigation', async ({ page }) => {
    // Test tab navigation
    await page.keyboard.press('Tab'); // Focus username
    await expect(loginPage.usernameField).toBeFocused();
    
    await page.keyboard.press('Tab'); // Focus password
    await expect(loginPage.passwordField).toBeFocused();
    
    await page.keyboard.press('Tab'); // Focus login button
    await expect(loginPage.loginButton).toBeFocused();
  });
});

// pages/LoginPage.ts
import { Page, Locator } from '@playwright/test';

export class LoginPage {
  readonly page: Page;
  readonly usernameField: Locator;
  readonly passwordField: Locator;
  readonly loginButton: Locator;
  readonly errorMessage: Locator;

  constructor(page: Page) {
    this.page = page;
    this.usernameField = page.locator('[data-testid=username]');
    this.passwordField = page.locator('[data-testid=password]');
    this.loginButton = page.locator('[data-testid=login-button]');
    this.errorMessage = page.locator('[data-testid=error-message]');
  }

  async goto() {
    await this.page.goto('/login');
  }

  async login(username: string, password: string) {
    await this.usernameField.fill(username);
    await this.passwordField.fill(password);
    await this.loginButton.click();
  }

  async clickLoginButton() {
    await this.loginButton.click();
  }
}
```

### Python
```python
# tests/test_login.py
import pytest
from playwright.sync_api import Page, expect
from pages.login_page import LoginPage
from pages.dashboard_page import DashboardPage

class TestLogin:
    
    def test_successful_login(self, page: Page):
        login_page = LoginPage(page)
        dashboard_page = DashboardPage(page)
        
        login_page.goto()
        login_page.login("testuser@example.com", "password123")
        
        # Verify successful login
        expect(page).to_have_url(re.compile(r".*dashboard"))
        expect(dashboard_page.welcome_message).to_have_text("Welcome, Test User!")
    
    def test_invalid_login(self, page: Page):
        login_page = LoginPage(page)
        
        login_page.goto()
        login_page.login("invalid@example.com", "wrongpassword")
        
        # Verify error message
        expect(login_page.error_message).to_be_visible()
        expect(login_page.error_message).to_have_text("Invalid username or password")

# pages/login_page.py
from playwright.sync_api import Page

class LoginPage:
    def __init__(self, page: Page):
        self.page = page
        self.username_field = page.locator('[data-testid=username]')
        self.password_field = page.locator('[data-testid=password]')
        self.login_button = page.locator('[data-testid=login-button]')
        self.error_message = page.locator('[data-testid=error-message]')
    
    def goto(self):
        self.page.goto('/login')
    
    def login(self, username: str, password: str):
        self.username_field.fill(username)
        self.password_field.fill(password)
        self.login_button.click()
```

## 🔍 Key Differences Analysis

### 1. **Setup and Teardown**
- **Selenium**: Manual driver management, explicit setup/teardown
- **Cypress**: Automatic browser management, simple beforeEach hooks
- **Playwright**: Built-in browser management, fixture-based setup

### 2. **Element Selection**
- **Selenium**: Multiple locator strategies, explicit waits required
- **Cypress**: jQuery-like selectors, automatic retry logic
- **Playwright**: Modern locator API with built-in auto-waiting

### 3. **Waiting Strategies**
- **Selenium**: Explicit waits with ExpectedConditions
- **Cypress**: Automatic retry and waiting built-in
- **Playwright**: Auto-waiting with actionability checks

### 4. **Error Handling**
- **Selenium**: Try-catch blocks, manual error checking
- **Cypress**: Automatic retry, built-in assertions
- **Playwright**: Promise-based with automatic retries

### 5. **Network Mocking**
- **Selenium**: Not supported natively
- **Cypress**: Excellent cy.intercept() API
- **Playwright**: Built-in page.route() functionality

### 6. **Debugging**
- **Selenium**: Basic debugging, external tools needed
- **Cypress**: Excellent time-travel debugging
- **Playwright**: Good debugging with trace viewer

### 7. **Test Organization**
- **Selenium**: Framework-dependent (TestNG, pytest, etc.)
- **Cypress**: Mocha-based describe/it structure
- **Playwright**: Built-in test runner with describe/test structure

This comparison shows how the same functionality can be achieved with different approaches, highlighting each framework's strengths and characteristics.