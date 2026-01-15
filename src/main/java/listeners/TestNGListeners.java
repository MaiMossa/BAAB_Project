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

public class TestNGListeners implements IExecutionListener, IInvokedMethodListener, ITestListener, ISuiteListener {

    private final File screenshots = new File("test-outputs/screenshots");
    private final File recordings = new File("test-outputs/recordings");
    private final File logs = new File("test-outputs/Logs");

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

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            ScreenRecorderUtils.startRecording();
            LogUtils.info("Test Case " + testResult.getName() + " started");
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        WebDriver driver = null;

        if (method.isTestMethod()) {
            // Stop recording and get the video file
            File videoFile = ScreenRecorderUtils.stopRecording(result.getName());

            // Attach video to Allure
            if (videoFile != null && videoFile.exists()) {
                AllureAttachmentManager.attachRecords();
            }

            // Soft assertions
            SoftAssertions.AssertIfUsed();

            if (result.getInstance() instanceof WebDriverProvider provider)
                driver = provider.getWebDriver();

            switch (result.getStatus()) {
                case ITestResult.FAILURE -> ScreenshotUtils.takeScreenShot(driver, "failed-" + result.getName());
                case ITestResult.SUCCESS -> ScreenshotUtils.takeScreenShot(driver, "passed-" + result.getName());
                case ITestResult.SKIP -> ScreenshotUtils.takeScreenShot(driver, "skipped-" + result.getName());
            }

            AllureAttachmentManager.attachLogs();
        } else if (method.isConfigurationMethod()) {
            // For Configuration Methods: Log Only
            switch (result.getStatus()) {
                case ITestResult.FAILURE -> LogUtils.info("Configuration Method ", result.getName(), "failed");
                case ITestResult.SUCCESS -> LogUtils.info("Configuration Method ", result.getName(), "passed");
                case ITestResult.SKIP -> LogUtils.info("Configuration Method ", result.getName(), "skipped");
            }
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtils.info("Test case", result.getName(), "passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogUtils.info("Test case", result.getName(), "failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtils.info("Test case", result.getName(), "skipped");
    }

    @Override
    public void onFinish(ISuite suite) { }

    private void cleanTestOutputDirectories() {
        FilesUtils.cleanDirectory(AllureConstants.RESULTS_FOLDER.toFile());
        FilesUtils.cleanDirectory(screenshots);
        FilesUtils.cleanDirectory(recordings);
        FilesUtils.cleanDirectory(logs);
    }

    private void createTestOutputDirectories() {
        FilesUtils.createDirs(ScreenshotUtils.SCREENSHOTS_PATH);
        FilesUtils.createDirs(ScreenRecorderUtils.RECORDINGS_PATH);
    }
}
