package main.java.components.eatable;

import main.java.Game;
import main.java.utilities.Coordinates;

import static main.java.utilities.Preconditions.require;

/**
 * Representes a {@code Carrot}.
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 */
public final class Carrot extends Eatable {

    //The carrot's health
    private final int MAX_HEALTH = 1;

    /**
     * Carrot's constructor.
     *
     * @param coordinates are the spawn {@link Coordinates} of this {@code Carrot}.
     * @throws IllegalArgumentException if the given {@link Coordinates} are out of bound.
     */
    public Carrot(Coordinates coordinates){
        require(coordinates.getX() >= 0 && coordinates.getX() <= Game.COLUMNS);
        require(coordinates.getY() > 0 && coordinates.getY() <= Game.COLUMNS);
        this.health = MAX_HEALTH;
        this.coordinates = coordinates;
    }

    /**
     * Redefinition of the abstract {@code getMAX_HEALTH} function to get the maximal health of a {@code Carrot}.
     *
     * @return the maximal health of a {@code Carrot}.
     */
    @Override
    protected int getMAX_HEALTH(){
        return MAX_HEALTH;
    }
}
