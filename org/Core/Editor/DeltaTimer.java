package org.Core.Editor;

public class DeltaTimer {
    float Time[];
    float lastTime[];

    
    double m_secondCounter = 0;
    double m_tempFps = 0;
    float fps;
    
    public DeltaTimer()
    {
        Time = new float[100];
        lastTime = new float[100];
    }
    public void setDelta(int index)
    {   
        lastTime[index] = System.currentTimeMillis();
    }
    public float getDelta(int index)
    {
        Time[index] = System.currentTimeMillis();
        return (float) (Time[index] - lastTime[index]);
    }

    public float getFPS(int index)
    {
        if (m_secondCounter <= 1) 
        {
            m_secondCounter += getDelta(index) / 1000;
            m_tempFps++;
        } else 
        {
            fps = (float)m_tempFps;

            m_secondCounter = 0;
            m_tempFps = 0;
            return fps;
        }
        return fps;
    }
}
