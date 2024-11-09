package org.Core.Game.Colliders;

import org.Core.Component.component;

import org.Core.Game.Collider;
import org.Core.Game.PrimitiveShape;

public class Polygon extends component implements Collider  {


    PrimitiveShape shape;
    short mask;

    public Polygon(PrimitiveShape shape)
    {
        this.shape = shape;
    }

    @Override
    public int compareTo(component o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public void start() {
        shape.setPosition(gameObject.getPosition().getX(), gameObject.getPosition().getY());    
        shape.updatePosition();
    }

    @Override
    public void update(float dt ) 
    {
        shape.setPosition(gameObject.getPosition().getX(), gameObject.getPosition().getY());    
        shape.updatePosition();


    
    }

    @Override
    public PrimitiveShape getShape() {
        return shape;
    }

    @Override
    public short layer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'layer'");
    }

    @Override
    public short mask() {
        return mask;
    }

    @Override
    public void setLayer(int layer, boolean active) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLayer'");
    }

    @Override
    public void setMask(short layer, boolean active) {
        this.mask = layer;
    }

    @Override
    public void detectCollison(Collider collider) {
        //boolean coll = org.Core.Utils.Math.CollisionDetector(this, collider);
       
        boolean isIntersects = this.shape.getMesh().intersects(collider.getShape().getMesh());

        //System.out.println(isIntersects);
    }

    @Override
    public boolean isSame(Collider collider) 
    {
        if(this.mask() == collider.mask() )
        {
            return true;
        }

        return false;
    }
    
}
