package org.Core.Component;



import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;
import java.util.List;


import org.Core.Game.Collider;
import org.Core.Game.Scene;
import org.Core.Rendering.Renderable;

import com.mongodb.lang.Nullable;

import glm_.vec2.*;

public  class GameObject{

   
  
    private  String name;
    private  String tag;
    private Scene scene;
    private Vec2 Position;
    private  List<component> components;
    public int Pixel = 377;
   
    private int layer;

    public GameObject(String name, String tag, Vec2 pos, int layer, Scene scene)
    {   

        this.tag = tag;
        this.name = name;
        this.layer = layer;
        this.scene = scene;
        components = new ArrayList<>();
        this.Position = pos;
    }
    public void start(float dt)
    {
        for(component c : components)
        {
            c.start();
        }
    }
    public void update(float dt )
    {
        for(component c : components)
        {
          c.update(dt);
          
        }
    }
    @Nullable
    public void SetPosition(float PosX, float PosY)
    {
        if(PosX == NULL)
        {
            Position.setX(PosX);
        }
        if(PosY == NULL)
        {
            Position.setY(PosY);
        }
       
        
    }
    public Vec2 getPosition() {
        return Position;
    }
    public Scene getScene() {
        return scene;
    }
    public String getName() {
        return name;
    }
    public String getTag() {
        return tag;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public <T> T getComponent(Class<T> componentClass) {
        for (component c : components) {
           
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public GameObject AddComponet(component c)
    {
        
        this.components.add(c);
        c.gameObject = this;

         if(c instanceof Collider)
         {
            this.getScene().AddCollider(this);
         }
         if(c instanceof Renderable)
         {
            this.getScene().addToRender(this);
         }
         this.getScene().AddGameObject(this);

         return this;
    }
}
