package com.apstamp45.beryl.render;

import com.apstamp45.beryl.game.components.SpriteRenderer;
import com.apstamp45.beryl.util.AssetPool;
import com.apstamp45.beryl.window.Window;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * A RenderBatch contains a list of
 * SpriteRenderer/s to be drawn to the
 * window by the Renderer.
 */
public class RenderBatch {

    /**
     * How many position values there are
     * per vertex.
     */
    private final int POS_SIZE = 2;

    /**
     * How many color values there are
     * per vertex.
     */
    private final int COLOR_SIZE = 4;

    /**
     * The offset value of the position
     * values.
     */
    private final int POS_OFFSET = 0;

    /**
     * The offset value of the color
     * values.
     */
    private final int COLOR_OFFSET = (POS_OFFSET + POS_SIZE) * Float.BYTES;

    /**
     * the total number of float values per vertex.
     */
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;

    /**
     * The total number of bytes per vertex.
     */
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    /**
     * Stores all the SpriteRenderers.
     */
    private SpriteRenderer[] spriteRenderers;

    /**
     * States whether this RenderBatch
     * currently has room for more
     * SpriteRenderer/s.
     */
    private boolean hasRoom = true;

    /**
     * The number of SpriteRenderer/s currently
     * being stored in spriteRenderers.
     */
    private int spriteRenderers$numberOfSprites = 0;

    /**
     * Stores all the vertex data of all
     * the sprites being drawn.
     */
    private float[] vertices;

    /**
     * The VAO ID.
     */
    private int vaoID;

    /**
     * The VBO ID.
     */
    private int vboID;

    /**
     * The shader to be used to draw the
     * sprites.
     */
    private Shader shader;

    /**
     * The max number of sprites that this
     * RenderBatch can render.
     */
    private final int maxBatchSize;

    /**
     * Creates a new RendererBatch.
     * @param maxBatchSize The max amount
     * of that can be drawn using this
     * RendererBatch.
     */
    public RenderBatch(int maxBatchSize, String shaderFile) {
        shader = AssetPool.getShader(shaderFile);
        this.maxBatchSize = maxBatchSize;
        spriteRenderers = new SpriteRenderer[maxBatchSize];
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        start();
    }

    /**
     * Runs when the batch is created.
     */
    public void start() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
        int[] indices = generateIndices();
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(indices.length);
        elementBuffer.put(indices).flip();
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    /**
     * Renders the batch.
     */
    public void render() {
        for (int i = 0; i < spriteRenderers$numberOfSprites; i++) {
            loadVertexProperties(i);
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        shader.use();
        shader.uploadMat4f("uProjection",
                           Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView",
                           Window.getCurrentScene().getCamera().getViewMatrix());
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, spriteRenderers$numberOfSprites * 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }

    /**
     * Generates the element array for the
     * given amount of sprites.
     * @return The element array.
     */
    private int[] generateIndices() {
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    /**
     * Sets the individual vertices based on
     * the index of the vertex.
     * @param elements The element array.
     * @param index The index of the current
     * vertex.
     */
    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    /**
     * Adds a SpriteRenderer to the array.
     * @param spriteRenderer The SpriteRenderer
     * to add.
     */
    public void addSpriteRenderer(SpriteRenderer spriteRenderer) {
        int index = spriteRenderers$numberOfSprites;
        spriteRenderers[index] = spriteRenderer;
        spriteRenderers$numberOfSprites++;
        loadVertexProperties(index);
        if (spriteRenderers$numberOfSprites >= maxBatchSize) {
            this.hasRoom = false;
        }
    }

    /**
     * Loads the vertex properties of the
     * given index.
     * @param index The index.
     */
    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = spriteRenderers[index];
        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = sprite.getColor();
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;
            offset += VERTEX_SIZE;
        }
    }

    /**
     * Sets the RenderBatch/es Shader.
     * @param shader The Shader.
     */
    public void setShader(Shader shader) {
        this.shader = shader;
    }

    /**
     * Tells whether this RenderBatch has
     * room for more SpriteRenderer/s.
     * @return this.hasRoom.
     */
    public boolean hasRoom() {
        return hasRoom;
    }
}
