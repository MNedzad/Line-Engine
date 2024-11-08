package org.Core.Events;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import org.Core.Window.Window;

public abstract class MainLoop implements Runnable {

    Window win;
    public Long window ;

    public void init(Window win)
    {
        

        this.win = win;
        window = win.getWindow();
    }

    @Override
    public void run() {
        while (!glfwWindowShouldClose(window)) {
         
            event();
            update();
            draw();
        }
    }
    public abstract void draw();
    public abstract void event();
    public abstract void update();
}
