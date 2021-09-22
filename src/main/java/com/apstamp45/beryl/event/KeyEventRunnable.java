package com.apstamp45.beryl.event;

/**
 * Used to run a function when
 * a key event happens.
 */
public interface KeyEventRunnable {

    /**
     * The function to run.
     * @param key The code of the key that was pressed.
     * @param scancode The scancode (whatever that is).
     * @param action The action that happened.
     * @param mods Modifier keys that were pressed
     * during the event.
     */
    public void run(int key, int scancode, int action, int mods);
}
