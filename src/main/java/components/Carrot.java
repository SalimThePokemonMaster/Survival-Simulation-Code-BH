package main.java.components;

import main.java.utilities.Coordinates;
import main.java.utilities.Wrapper;

public class Carrot extends Eatable {


    public Carrot(Coordinates coordinates){
        this.health = 1;
        this.coordinates = coordinates;
    }

    boolean eat(Peasant by) {
        if (isDead()) {
            return false;
        } else {
            health--;
            if (isDead()) {}
            return isDead();
        }
    }

    @Override
    boolean reduceHealth(Peasant by) {
        reduce(1); //????? est ce que on garde ca ?
        return false;
    }

    @Override
    void update(Wrapper inf) {

    }
}
