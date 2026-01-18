package drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.LogUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class EdgeFactory extends AbstractDriver
        implements WebDriverOptionsAbstract<EdgeOptions> {

    /**
     * Creates and configures EdgeOptions used to initialize the Microsoft Edge browser.
     * This method handles:
     *     Disabling browser notifications, password manager, and autofill
     *     Setting Edge startup arguments (maximize window, disable extensions, etc.)
     *     Defining the page load strategy
     *     Enabling headless mode for LocalHeadless and Remote execution
     *     Allowing insecure certificates for testing environments
     *
     * @return a fully configured {@link EdgeOptions} instance
     */
    @Override
    public EdgeOptions getOptions() {
        EdgeOptions options = new EdgeOptions();

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

        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        if (executionType.equalsIgnoreCase("LocalHeadless")
                || executionType.equalsIgnoreCase("Remote")) {
            options.addArguments("--headless=new");
        }

        options.setAcceptInsecureCerts(true);
        return options;
    }

    /**
     * Starts and returns a WebDriver instance for Microsoft Edge
     * based on the configured execution type.
     * Supported execution types:
     *     Local - Runs Edge locally with UI
     *     LocalHeadless - Runs Edge locally in headless mode
     *     Remote - Runs Edge on a remote Selenium Grid
     * For remote execution, this method connects to Selenium Grid
     * using the configured host and port.
     *
     * @return an initialized {@link WebDriver} instance
     * @throws IllegalArgumentException if an invalid execution type is provided
     */
    @Override
    public WebDriver startDriver() {
        if (executionType.equalsIgnoreCase("Local")
                || executionType.equalsIgnoreCase("LocalHeadless")) {

            return new EdgeDriver(getOptions());

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
