package main.java.components.eatable;

import main.java.components.Peasant;
import main.java.utilities.Coordinates;

public class Carrot extends Eatable {
    private final int MAX_HEALTH = 1;

    public Carrot(Coordinates coordinates){
        this.health = MAX_HEALTH;
        this.coordinates = coordinates;
    }

    @Override
    protected int getMAX_HEALTH(){
        return MAX_HEALTH;
    }

}
