package org.Core.Game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.Core.Events.KeyInput;
import org.Core.Window.Window;



import glm_.glm;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;



public class Camera  implements KeyInput  {
    Vec3 cameraPos;
    Vec3 cameraTarget;

    Vec3 up;
    Mat4 proj;
    boolean Move = false;

    Vec3 cameraUp;
    Vec3 cameraFront;
    Window win;
    Mat4 view;
    boolean changeFov = true;
    glm GLM = glm.INSTANCE;
    public Camera(Window win) {
        cameraPos = new Vec3(0, 0, 3.0f);
        cameraTarget = new Vec3(0.f, 0.f, 0.f);

        cameraUp = new Vec3(0.f, 1.0f, 0.f);
        cameraFront = new Vec3(0.f, 0.f, -1.f);
        this.win = win;

        proj = new Mat4();

    }

    public void moveX(float Speed, int Direction) {
        cameraPos.setX(cameraPos.getX() + (Direction) * Speed);

        Move = true;

    }

    public void moveY(float Speed, int Direction) {
        cameraPos.setY(cameraPos.getY() + (Direction) * Speed);

        Move = true;

    }

    public void freeCam(float delta) {

        if (this.getKeys(GLFW_KEY_A)) {

            this.moveX(0.0001f * 5 * delta, -1);
            Move = true;
        }
        if (this.getKeys(GLFW_KEY_D)) {
            this.moveX(0.0001f * 5 * delta, 1);
            Move = true;
        }
        if (this.getKeys(GLFW_KEY_W)) {
            this.moveY(0.0001f * 5 * delta, 1);
            Move = true;
        }
        if (this.getKeys(GLFW_KEY_S)) {
            this.moveY(0.0001f * 5 * delta, -1);
            Move = true;
        }

        Update(delta);
    }

    public boolean isChangingFov() {
        return changeFov;
    }

    public boolean Moving() {
        return Move;
    }

    public void setChangeFov(boolean changeFov) {
        this.changeFov = changeFov;
    }

    public void SetFov(float fov) {
        
        setChangeFov(true);
        
        proj = GLM.perspective((float)Math.toRadians(fov), (float) (win.getWidth() / win.getHeight()), 0f, 100.f);
    }

    public Mat4 GetProjection() {
        return proj;
    }

    public Vec3 getCameraPos() {
        return cameraPos;
    }

    public Mat4 getView() {
    
       view = GLM.lookAt(cameraPos, cameraFront.plus(cameraPos), cameraUp);
       
       return view;
    }

    public void Update(float delta) {
        if (Move) 
        {
            
            view = GLM.lookAt(cameraPos, cameraFront.plus(cameraPos), cameraUp);
        }
    }

}
