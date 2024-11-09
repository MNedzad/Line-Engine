package org.Core;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;


import java.io.File;

import java.util.ArrayList;
import java.util.List;


import org.Core.Component.GameObject;
import org.Core.Component.Shade;
import org.Core.Component.Sprite;
import org.Core.Component.SpriteSheet;
import org.Core.Component.Texture2D;
import org.Core.Component.collisonHandler;
import org.Core.Editor.DeltaTimer;

import org.Core.Events.MainLoop;
import org.Core.Game.Camera;
import org.Core.Game.ParticleSystem;
import org.Core.Game.Scene;
import org.Core.Game.Animation.Animation;
import org.Core.Game.Animation.Animator;
import org.Core.Game.Colliders.Box;
import org.Core.Game.Colliders.Polygon;

import org.Core.Rendering.FontReneder;
import org.Core.Rendering.Mesh;
import org.Core.Rendering.CFont;
import org.Core.Rendering.Terrain;
import org.Core.Utils.FileLoader;

import org.Core.Window.Window;


import glm_.vec2.*;


import static org.lwjgl.glfw.GLFW.*;

public class Engine extends MainLoop {

    Window win = new Window();
    Terrain spirit;
    Shade shade;
    Texture2D texture;
    Texture2D textureBtf;
    Texture2D Tiles;
    Sprite object;
    int PixelX = 377;
    int PixelY = 377;
    Camera cam;
    double lastTime[];
    double nowTime[];
    CFont font;
    SpriteSheet sheet;
    List<Terrain> spr;
    FileLoader fl;
    FontReneder batch;
    GameObject Player;
    GameObject Box;

    float fov = 10;
    private float delta[];
    private double fps;

    double m_secondCounter = 0;
    double m_tempFps = 0;

    DeltaTimer dt;
    Scene cScene;
    Terrain terrain;
    
    Engine() {

        //Texture Load //
        cam = new Camera(win);
        font = new CFont("Assets/Fonts/Lato-Italic.ttf", 16);
        delta = new float[10];
        lastTime = new double[10];
        nowTime = new double[10];
        texture = new Texture2D("Assets/ice.png");
        Tiles = new Texture2D("Assets/Terraria/tiles.png");
        textureBtf = new Texture2D("Assets/btf.jpeg");

        dt = new DeltaTimer();

        fl = new FileLoader();

        shade = new Shade();
        shade.compileDefautl();

        shade.LinkShade();
        shade.Bind();

        shade.LinkShade();

        sheet = new SpriteSheet(Tiles);
        sheet.create(16, 16, 533, 0);
        fl.TSXLoader(new File("Assets/Terraria/tiles.tsx"));
        fl.FileReader(new File("Assets/Terraria/map2.tmj"));
        
        object = new Sprite(Tiles);

        spr = new ArrayList<>(2040);

        MainLoop();

    }
    private void MainLoop() {

        glClearColor(0.5f, 0.8f, 1.0f, 1.0f);
        glLoadIdentity();

        batch = new FontReneder();
        batch.generateShade();
        batch.font = font;
        batch.initBatch(win);

        cam.Update(delta[0]);
        cam.SetFov(fov);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        cScene = new Scene();

        cScene.setCam(cam);

        Mesh mesh = new Mesh(new Sprite(texture));
        mesh.setShade(shade);
        mesh.setShape(new Box(16, 16));

        Animator animator = new Animator();
        Animation anim = new Animation();
        anim.setName("Default");
        anim.addFrame(sheet.getSprite(2), 1);
        anim.addFrame(sheet.getSprite(3), 1);
        anim.addFrame(sheet.getSprite(4), 1);
        anim.addFrame(sheet.getSprite(5), 1);

        animator.addAnimation(anim);
        animator.setDefaultState("Default");


        Box = new GameObject("Box", "box", new Vec2(0, 0), 2, cScene);

        Polygon object2 = new Polygon(new Box(16, 16));
        object2.setMask((short) 3, false);
        Box.AddComponet(object2);
        ParticleSystem ParticleSystem = new ParticleSystem();
        Box.start(delta[0]);

        {

            Player = new GameObject("Player", "pl", new Vec2(0, 0), 2, cScene);
            Polygon object = new Polygon(new Box(16, 16));
            object.setMask((short) 0, false);
            terrain = new Terrain(shade, sheet, fl);
            Box.AddComponet(terrain);
            Player.AddComponet(object);
           Player.AddComponet(mesh);
            Player.AddComponet(ParticleSystem);
            
     
            Player.AddComponet(animator);
            //Player.AddComponet(new CharacterController());
            Player.AddComponet(new collisonHandler(object));
            Player.start(delta[0]);
        }

        cScene.start();

        shade.Bind();
        lastTime[0] = System.currentTimeMillis();

        this.init(win);
        this.run();
     
    }
    int Code;
    int number = 20;
    @Override
    public void draw() {
        lastTime[1] = System.currentTimeMillis();
        dt.setDelta(0);
        glClear(GL_COLOR_BUFFER_BIT);

        glActiveTexture(GL_TEXTURE0);
        glEnable(GL_TEXTURE_2D);

        cScene.DrawFromRender(delta[0]);
        

        glBindTexture(GL_TEXTURE_2D, 0);

        batch.addText(Double.toString(fps) + " ", 1, win.getHeight(), 1f, 0x594200);
        batch.addText("Global: " + dt.getFPS(0) + " ms", 1, win.getHeight() - 20, 1f, 0x594200);
        batch.addText("Draw: " +Double.toString( delta[1]) + " ms", 1, win.getHeight() - 40, 1f, 0x594200);
        batch.addText("Update: " +Double.toString( delta[3]) + " ms", 1, win.getHeight() - 60, 1f, 0x594200);

        batch.flushBatch();
        
        getDelta(1);

        glfwSwapBuffers(window);
        
        dt.getFPS(0);
        dt.setDelta(0);
        GetFps(0);
        lastTime[0] = System.currentTimeMillis();
    }
    @Override
    public void event() {
        lastTime[2] = System.currentTimeMillis();
        dt.getDelta(0);
        glfwPollEvents();
        glfwSetScrollCallback(window, (win, dx, dy) -> {
            fov -= (float) dy;
            if (fov < 1.0f)
                fov = 1.0f;
            if (fov > 50.0f)
                fov = 50.0f;
        });

        cam.SetListener(Window.window);
        cam.freeCam((float)delta[0]);
        getDelta(2);
    }
    @Override
    public void update() {
        lastTime[3] = System.currentTimeMillis();
        cam.SetFov(fov);


        Box.update(delta[0]);

        Player.update(delta[0]);
      
        Code = glGetError();
        if (Code > 0) {
            System.out.println("error Code: " + Code);
        }
        getDelta(3);
    }
    public void getDelta(int index)
    {
        nowTime[index] = System.currentTimeMillis();
        delta[index] = (float) (nowTime[index] - lastTime[index]);
        
    }
    public void GetFps(int index) {
        getDelta(index);
        if (m_secondCounter <= 1) {
            m_secondCounter += delta[index] / 1000;
            m_tempFps++;
        } else {

            fps = m_tempFps;

            m_secondCounter = 0;
            m_tempFps = 0;
        }
    }
}
