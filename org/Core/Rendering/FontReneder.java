package org.Core.Rendering;


import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.shaderc.Shaderc;

import glm_.glm;
import glm_.mat4x4.Mat4;

import static org.lwjgl.opengl.GL11.GL_CONSTANT_ATTENUATION;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;

import java.awt.Window;
import java.nio.FloatBuffer;

import org.Core.Component.Shade;

public class FontReneder {
    // private float[] vertices = {
    // // x, y, r, g, b ux, uy
    // 0.5f, 0.5f, 1.0f, 0.2f, 0.11f, 1.0f, 0.0f,
    // 0.5f, -0.5f, 1.0f, 0.2f, 0.11f, 1.0f, 1.0f,
    // -0.5f, -0.5f, 1.0f, 0.2f, 0.11f, 0.0f, 1.0f,
    // -0.5f, 0.5f, 1.0f, 0.2f, 0.11f, 0.0f, 0.0f
    // };
    String shadeFont = "#version 330 core\r\n" + //
            "layout(location=0) in vec2 aPos;\r\n" + //
            "layout(location=1) in vec3 aColor;\r\n" + //
            "layout(location=2) in vec2 aTexCoords;\r\n" + //
            "\r\n" + //
            "out vec2 fTexCoords;\r\n" + //
            "out vec3 fColor;\r\n" + //
            "uniform mat4 projection;\r\n" +
            "\r\n" + //
            "void main()\r\n" + //
            "{\r\n" + //
            "    fTexCoords = aTexCoords;\r\n" + //
            "    fColor = aColor;\r\n" + //
            "    gl_Position = projection *  vec4(aPos, -5, 1);\r\n" + //
            "}\r\n" + //
            "";
    String fragment = "#version 330 core\r\n" + //
                "\r\n" + //
                "in vec2 fTexCoords;\r\n" + //
                "in vec3 fColor;\r\n" + //
                "\r\n" + //
                "uniform sampler2D uFontTexture;\r\n" + //
                "\r\n" + //
                "out vec4 color;\r\n" + //
                "\r\n" + //
                "void main()\r\n" + //
                "{\r\n" + //
                "    float c = texture(uFontTexture, fTexCoords).r;\r\n" + //
                "    color = vec4(1, 1, 1, c) * vec4(fColor, 1);\r\n" + //
                "}";
    private int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    // 25 quads

    public static int BATCH_SIZE = 100;
    public static int VERTEX_SIZE = 7;
    public float[] vertices = new float[BATCH_SIZE * VERTEX_SIZE];
    public int size = 0;
    private Mat4 projection = new Mat4();
    int code;
    public int vao;
    public int vbo;
    public Shade shader;
    public CFont font;
    FloatBuffer vBuffer;
    glm GLM = glm.INSTANCE;

    public void generateEbo() {
        int elementSize = BATCH_SIZE * 3;
        int[] elementBuffer = new int[elementSize];

        for (int i = 0; i < elementSize; i++) {
            elementBuffer[i] = indices[(i % 6)] + ((i / 6) * 4);
        }

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
    }
    public void generateShade()
    {
        shader = new Shade();

        shader.compileShade(shadeFont, shader.getVertexShader());

        shader.compileShade(fragment, shader.getFragmentShader());

        shader.LinkShade();

        
        
    }
    public void initBatch(org.Core.Window.Window win) {
        projection.identity();
   
        //projection.ortho(0, win.getWidth(), 0, win.getHeight(), 1f, 100f);
        projection = GLM.ortho(0, win.getWidth(), 0, win.getHeight(), 1f, 100f);
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);

        generateEbo();

        int stride = 7 * Float.BYTES;
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);

  
        
    }

    public void flushBatch() {
        // Clear the buffer on the GPU, and then upload the CPU contents, and then draw
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);
       
     
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

       code =glGetError();
        if( code > 0)
        {
            System.out.println("Something wrong " + code);
        }



        // Draw the buffer that we just uploaded
        shader.Bind();
        glActiveTexture(GL_TEXTURE0 + 1 );
        glBindTexture(GL_TEXTURE_BUFFER, font.textureId);
        
        glUniform1i(glGetUniformLocation(shader.getShaderProgram(), "uFontTexture"), 0);

        

        glUniformMatrix4fv(glGetUniformLocation(shader.getShaderProgram(), "projection"), false, projection.toBuffer().asFloatBuffer());


        glBindVertexArray(vao);

        glDrawElements(GL_TRIANGLES, size * 6, GL_UNSIGNED_INT, 0);

        // Reset batch for use on next draw call
        size = 0;
    }

    public void addCharacter(float x, float y, float scale, CharInfo charInfo, int rgb) {
        // If we have no more room in the current batch, flush it and start with a fresh
        // batch
        if (size >= BATCH_SIZE - 4) {
            flushBatch();
        }

        float r = (float) ((rgb >> 16) & 0xFF) / 255.0f;
        float g = (float) ((rgb >> 8) & 0xFF) / 255.0f;
        float b = (float) ((rgb >> 0) & 0xFF) / 255.0f;

        float x0 = x;
        float y0 = y;
        float x1 = x + scale * charInfo.width;
        float y1 = y + scale * charInfo.height;

        float ux0 = charInfo.textureCoordinates[0].getX();
        float uy0 = charInfo.textureCoordinates[0].getY()/ 0.9F;
        float ux1 = charInfo.textureCoordinates[1].getX();
        float uy1 = charInfo.textureCoordinates[1].getY() / 0.9F;

     

        int index = size * 7;
        vertices[index] = x1;
        vertices[index + 1] = y0;
        vertices[index + 2] = r;
        vertices[index + 3] = g;
        vertices[index + 4] = b;
        vertices[index + 5] = ux1;
        vertices[index + 6] = uy0;

        index += 7;
        vertices[index] = x1;
        vertices[index + 1] = y1;
        vertices[index + 2] = r;
        vertices[index + 3] = g;
        vertices[index + 4] = b;
        vertices[index + 5] = ux1;
        vertices[index + 6] = uy1;

        index += 7;
        vertices[index] = x0 ;
        vertices[index + 1] = y1 ;
        vertices[index + 2] = r;
        vertices[index + 3] = g;
        vertices[index + 4] = b;
        vertices[index + 5] = ux0;
        vertices[index + 6] = uy1 ;

        index += 7;
        vertices[index] = x0;
        vertices[index + 1] = y0;
        vertices[index + 2] = r;
        vertices[index + 3] = g;
        vertices[index + 4] = b;
        vertices[index + 5] = ux0;
        vertices[index + 6] = uy0;

        size += 4;
    }
    char c;
    CharInfo charInfo;
    public void addText(String text, float x, float y, float scale, int rgb) {
        for (int i = 0; i < text.length(); i++) {
            c = text.charAt(i);

            charInfo  = font.getCharacter(c);
            if (charInfo.width == 0) {
                System.out.println("Unknown character " + c);
                continue;
            }

            float xPos = x ;
            float yPos = (y  - font.getFontSize());
            addCharacter(xPos, yPos, scale, charInfo, rgb);
            x += charInfo.width * scale;
        }
    }
}