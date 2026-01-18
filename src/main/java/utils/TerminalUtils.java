package utils;

import java.io.IOException;
import java.util.Arrays;

public class TerminalUtils {

    /**
     * Executes a terminal (command-line) command on the current operating system.
     *
     * This method splits the command into parts (command + arguments)
     * and executes it using Java's Runtime.exec().
     * Logs an error if the command fails or is interrupted.
     *
     * Example usage:
     * executeTerminalCommand("ping", "google.com");
     *
     * @param commandParts the parts of the command to execute (command and arguments)
     */
    public static void executeTerminalCommand(String... commandParts) {
        try {
            Process process = Runtime.getRuntime().exec(commandParts);

            // Wait for the command to complete
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                LogUtils.error("Command failed with exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            LogUtils.error("Failed to execute terminal command: " + Arrays.toString(commandParts), e.getMessage());
        }
    }
}
