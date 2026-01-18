package utils;

import org.openqa.selenium.By;

public class ByUtils {

    /**
     * Creates a locator using a custom test-id attribute.
     *
     * This method helps in locating elements using a stable
     * and test-friendly attribute commonly used in automation.
     *
     * @param id the value of the test-id attribute
     * @return a By locator using a CSS selector
     */
    public static By testId(String id) {
        return By.cssSelector("[test-id='" + id + "']");
    }

    /**
     * Creates a locator using a data-qa attribute.
     *
     * This method is useful for automation-friendly selectors
     * that are independent from UI changes.
     *
     * @param qa the value of the data-qa attribute
     * @return a By locator using a CSS selector
     */
    public static By dataQa(String qa) {
        return By.cssSelector("[data-qa='" + qa + "']");
    }
}
