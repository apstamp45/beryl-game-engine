package com.apstamp45.lwjgl_test;

import com.apstamp45.beryl.event.MouseEvent;
import com.apstamp45.beryl.game.GameObject;
import com.apstamp45.beryl.game.components.SpriteRenderer;
import com.apstamp45.beryl.render.Camera;
import com.apstamp45.beryl.render.Renderer;
import com.apstamp45.beryl.game.Transform;
import com.apstamp45.beryl.util.AssetPool;
import com.apstamp45.beryl.window.Scene;
import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * This Scene is used for testing things.
 */
public class TestScene extends Scene {

    @Override
    public void init() {

        // This will run when this Scene is entered

        // Create a Camera
        camera = new Camera(new Vector2f());

        // Create a new Renderer
        renderer = new Renderer();

        // Create the mouse nommer
        GameObject mouseNommer = new GameObject("mouseNommer",
                                                new Transform(new Vector2f(100.0f, 100.0f),
                                                              new Vector2f(50.0f, 50.0f))) {
            @Override
            public void update(float dt) {
                System.out.println(1.0f / dt);
                transform.position.x = (float) MouseEvent.getX() - transform.scale.x / 2.0f;
                transform.position.y = (float) MouseEvent.getY() - transform.scale.y / 2.0f;
            }
        };

        // Add a SpriteRenderer to mouseNommer
        mouseNommer.addComponent(new SpriteRenderer(new Vector4f(0.0f, 0.5f, 0.5f, 1.0f)));

        // Add mouseNommer to the Scene
        addGameObject(mouseNommer);

        // Load the necessary resources
        loadResources();
    }

    private void loadResources() {
        // Load the default shader
        AssetPool.getShader("resources/shaders/default.glsl");
    }

    @Override
    public void update(float dt) {

        // This will run every frame

        // Update all the GameObject/s
        for (GameObject gameObject : gameObjects) {
            gameObject.update(dt);
        }

        // Render the GameObjects
        renderer.render();
    }
}
