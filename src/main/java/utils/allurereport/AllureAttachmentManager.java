package utils.allurereport;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import utils.FilesUtils;
import utils.LogUtils;
import utils.ScreenRecorderUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static utils.ConfigUtils.getConfigValue;

/**
 * Utility class for attaching files to Allure reports.
 *
 * This class provides methods to attach:
 * - Screenshots
 * - Log files
 * - Test execution videos (screen recordings)
 *
 * All attachments are added to the Allure report for better debugging and traceability.
 */
public class AllureAttachmentManager {

    /**
     * Attaches a screenshot file to the Allure report.
     *
     * @param name the display name of the attachment
     * @param path the path to the screenshot file
     */
    public static void attachScreenshot(String name, String path) {
        try {
            Path screenshot = Path.of(path);
            if (Files.exists(screenshot)) {
                Allure.addAttachment(name, Files.newInputStream(screenshot));
            } else {
                LogUtils.error("Screenshot not found: " + path);
            }
        } catch (Exception e) {
            LogUtils.error("Error attaching screenshot", e.getMessage());
        }
    }

    /**
     * Attaches the main log file to the Allure report.
     *
     * This method:
     * - Shuts down the logger to flush logs
     * - Reads the log file (logs.log) from the logs folder
     * - Reconfigures the logger after attaching
     */
    public static void attachLogs() {
        try {
            LogManager.shutdown();
            File logFile = new File(LogUtils.LOGS_PATH + File.separator + "logs.log");
            ((LoggerContext) LogManager.getContext(false)).reconfigure();
            if (logFile.exists()) {
                Allure.attachment("logs.log", Files.readString(logFile.toPath()));
            }
        } catch (Exception e) {
            LogUtils.error("Error attaching logs", e.getMessage());
        }
    }

    /**
     * Attaches the latest screen recording (video) to the Allure report if enabled.
     *
     * The method checks the "recordTests" config value and attaches the latest .mp4
     * file from the recordings folder.
     */
    public static void attachRecords() {
        if (getConfigValue("recordTests").equalsIgnoreCase("true")) {
            try {
                File record = FilesUtils.getLatestFile(ScreenRecorderUtils.RECORDINGS_PATH);
                if (record != null && record.getName().endsWith(".mp4")) {
                    Allure.addAttachment(
                            "Test Execution Video",
                            "video/mp4",
                            Files.newInputStream(record.toPath()),
                            ".mp4"
                    );
                }
            } catch (Exception e) {
                LogUtils.error("Error attaching records", e.getMessage());
            }
        }
    }
}
