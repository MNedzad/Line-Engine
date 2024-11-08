package org.Core.Game;



import java.util.ArrayList;
import java.util.List;

import org.Core.Component.Shade;
import org.Core.Component.component;
import org.Core.Game.Colliders.Box;
import org.Core.Rendering.Mesh;
import org.Core.Rendering.Renderable;


import glm.vec._2.Vec2;


public class ParticleSystem extends component
{
    List<Particle> particles;

    @Override
    public int compareTo(component o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public void start() 
    {
    }

    @Override
    public void update(float dt) 
    {  

    }
}
class Particle
{
    Mesh mesh;
    float duration;
    int size;
}