package org.Core.Game;



import java.util.ArrayList;
import java.util.List;

import org.Core.Component.GameObject;

import org.Core.Rendering.Renderable;

public class Scene  {
    List<GameObject> GameObject = new ArrayList<>();
    List<Collider> Collider = new ArrayList<>(); 
    List<Renderable> Renderer = new ArrayList<>(); 
    Camera cam;
    float time;
    public List<Collider> getCollider() 
    {
      
       return Collider;
    }
    public void setCam(Camera cam) {
        this.cam = cam;
    }
    public Camera getCam() {
        return cam;
    }
    public void AddGameObject(GameObject  object)
    {
        this.GameObject.add(object.getComponent(GameObject.class));
    }
    public void AddCollider(GameObject  collider)
    {
        this.Collider.add(collider.getComponent(Collider.class));
    }
    public void addToRender(GameObject Render) 
    {
  
        Renderer.add(Render.getComponent(Renderable.class));
    }

    public void start()
    {
        Renderer.forEach(e ->
        {
            e.start();
        });
    }

    public void DrawFromRender(float dt)
    {   
        for (int i = 0; i < Renderer.size() ; i++) {
            Renderer.get(i).Draw();
        }
        
    
    }
}
