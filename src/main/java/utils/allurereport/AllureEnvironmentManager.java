package utils.allurereport;

import com.google.common.collect.ImmutableMap;
import utils.LogUtils;

import java.io.File;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;
import static utils.ConfigUtils.getConfigValue;

/**
 * Utility class for setting Allure environment variables.
 *
 * This class sets up environment details that will appear in the Allure report
 * under the "Environment" tab. It also ensures that the Allure CLI binaries are
 * downloaded and extracted for report generation.
 */
public class AllureEnvironmentManager {

    /**
     * Sets the Allure environment variables such as:
     * - OS
     * - Java version
     * - Browser
     * - Execution type
     * - Base URL
     *
     * This method also triggers the download and extraction of Allure CLI binaries.
     */
    public static void setEnvironmentVariables() {
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("OS", getConfigValue("os.name"))
                        .put("Java version:", getConfigValue("java.runtime.version"))
                        .put("Browser", getConfigValue("browserType"))
                        .put("Execution Type", getConfigValue("executionType"))
                        .put("URL", getConfigValue("baseUrlWeb"))
                        .build(),
                String.valueOf(AllureConstants.RESULTS_FOLDER) + File.separator
        );
        LogUtils.info("Allure environment variables set.");
        AllureBinaryManager.downloadAndExtract();
    }
}
