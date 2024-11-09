package org.Core.Component;


import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;

import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;


public class Texture2D {
    private String filePath;
    private int texID;
    private int width ;
    private int height ;

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getTexID() {
        return texID;
    }
    
    public Texture2D(String FilePath)
    {
        IntBuffer Width;
        IntBuffer Height;
        filePath = FilePath;

        texID = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

         
        stbi_set_flip_vertically_on_load(true);
        Width = BufferUtils.createIntBuffer(1);
        Height = BufferUtils.createIntBuffer(1);

        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(FilePath, Width, Height, channels,0 );

        int num = channels.get();
        System.out.println(num);
        if(image != null)
        {
            width = Width.get(0);
            height = Height.get(0);
            if(num == 3)
            {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, Width.get(), Height.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
                glGenerateMipmap(GL_TEXTURE_2D);
            }else if(num == 4)
            {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Width.get(), Height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
                glGenerateMipmap(GL_TEXTURE_2D);
            }
 
            
        
        } else
        {
            assert false : "Error:(Texture) Could not load image '" + filePath  + "'";
            return;
        } 
        
        stbi_image_free(image);
        

    }
    public void bind()
    {

       glBindTexture(GL_TEXTURE_2D, texID);
        
    }
    public void unbind()
    {
        //
    }
}
