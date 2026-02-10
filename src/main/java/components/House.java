package main.java.components;

import main.java.utilities.Coordinates;
import main.java.utilities.Movable;
import main.java.utilities.Wrapper;

import java.util.List;

public class House extends Constituent{
    int remaining = 1;

    public House(Coordinates coordinates){
        this.coordinates = coordinates;
        this.movable = Movable.FIXED;
    }


    /*public Character update() {
        if (remaining > 0) {
            remaining--;
            return new Character(this.coordinates.translated(0, 1), this);
        } else {
            return null;
        }
    }*/

    @Override
    void move(List<Eatable> allEatable,  List<Constituent> allConstituent) {
        throw new IllegalArgumentException("A carrot can't move !");
    }

    @Override
    void update(Wrapper inf) {

    }
}
