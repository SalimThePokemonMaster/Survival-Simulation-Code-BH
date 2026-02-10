package main.java.components;

import main.java.utilities.Coordinates;
import main.java.utilities.Movable;
import main.java.utilities.Wrapper;

import java.util.List;

public abstract class Constituent {
    public Coordinates coordinates;
    public int health;
    public Movable movable;

    public boolean canMove(){
        return movable == Movable.MOVABLE;
    }

    abstract void move(List<Eatable> allEatable,  List<Constituent> allConstituent);

    abstract void update(Wrapper inf);

    protected void reduce(int a){
        health -= a ;
    }

    public boolean isDead(){
        return health <= 0 ;
    }
}
