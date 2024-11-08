package org.Core.Component;

import org.Core.Game.Camera;
import org.Core.Game.Collider;
import org.Core.Utils.Math;


public class collisonHandler extends component {

    Math math;

    private Collider collider;

   public collisonHandler(Collider collider)
    {
        this.collider = collider;
    }

    @Override
    public void start() {
   
    }
    public void isCollison(Collider collider)
    {
        if(!this.collider.isSame(collider))
            this.collider.detectCollison(collider);

    }
    @Override
    public int compareTo(component o) {
        return 0;
    }
    @Override
    public void update(float dt) {
        gameObject.getScene().getCollider().stream().forEach(this::isCollison);
    }
    
}
