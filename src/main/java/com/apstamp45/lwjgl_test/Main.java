package com.apstamp45.lwjgl_test;

import com.apstamp45.beryl.window.Window;

/**
 * This program tests out my game engine,
 * Beryl.
 */
public class Main {
    public static void main(String[] args) {
        Window window = Window.get(800, 500, "Test");
        window.run(new TestScene());
    }
}
