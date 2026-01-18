import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.*;
import org.example.utils.ConfigReaderUtils;
import org.example.pages.AmazonHomePage;
import org.example.pages.AmazonProductPage;
import org.example.pages.AmazonSearchResultsPage;
import org.example.pages.GooglePage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * PlayWright Test class using Page Object Model design pattern
 */
public class PlayWrightTest {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeMethod
    public void setUp() {
        // Initialize Playwright
        playwright = Playwright.create();

        // Launch Chrome browser
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );

        // Create a new page
        page = browser.newPage();
    }

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

        // Validate title using TestNG assertion (getting expected title from config)
        String expectedTitle = googlePage.getExpectedTitle();
        Allure.step("Verify page title matches expected value: " + expectedTitle, () -> {
            Assert.assertEquals(actualTitle, expectedTitle,
                "Page title does not match expected value. Actual: " + actualTitle);
        });

        System.out.println("Test passed! Page title is: " + actualTitle);
    }

    @Test
    @Epic("E-commerce Testing")
    @Feature("Shopping Cart")
    @Story("Add Product to Cart")
    @Description("This test verifies the complete flow of adding a mobile product to the cart on Amazon")
    @Severity(SeverityLevel.BLOCKER)
    public void testAddMobileToCart() {
        // Using Page Object Model
        ConfigReaderUtils config = ConfigReaderUtils.getInstance();
        
        // Step 1: Navigate to Amazon homepage
        Allure.step("Navigate to Amazon homepage", () -> {
            AmazonHomePage amazonHomePage = new AmazonHomePage(page);
            amazonHomePage.navigate();
        });

        // Step 2: Search for "motorola" (getting search query from config)
        AmazonSearchResultsPage searchResultsPage = Allure.step(
            "Search for product: " + config.getMotorolaSearchQuery(), 
            () -> {
                AmazonHomePage amazonHomePage = new AmazonHomePage(page);
                return amazonHomePage.search(config.getMotorolaSearchQuery());
            }
        );

        // Step 3: Click on the first search result
        AmazonProductPage productPage = Allure.step("Click on the first search result", () -> {
            return searchResultsPage.clickFirstSearchResult();
        });

        // Step 4: Handle popups, options, and add product to cart
        Allure.step("Add product to cart", () -> {
            productPage.addProductToCart();
        });

        // Step 5: Verify item was added to cart
        Allure.step("Verify item was added to cart", () -> {
            boolean cartUpdated = productPage.verifyItemAddedToCart();
            Assert.assertTrue(cartUpdated, "Mobile was not successfully added to cart");
        });

        System.out.println("Test passed! Mobile added to cart successfully");
    }

    @AfterMethod
    public void tearDown() {
        // Close browser and playwright
        if (browser != null) {
          //  browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}