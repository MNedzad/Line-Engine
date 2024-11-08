package org.Core.Utils;


import org.Core.Game.Collider;
import org.Core.Game.PrimitiveShape;

import glm.vec._2.Vec2;

public class Math {
    public static boolean CollisionDetector(Collider object1, Collider object2)
    {
        Vec2 pos = object1.getShape().getPosition();
        Vec2 pos2 = object2.getShape().getPosition();

        Vec2 size = object1.getShape().getSize();
        Vec2 size2 = object2.getShape().getSize();
 

        return (pos.x + size.x > pos2.x && pos.x < pos2.x + size2.x ) &&  (pos.y + size.y > pos2.y && pos.y < pos2.y + size2.y ) ;
    }
}
