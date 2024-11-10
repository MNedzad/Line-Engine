package org.Core.Rendering;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;

import static org.lwjgl.opengl.GL11.glDrawArrays;

import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.Core.Component.Shade;
import org.Core.Component.Sprite;
import org.Core.Component.Texture2D;
import org.Core.Component.component;
import org.Core.Game.Camera;
import org.Core.Game.PrimitiveShape;
import org.locationtech.jts.geom.Coordinate;

import org.lwjgl.BufferUtils;

import glm_.glm;
import glm_.mat4x4.Mat4;
import glm_.vec2.Vec2;
import glm_.vec3.Vec3;

;

public class Mesh extends Renderable {

    glm GLM = glm.INSTANCE;
    boolean Collision = false;

    public void setVertices() {

        int offset = 0;

        // Loop through four vertices
        for (int i = 0; i < Shape.getMesh().getNumPoints() - 1; i++) {
             // Set position of points with scale
            vertices[offset] = (float) getCoordiantes(i).x / scale;
            vertices[offset + 1] = (float) getCoordiantes(i).y / scale;

            vertices[offset + 2] = sprite.Color[i].getX();
            vertices[offset + 3] = sprite.Color[i].getY();
            vertices[offset + 4] = sprite.Color[i].getZ();

            vertices[offset + 5] = sprite.texCoords[i].getX();
            vertices[offset + 6] = sprite.texCoords[i].getY();

            // Increment the offset by the size of a vertex to move to the next vertex's data
            offset += Vertex_Size;
        }

    }

    public Mesh(Texture2D texture) {
        this.sprite = new Sprite(texture);
        this.vertices = new float[28];
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }
    
    public void flushMesh() {
        // put vertex data into the buffer and prepare it for reading by flipping the buffer
        verticesBuffer.put(vertices).flip();

        // Bind the Vertex Buffer Object (VBO) to the GL_ARRAY_BUFFER target.
        glBindBuffer(GL_ARRAY_BUFFER, VBO);

        // Allocate space for the buffer, considering the number of vertices to be handled (VERTEX_SIZE * BATCH_SIZE), 
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);

        // Upload the new vertex data to the buffer starting at offset 0. This replaces part of the buffer with new data.
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Bind the shader program
        shade.Bind();

        // Active GL_TEXTURE0 
        glActiveTexture(GL_TEXTURE0);

        // Bind Sprite texture
        sprite.getTexture().bind();

        // Bind vertex array object 
        glBindVertexArray(VAO);

        // Render quads using vertex data starting from the current vertex buffer.
        glDrawArrays(GL_QUADS, 0, 6);

        // Check for OpenGL errors after the draw call.
        if (glGetError() > 0) {
            System.err.println("Something Wrong With Rendering");
        }
    }

    public void initMesh() {
        // Create a new 4x4 matrix to represent the transformation model
        model = new Mat4();

        // Apply a translation transformation to the model matrix.
        model = GLM.translate(model, new Vec3((0) / scale, (0) / scale, 0.0f));

      
        {   
            //  Generate vertex arrays  object (VAO).
            VAO = glGenVertexArrays();
            // Generate vertex buffer object (VBO).
            VBO = glGenBuffers();
            // Generate element buffer object (EBO).
            EBO = glGenBuffers();

            // Bind the Vertex Array Object (VAO) to make it active
            glBindVertexArray(VAO);

            // Bind the Vertex Buffer Object (VBO) to the GL_ARRAY_BUFFER.
            glBindBuffer(GL_ARRAY_BUFFER, VBO);

            // Allocate space for the buffer, considering the number of vertices to be handled (VERTEX_SIZE * BATCH_SIZE), 
            glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);

            // Construct a direct native-order floatbuffer with the specified number of elements.
            verticesBuffer = BufferUtils.createFloatBuffer(Float.BYTES * VERTEX_SIZE * BATCH_SIZE);

            // Define stride of attribut pointer,
            int stride = 7 * Float.BYTES;

            // Set up vertex attribute pointer for mesh coordinates
            glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
            glEnableVertexAttribArray(0);

            // Set up vertex attribute pointer for color
            glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 2 * Float.BYTES);
            glEnableVertexAttribArray(1);

            // Set up vertex attribute pointer for UV
            glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 5 * Float.BYTES);
            glEnableVertexAttribArray(2);

            // Unbind the currently bound vertex buffer object (VBO) by binding to 0.
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            // Unbind the currently bound vertex array object (VAO) by binding to 0.
            glBindVertexArray(0);
        }
        {
            // bind the shade program
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

            //  Upload transform to shader program
            glUniformMatrix4fv(transform, false,
                    model.toBuffer().asFloatBuffer());
        }
    }

    @Override
    public int compareTo(component o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public void start() {
        // Set mesh position from the gameObject
        setPosition(this.gameObject.getPosition());

        // Get the camera object from the current scene associated with this game object
        this.cam = this.gameObject.getScene().getCam();

        setVertices();

        initMesh();
    }

    public Renderable setShape(PrimitiveShape Shape) {
        this.Shape = Shape;
        return this;
    }

    public void setPosition(Vec2 Pos) {
        Shape.setPosition(Pos.getX(), Pos.getY());
        Shape.updatePosition();
    }

    public void setCoords(PrimitiveShape shape) {
        this.Shape = shape;
        this.Shape.updatePosition();
    }

    private Coordinate getCoordiantes(int index) {
        return Shape.getMesh().getCoordinates()[index];
    }

    @Override
    public void 
    update(float dt) {

        setVertices();
        setPosition(this.gameObject.getPosition());

    }

    @Override
    public void Draw() {

        flushMesh();
    }

    public void setShade(Shade shade) {
        this.shade = shade;
    }
     // Get the camera object from the current scene associated with this game object
    public void setupCam(Camera cam) {
       
        this.cam = cam;
    }
}
