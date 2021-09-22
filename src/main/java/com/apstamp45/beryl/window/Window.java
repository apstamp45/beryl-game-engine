package com.apstamp45.beryl.window;

import com.apstamp45.beryl.event.KeyEvent;
import com.apstamp45.beryl.event.MouseEvent;

import com.apstamp45.beryl.event.WindowEvent;
import com.apstamp45.beryl.util.SystemInfo;
import com.apstamp45.beryl.util.Time;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * This class handles all the window stuff.
 */
public class Window extends WindowSize {

    /**
     * This String stores the window's title.
     */
    private String title;

    /**
     * This stores the glfw window's memory address.
     */
    private long glfwWindow;

    /**
     * This Window stores the instance of the
     * Window class.
     */
    private static Window window;

    /**
     * This scene is the one that is currently
     * running in the window.
     */
    private Scene currentScene;

    /**
     * This constructor creates an instance of the
     * Window class (duh).
     * @param width The window's width.
     * @param height The window's height.
     * @param title The window's title.
     */
    private Window(int width, int height, String title) {

        // Get system info
        SystemInfo.getSystemInfo();
        WindowSize.width = width;
        WindowSize.height = height;
        this.title = title;
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window(1960, 1080, "Beryl");
        }
        return Window.window;
    }

    /**
     * This function creates a Window if one was not
     * already created, and returns the instance.
     * @param width The window's width.
     * @param height The window's height.
     * @param title The window's title.
     * @return this.window
     */
    public static Window get(int width, int height, String title) {
        if (Window.window == null) {
            Window.window = new Window(width, height, title);
        }
        return Window.window;
    }

    /**
     * This function runs the window.
     * @param scene The scene to begin running.
     */
    public void run(Scene scene) {
        currentScene = scene;
        init();
        loop();
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    /**
     * Initializes the Window.
     */
    private void init() {

        // Set GLFW error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Make sure GLFW is running correctly
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Set GLFW window hints
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the GLFW window
        glfwWindow = glfwCreateWindow(window.width, window.height, window.title, NULL, NULL);

        // Make sure the window is running correctly
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Window could not be created.");
        }

        // Set the event listeners
        glfwSetCursorPosCallback(glfwWindow, MouseEvent::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseEvent::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseEvent::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyEvent::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, WindowEvent::windowSizeCallback);

        glfwMakeContextCurrent(glfwWindow);

        // Enable V-Sync
        glfwSwapInterval(1);

        // Show the window
        glfwShowWindow(glfwWindow);

        // Make stuff work
        GL.createCapabilities();

        // Run the scene initialization.
        get().currentScene.init();
        get().currentScene.start();
    }

    /**
     * Runs until the window closes.
     */
    private void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            if (dt >= 0) {
                get().currentScene.update(dt);
            }
            glfwSwapBuffers(glfwWindow);
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    /**
     * Sets the current scene.
     * @param newScene The new Scene.
     */
    public static void changeScene(Scene newScene) {
        get().currentScene = newScene;
        get().currentScene.init();
    }

    /**
     * Gets the Window's current scene.
     * @return The Window's current scene.
     */
    public static Scene getCurrentScene() {
        return get().currentScene;
    }

    /**
     * Gets the window's width.
     * @return The window's width.
     */
    public static int getWidth() {
        return WindowSize.width;
    }

    /**
     * Gets the window's height.
     * @return The window's height.
     */
    public static int getHeight() {
        return WindowSize.height;
    }
}
