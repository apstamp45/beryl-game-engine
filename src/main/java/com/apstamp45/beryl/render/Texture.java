package com.apstamp45.beryl.render;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

/**
 * Handles textures.
 */
public class Texture {

    /**
     * Stores the filepath of the texture.
     */
    private String filepath;

    /**
     * Stores the texture's ID.
     */
    private int textureID;

    /**
     * Shows whether this texture
     * is being used.
     */
    private boolean isBound;

    /**
     * Creates a Texture.
     * @param filepath The path to the texture
     * file.
     */
    public Texture(String filepath) {
        this.filepath = filepath;

        // generate texture
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        isBound = true;

        // set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // load the image
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        // check that the image is working
        if (image != null) {
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0),
                             height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0),
                             height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                System.err.println("ERROR: '" + filepath + "' unrecognized color channel number: "
                                   + channels.get(0));
            }
        } else {
            System.err.println("ERROR: '" + filepath + "' could not load texture");
        }
        stbi_image_free(image);
    }

    /**
     * Binds this texture to the GPU.
     */
    public void bind() {
        if (!isBound) {
            glBindTexture(GL_TEXTURE_2D, textureID);
            isBound = true;
        }
    }

    /**
     * Unbinds this texture.
     */
    public void unBind() {
        glBindTexture(GL_TEXTURE_2D, 0);
        isBound = false;
    }
}
