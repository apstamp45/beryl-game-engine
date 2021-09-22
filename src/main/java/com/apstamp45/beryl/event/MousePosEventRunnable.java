package com.apstamp45.beryl.event;

/**
 * This interface is made to run a
 * function when a mouse position
 * event occurs.
 */
public interface MousePosEventRunnable {

    /**
     * Runs when a mouse position event
     * occurs.
     * @param xPos The cursor's new x position.
     * @param yPos The cursor's new y position.
     */
    public void run(double xPos, double yPos);
}
