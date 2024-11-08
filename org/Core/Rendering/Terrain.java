package org.Core.Rendering;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

import static org.lwjgl.opengl.GL11.glDrawElements;

import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
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
import org.Core.Window.Window;
import org.locationtech.jts.geom.CoordinateFilter;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWNativeWin32;

import org.lwjgl.opengl.GL15;

import org.lwjgl.opengl.WGL;
import org.lwjgl.system.windows.User32;

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

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
    }

    @Override
    public void start() {
        map = fl.getMap();
        sheet.getSprite(map.getData(0, 0)).getTexture().bind();
        cam = gameObject.getScene().getCam();
        init();

        Shape = new Box(sizePIX.getX(), sizePIX.getY());

    }

    public void init() {

        model = new Mat4(1.0f);
        model = GLM.translate(model, new Vec3((0) / scale, (0) / scale, 0.0f));

        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);

        generateEbo();

        verticesBuffer = BufferUtils.createFloatBuffer(Float.BYTES * VERTEX_SIZE * BATCH_SIZE);

        int stride = 7 * Float.BYTES;
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);

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

    public void flushBatch() {

        {
            verticesBuffer.put(vertices).flip();

            ChunkLenght = fl.getMap().getChunkLenght();

            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);

            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

            shade.Bind();
            glActiveTexture(GL_TEXTURE0);
            sheet.getSprite(fl.getMap().getData(0, 0)).getTexture().bind();

            glBindVertexArray(VAO);

            glDrawElements(GL_TRIANGLES, sizebtc * 6, GL_UNSIGNED_INT, 0);

            if (glGetError() > 0) {
                System.err.println("Something Wrong With Rendering");
            }

            // Reset batch for use on next draw call
            sizebtc = 0;
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
    public void update(float dt) {
        shade.Bind();
        glUniformMatrix4fv(glGetUniformLocation(shade.getShaderProgram(), "view"), false, cam.getView().toBuffer().asFloatBuffer());
        camPos = cam.getCameraPos();

    }

    private void setVertices(int x, int y, int chunks) {

        Data = map.getData(16 * y + x, chunks);
        if (Data == 0)
            return;
        Shape.setPosition(16.f * xs, -16.f * ys);
        Shape.updatePosition();

        if (sizebtc >= BATCH_SIZE - 4) {

            flushBatch();
        }

        index = sizebtc * 7;
        for (int i = 0; i < 4; i++) {

            vertices[index] = (float) Shape.getMesh().getCoordinates()[i].x / scale;
            vertices[index + 1] = (float) Shape.getMesh().getCoordinates()[i].y / scale;

            vertices[index + 2] = sheet.getSprite(Data).Color[i].getX();
            vertices[index + 3] = sheet.getSprite(Data).Color[i].getY();
            vertices[index + 4] = sheet.getSprite(Data).Color[i].getZ();

            vertices[index + 5] = sheet.getSprite(Data - 1).texCoords[i].getX();
            vertices[index + 6] = sheet.getSprite(Data - 1).texCoords[i].getY();
            index += 7;
        }
        sizebtc += 4;

    }

    private boolean isOnScreen(int x) {
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
