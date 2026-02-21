package main.java.components.eatable;

import main.java.components.Element;
import main.java.components.Peasant;
import main.java.utilities.Coordinates;

import java.rmi.NoSuchObjectException;

public abstract class Eatable extends Element {
    protected int health;

    public boolean eat(){
        health -= 1;
        return !hasNotBeenEaten() ;
    }

    public boolean hasNotBeenEaten(){
        return health >= 0;
    }

    protected abstract int getMAX_HEALTH();

    public void revive(){
        int n = java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 50);
        int n2 = java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 40);
        coordinates = new Coordinates(n, n2);
        health = getMAX_HEALTH();
    }
}