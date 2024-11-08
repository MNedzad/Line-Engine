package org.Core.Component;

import java.util.ArrayList;
import java.util.List;

import glm_.vec2.Vec2;



public class SpriteSheet {
    List<Sprite> SpriteList = new ArrayList<>();
    Shade shade;
    Texture2D texture;
    Vec2 size;

    public SpriteSheet(Texture2D texture) {
        this.texture = texture;

    }

    public void create(int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        size = new Vec2(spriteWidth, spriteHeight);
        Sprite sprite = new Sprite(texture);
        int currentX = 0;
        int currentY = sprite.texture.getHeight() - spriteHeight;
        Sprite sp;
        for (int i = 0; i < numSprites; i++) {
            sp = new Sprite(texture);
            float topY = (currentY + spriteHeight) / (float) sprite.texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) sprite.texture.getWidth();
            float leftX = currentX / (float) sprite.texture.getWidth();
            float bottomY = currentY / (float) sprite.texture.getHeight();

            Vec2[] texCoords = {
                    new Vec2(rightX, topY),
                    new Vec2(rightX, bottomY),
                    new Vec2(leftX, bottomY),
                    new Vec2(leftX, topY)
            };
            sp.texCoords = texCoords;

            SpriteList.add(sp);
            currentX += spriteWidth + spacing;
            if (currentX >= sprite.texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }

        }
        
    }
    public int getSize()
    {
        return SpriteList.size();
    }
    public Sprite getSprite(int index) {

        return SpriteList.get(index);
    }

}
