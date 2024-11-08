package org.Core.Rendering;


import org.Core.Window.Window;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;

import static org.lwjgl.opengl.GL11.glEnable;

import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.Random;



public class WindowTest {

    private long window;
    private CFont font;

    public WindowTest() {
        init();
        font = new CFont("C:/Windows/Fonts/Arial.ttf", 64);
    }

    private void init() {
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(1920, 1080, "Font Rendering", NULL, NULL);
        if (window == NULL) {
            System.out.println("Could not create window.");
            glfwTerminate();
            return;
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        // Initialize gl functions for windows using GLAD
        GL.createCapabilities();
    }

    public void run() {

        FontReneder batch = new FontReneder();
        batch.generateShade();
        batch.font = font;
        batch.initBatch(Window);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Random random = new Random();
      

   
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(0, 0, 0, 1);

            batch.addText("My name is Gabe!", 100, 300, 1, 0xAA01BB);
           
            batch.addText("Hello world!", 200, 200, 1f, 0xFF00AB0);
           batch.addText("My name is Gabe!", 100, 300, 1.1f, 0xAA01BB);

            String message = "";
            for (int i = 0; i < 10; i++) {
                message += (char) (random.nextInt('z' - 'a') + 'a');
            }
           batch.addText(message, 200, 400, 1.1f, 0xAA01BB);

            batch.flushBatch();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}