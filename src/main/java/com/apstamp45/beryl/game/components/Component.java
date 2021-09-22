package com.apstamp45.beryl.game.components;

import com.apstamp45.beryl.game.GameObject;

/**
 * Represents a component of a GameObject.
 */
public abstract class Component {

    /**
     * Stores the parent GameObject.
     */
    public GameObject gameObject;

    /**
     * Runs when the parent GameObject
     * starts.
     */
    public void start() {

    }

    /**
     * Runs on every game loop.
     * @param dt The time that passed in
     * since the last loop.
     */
    public abstract void update(float dt);
}
