package org.Core.Game;

import org.geotools.geometry.Position2D;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;

import org.locationtech.jts.geom.Polygon;

import glm_.vec2.Vec2;


public class PrimitiveShape {
    Vec2 Position = new Vec2();
    Vec2 Size = new Vec2();
    Position2D pos;
    protected GeometryBuilder gb = new GeometryBuilder();
    protected Polygon Polygon;
    public Coordinate[] mesh;

    protected PrimitiveShape(Vec2 size) {
        Size = size;
    }

    public Vec2 getPosition() {
        return Position;
    }

    public Polygon getMesh() {
        return Polygon;
    }

    public void setMesh() {
        Polygon = JTSFactoryFinder.getGeometryFactory().createPolygon(mesh);
    }

    public Vec2 getSize() {
        return Size;
    }

    public void setPosition(float x, float y) {
        Position.setX(x);
        Position.setY(y);
    }

    public void init()
    {
        
    }
    public void updatePosition() {
    }
}
