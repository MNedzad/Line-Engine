package org.Core.Game;

import java.util.ArrayList;
import java.util.List;

import org.Core.Component.component;

public class GameObject 
{
   private List<component> Obj;

    public GameObject()
    {
        Obj = new ArrayList<>();
    }


    public void addObject(component object)
    {
        Obj.add(object);
    }

    public void Start()
    {
        for (int i = 0; i < Obj.size(); i++) 
        {
            Obj.get(i).start();
        }
    }
    public void Update(float dt)
    {
        for (int i = 0; i < Obj.size(); i++) 
        {
            Obj.get(i).update(dt);;
        }
    }
}
