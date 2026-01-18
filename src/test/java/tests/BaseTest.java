package tests;

import drivers.GUIDriver;
import drivers.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.JsonUtils;
import utils.PropertiesUtils;

/**
 * BaseTest is the parent class for all test classes.
 *
 * Responsibilities:
 * - Initialize test data from JSON files.
 * - Initialize and manage the WebDriver instance via GUIDriver.
 * - Provide a standard method to quit the browser after tests.
 * - Implement WebDriverProvider interface for accessing WebDriver.
 */
public class BaseTest implements WebDriverProvider {

    /**
     * Test data utility to read JSON files for test input.
     */
    public JsonUtils testData;

    /**
     * GUIDriver instance managing WebDriver for this test class.
     */
    public GUIDriver driver;

    /**
     * Sets up the test environment before any test methods in the class are run.
     *
     * Initializes:
     * - JsonUtils with the "test-data" folder.
     * - GUIDriver (which starts the browser).
     */
    @BeforeClass
    public void beforeClass() {
        testData = new JsonUtils("test-data");
        String url = System.getProperty("baseUrlWeb");
        driver = new GUIDriver();
        driver.browser().navigate(url);
    }

    /**
     * Cleans up the test environment after all test methods in the class are run.
     *
     * Quits the browser and releases WebDriver resources.
     */
    @AfterClass
    public void closeBrowser() {
        driver.quit();
    }

    /**
     * Returns the WebDriver instance for the current test.
     *
     * @return WebDriver instance managed by GUIDriver
     */
    @Override
    public WebDriver getWebDriver() {
        return driver.get();
    }
}
