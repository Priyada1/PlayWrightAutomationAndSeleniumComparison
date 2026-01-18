package org.example.pages;

import com.microsoft.playwright.Page;
import org.example.utils.ConfigReaderUtils;

/**
 * Page Object Model for Amazon product page
 */
public class AmazonProductPage extends BasePage {

    // Locators for Add to Cart button (multiple selectors for reliability)
    private static final String[] ADD_TO_CART_SELECTORS = {
        "#add-to-cart-button",
        "input#add-to-cart-button",
        "input[name='submit.add-to-cart']",
        "#add-to-cart-button-ubb",
        "span:has-text('Add to Cart')",
        "input[value='Add to Cart']",
        "#addToCart",
        "[data-action='add-to-cart']",
        "button:has-text('Add to Cart')",
        "#mbc-buybutton-addtocart-1-announce"
    };

    // Other locators
    private static final String DELIVERY_POPUP = "#nav-global-location-popup-link";
    private static final String CLOSE_BUTTON = "button[aria-label='Close']";
    private static final String SIZE_DROPDOWN = "#native_dropdown_selected_size_name";
    private static final String CART_COUNT = "#nav-cart-count";
    private static final String ADD_TO_CART_TEXT = "Add to Cart";

    // Verification messages
    private static final String CART_SUCCESS_MESSAGE_ADDED_TO_CART = "added to cart";
    private static final String CART_SUCCESS_MESSAGE_ADDED_TO_YOUR_CART = "added to your cart";

    private final ConfigReaderUtils config;

    public AmazonProductPage(Page page) {
        super(page);
        this.config = ConfigReaderUtils.getInstance();
    }

    /**
     * Handle any popups that might appear on the product page
     */
    public AmazonProductPage handlePopups() {
        try {
            // Close "Deliver to" popup if it appears
            if (page.locator(DELIVERY_POPUP).isVisible()) {
                page.locator(DELIVERY_POPUP).click();
                wait(config.getShortTimeout());
            }
            // Close any other popups
            if (page.locator(CLOSE_BUTTON).isVisible()) {
                page.locator(CLOSE_BUTTON).first().click();
                wait(config.getShortTimeout());
            }
        } catch (Exception e) {
            // Ignore if popups don't exist
        }
        return this;
    }

    /**
     * Handle product options if they exist (color, size, etc.)
     */
    public AmazonProductPage handleProductOptions() {
        try {
            // Try to select first available option if dropdown exists
            if (page.locator(SIZE_DROPDOWN).isVisible()) {
                page.locator(SIZE_DROPDOWN).selectOption("1");
                wait(config.getShortTimeout());
            }
        } catch (Exception e) {
            // Ignore if no options to select
        }
        return this;
    }

    /**
     * Add product to cart
     */
    public AmazonProductPage addToCart() {
        wait(config.getMediumTimeout()); // Wait for Add to Cart button to be available

        boolean addedToCart = false;

        // Try various selectors for Add to Cart button
        for (String selector : ADD_TO_CART_SELECTORS) {
            try {
                if (page.locator(selector).count() > 0 && page.locator(selector).isVisible()) {
                    page.locator(selector).click();
                    addedToCart = true;
                    System.out.println("Found Add to Cart button with selector: " + selector);
                    break;
                }
            } catch (Exception e) {
                // Continue to next selector
                continue;
            }
        }

        // If still not found, try to find any button containing "Add to Cart" text
        if (!addedToCart) {
            try {
                String addToCartText = "text=" + ADD_TO_CART_TEXT;
                page.locator(addToCartText).first().click();
                addedToCart = true;
                System.out.println("Found Add to Cart button using text locator");
            } catch (Exception e) {
                // Last attempt failed
            }
        }

        if (!addedToCart) {
            throw new RuntimeException("Could not find 'Add to Cart' button on the product page. " +
                    "Page URL: " + getCurrentUrl());
        }

        // Wait for cart confirmation to appear
        wait(config.getLongTimeout());
        return this;
    }

    /**
     * Verify that item was added to cart
     */
    public boolean verifyItemAddedToCart() {
        String pageContent = page.content().toLowerCase();
        boolean cartUpdated = pageContent.contains(CART_SUCCESS_MESSAGE_ADDED_TO_CART) ||
                             pageContent.contains(CART_SUCCESS_MESSAGE_ADDED_TO_YOUR_CART) ||
                             page.locator(CART_COUNT).isVisible();
        return cartUpdated;
    }

    /**
     * Complete flow: handle popups, options, and add to cart
     */
    public AmazonProductPage addProductToCart() {
        handlePopups();
        handleProductOptions();
        addToCart();
        return this;
    }
}

