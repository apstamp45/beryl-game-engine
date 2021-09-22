package com.apstamp45.beryl.render;

import org.joml.Vector2f;

/**
 * The Position and size of the
 * GameObject.
 */
public class Transform {

    /**
     * The GameObject's position.
     */
    public Vector2f position;

    /**
     * The GameObject's size.
     */
    public Vector2f scale;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    /**
     * Creates a Transform at the
     * specified position.
     * @param position The position.
     */
    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    /**
     * Creates a Transform with the specified
     * position and size.
     * @param position The position.
     * @param scale The size.
     */
    public Transform(Vector2f position, Vector2f scale) {
        init (position, scale);
    }

    /**
     * Sets the Transform's fields.
     * @param position The position.
     * @param scale The size.
     */
    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }
}
