package com.apstamp45.beryl.util;

import com.apstamp45.beryl.render.Shader;
import com.apstamp45.beryl.render.Texture;

import java.io.File;
import java.util.HashMap;

/**
 * Holds onto a static context of
 * all the games resources to make
 * sure all resources are only loaded
 * once.
 */
public class AssetPool {

    /**
     * Stores all the game's shaders.
     */
    private static HashMap<String, Shader> shaders = new HashMap<>();

    /**
     * Stores all the game's textures.
     */
    private static HashMap<String, Texture> textures = new HashMap<>();

    /**
     * Returns the reference to the Shader at
     * the specified filepath, or, if none was
     * created, creates one.
     * @param filepath The path to the shader.
     * @return The Shader.
     */
    public static Shader getShader(String filepath) {
        File file = new File(filepath);
        String absolutePath = file.getAbsolutePath();
        if (shaders.containsKey(absolutePath)) {
            return shaders.get(absolutePath);
        } else {
            Shader shader = new Shader(filepath);
            shader.compile();
            shaders.put(absolutePath, shader);
            return shader;
        }
    }

    /**
     * Returns the reference to the Texture at
     * the specified filepath, or, if none was
     * created, creates one.
     * @param filepath The path to the texture.
     * @return The Texture.
     */
    public static Texture getTexture(String filepath) {
        File file = new File(filepath);
        String absolutePath = file.getAbsolutePath();
        if (textures.containsKey(absolutePath)) {
            return textures.get(absolutePath);
        } else {
            Texture texture = new Texture(filepath);
            textures.put(absolutePath, texture);
            return texture;
        }
    }
}
