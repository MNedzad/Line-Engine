package org.Core.Rendering;


import static org.lwjgl.opengl.GL11.GL_FLOAT;


import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

import static org.lwjgl.opengl.GL11.glDrawElements;

import static org.lwjgl.opengl.GL11.glGetError;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.Core.Component.Shade;

import org.Core.Component.SpriteSheet;
import org.Core.Component.Texture2D;
import org.Core.Component.component;
import org.Core.Game.Camera;

import org.Core.Game.Scene;
import org.Core.Game.Colliders.Box;
import org.Core.Utils.FileLoader;
import org.Core.Utils.MapClass;
import org.lwjgl.BufferUtils;


import org.lwjgl.opengl.GL15;


import glm_.glm;
import glm_.mat4x4.Mat4;
import glm_.vec2.Vec2;
import glm_.vec3.Vec3;

public class Terrain extends Renderable {
    int Type;
    int shaderProgram;
    boolean update = true;
    int texCoord0Loc;
    boolean dirty = true;
    int VBO;
    int VAO;

    Mat4 model;
    int code;
    glm GLM = glm.INSTANCE;
    Scene scene;
    Vec2 sizePIX;
    Texture2D tex;
    Camera cam;
    public Shade shade;
    FloatBuffer verticesBuffer;
    MapClass map;
    SpriteSheet sheet;
    FileLoader fl;

    int Size = 16;
    long dc;
    long glrc;
    long glrc1;
    int sizebtc;

    boolean first = true;
    private int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    public Terrain(Shade shade, SpriteSheet sheet, FileLoader fl) {
        super();
        this.shade = shade;
        this.sheet = sheet;
        this.fl = fl;

        sizePIX = new Vec2(16, 16);
        vertices = new float[BATCH_SIZE * Vertex_Size];

    }

    public void generateEbo() {
        int elementSize = BATCH_SIZE * 3;
        int[] elementBuffer = new int[elementSize];

        for (int i = 0; i < elementSize; i++) {
            elementBuffer[i] = indices[(i % 6)] + ((i / 6) * 4);
        }

        // Gen Buffers
        int ebo = glGenBuffers();

        // Bind th EBO to the GL_ELEMENT_ARRAY_BUFFER target.
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

        // Bind Indicate on elementBuffer
        glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
    }

    @Override
    public void start() {
        // Get the map object 
        map = fl.getMap();

        // Bind texture to the GL_TEXTURE_2D
        sheet.getSprite(map.getData(0, 0)).getTexture().bind();

        // Get the camera object from the current scene associated with this game object
        cam = gameObject.getScene().getCam();

        init();

        // Create box shape of object dimension
        Shape = new Box(sizePIX.getX(), sizePIX.getY());
    }

    public void init() {
        // Create a new 4x4 matrix to represent the transformation model
        model = new Mat4();

        // Apply a translation transformation to the model matrix
        model = GLM.translate(model, new Vec3((0) / scale, (0) / scale, 0.0f));
        
        //  Generate vertex arrays      
        VAO = glGenVertexArrays();

        // Bind vertex array object 
        glBindVertexArray(VAO);

        // Generate Vertex Buffer Object 
        VBO = glGenBuffers();
        
        // Bind the Vertex Buffer Object (VBO) to the GL_ARRAY_BUFFER target.
        glBindBuffer(GL_ARRAY_BUFFER, VBO);

        // Allocate space for the buffer, considering the number of vertices to be handled (VERTEX_SIZE * BATCH_SIZE), 
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);

        // Generate and Bind Indicate  to the element buffer
        generateEbo();

        // Construct a direct native-order floatbuffer with the specified number of elements.
        verticesBuffer = BufferUtils.createFloatBuffer(Float.BYTES * VERTEX_SIZE * BATCH_SIZE);

        // Define stride of attribut pointer
        int stride = 7 * Float.BYTES;

        // Set up vertex attribute pointer for mesh coordinates
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0); // Enable attribute 0

        // Set up vertex attribute pointer for color
        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1); // Enable attribute 1

        // Set up vertex attribute pointer for UV
        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);// Enable attribute 2

        {
            // bind the shader program
            shade.Bind();

            // Get the location of the unifrom variable in the shader program.
            int projection = glGetUniformLocation(shade.getShaderProgram(), "projection");
            int view = glGetUniformLocation(shade.getShaderProgram(), "view");
            int transform = glGetUniformLocation(shade.getShaderProgram(), "transform");

            // Upload camera view and projectiom to shader program.
            glUniformMatrix4fv(projection, false,
                    cam.GetProjection().toBuffer().asFloatBuffer());
            glUniformMatrix4fv(view, false,
                    cam.getView().toBuffer().asFloatBuffer());

            // Enable the vertex attribute for texture coordinates
            glEnableVertexAttribArray(glGetAttribLocation(shade.getShaderProgram(), "InTexCoord"));
            
            // Upload transform to shader program
            glUniformMatrix4fv(transform, false, model.toBuffer().asFloatBuffer());
        }

    }

    public void flushBatch() {
        {
       
            {
                // Update the vertex buffer with the new vertices data, and prepare the buffer for the next operation.
                verticesBuffer.put(vertices).flip();

                // Get the length of the chunk, presumably to control the rendering of map sections.
                ChunkLenght = fl.getMap().getChunkLenght();

                // Bind the Vertex Buffer Object (VBO) to the GL_ARRAY_BUFFER target.
                glBindBuffer(GL_ARRAY_BUFFER, VBO);

                // Allocate space for the buffer, considering the number of vertices to be handled (VERTEX_SIZE * BATCH_SIZE), 
                glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);

                // Upload the new vertex data to the buffer starting at offset 0. This replaces part of the buffer with new data.
                glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            }
            {
                // Bind the shader program 
                shade.Bind();

                // Activate gl_texture0
                glActiveTexture(GL_TEXTURE0);

                // Bind the texture of the sprite that corresponds to the specific data in the map.
                sheet.getSprite(fl.getMap().getData(0, 0)).getTexture().bind();

                // Bind VAO, which contains the configuration for the vertex
                glBindVertexArray(VAO);

                // Render triangles using the indices stored in the currently bound index buffer.
                glDrawElements(GL_TRIANGLES, sizebtc * 6, GL_UNSIGNED_INT, 0);

                // Check for OpenGL errors after the draw call.
                if (glGetError() > 0) {
                    System.err.println("Something Wrong With Rendering");
                }
    
                // Reset batch for use on next draw call
                sizebtc = 0;
            }
        }
    }

    int index = 0;
    int Data;
    int xs;
    int ys;
    int ChunkLenght;
    Vec3 camPos;
    float tx;
    float ty;

    @Override
    public void update(float dt) 
    {
        // Bind the shader program 
        shade.Bind();

        // Get the location of the 'view' in the shader program
        int location = glGetUniformLocation(shade.getShaderProgram(), "view");

        //  Upload the 'view' variable in the shader program
        glUniformMatrix4fv(location, false,
                cam.getView().toBuffer().asFloatBuffer());

        // Get Camera Position 
        camPos = cam.getCameraPos();

    }

    private void setVertices(int x, int y, int chunks) {
        // Retrieve the data from the map at the specified position
        Data = map.getData(16 * y + x, chunks);

        // Return if Data is equal to 0
        if (Data == 0)
            return;

        // Set position for collision 
        Shape.setPosition(16.f * xs, -16.f * ys);
        
        // Upload position
        Shape.updatePosition();

        // Check if the current size of the batch (sizebtc) is greater than or equal to the threshold
        if (sizebtc >= BATCH_SIZE - 4) 
            // Flush the current batch to process its contents
            flushBatch();
        
        // Calculate the starting index in the vertices array based on the current batch size (sizebtc)
        // Each entry in this batch contributes 7 floats in the vertices array.
        index = sizebtc * 7;

        // Loop through four vertices
        for (int i = 0; i < 4; i++) {
            // Set position of points with scale
            vertices[index] = (float) Shape.getMesh().getCoordinates()[i].x / scale;
            vertices[index + 1] = (float) Shape.getMesh().getCoordinates()[i].y / scale;

            vertices[index + 2] = sheet.getSprite(Data).Color[i].getX();
            vertices[index + 3] = sheet.getSprite(Data).Color[i].getY();
            vertices[index + 4] = sheet.getSprite(Data).Color[i].getZ();

            vertices[index + 5] = sheet.getSprite(Data - 1).texCoords[i].getX();
            vertices[index + 6] = sheet.getSprite(Data - 1).texCoords[i].getY();
            index += 7;
        }
        // Increament  the batch size counter 'sizebtc' by 4
        sizebtc += 4;

    }

    private boolean isOnScreen(int x) 
    {
        //System.out.println((camPos.getX() * scale) + (Size * 8) * 1.8);
        return x * Size >= (camPos.getX() * scale) + (Size * 8) * 1.8
                || x * Size <= (camPos.getX() * scale) - (Size * 8) * 1.8;
    }

    private boolean isOnScreenY(int y) {
        return y * Size >= -(camPos.getY() * scale) + (Size * 8) * 1.8
                || y * Size <= -(camPos.getY() * scale) - (Size * 8) * 1.8;
    }

    @Override
    public int compareTo(component o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public void Draw() {
        for (int c = 0; c < ChunkLenght; c++) {
            ty = map.getChunksY(c);
            for (int y = 0; y < 16; y++) {
                ys = y + (int) ty;
                if (isOnScreenY(ys))
                    continue;

                tx = map.getChunksX(c);
                for (int x = 0; x < 16; x++) {

                    xs = x + (int) tx;
                    if (isOnScreen(xs))
                        continue;

                    setVertices(x, y, c);
                }
            }

        }
        flushBatch();
    }
}
