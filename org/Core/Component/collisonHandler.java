package org.Core.Component;

import org.Core.Game.Collider;



public class collisonHandler extends component {

    
    private Collider collider;

   public collisonHandler(Collider collider)
    {
        this.collider = collider;
    }

    @Override
    public void start() {
   
    }

    /*
     *  Check colider of object
     *  Prevent same collision 
     */
    public void isCollison(Collider collider)
    {
        if(!this.collider.isSame(collider))
            this.collider.detectCollison(collider);
            // checks if there is a collision with object
    }
    @Override
    public int compareTo(component o) {
        return 0;
    }
    
    /*
     * Every Tick Loop
     */
    @Override
    public void update(float dt) {
        gameObject.getScene().getCollider().stream().forEach(this::isCollison);
        // Get all colider on scene and check colider 
    }
    
}
