package com.apstamp45.beryl.event;

import com.apstamp45.beryl.window.Window;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * This class handles all the
 * window's mouse events.
 */
public class MouseEvent {

    /**
     * The class instance.
     */
    private static MouseEvent mouseEvent;

    /**
     * The scroll wheel's x position.
     */
    private double scrollX;

    /**
     * The scroll wheel's y position.
     */
    private double scrollY;

    /**
     * The mouse's x position.
     */
    private double xPos;

    /**
     * The mouse's y position.
     */
    private double yPos;

    /**
     * The mouse's last x position.
     */
    private double lastX;

    /**
     * The mouse's last y position.
     */
    private double lastY;

    /**
     * Stores all the mouse buttons' pressed status.
     */
    private boolean[] mouseButtonPressed = new boolean[3];

    /**
     * Shows weather or not the mouse is dragging.
     */
    private boolean isDragging;

    /**
     * Stores a function that will run on a
     * mouse position change event.
     */
    private MousePosEventRunnable mousePosEventRunnable;

    /**
     * Stores a function that will run on a
     * mouse scroll event.
     */
    private MouseScrollEventRunnable mouseScrollEventRunnable;

    /**
     * Stores a function that will run on a
     * mouse button event.
     */
    private MouseButtonEventRunnable mouseButtonEventRunnable;

    /**
     * Create a MouseListener.
     */
    private MouseEvent() {
        scrollX = 0.0;
        scrollY = 0.0;
        xPos = 0.0;
        yPos = 0.0;
        lastX = 0.0;
        lastY = 0.0;
    }

    /**
     * Creates an instance of MouseListener
     * if none was created already, then
     * returns the instance.
     * @return MouseListener.mouseListener
     */
    public static MouseEvent get() {
        if (mouseEvent == null) {
            MouseEvent.mouseEvent = new MouseEvent();
        }
        return MouseEvent.mouseEvent;
    }

    /**
     * Runs whenever the mouse moves.
     * @param window The window that the event happened in.
     * @param xPos The mouse's new x position.
     * @param yPos The mouse's new y position.
     */
    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = Window.getHeight() - (float) yPos;
        get().isDragging = get().mouseButtonPressed[0] ||
                get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
        if (get().mousePosEventRunnable != null) {
            get().mousePosEventRunnable.run(xPos, yPos);
        }
    }

    /**
     * Handles the mouse button events.
     * @param window The window that the event happened in.
     * @param button The button that caused the action.
     * @param action The action that happened.
     * @param mods The moderator keys that were pressed
     * during the event.
     */
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button >= get().mouseButtonPressed.length) {
            System.err.println("That mouse button is not being supported by Beryl.");
        }
        switch (action) {
            case GLFW_PRESS:
                get().mouseButtonPressed[button] = true;
                break;
            case GLFW_RELEASE:
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
                break;
        }
        if (get().mouseButtonEventRunnable != null) {
            get().mouseButtonEventRunnable.run(button, action, mods);
        }
    }

    /**
     * Handles mouse scroll actions.
     * @param window The window that he action occurs.
     * @param xOffset The scroll wheel's x offset.
     * @param yOffset The scroll wheel's y offset.
     */
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
        if (get().mouseScrollEventRunnable != null) {
            get().mouseScrollEventRunnable.run(xOffset, yOffset);
        }
    }

    /**
     * Resets the scroll x and y, and
     * resets the last x and y fields.
     */
    public static void endFrame() {
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    /**
     * Gets the mouse's x position.
     * @return The mouse's x position.
     */
    public static double getX() {
        return get().xPos;
    }

    /**
     * Gets the mouse's y position (bottom
     * is 0).
     * @return The mouse's y position.
     */
    public static double getY() {
        return get().yPos;
    }

    /**
     * Returns the change in the mouse's x
     * position over the last frame.
     * @return The change in the mouse's x
     * position over the last frame.
     */
    public static double getDx() {
        return get().lastX - get().xPos;
    }

    /**
     * Returns the change in the mouse's y
     * position over the last frame.
     * @return The change in the mouse's y
     * position over the last frame.
     */
    public static double getDy() {
        return get().lastY - get().yPos;
    }

    /**
     * Gets the scroll's x offset.
     * @return The scroll's x offset.
     */
    public static double getScrollX() {
        return get().scrollX;
    }

    /**
     * Gets the scroll's y offset.
     * @return The scroll's y offset.
     */
    public static double getScrollY() {
        return get().scrollY;
    }

    /**
     * Returns wether or not the mouse is dragging.
     * @return The mouse's dragging status.
     */
    public static boolean isDragging() {
        return get().isDragging;
    }

    /**
     * Returns weather or not a specified button
     * is pressed.
     * @param button The button to check.
     * @return Weather or not the specified
     * button is pressed.
     */
    public static boolean buttonIsPressed(int button) {
        if (button >= get().mouseButtonPressed.length) {
            System.err.println("Could not check if that button " +
                    "was pressed (out of bounds).");
        }
        return get().mouseButtonPressed[button];
    }

    /**
     * Sets a runnable that will run a function
     * when a mouse position event happens.
     * @param runnable The runnable.
     */
    public static void setOnMousePosEvent(MousePosEventRunnable runnable) {
        get().mousePosEventRunnable = runnable;
    }

    /**
     * Sets a runnable that will run a function
     * when a mouse button event happens.
     * @param runnable The runnable.
     */
    public static void setOnMouseButtonEvent(MouseButtonEventRunnable runnable) {
        get().mouseButtonEventRunnable = runnable;
    }

    /**
     * Sets a runnable that will run a function
     * when a mouse scroll event happens.
     * @param runnable The runnable.
     */
    public static void setOnMouseScrollEvent(MouseScrollEventRunnable runnable) {
        get().mouseScrollEventRunnable = runnable;
    }
}
