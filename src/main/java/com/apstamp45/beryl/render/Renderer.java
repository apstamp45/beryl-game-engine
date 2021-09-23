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
     * Renderer/s will be created with this shader
     * as default.
     */
    private static String DEFAULT_SHADER_FILE = null;

    /**
     * How many sprites can each batch hold.
     */
    private final int MAX_BATCH_SIZE;

    /**
     * Stores all the RenderBatch/es.
     */
    private final LinkedList<RenderBatch> renderBatches = new LinkedList<>();

    /**
     * The Renderer's shader file.
     */
    private String shaderFile;

    /**
     * Creates a Renderer.
     */
    public Renderer() {
        MAX_BATCH_SIZE = 1000;
        if (Renderer.DEFAULT_SHADER_FILE == null) {
            throw new IllegalStateException("Default shader not set. "
                                            + "Use Renderer.setDefaultShader() to do so.");
        }
        RenderBatch renderBatch = new RenderBatch(MAX_BATCH_SIZE, DEFAULT_SHADER_FILE);
        renderBatches.add(renderBatch);
    }

    /**
     * Creates a Renderer.
     * @param shaderFile The Renderer's shader.
     */
    public Renderer(String shaderFile) {
        this.shaderFile = shaderFile;
        MAX_BATCH_SIZE = 1000;
        RenderBatch renderBatch = new RenderBatch(MAX_BATCH_SIZE, shaderFile);
        renderBatches.add(renderBatch);
    }

    /**
     * Creates a Renderer.
     * @param maxBatchSize How many SpriteRenderer/s
     * each RenderBatch will hold.
     */
    public Renderer(int maxBatchSize) {
        MAX_BATCH_SIZE = maxBatchSize;
        if (Renderer.DEFAULT_SHADER_FILE == null) {
            throw new IllegalStateException("Default shader not set. "
                    + "Use Renderer.setDefaultShader() to do so.");
        }
        RenderBatch renderBatch = new RenderBatch(MAX_BATCH_SIZE, DEFAULT_SHADER_FILE);
        renderBatches.add(renderBatch);
    }

    /**
     * Creates a Renderer.
     * @param shaderFile The Renderer's shader.
     * @param maxBatchSize How many SpriteRenderer/s
     * each RenderBatch will hold.
     */
    public Renderer(String shaderFile, int maxBatchSize) {
        this.shaderFile = shaderFile;
        MAX_BATCH_SIZE = maxBatchSize;
        RenderBatch renderBatch = new RenderBatch(MAX_BATCH_SIZE, shaderFile);
        renderBatches.add(renderBatch);
    }

    /**
     * Sets the default shader file. This
     * shader will be used to create new
     * Renderers.
     * @param shaderFile The shader file.
     */
    public static void setDefaultShaderFile(String shaderFile) {
        Renderer.DEFAULT_SHADER_FILE = shaderFile;
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
            RenderBatch renderBatch = new RenderBatch(MAX_BATCH_SIZE, shaderFile);
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
