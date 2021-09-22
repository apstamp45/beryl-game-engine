package com.apstamp45.beryl.render;

import com.apstamp45.beryl.util.SystemInfo;

import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

/**
 * This class handles the GLSL shaders.
 */
public class Shader {

    /**
     * Stores the shader program.
     */
    private int shaderProgramID;

    /**
     * Stores the source of the vertex shader.
     */
    private String vertexSource;

    /**
     * Stores the source of the fragmentShader.
     */
    private String fragmentSource;

    /**
     * Stores the filepath of the shader.
     */
    private final String filepath;

    private boolean isBeingUsed = false;

    /**
     * Creates a shader with the specified shader file.
     * @param filepath The path to the shader.
     */
    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitSource = source.split("#type( )+([a-zA-Z])+");

            int index = source.indexOf("#type") + 6;
            int endOfLine = source.indexOf(SystemInfo.endOfLine, index);
            String firstSource = source.substring(index, endOfLine).trim();

            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf(SystemInfo.endOfLine, index);
            String secondSource = source.substring(index, endOfLine).trim();

            if (firstSource.equals("vertex")) {
                vertexSource = splitSource[1];
            } else if (firstSource.equals("fragment")) {
                fragmentSource = splitSource[1];
            } else {
                throw new IOException("Unexpected token '" + firstSource + "'");
            }

            if (secondSource.equals("vertex")) {
                vertexSource = splitSource[2];
            } else if (secondSource.equals("fragment")) {
                fragmentSource = splitSource[2];
            } else {
                throw new IOException("Unexpected token '" + secondSource + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compiles the shader.
     */
    public void compile() {
        int vertexID;
        int fragmentID;
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + filepath + "'" + SystemInfo.endOfLine + "\t vertex shader compilation failed.");
            System.err.println(glGetShaderInfoLog(vertexID, length));
        }
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + filepath + "'" + SystemInfo.endOfLine + "\t fragment shader compilation failed.");
            System.err.println(glGetShaderInfoLog(fragmentID, length));
        }
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + filepath + "'" + SystemInfo.endOfLine + "\t Could not link shaders.");
            System.err.println(glGetShaderInfoLog(shaderProgramID, length));
        }
    }

    /**
     * Use this shader as current shader.
     */
    public void use() {
        if (!isBeingUsed) {
            glUseProgram(shaderProgramID);
            isBeingUsed = true;
        }
    }

    /**
     * Stop using this shader.
     */
    public void detach() {
        glUseProgram(0);
        isBeingUsed = false;
    }

    /**
     * Uploads a 4x4 float matrix to the shader.
     * @param varName The uniform variable name.
     * @param mat4 The 4f matrix.
     */
    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    /**
     * Uploads a 3x3 float matrix to the shader.
     * @param varName The uniform variable name.
     * @param mat3 The 3f matrix.
     */
    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    /**
     * Uploads a float vector of length 4.
     * @param varName The uniform variable name.
     * @param vec4 The vec4.
     */
    public void uploadVec4f(String varName, Vector4f vec4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec4.x, vec4.y, vec4.z, vec4.w);
    }

    /**
     * Uploads a float vector of length 3.
     * @param varName The uniform variable name.
     * @param vec3 The vec3.
     */
    public void uploadVec3f(String varName, Vector3f vec3) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec3.x, vec3.y, vec3.z);
    }

    /**
     * Uploads a float vector of length 2.
     * @param varName The uniform variable name.
     * @param vec2 The vec2.
     */
    public void uploadVec2f(String varName, Vector2f vec2) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec2.x, vec2.y);
    }

    /**
     * Uploads a float to the shader.
     * @param varName The variable name.
     * @param val The float.
     */
    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }

    /**
     * Uploads an int to the shader.
     * @param varName The variable name.
     * @param val The int.
     */
    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, val);
    }

    /**
     * Uploads a texture to the shader.
     * @param varName The variable name.
     * @param slot The texture slot to use.
     */
    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }
}
