package main.java.components;

import main.java.utilities.Coordinates;
import main.java.utilities.Movable;
import main.java.utilities.Wrapper;

import java.util.List;

public class Carrot extends Eatable {


    public Carrot(Coordinates coordinates){
        this.health = 1;
        this.coordinates = coordinates;
        this.movable = Movable.FIXED;
    }

    boolean eat(Character by) {
        if (isDead()) {
            return false;
        } else {
            health--;
            if (isDead()) {}
            return isDead();
        }
    }

    @Override
    boolean reduceHealth(Character by) {
        reduce(1); //????? est ce que on garde ca ?
        return false;
    }

    @Override
    void move(List<Eatable> allEatable,  List<Constituent> allConstituent) {
        throw new IllegalArgumentException("A carrot can't move !");
    }

    @Override
    void update(Wrapper inf) {

    }
}
