package com.apstamp45.beryl.event;

/**
 * This interface is made to run when a
 * Mouse button event happens.
 */
public interface MouseButtonEventRunnable {

    /**
     * Runs when a mouse button event happens.
     * @param button The mouse button that caused
     * the event.
     * @param action The action that occurred.
     * @param mods Modifier keys that were pressed
     * during the event.
     */
    public void run(int button, int action, int mods);
}
