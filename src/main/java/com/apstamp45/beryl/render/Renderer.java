package com.apstamp45.beryl.render;

import com.apstamp45.beryl.game.GameObject;
import com.apstamp45.beryl.game.components.SpriteRenderer;

import java.util.LinkedList;

/**
 * Renders the RenderBatch/es to the
 * window.
 */
public class Renderer {

    /**
     * How many sprites can each batch hold.
     */
    private final int MAX_BATCH_SIZE = 1000;

    /**
     * Stores all the RenderBatch/es.
     */
    private LinkedList<RenderBatch> renderBatches = new LinkedList<>();

    public Renderer() {
        RenderBatch renderBatch = new RenderBatch(MAX_BATCH_SIZE);
        renderBatches.add(renderBatch);
    }

    /**
     * Render the batches.
     */
    public void render() {
        for (RenderBatch renderBatch : renderBatches) {
            renderBatch.render();
        }
    }

    /**
     * Adds a RenderBatch to the list.
     * @param gameObject The RenderBatch.
     */
    public void add(GameObject gameObject) {
        SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        if (spriteRenderer != null) {
            add(spriteRenderer);
        }
    }

    /**
     * Adds a SpriteRenderer to the
     * current BatchRenderer.
     * @param spriteRenderer The
     * SpriteRenderer to add.
     */
    private void add(SpriteRenderer spriteRenderer) {
        boolean added = false;
        for (RenderBatch batch : renderBatches) {
            if (batch.hasRoom()) {
                batch.addSpriteRenderer(spriteRenderer);
                added = true;
                break;
            }
        }
        if (!added) {
            RenderBatch renderBatch = new RenderBatch(MAX_BATCH_SIZE);
            renderBatch.start();
            renderBatches.add(renderBatch);
            renderBatch.addSpriteRenderer(spriteRenderer);
        }
    }

    /**
     * Sets the Shader that will be used
     * to draw all the RenderBatch/es.
     * @param shaderFile The path to the
     * shader file.
     */
    public void setShader(String shaderFile) {
        Shader shader = new Shader(shaderFile);
        shader.compile();
        for (RenderBatch renderBatch : renderBatches) {
            renderBatch.setShader(shader);
        }
    }
}
