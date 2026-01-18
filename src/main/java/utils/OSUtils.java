package utils;

public class OSUtils {

    /**
     * Enum representing the main types of operating systems.
     */
    public enum OS {
        WINDOWS, // Microsoft Windows
        MAC,     // macOS
        LINUX,   // Linux or Unix
        OTHER    // Any other OS
    }

    /**
     * Detects the current operating system of the machine.
     *
     * This method reads the "os.name" system property,
     * converts it to lowercase, and matches it to the OS enum.
     *
     * @return the current operating system as an OS enum value
     */
    public static OS getCurrentOS() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) return OS.WINDOWS;
        if (os.contains("mac")) return OS.MAC;
        if (os.contains("nix") || os.contains("nux")) return OS.LINUX;

        return OS.OTHER;
    }
}
