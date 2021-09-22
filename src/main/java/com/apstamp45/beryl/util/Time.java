package com.apstamp45.beryl.util;

/**
 * Keeps track of the game's time stuff.
 */
public class Time {

    /**
     * The time at which the game started.
     */
    public static float startTime = System.nanoTime();

    /**
     * Gets the time since the application started
     * in seconds.
     * @return The time since the application started
     * (in seconds).
     */
    public static float getTime() {
        return (System.nanoTime() - startTime) * 1E-9f;
    }
}
