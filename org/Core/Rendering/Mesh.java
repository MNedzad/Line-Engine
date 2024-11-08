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
    public void setVertices() {

        int offset = 0;
        for (int i = 0; i < Shape.getMesh().getNumPoints() - 1; i++) {

            vertices[offset] = (float) getCoordiantes(i).x / scale;
            vertices[offset + 1] = (float) getCoordiantes(i).y / scale;

            vertices[offset + 2] = sprite.Color[i].getX();
            vertices[offset + 3] = sprite.Color[i].getY();
            vertices[offset + 4] = sprite.Color[i].getZ();

            vertices[offset + 5] = sprite.texCoords[i].getX();
            vertices[offset + 6] = sprite.texCoords[i].getY();

            offset += Vertex_Size;
        }

    }

    public Mesh(Sprite sprite) {
        this.sprite = sprite;
        this.vertices = new float[28];
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void flushMesh() {
        verticesBuffer.put(vertices).flip();

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);

        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shade.Bind();
        glActiveTexture(GL_TEXTURE0 );
        sprite.getTexture().bind();

        glBindVertexArray(VAO);

        glDrawArrays(GL_QUADS, 0, 6);

        if (glGetError() > 0) {
            System.err.println("Something Wrong With Rendering");
        }
    }
    public void initMesh()
    {

        model = new Mat4();
        model = GLM.translate(model, new Vec3((0) / scale, (0) / scale, 0.0f));
        // VAO , VBO setup
        {
            VAO = glGenVertexArrays();
            VBO = glGenBuffers();
            EBO = glGenBuffers();

            glBindVertexArray(VAO);
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);
            verticesBuffer=  BufferUtils.createFloatBuffer(Float.BYTES * VERTEX_SIZE * BATCH_SIZE);
            int stride = 7 * Float.BYTES;
            glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
            glEnableVertexAttribArray(0);
    
            glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 2 * Float.BYTES);
            glEnableVertexAttribArray(1);
    
            glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 5 * Float.BYTES);
            glEnableVertexAttribArray(2);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
        // shade cam 
        {
        shade.Bind();
        
        // camera view
        glUniformMatrix4fv(glGetUniformLocation(shade.getShaderProgram(), "projection"), false,
        cam.GetProjection().toBuffer().asFloatBuffer());

        glUniformMatrix4fv(glGetUniformLocation(shade.getShaderProgram(), "view"), false, cam.getView().toBuffer().asFloatBuffer());

        // object transform
        glEnableVertexAttribArray(glGetAttribLocation(shade.getShaderProgram(), "InTexCoord"));
        glUniformMatrix4fv(glGetUniformLocation(shade.getShaderProgram(), "transform"), false, model.toBuffer().asFloatBuffer());
        }
    }
    @Override
    public int compareTo(component o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public void start() {

        setPosition(this.gameObject.getPosition());
        setupCam(this.gameObject.getScene().getCam());
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
    public void update(float dt) {

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

    private void setupCam(Camera cam) {
        this.cam = cam;

        if (cam != null) {
            glUniformMatrix4fv(glGetUniformLocation(shade.getShaderProgram(), "projection"), false,
                    cam.GetProjection().toBuffer().asFloatBuffer());
            glUniformMatrix4fv(glGetUniformLocation(shade.getShaderProgram(), "view"), false, cam.getView().toBuffer().asFloatBuffer());
        }
    }
}
