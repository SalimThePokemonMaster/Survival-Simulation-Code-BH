package main.java.components.eatable;

import main.java.components.Peasant;
import main.java.utilities.Coordinates;

import java.util.Set;

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
        reduce(); //????? est ce que on garde ca ?
        return false;
    }

    public boolean canBeEaten() {
        return !isDead();
    }
}
