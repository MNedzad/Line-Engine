package org.Core.Component;

import org.Core.Game.PrimitiveShape;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import glm_.vec3.*;
import glm_.vec2.*;

public class Sprite {
    public Texture2D texture;

    public Vec2[] texCoords;
    public Vec3 Color[];

    public Sprite(Texture2D texture) {
        this.texture = texture;
        Vec2[] texCoords = {
                new Vec2(1, 1),
                new Vec2(1, 0),
                new Vec2(0, 0),
                new Vec2(0, 1)
        };
        Color = new Vec3[] {
                new Vec3(1, 1, 1),
                new Vec3(1, 1, 1),
                new Vec3(1, 1, 1),
                new Vec3(1, 1, 1)
        };
        this.texCoords = texCoords;
    }

    public Sprite() {
        Vec2[] texCoords = {
                new Vec2(1, 1),
                new Vec2(1, 0),
                new Vec2(0, 0),
                new Vec2(0, 1)
        };
        Color = new Vec3[] {
                new Vec3(1, 1, 1),
                new Vec3(1, 1, 1),
                new Vec3(1, 1, 1),
                new Vec3(1, 1, 1)
        };
        this.texCoords = texCoords;
    }
    public void setTexture(Texture2D texture) {
        this.texture = texture;
    }
    public Texture2D getTexture() {
        return this.texture;
    }

    public Sprite setColor(Vec3[] color) {
        Color = color;
        return this;
    }

    public Vec2[] getTexCoords() {

        return this.texCoords;
    }
}
