package com.apstamp45.beryl.event;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * This class handles all the keyboard events.
 */
public class KeyEvent {

    /**
     * The class instance.
     */
    private static KeyEvent keyEvent;

    /**
     * Stores the states of all the key bindings
     * in glfw.
     */
    private boolean[] keyPressed = new boolean[350];

    /**
     * Stores a function that will be run on a key event.
     */
    private KeyEventRunnable keyEventRunnable;

    /**
     * Creates a KeyEvent.
     */
    private KeyEvent() {}

    /**
     * Gets the class instance. If none
     * was created, yet, it creates one.
     * @return The class instance.
     */
    public static KeyEvent get() {
        if (KeyEvent.keyEvent == null) {
            KeyEvent.keyEvent = new KeyEvent();
        }
        return KeyEvent.keyEvent;
    }

    /**
     * Handles the key events.
     * @param window The window in which the event happened.
     * @param key The key that caused the action.
     * @param scancode IDK.
     * @param action The key event action code.
     * @param mods Moderator keys that were pressed when the
     * action occurred.
     */
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key >= get().keyPressed.length) {
            System.err.println("Key code not recognized.");
            return;
        }
        switch (action) {
            case GLFW_PRESS:
                get().keyPressed[key] = true;
                break;
            case GLFW_RELEASE:
                get().keyPressed[key] = false;
                break;
        }
        if (get().keyEventRunnable != null) {
            get().keyEventRunnable.run(key, scancode, action, mods);
        }
    }

    /**
     * Gets the status of the specified key.
     * @param key The key to check.
     * @return The key's status.
     */
    public static boolean isKeyPressed(int key) {
        if (key >= get().keyPressed.length) {
            System.err.println("Key code not recognized.");
            return false;
        }
        return get().keyPressed[key];
    }

    /**
     * Sets the function that will be run on a key event.
     * @param runnable The runnable.
     */
    public static void setOnKeyEvent(KeyEventRunnable runnable) {
        get().keyEventRunnable = runnable;
    }
}
