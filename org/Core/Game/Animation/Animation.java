package org.Core.Game.Animation;

import java.util.ArrayList;
import java.util.List;

import org.Core.Component.Sprite;


public class Animation  {
    public final float Tick = 1000;
    String name;
    List<Frame> frames;
    int currentFrame = 0;
    private float time = 0.0f;
    boolean repeat = true;
    private static Sprite defaultSprite = new Sprite();

    public Animation()
    {
       
        frames = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }
    public void addFrame(Sprite sprite,  float FrameTime) {

        // Define new frame 
        Frame frame = new Frame(sprite, FrameTime * Tick);
        // push frame in List 
        frames.add(frame);
    }
    public void removeFrame(int index)
    {
        frames.remove(index);
    }
    float frameInterval;
    public void update(float dt)
    {
        if (!(currentFrame < frames.size())) return;

        // Get frame time of current Frame
        frameInterval = frames.get(currentFrame).getFrameTime();

        // increas time by delta time
        time += dt;
        /*
         * change frame when frameInterval is less than time 
         */
        while(time >= frameInterval )
        {   
            time -= frameInterval;
            currentFrame = (currentFrame + 1) % frames.size();
        }

    }

    public Sprite getCurrentFrame()
    {
        if(currentFrame < frames.size())
        {
            return frames.get(currentFrame).getSprite();
        }
        return defaultSprite;
    }
}
