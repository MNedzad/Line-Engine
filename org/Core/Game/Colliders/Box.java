package org.Core.Game.Colliders;

import org.Core.Game.PrimitiveShape;
import org.locationtech.jts.geom.Coordinate;

import glm_.vec2.Vec2;



public class Box extends PrimitiveShape
{
    Vec2 Position;
    Vec2 Size;
    
    public Box(float x , float y)
    {
        super(new Vec2(x, y));
        
    }
    @Override 
    public void init()
    {
         Position = this.getPosition();
       
         Size = this.getSize();
        
        this.mesh = new Coordinate[]{
            new Coordinate(Position.getX(), Position.getY()),
            new Coordinate(Position.getX(), Position.getY() - Size.getY()),

            new Coordinate(Position.getX() - Size.getX(), Position.getY() - Size.getY()),
            new Coordinate(Position.getX()- Size.getX(), Position.getY()),
            new Coordinate(Position.getX(), Position.getY()),

        };
      
        this.setMesh();
        mesh = null;
    
    }
    @Override
    public void updatePosition()
    {
        
        if(mesh == null)
        {
           
            init();
        }
         



    
    }


}
