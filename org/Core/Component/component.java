package org.Core.Component;



public abstract class  component implements Comparable<component> {
    public GameObject gameObject = null ;
  
    public component() 
    {

    }

    public abstract void start( );

    public abstract  void update(float dt );

}
