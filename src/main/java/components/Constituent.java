package main.java.components;

import main.java.utilities.Coordinates;
import main.java.utilities.Wrapper;

public abstract class Constituent {
    public Coordinates coordinates;
    public int health;

    abstract void update(Wrapper inf);

    protected void reduce(int a){
        health -= a ;
    }

    public boolean isDead(){
        return health <= 0 ;
    }
}
