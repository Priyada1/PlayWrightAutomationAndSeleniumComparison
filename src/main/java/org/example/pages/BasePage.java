package org.example.pages;

import com.microsoft.playwright.Page;

/**
 * Base Page class that contains common methods and properties
 * that can be shared across all page objects
 */
public class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    /**
     * Navigate to a URL
     */
    public void navigate(String url) {
        page.navigate(url);
        page.waitForLoadState();
    }

    /**
     * Get the page title
     */
    public String getTitle() {
        return page.title();
    }

    /**
     * Get the current page URL
     */
    public String getCurrentUrl() {
        return page.url();
    }

    /**
     * Wait for page to load completely
     */
    public void waitForPageLoad() {
        page.waitForLoadState();
    }

    /**
     * Wait for a specific timeout
     */
    public void wait(int milliseconds) {
        page.waitForTimeout(milliseconds);
    }
}

