package com.apstamp45.beryl.event;

/**
 * Used to run a function whenever a
 * window size event happens.
 */
public interface WindowSizeEventRunnable {

    /**
     * The function to run.
     * @param width The window's new width.
     * @param height The window's new height.
     */
    public void run(int width, int height);
}
