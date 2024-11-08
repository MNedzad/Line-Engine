package org.Core.Rendering;

import java.nio.FloatBuffer;

import org.Core.Component.Shade;
import org.Core.Component.Sprite;
import org.Core.Component.component;
import org.Core.Game.Camera;
import org.Core.Game.PrimitiveShape;

import glm_.mat4x4.Mat4;



public abstract class Renderable  extends component
{
    int scale = 377 * 2;
    float vertices[];

    int Vertex_Size = 7;
    protected static int BATCH_SIZE = 150;
    protected static int VERTEX_SIZE = 7;
    int VBO, EBO, VAO;
    Mat4 model;
    boolean update = false;
    Sprite sprite;
    PrimitiveShape Shape;
    Shade shade;
    Camera cam;
    FloatBuffer verticesBuffer;
    
    public abstract void Draw();
}
