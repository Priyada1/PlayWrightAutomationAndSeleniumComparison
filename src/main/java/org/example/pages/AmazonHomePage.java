package org.example.pages;

import com.microsoft.playwright.Page;
import org.example.utils.ConfigReaderUtils;

/**
 * Page Object Model for Amazon homepage
 */
public class AmazonHomePage extends BasePage {

    // Locators
    private static final String SEARCH_BOX = "#twotabsearchtextbox";
    private static final String SEARCH_BUTTON = "#nav-search-submit-button";

    private final ConfigReaderUtils config;

    public AmazonHomePage(Page page) {
        super(page);
        this.config = ConfigReaderUtils.getInstance();
    }

    /**
     * Navigate to Amazon homepage
     */
    public AmazonHomePage navigate() {
        super.navigate(config.getAmazonUrl());
        return this;
    }

    /**
     * Enter search query in the search box
     */
    public AmazonHomePage enterSearchQuery(String query) {
        page.locator(SEARCH_BOX).waitFor();
        page.locator(SEARCH_BOX).fill(query);
        return this;
    }

    /**
     * Click the search button
     */
    public AmazonSearchResultsPage clickSearchButton() {
        page.locator(SEARCH_BUTTON).click();
        waitForPageLoad();
        return new AmazonSearchResultsPage(page);
    }

    /**
     * Perform a search operation and return search results page
     */
    public AmazonSearchResultsPage search(String query) {
        enterSearchQuery(query);
        return clickSearchButton();
    }
}

