package org.Core.Game.Animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.Core.Component.component;
import org.Core.Rendering.Mesh;

public class Animator extends component {

    List<Animation> animations = new ArrayList<>();
    public HashMap<StateMap, String> stateTransfers = new HashMap<>();
    Animation anim = null;
    private String defaultStateTitle;
    
    public void setDefaultState(String defaultStateTitle) {
        for(Animation anim : animations)
        {
            if(!(anim.name.equals(defaultStateTitle))) continue;

            this.anim = anim;
        }
    }

    public void addTrigger(String Trigger, String from, String to)
    {
        stateTransfers.put(new StateMap(from, Trigger), to);
    }
    public void addAnimation(Animation anim)
    {
        animations.add(anim);
    }
    public void Play(String Title)
    {
        for(StateMap map : stateTransfers.keySet())
        {
            if(!(map.equals(anim.name) && map.equals(Title))) continue;

            if(stateTransfers.get(map) == null) break;
            
            int newStateIndex = stateIndexOf(stateTransfers.get(map));
                
            if(newStateIndex < -1) break;
            
            anim = animations.get(newStateIndex);
        }
    }
    private int stateIndexOf(String stateTitle) {
        int index = 0;
        for (Animation anim : animations) {
            if (!anim.name.equals(stateTitle)) {
                index++;
                continue;
            }
           
            return index;
        }
        return -1;
    }
    @Override
    public void start() 
    {
        for(Animation anim : animations)
        {
            if(!anim.name.equals(defaultStateTitle))  continue;
            

            this.anim = anim;
            break;
        }
    }

    @Override
    public void update(float dt) 
    {
        if(anim != null)
        {
            anim.update(dt);
            Mesh object = gameObject.getComponent(Mesh.class);
            if(object != null)
            {
        
                object.setSprite(anim.getCurrentFrame());
            }
        }
    }


    @Override
    public int compareTo(component o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
    
}

/**
 * StateMap
 */
class StateMap 
{
        public String state;
        public String title;

        public StateMap() {}

        public StateMap(String state, String title) {
            this.state = state;
            this.title = title;
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != StateMap.class) return false;
            StateMap t2 = (StateMap)o;
            return t2.title.equals(this.title) && t2.state.equals(this.state);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, title);
        }
    
}