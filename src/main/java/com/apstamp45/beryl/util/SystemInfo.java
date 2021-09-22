package com.apstamp45.beryl.util;

import java.awt.*;

/**
 * This class handles anything that
 * is OS-specific.
 */
public class SystemInfo {

    /**
     * Shows what operating system the program
     * is running on.
     */
    public static String os;

    /**
     * Specifies the end of line character/s used
     * in the current OS.
     */
    public static String endOfLine;

    /**
     * Specifies the file path separator used
     * in the current OS.
     */
    public static String pathSeparator;

    /**
     * Specifies the file path start in the
     * current OS.
     */
    public static String pathStarter;

    /**
     * The computer monitor's screen width.
     */
    public static float screenWidth;

    /**
     * The computer monitor's screen height.
     */
    public static float screenHeight;

    /**
     * Gets the system's info.
     */
    public static void getSystemInfo() {

        // Get OS information
        os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            pathSeparator = "\\";
            pathStarter = "C:\\";
            endOfLine = "\r\n";
        } else if (os.contains("osx") || os.contains("nix") || os.contains("aix") || os.contains("nux")) {
            pathSeparator = "/";
            pathStarter = "/";
            endOfLine = "\n";
        }

        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
    }
}
