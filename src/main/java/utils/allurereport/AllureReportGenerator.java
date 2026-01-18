package utils.allurereport;

import org.apache.commons.io.FileUtils;
import utils.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static utils.ConfigUtils.getConfigValue;
import static utils.allurereport.AllureConstants.HISTORY_FOLDER;
import static utils.allurereport.AllureConstants.RESULTS_HISTORY_FOLDER;

/**
 * Utility class for generating, renaming, opening, and managing Allure reports.
 *
 * Responsibilities:
 * - Generating Allure reports (single-file or full report)
 * - Renaming generated reports with timestamp
 * - Opening the report automatically after generation
 * - Copying historical data to results folder
 */
public class AllureReportGenerator {

    /**
     * Generates the Allure report.
     *
     * @param isSingleFile if true, generates a single HTML file; otherwise, full report folder
     */
    public static void generateReports(boolean isSingleFile) {
        Path outputFolder = isSingleFile ? AllureConstants.REPORT_PATH : AllureConstants.FULL_REPORT_PATH;
        List<String> command = new ArrayList<>(List.of(
                AllureBinaryManager.getExecutable().toString(),
                "generate",
                AllureConstants.RESULTS_FOLDER.toString(),
                "-o", outputFolder.toString(),
                "--clean"
        ));
        if (isSingleFile) command.add("--single-file");
        TerminalUtils.executeTerminalCommand(command.toArray(new String[0]));
    }

    /**
     * Renames the generated Allure report file with a timestamp.
     *
     * @return the new report file name
     */
    public static String renameReport() {
        String newFileName = AllureConstants.REPORT_PREFIX + TimeUtils.getTimestamp() + AllureConstants.REPORT_EXTENSION;
        FilesUtils.renameFile(AllureConstants.REPORT_PATH.resolve(AllureConstants.INDEX_HTML).toString(), newFileName);
        return newFileName;
    }

    /**
     * Opens the Allure report automatically based on the OS.
     *
     * Supports:
     * - Windows (cmd start)
     * - Mac/Linux (open command)
     *
     * @param reportFileName the report file to open
     */
    public static void openReport(String reportFileName) {
        if (!getConfigValue("OpenAllureReportAfterExecution").equalsIgnoreCase("true")) return;

        Path reportPath = AllureConstants.REPORT_PATH.resolve(reportFileName);
        switch (OSUtils.getCurrentOS()) {
            case WINDOWS -> TerminalUtils.executeTerminalCommand("cmd.exe", "/c", "start", reportPath.toString());
            case MAC, LINUX -> TerminalUtils.executeTerminalCommand("open", reportPath.toString());
            default -> LogUtils.warn("Opening Allure Report is not supported on this OS.");
        }
    }

    /**
     * Copies the historical Allure report data from full-report history to results history folder.
     */
    public static void copyHistory() {
        try {
            FileUtils.copyDirectory(HISTORY_FOLDER.toFile(), RESULTS_HISTORY_FOLDER.toFile());
        } catch (Exception e) {
            LogUtils.error("Error copying history files", e.getMessage());
        }
    }
}
