package org.example.pages;

import com.microsoft.playwright.Page;
import org.example.utils.ConfigReaderUtils;

/**
 * Page Object Model for Google homepage
 */
public class GooglePage extends BasePage {

    // Locators
    private static final String SEARCH_BOX = "textarea[name='q']";
    private static final String SEARCH_BUTTON = "input[name='btnK']";

    private final ConfigReaderUtils config;

    public GooglePage(Page page) {
        super(page);
        this.config = ConfigReaderUtils.getInstance();
    }

    /**
     * Navigate to Google homepage
     */
    public GooglePage navigate() {
        super.navigate(config.getGoogleUrl());
        return this;
    }

    /**
     * Get the page title
     */
    public String getPageTitle() {
        return getTitle();
    }

    /**
     * Get expected page title from config
     */
    public String getExpectedTitle() {
        return config.getGoogleExpectedTitle();
    }

    /**
     * Enter search query in the search box
     */
    public GooglePage enterSearchQuery(String query) {
        page.locator(SEARCH_BOX).fill(query);
        return this;
    }

    /**
     * Click the search button
     */
    public GooglePage clickSearchButton() {
        page.locator(SEARCH_BUTTON).click();
        return this;
    }

    /**
     * Perform a search operation
     */
    public GooglePage search(String query) {
        enterSearchQuery(query);
        clickSearchButton();
        waitForPageLoad();
        return this;
    }
}

