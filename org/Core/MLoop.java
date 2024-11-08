package org.Core;



public class MLoop {
    private double beginTime = 0;
    public double deltaTime;
    protected void Loop()
    {
        beginTime = System.nanoTime()/1e9;
        System.out.println("AS");
    }

    public void setDelta()
    {
        double endTime = System.nanoTime()/1e9;

        deltaTime = endTime- beginTime;
    }
    public double getDelta()
    {
        return deltaTime;
    }
}
