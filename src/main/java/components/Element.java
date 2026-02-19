package main.java.components;

import main.java.components.eatable.Eatable;
import main.java.utilities.Coordinates;

import java.util.Set;

public abstract class Element {
    public Coordinates coordinates;
    public int health;

    protected void reduce(){
        health -= 1;
    }

    public boolean isDead(){
        return health <= 0 ;
    }
}
