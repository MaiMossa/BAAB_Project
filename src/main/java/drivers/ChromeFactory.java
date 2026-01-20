package drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.LogUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ChromeFactory extends AbstractDriver
        implements WebDriverOptionsAbstract<ChromeOptions> {

    /**
     * Creates and configures ChromeOptions used to initialize the Chrome browser.
     * This method is responsible for:
     *     Disabling browser notifications, password manager, and autofill
     *     Setting Chrome startup arguments (maximize window, disable extensions, etc.)
     *     Configuring page load strategy
     *     Enabling headless mode for LocalHeadless and Remote execution types
     *     Allowing insecure certificates for test environments
     * @return a fully configured {@link ChromeOptions} instance
     */
    @Override
    public ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("autofill.profile_enabled", false);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        options.setPageLoadStrategy(PageLoadStrategy.NONE);

        if (executionType.equalsIgnoreCase("LocalHeadless")
                || executionType.equalsIgnoreCase("Remote")) {
            options.addArguments("--headless=new");
        }

        options.setAcceptInsecureCerts(true);
        return options;
    }

    /**
     * Starts and returns a WebDriver instance based on the execution type.
     * Supported execution types:
     *     Local - Runs Chrome locally with UI
     *     LocalHeadless - Runs Chrome locally in headless mode
     *    Remote - Runs Chrome on a remote Selenium Grid
     * For remote execution, the driver connects to a Selenium Grid using the
     * configured host and port.
     *
     * @return an initialized {@link WebDriver} instance
     * @throws IllegalArgumentException if an invalid execution type is provided
     */
    @Override
    public WebDriver startDriver() {
        if (executionType.equalsIgnoreCase("Local")
                || executionType.equalsIgnoreCase("LocalHeadless")) {

            return new ChromeDriver(getOptions());

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
