package org.Core.Rendering;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.PublicKey;
import java.util.ArrayList;

import org.Core.Component.Shade;
import org.Core.Component.Sprite;
import org.Core.Component.Texture2D;
import org.Core.Component.component;
import org.Core.Game.Camera;
import org.Core.Game.Scene;
import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL40;
import org.lwjgl.openvr.Texture;

import glm.vec._4.Vec4;
import jglm.Mat;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import glm.mat._3.Mat3;
import glm.mat._4.*;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
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
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetShaderiv;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;
import static org.lwjgl.opengl.GL40.*;

public abstract interface Renderer{


        int PixelX = 377;
        int PixelY = 377;
        float DefaultPositionX = 0;
        float DefaultPositionY = 0;

        Vec2 Pos = new Vec2(-0.2f, -0.2f);
        Vec2 Size = new Vec2(1.f, 1.f);

        float vertices[] = {

                        // positions // colors // texture coords

                        Pos.x, Pos.y, 0.0f, 1.0f, 0.0f, 0.0f, 0, 0,
                        Pos.x, Pos.y - Size.y, 0.0f, 0.0f, 1.0f, 0.0f, 1, 0,
                        Pos.x - Size.x, Pos.y - Size.y, 0.0f, 0.0f, 0.0f, 1.0f, 0, 0,
                        Pos.x - Size.x, Pos.y, 0.0f, 1.0f, 1.0f, 0.0f, 0, 1
        };


         float[] vert = {
                        // x, y, r, g, b ux, uy
                        0.5f, 0.5f, 1.0f, 0.2f, 0.11f, 1.0f, 0.0f,
                        0.5f, -0.5f, 1.0f, 0.2f, 0.11f, 1.0f, 1.0f,
                        -0.5f, -0.5f, 1.0f, 0.2f, 0.11f, 0.0f, 1.0f,
                        -0.5f, 0.5f, 1.0f, 0.2f, 0.11f, 0.0f, 0.0f
        };

         int[] indices = {
                        0, 1, 3,
                        1, 2, 3
        };
      
        

        public abstract void ShaderBind(Shade shader);

        public abstract void DrawElement();

        public abstract void setSize(Vec2 Size);

        public abstract void setPosition(Vec2 position);

        public abstract void init();

  
        

}
