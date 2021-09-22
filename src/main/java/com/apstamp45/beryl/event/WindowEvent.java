package com.apstamp45.beryl.event;

import com.apstamp45.beryl.window.WindowSize;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * This class handles the window events.
 */
public class WindowEvent extends WindowSize {

    /**
     * Stores the class instance.
     */
    private static WindowEvent windowEvent;

    /**
     * Stores a function that will be run when
     * a window size event occurs.
     */
    private WindowSizeEventRunnable windowSizeEventRunnable;

    /**
     * Creates a WindowEvent.
     */
    private WindowEvent() {}

    /**
     * Gets the class instance.
     * @return The class instance.
     */
    public static WindowEvent get() {
        if (windowEvent == null) {
            windowEvent = new WindowEvent();
        }
        return windowEvent;
    }

    /**
     * Runs when the window's size is changed.
     * @param window The window.
     * @param width The window's new width.
     * @param height The window's new height.
     */
    public static void windowSizeCallback(long window, int width, int height) {
        WindowSize.width = width;
        WindowSize.height = height;
        glViewport(0, 0, width, height);
        if (get().windowSizeEventRunnable != null) {
            get().windowSizeEventRunnable.run(width, height);
        }
    }

    /**
     * Sets the runnable that will be called when a
     * window size event occurs.
     * @param windowSizeEventRunnable The runnable.
     */
    public static void setOnWindowSizeEvent(WindowSizeEventRunnable windowSizeEventRunnable) {
        get().windowSizeEventRunnable = windowSizeEventRunnable;
    }
}
