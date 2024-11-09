package org.Core.Window;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;




import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.*;

import static org.lwjgl.system.MemoryUtil.*;


public class Window {
    

    private String tile;
    private int width, height;
   static public long window;


    public Window() {
        this.tile = "Line Game engine";
        this.height =  768;
        this.width = 1366;

        init();
    }

    public  long getWindow() {
        return window;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        
        
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
            
        if(!glfwVulkanSupported())
            throw new IllegalStateException("Unable to initialize Vulkan");

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        
    

        window = glfwCreateWindow(width, height, tile, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");


        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
        
        // Make the window visible
        glfwShowWindow(window);
        GL.createCapabilities();
    }
    public int getWidth() {
      
        return width;
    }

    public int getHeight() {
       
        return height;
    }
}