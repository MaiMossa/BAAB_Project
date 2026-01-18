package drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.LogUtils;

import java.net.URI;

public class FirefoxFactory extends AbstractDriver
        implements WebDriverOptionsAbstract<FirefoxOptions> {

    /**
     * Creates and configures FirefoxOptions used to initialize the Firefox browser.
     *
     * This method is responsible for:
     *      configuring browser startup arguments,
     *      enabling headless mode when required, setting the page load strategy,
     *      and allowing insecure certificates for test environments.
     *
     * Headless mode is enabled when the execution type is LocalHeadless
     * or Remote.
     *
     * @return a fully configured FirefoxOptions instance
     */
    @Override
    public FirefoxOptions getOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();

        firefoxOptions.addArguments("--start-maximized");
        firefoxOptions.addArguments("--disable-extensions");
        firefoxOptions.addArguments("--disable-infobars");
        firefoxOptions.addArguments("--disable-notifications");
        firefoxOptions.addArguments("--remote-allow-origins=*");

        if (executionType.equalsIgnoreCase("LocalHeadless")
                || executionType.equalsIgnoreCase("Remote")) {
            firefoxOptions.addArguments("--headless=new");
        }

        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        firefoxOptions.setAcceptInsecureCerts(true);

        return firefoxOptions;
    }

    /**
     * Starts and returns a WebDriver instance for Firefox
     * based on the configured execution type.
     *
     *      Local - runs Firefox locally with UI.
     *      LocalHeadless - runs Firefox locally without UI.
     *      Remote - runs Firefox on a remote Selenium Grid.
     *
     * For remote execution, the driver connects to Selenium Grid
     * using the configured host and port.
     *
     * @return an initialized WebDriver instance
     * @throws IllegalArgumentException if an invalid execution type is provided
     */
    @Override
    public WebDriver startDriver() {
        if (executionType.equalsIgnoreCase("Local")
                || executionType.equalsIgnoreCase("LocalHeadless")) {

            return new FirefoxDriver(getOptions());

        } else if (executionType.equalsIgnoreCase("Remote")) {

            try {
                return new RemoteWebDriver(
                        new URI("http://" + remoteExecutionHost + ":"
                                + remoteExecutionPort + "/wd/hub").toURL(),
                        getOptions());
            } catch (Exception e) {
                LogUtils.error(e.getMessage());
                return null;
            }

        } else {
            throw new IllegalArgumentException(
                    "Invalid execution type: " + executionType);
        }
    }
}
