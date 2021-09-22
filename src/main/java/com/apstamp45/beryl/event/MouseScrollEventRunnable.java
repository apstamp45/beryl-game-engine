package com.apstamp45.beryl.event;

/**
 * This interface is made to run a
 * function when a mouse scroll event
 * happens.
 */
public interface MouseScrollEventRunnable {

    /**
     * Runs when a mouse scroll event occurs.
     * @param xOffset The mouse scroll's x offset.
     * @param yOffset The mouse scroll's y offset.
     */
    public void run(double xOffset, double yOffset);
}
