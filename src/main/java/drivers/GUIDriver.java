package drivers;

import org.openqa.selenium.WebDriver;
import utils.*;
import validations.Assertions;
import validations.SoftAssertions;

import static org.aspectj.bridge.MessageUtil.fail;

public class GUIDriver {

    // Thread-safe storage for WebDriver instances
    private final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    // Browser type loaded from configuration file
    String browserName = ConfigUtils.getConfigValue("browserType");

    WebDriver driver;

    /**
     * Initializes the GUI driver based on the configured browser type.
     *
     * This constructor:
     * - Reads the browser name from configuration
     * - Creates the corresponding WebDriver using the appropriate factory
     * - Stores the driver instance in a ThreadLocal variable
     *
     * This design supports parallel execution safely.
     */
    public GUIDriver() {
        driver = getDriver(browserName).startDriver();
        setDriver(driver);
    }

    /**
     * Quits the current WebDriver instance if it exists.
     *
     * This method should be called after test execution
     * to properly close the browser and release resources.
     */
    public void quit() {
        if (get() != null) {
            get().quit();
        }
    }

    /**
     * Retrieves the current WebDriver instance from ThreadLocal storage.
     *
     * If the driver is not initialized, an error is logged
     * and the test execution is failed.
     *
     * @return the active WebDriver instance
     */
    public WebDriver get() {
        if (driverThreadLocal.get() == null) {
            LogUtils.error("Driver is null");
            fail("Driver is null");
            return null;
        }
        return driverThreadLocal.get();
    }

    /**
     * Returns the appropriate driver factory based on the browser name.
     *
     * Supported browsers:
     * - chrome
     * - firefox
     * - edge
     *
     * @param browserName the browser type read from configuration
     * @return an instance of AbstractDriver corresponding to the browser
     * @throws IllegalArgumentException if an invalid browser name is provided
     */
    private AbstractDriver getDriver(String browserName) {
        return switch (browserName.toLowerCase()) {
            case "chrome" -> new ChromeFactory();
            case "firefox" -> new FirefoxFactory();
            case "edge" -> new EdgeFactory();
            default -> throw new IllegalArgumentException("Invalid browser name: " + browserName);
        };
    }

    /**
     * Stores the provided WebDriver instance in ThreadLocal storage.
     *
     * This ensures thread safety when running tests in parallel.
     *
     * @param driver the WebDriver instance to be stored
     */
    private void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    /**
     * Provides access to element-related actions.
     *
     * @return a new instance of ElementActions bound to the current WebDriver
     */
    public ElementActions element() {
        return new ElementActions(get());
    }

    /**
     * Provides access to browser-level actions.
     *
     * @return a new instance of BrowserActions bound to the current WebDriver
     */
    public BrowserActions browser() {
        return new BrowserActions(get());
    }

    /**
     * Provides access to frame-related actions.
     *
     * @return a new instance of FrameActions bound to the current WebDriver
     */
    public FrameActions frame() {
        return new FrameActions(get());
    }

    /**
     * Provides hard assertion validations using the current driver context.
     *
     * @return an Assertions instance configured with element actions and waits
     */
    public Assertions validate() {
        return new Assertions(element(), new Waits(get()), get());
    }

    /**
     * Provides soft assertion validations using the current driver context.
     *
     * Soft assertions allow test execution to continue
     * even if some validations fail.
     *
     * @return a SoftAssertions instance configured with element actions and waits
     */
    public SoftAssertions softValidate() {
        return new SoftAssertions(element(), new Waits(get()), get());
    }

    /**
     * Provides access to alert-related actions.
     *
     * @return an AlertUtils instance bound to the current WebDriver
     */
    public AlertUtils alert() {
        return new AlertUtils(get());
    }
}
