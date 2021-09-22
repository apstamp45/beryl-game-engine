package com.apstamp45.beryl.render;

import com.apstamp45.beryl.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Is used to have a view perspective
 * in the game.
 */
public class Camera {

    /**
     * The camera's projection matrix (IDK).
     */
    private Matrix4f projectionMatrix;

    /**
     * The camera's view matrix.
     */
    private Matrix4f viewMatrix;

    /**
     * The camera's position.
     */
    private Vector2f position;

    /**
     * Creates a Camera.
     * @param position The camera's position.
     */
    public Camera(Vector2f position) {
        this.position = position;
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        adjustProjection();
    }

    /**
     * Sets the camera's projection matrix.
     */
    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, Window.getWidth(), 0.0f,
                Window.getHeight(),0.0f, 100.0f);
    }

    /**
     * Sets and returns the camera's view matrix.
     * @return The camera's view matrix.
     */
    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f), cameraUp);
        return viewMatrix;
    }

    /**
     * Gets the camera's projection matrix.
     * @return The camera's projection matrix.
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Sets the position of the camera.
     * @param x The camera's new x position.
     * @param y The camera's new y position.
     */
    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }
}
