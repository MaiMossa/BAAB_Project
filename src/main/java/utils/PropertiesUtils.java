package utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

public class PropertiesUtils {

    // Path to the properties files folder
    private static final String propertiesPath = "src/main/resources/";

    /**
     * Loads all .properties files from the resources folder and merges them into a single Properties object.
     *
     * This method also merges the loaded properties with the system properties,
     * making them accessible via System.getProperties().
     *
     * @return a Properties object containing all loaded properties, or null if loading fails
     */
    public static Properties loadProperties() {
        try {
            Properties properties = new Properties();

            // Get all .properties files in the resources folder recursively
            Collection<File> propertiesFilesList;
            propertiesFilesList = FileUtils.listFiles(new File(propertiesPath), new String[]{"properties"}, true);

            // Load each properties file
            propertiesFilesList.forEach(propertyFile -> {
                try {
                    properties.load(new FileInputStream(propertyFile));
                } catch (IOException ioe) {
                    LogUtils.error(ioe.getMessage());
                }
                // Merge with system properties
                properties.putAll(System.getProperties());
                System.getProperties().putAll(properties);
            });

            LogUtils.info("Loading Properties File Data");
            return properties;

        } catch (Exception e) {
            LogUtils.error("Failed to Load Properties File Data because: " + e.getMessage());
            return null;
        }
    }

    /**
     * Adds or updates a property in the properties file.
     *
     * This method first loads existing properties, sets the new key-value pair,
     * and then writes it back to the properties file.
     *
     * @param key the property key
     * @param value the property value
     * @throws IOException if an error occurs while writing to the file
     */
    public static void AddProperty(String key, String value) throws IOException {
        Properties properties = loadProperties();
        FileOutputStream output = new FileOutputStream(propertiesPath);
        assert properties != null;
        properties.setProperty(key, value);
        properties.store(output, null);
    }
}
