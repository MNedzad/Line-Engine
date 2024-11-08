package org.Core.Game.Animation;

import org.Core.Component.Sprite;

public class Frame {
    private Sprite sprite;
    private float frameTime;

    Frame(Sprite sprite, float frameTime)
    {
        this.sprite = sprite;
        this.frameTime = frameTime;
    }
    public Sprite getSprite() {
        return sprite;
    }
    public float getFrameTime() {
        return frameTime;
    }
}
