package org.example.pages;

import com.microsoft.playwright.Page;
import org.example.utils.ConfigReaderUtils;

/**
 * Page Object Model for Amazon search results page
 */
public class AmazonSearchResultsPage extends BasePage {

    // Locators
    private static final String SEARCH_RESULT_ITEM = "[data-component-type='s-search-result']";

    private final ConfigReaderUtils config;

    public AmazonSearchResultsPage(Page page) {
        super(page);
        this.config = ConfigReaderUtils.getInstance();
    }

    /**
     * Click on the first search result
     */
    public AmazonProductPage clickFirstSearchResult() {
        page.locator(SEARCH_RESULT_ITEM).first().waitFor();
        page.locator(SEARCH_RESULT_ITEM).first().click();
        waitForPageLoad();
        wait(config.getMediumTimeout()); // Additional wait for dynamic content
        return new AmazonProductPage(page);
    }

    /**
     * Get the number of search results
     */
    public int getSearchResultsCount() {
        return page.locator(SEARCH_RESULT_ITEM).count();
    }
}

