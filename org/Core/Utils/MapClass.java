package org.Core.Utils;

/*
 * @Param width, height of layer size
 */

import java.util.ArrayList;
import java.util.List;

public class MapClass  
 {
     int height;
     boolean infinite;
    public List<Data> layers = new ArrayList<>(0);
     int tileheight;
     int tilewidth;
     int width;
     double version;
    public int getLength()
    {
       return layers.get(0).chunks.size();
    }
    public int getData(int index, int chunks) 
    {
        int[]data = layers.get(0).data != null ? layers.get(0).data :  layers.get(0).chunks.get(chunks).data;
        if(data== null)
            return -1;
      
        return data[index];

    }
    public int getChunkLenght()
    {
        if(layers.get(0).chunks.get(0).data == null)
            return -1;
        
        return layers.get(0).chunks.size();
    }
    public float getChunksX(int Index)
    {


        return layers.get(0).chunks.get(Index).x;
    }
    public float getChunksY(int Index)
    {

        return layers.get(0).chunks.get(Index).y;
    }
    public float getHeight(String name)
    {
        for (Data data : layers) {
            
            if(data.name.equals(name))
            {
                
               return data.objects.get(0).height;
            }
        }
       return 0;
    }
    public float getWidth(String name)
    {
        for (Data data : layers) {
            if(data.name.equals(name))
            {
               return data.objects.get(0).width;
            }
        }
       return 0;
    }
    public float getX(String name)
    {
        for (Data data : layers) {
            if(data.name.equals(name))
            {
               return data.objects.get(0).width;
            }
        }
       return 0;
    }
    public float getY(String name)
    {
       for (Data data : layers) {
        if(data.name.equals(name))
        {
           return data.objects.get(0).width;
        }
    }
   return 0;
    }
 
}
 class Data 
 {
    public int[] data;
    String draworder;
    String name;
    List<Objects> objects = new ArrayList<>(0);
    List<Chunks> chunks = new ArrayList<>(0);
  
 }
 class Chunks
 {
    public int[] data;
    int x;
    int y;
 }
 /**
  * objects
  */
 class Objects {
     float height, width, x ,y;
     boolean visible;
 }