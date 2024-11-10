package org.Core.Game;




import java.util.List;

import org.Core.Component.Shade;
import org.Core.Component.Texture2D;
import org.Core.Component.component;
import org.Core.Game.Colliders.Box;
import org.Core.Rendering.Mesh;

import glm_.vec2.Vec2;





public class ParticleSystem extends component
{
    List<Particle> particles;
    Texture2D texture;
    public Particle particle = new Particle();
    Shade shade;
    @Override
    public int compareTo(component o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
    public void setTexture(Texture2D texture)
    {
       this.texture = texture;
    }

    public void setShade(Shade shade)
    {
        this.shade = shade;
    }
    public Mesh getMesh()
    {
        return particle.mesh;
    }
    @Override
    public void start() 
    {
        particle.mesh = new Mesh(texture);
        particle.mesh.setShade(shade);
        
        particle.mesh.setShape(new Box(16,16));

        particle.mesh.gameObject = this.gameObject;
        
        // 
        particle.mesh.start();
        
    }   
    public void Draw()
    {
        particle.mesh.Draw();
    }
    @Override
    public void update(float dt) 
    {  
    
        particle.mesh.update(dt);
      
    }
}
 class Particle
{
    Mesh mesh;
    float duration;
    int size;
}