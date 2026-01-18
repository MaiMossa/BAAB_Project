package utils.allurereport;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static utils.ConfigUtils.getConfigValue;

/**
 * This class contains all constants used for Allure reporting in the framework.
 *
 * It defines:
 * - Paths for results, reports, history, and extraction directories
 * - Naming conventions for reports and attachments
 * - URLs and directories for Allure binaries
 * - Video attachment constants
 */
public class AllureConstants {

    /**
     * Path to the user working directory (project root).
     */
    public static final Path USER_DIR = Paths.get(getConfigValue("user.dir"), File.separator);

    /**
     * Path to the user's home directory.
     */
    public static final Path USER_HOME = Paths.get(getConfigValue("user.home"), File.separator);

    /**
     * Folder where Allure test results (XML/JSON) are stored.
     */
    public static final Path RESULTS_FOLDER = Paths.get(String.valueOf(USER_DIR), "test-outputs", "allure-results", File.separator);

    /**
     * Folder where Allure reports are generated.
     */
    public static final Path REPORT_PATH = Paths.get(String.valueOf(USER_DIR), "test-outputs", "reports", File.separator);

    /**
     * Folder for the full Allure report including history.
     */
    public static final Path FULL_REPORT_PATH = Paths.get(String.valueOf(USER_DIR), "test-outputs", "full-report", File.separator);

    /**
     * Folder for storing historical data in full reports.
     */
    public static final Path HISTORY_FOLDER = Paths.get(FULL_REPORT_PATH.toString(), "history", File.separator);

    /**
     * Folder for storing historical data in results folder.
     */
    public static final Path RESULTS_HISTORY_FOLDER = Paths.get(RESULTS_FOLDER.toString(), "history", File.separator);

    /**
     * Default index HTML file name for Allure reports.
     */
    public static final String INDEX_HTML = "index.html";

    /**
     * Prefix used for Allure report file names.
     */
    public static final String REPORT_PREFIX = "AllureReport_";

    /**
     * Extension used for Allure report files.
     */
    public static final String REPORT_EXTENSION = ".html";

    /**
     * Default display name for video attachments in Allure reports.
     */
    public static final String VIDEO_ATTACHMENT_NAME = "Test Execution Video";

    /**
     * MIME type for video attachments.
     */
    public static final String VIDEO_MIME_TYPE = "video/mp4";

    /**
     * File extension for video attachments.
     */
    public static final String VIDEO_EXTENSION = ".mp4";

    /**
     * Base URL to download Allure CLI zip files.
     */
    public static final String ALLURE_ZIP_BASE_URL = "https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/";

    /**
     * Local directory to store downloaded Allure zip files.
     */
    public static final String ALLURE_LOCAL_ZIP_DIR = "src/main/resources/allure";

    /**
     * Directory where Allure binaries will be extracted.
     */
    public static final Path EXTRACTION_DIR = Paths.get(String.valueOf(USER_HOME), ".m2/repository/allure", File.separator);
}
