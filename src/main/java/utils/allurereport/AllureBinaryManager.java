package utils.allurereport;

import org.jsoup.Jsoup;
import utils.LogUtils;
import utils.OSUtils;
import utils.TerminalUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility class for managing Allure binaries.
 *
 * Responsibilities:
 * - Resolving the latest Allure version from GitHub.
 * - Downloading the Allure zip file if it does not exist locally.
 * - Extracting the zip to the configured extraction directory.
 * - Providing the path to the Allure executable.
 * - Setting execution permissions for non-Windows OS.
 */
public class AllureBinaryManager {

    /**
     * Lazy initialization holder for resolving the latest Allure version.
     */
    private static class LazyHolder {
        static final String VERSION = resolveVersion();

        /**
         * Resolves the latest Allure version from GitHub releases page.
         *
         * @return latest Allure version as a String
         */
        private static String resolveVersion() {
            try {
                String url = Jsoup.connect("https://github.com/allure-framework/allure2/releases/latest")
                        .followRedirects(true).execute().url().toString();
                LogUtils.info("Resolved Allure version: " + url.split("/tag/")[1]);
                return url.split("/tag/")[1];
            } catch (IOException e) {
                throw new IllegalStateException("Unable to resolve Allure version", e);
            }
        }
    }

    /**
     * Downloads and extracts the Allure binaries if they are not already present.
     *
     * Steps performed:
     * - Checks if binaries already exist.
     * - Downloads the zip file from Maven repository if needed.
     * - Extracts the zip file into the extraction directory.
     * - Sets executable permissions on non-Windows OS.
     * - Deletes the zip file after extraction.
     */
    public static void downloadAndExtract() {
        try {
            String version = LazyHolder.VERSION;
            Path extractionDir = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version);
            if (Files.exists(extractionDir)) {
                LogUtils.info("Allure binaries already exist.");
                return;
            }

            if (!OSUtils.getCurrentOS().equals(OSUtils.OS.WINDOWS)) {
                TerminalUtils.executeTerminalCommand("chmod", "u+x", AllureConstants.USER_DIR.toString());
            }

            Path zipPath = downloadZip(version);
            extractZip(zipPath);

            LogUtils.info("Allure binaries downloaded and extracted.");

            if (!OSUtils.getCurrentOS().equals(OSUtils.OS.WINDOWS)) {
                TerminalUtils.executeTerminalCommand("chmod", "u+x", getExecutable().toString());
            }

            Files.deleteIfExists(Files.list(AllureConstants.EXTRACTION_DIR)
                    .filter(p -> p.toString().endsWith(".zip"))
                    .findFirst()
                    .orElseThrow());

        } catch (Exception e) {
            LogUtils.error("Error downloading or extracting binaries", e.getMessage());
        }
    }

    /**
     * Returns the path to the Allure executable based on the current operating system.
     *
     * @return Path object pointing to the Allure executable
     */
    public static Path getExecutable() {
        String version = LazyHolder.VERSION;
        Path binaryPath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version, "bin", "allure");
        return OSUtils.getCurrentOS() == OSUtils.OS.WINDOWS
                ? binaryPath.resolveSibling(binaryPath.getFileName() + ".bat")
                : binaryPath;
    }

    /**
     * Downloads the Allure zip file for the specified version.
     *
     * @param version the Allure version to download
     * @return Path to the downloaded zip file
     * @throws IOException if downloading fails
     */
    private static Path downloadZip(String version) throws IOException {
        try {
            String url = "https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/" + version + "/allure-commandline-" + version + ".zip";
            Path zipFile = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version + ".zip");
            if (!Files.exists(zipFile)) {
                Files.createDirectories(AllureConstants.EXTRACTION_DIR);
                try (BufferedInputStream in = new BufferedInputStream(new URI(url).toURL().openStream());
                     OutputStream out = Files.newOutputStream(zipFile)) {
                    in.transferTo(out);
                } catch (Exception e) {
                    LogUtils.error("Invalid URL for Allure download: ", e.getMessage());
                }
            }
            return zipFile;
        } catch (Exception e) {
            LogUtils.error("Error downloading Allure zip file", e.getMessage());
            return Paths.get("");
        }
    }

    /**
     * Extracts the contents of a zip file to the extraction directory.
     *
     * @param zipPath Path to the zip file to extract
     * @throws IOException if an error occurs during extraction
     */
    private static void extractZip(Path zipPath) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                Path filePath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), File.separator, entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zipInputStream, filePath);
                }
            }
        } catch (Exception e) {
            LogUtils.error("Error extracting Allure zip file", e.getMessage());
        }
    }
}
