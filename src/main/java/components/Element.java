package main.java.components;

import main.java.utilities.Coordinates;
import main.java.utilities.Group;

public abstract class Element {
    public Coordinates coordinates;
    public int health;

    protected void reduce(){
        health -= 1;
    }

    public boolean isDead(){
        return health <= 0 ;
    }

    abstract void update(Group inf);
}
