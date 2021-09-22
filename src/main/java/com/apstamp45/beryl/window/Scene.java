package com.apstamp45.beryl.window;

import com.apstamp45.beryl.game.GameObject;
import com.apstamp45.beryl.render.Camera;
import com.apstamp45.beryl.render.Renderer;

import java.util.ArrayList;

/**
 * This interface defines a scene.
 */
public abstract class Scene {

    /**
     * The Scene's Renderer.
     */
    protected Renderer renderer;

    /**
     * The scene's camera.
     */
    protected Camera camera;

    /**
     * Specifies whether the Scene is running.
     */
    private boolean isRunning = false;

    /**
     * Stores all the GameObjects in this Scene.
     */
    public ArrayList<GameObject> gameObjects = new ArrayList<>();

    /**
     * This function will run when the Scene was created.
     */
    public abstract void init();

    /**
     * Runs when this Scene is entered.
     */
    public void start() {
        renderer = new Renderer();
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            renderer.add(gameObject);
        }
        isRunning = true;
    }

    /**
     * Adds a GameObject to the Scene.
     * @param gameObject The GameObject.
     */
    public void addGameObject(GameObject gameObject) {
        if (!isRunning) {
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.start();
            renderer.add(gameObject);
        }
    }

    /**
     * This function will run every frame, as long as this
     * scene is selected.
     * @param dt The time that elapsed in the last frame.
     */
    public abstract void update(float dt);

    /**
     * Gets this Scene's Camera.
     * @return This Scene's Camera.
     */
    public Camera getCamera() {
        return camera;
    }
}
