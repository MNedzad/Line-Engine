package org.Core.Events;


import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;



public interface KeyInput{

    final boolean keys[] = new boolean[65535];
    
  

    public default void SetListener(long window) {
        glfwSetKeyCallback(window, (win, key,  scancode,  action,  mods) -> {
       
            
            switch (action) {
                case GLFW_PRESS:
                    keys[key] = true;
                    break;
                case GLFW_RELEASE:
                    keys[key] = false;
                break;
            

                default:
                    break;
            }
        });
    }
    public default boolean getKeys(int Key) {
        return keys[Key];
    }
}
