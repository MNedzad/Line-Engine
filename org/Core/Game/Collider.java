package org.Core.Game;

import java.util.Set;

public interface Collider  
{
     PrimitiveShape getShape();


     short layer();

     short mask();


     void setLayer(int layer, boolean active);

     void setMask(short layer, boolean active);
     public void detectCollison(Collider collider);
     public boolean isSame(Collider collider);
}
