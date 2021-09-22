package com.apstamp45.lwjgl_test;

import com.apstamp45.beryl.window.Window;

/**
 * This program tests out my game engine,
 * Beryl.
 */
public class Main {
    public static void main(String[] args) {

        // Creates a window with the size of 800x500 pixels, and
        // title as "Test"
        Window window = Window.get(800, 500, "Test");

        // Runs the window.
        window.run(new TestScene());
    }
}
