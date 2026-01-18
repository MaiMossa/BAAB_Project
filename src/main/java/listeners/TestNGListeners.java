package listeners;

import drivers.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import utils.FilesUtils;
import utils.LogUtils;
import utils.ScreenRecorderUtils;
import utils.ScreenshotUtils;
import utils.allurereport.AllureAttachmentManager;
import utils.allurereport.AllureConstants;
import utils.allurereport.AllureEnvironmentManager;
import utils.allurereport.AllureReportGenerator;
import validations.SoftAssertions;

import java.io.File;

import static utils.PropertiesUtils.loadProperties;

public class TestNGListeners implements IExecutionListener,
        IInvokedMethodListener,
        ITestListener,
        ISuiteListener {

    private final File screenshots = new File("test-outputs/screenshots");
    private final File recordings = new File("test-outputs/recordings");
    private final File logs = new File("test-outputs/Logs");

    /**
     * Executes once before the entire test execution starts.
     *
     * This method is responsible for preparing the test environment by:
     * - Cleaning old test output directories
     * - Creating required directories
     * - Loading configuration properties
     * - Setting Allure environment variables
     */
    @Override
    public void onExecutionStart() {
        LogUtils.info("Test Execution started");

        cleanTestOutputDirectories();
        LogUtils.info("Directories cleaned");

        createTestOutputDirectories();
        LogUtils.info("Directories created");

        loadProperties();
        LogUtils.info("Properties loaded");

        AllureEnvironmentManager.setEnvironmentVariables();
        LogUtils.info("Allure environment set");
    }

    /**
     * Executes once after the entire test execution finishes.
     *
     * This method handles Allure report generation by:
     * - Generating initial reports
     * - Copying report history
     * - Regenerating reports with history
     * - Renaming and opening the final report
     */
    @Override
    public void onExecutionFinish() {
        AllureReportGenerator.generateReports(false);
        AllureReportGenerator.copyHistory();
        LogUtils.info("History copied");

        AllureReportGenerator.generateReports(true);
        String newFileName = AllureReportGenerator.renameReport();
        AllureReportGenerator.openReport(newFileName);

        LogUtils.info("Test Execution Finished");
    }

    /**
     * Executes before each invoked test method.
     *
     * If the method is a test method, screen recording is started
     * and the test start is logged.
     *
     * @param method the invoked TestNG method
     * @param testResult the current test result
     */
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            ScreenRecorderUtils.startRecording();
            LogUtils.info("Test Case " + testResult.getName() + " started");
        }
    }

    /**
     * Executes after each invoked method.
     *
     * This method handles:
     * - Stopping screen recording and attaching the video
     * - Executing soft assertions if used
     * - Taking screenshots based on test status
     * - Attaching logs to Allure reports
     *
     * Configuration methods are only logged without screenshots or recordings.
     *
     * @param method the invoked TestNG method
     * @param result the result of the invoked method
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        WebDriver driver = null;

        if (method.isTestMethod()) {

            File videoFile = ScreenRecorderUtils.stopRecording(result.getName());

            if (videoFile != null && videoFile.exists()) {
                AllureAttachmentManager.attachRecords();
            }

            SoftAssertions.AssertIfUsed();

            if (result.getInstance() instanceof WebDriverProvider provider) {
                driver = provider.getWebDriver();
            }

            switch (result.getStatus()) {
                case ITestResult.FAILURE ->
                        ScreenshotUtils.takeScreenShot(driver, "failed-" + result.getName());
                case ITestResult.SUCCESS ->
                        ScreenshotUtils.takeScreenShot(driver, "passed-" + result.getName());
                case ITestResult.SKIP ->
                        ScreenshotUtils.takeScreenShot(driver, "skipped-" + result.getName());
            }

            AllureAttachmentManager.attachLogs();

        } else if (method.isConfigurationMethod()) {

            switch (result.getStatus()) {
                case ITestResult.FAILURE ->
                        LogUtils.info("Configuration Method ", result.getName(), "failed");
                case ITestResult.SUCCESS ->
                        LogUtils.info("Configuration Method ", result.getName(), "passed");
                case ITestResult.SKIP ->
                        LogUtils.info("Configuration Method ", result.getName(), "skipped");
            }
        }
    }

    /**
     * Executes when a test case passes successfully.
     *
     * @param result the result of the passed test
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtils.info("Test case", result.getName(), "passed");
    }

    /**
     * Executes when a test case fails.
     *
     * @param result the result of the failed test
     */
    @Override
    public void onTestFailure(ITestResult result) {
        LogUtils.info("Test case", result.getName(), "failed");
    }

    /**
     * Executes when a test case is skipped.
     *
     * @param result the result of the skipped test
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtils.info("Test case", result.getName(), "skipped");
    }

    /**
     * Executes after the TestNG suite finishes execution.
     *
     * @param suite the executed TestNG suite
     */
    @Override
    public void onFinish(ISuite suite) {
        // No implementation needed
    }

    /**
     * Cleans all test output directories before execution starts.
     *
     * This includes Allure results, screenshots, recordings, and logs.
     */
    private void cleanTestOutputDirectories() {
        FilesUtils.cleanDirectory(AllureConstants.RESULTS_FOLDER.toFile());
        FilesUtils.cleanDirectory(screenshots);
        FilesUtils.cleanDirectory(recordings);
        FilesUtils.cleanDirectory(logs);
    }

    /**
     * Creates required directories for screenshots and screen recordings.
     */
    private void createTestOutputDirectories() {
        FilesUtils.createDirs(ScreenshotUtils.SCREENSHOTS_PATH);
        FilesUtils.createDirs(ScreenRecorderUtils.RECORDINGS_PATH);
    }
}
