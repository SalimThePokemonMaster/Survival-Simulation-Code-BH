package main.java.components;

import main.java.Game;
import main.java.components.eatable.Eatable;
import main.java.utilities.*;
import java.util.Optional;
import java.util.Set;

import static main.java.utilities.Preconditions.require;

/**
 * Final class representing a {@code Peasant}. A  {@code Peasant} is a person, a character whose purpose is to survive on the map.
 * The {@code Peasant} acts as follows:
 * - During the day ({@code HELIOS}: he tries to eat the most {@link Eatable} he can.
 * - During the night ({@code ARTEMIS}): he goes into his {@link House} and sleeps until the next day.
 *
 * To survive, a {@code Peasant} has to eat at least a certain amount of {@link Eatable}. If he achieves that goal, he
 * survives and can reproduce with another {@code Peasant}, he dies otherwise.
 *
 * A {@code Peasant} is a {@link Movable} implying that to eat, to search for{@link Eatable} he simply moves.
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 *
 */
public final class Peasant extends Element implements Movable {

    /**
     * To diversify the behavior of a {@code Peasant}, he can have different states :
     *
     * <ul>
     *     <li>{@link #NORMAL}: Standart state of the {@code Peasant}, he tries to go to the nearest {@link Eatable} or
     *     to his {@link House}.</li>
     *
     *     <li>{@link #IDLE}: Inactivity state of the {@code Peasant} that tales in that implementation just one
     *     cycle. During that cycle, the {@code Peasant} will do nothing.</li>
     * </ul>
     *
     */
    public enum CharacterState {
        NORMAL,
        IDLE
    }

    // The number of eatables a peasant has to eat each day.
    public final static int QUANTITY_TO_EAT = 3;

    private final House myHouse;
    private CharacterState state;
    private int eaten;
    private boolean isAsleep;

    /**
     * {@code Peasant} constructor.
     * A {@code Peasant} is created in the {@code NORMAL} state.
     *
     * @param coordinates are the initial {@link Coordinates} of the {@code Peasant}.
     * @param house is the {@link House} to which this {@code Peasant} is linked.
     * @throws IllegalArgumentException if the given {@link Coordinates} are out of bound.
     */
    public Peasant(Coordinates coordinates, House house){
        require(coordinates.getX() >= 0 && coordinates.getX() <= Game.COLUMNS);
        require(coordinates.getY() > 0 && coordinates.getY() <= Game.COLUMNS);
        this.coordinates = coordinates;
        this.state = CharacterState.NORMAL;
        this.myHouse = house;
    }

    /**
     * Informs if this has eaten enough {@link Eatable} this day.
     *
     * @return true if this has eaten enough {@link Eatable}, false otherwise.
     */
    public boolean hasEatenEnough(){ return eaten >= QUANTITY_TO_EAT; }

    /**
     * Informs if this is 'sleeping' in his {@link House}.
     * Sleep occurs when it is night ({@code ARTEMIS}) and that the {@code Peasant} has returned to its {@link House}.
     *
     * @return true if this is sleeping,  false otherwise.
     */
    public boolean isAsleep() { return isAsleep; }

    /**
     * Private utility function finding the nearest {@link Eatable}.
     *
     * @param allEatable is the {@link Set} of all {@link Eatable}.
     * @return an {@link Optional} containing the nearest {@link Eatable}.
     */
    private Optional<Eatable> target(Set<Eatable> allEatable){
        return allEatable.stream()
            .reduce((a, b) -> {
                if (coordinates.distanceTo(a.coordinates) <= coordinates.distanceTo(b.coordinates)) {
                    return a;
                } else {
                    return b;
                }
            });
    }


    /**
     * The update function is the main part of the functioning of the simulation, each clock cycle, the function will
     * be called and makes an update, an evolution on the {@code Peasant}. The update function dictates the behaviour
     * of the {@code Peasant}.
     * The actions chosen by the {@code Peasant} are mostly chosen depending on two parameters, the current {@link main.java.Game.Period}
     * and the {@link CharacterState} of the {@code Peasant}.
     * Dee the {@link CharacterState} :
     *  - {@code IDLE}: in that state, the {@code Peasant} does nothing for a cycle and then switches automatically its state
     *  to {@code NORMAL}. That behavior is the same independently to the {@link main.java.Game.Period}.
     *  - {@code NORMAL}: int that state, the behavior of the {@code Peasant} is it's "normal" one. To determine it,
     *  knowing the {@link main.java.Game.Period} is necessary.
     *      - {@code HELIOS}: during the day, the {@code Peasant} has a certain probability to do nothing and to pass
     *      to {@code IDLE} state but in most cases, he will rush to the nearest {@link Eatable}, if he's at the same
     *      {@link Coordinates}, he eats it.
     *      - {@code ARTEMIS}: during the night, the {@code Peasant} rushes to his {@link House}, if he's at the same
     *      {@link Coordinates}, he enters into it and "falls asleep".
     *
     * @param allEatable is the {@link Set} of all {@link Eatable} present in the simulation.
     * @param actualPeriod is the actual {@link main.java.Game.Period} is the simulation.
     */
    public void update(Set<Eatable> allEatable, Game.Period actualPeriod) {
        switch (state){

            case NORMAL -> {

                if(actualPeriod == Game.Period.HELIOS){

                    //The peasant has a probability of 20% to stop itself for a cycle and do nothing.
                    if(Math.random() > 0.2){
                        //The nearest eatable is got
                        Optional<Eatable> targetO = target(allEatable);

                        //To exploit an Optional, checking the presence is necessary.
                        if (targetO.isPresent()){

                            Eatable target = targetO.get();

                            //Depending on the current coordinates of this peasant and of the eatable, this will rush
                            //to it or eat it.
                            if (target.coordinates.equals(coordinates)) {

                                //eat() function of eatable has as side effect to reduce the health of the considered
                                //eatable and to return whether it has been fully eaten or not.
                                if(target.eat()){
                                    eaten++;
                                }
                            } else {
                                coordinates = rush(coordinates, target.coordinates);
                            }

                        } else {
                            //If the target is not present in the Optional, the peasant passes to IDLE state because
                            //that case means that there is no eatable left.
                            state = CharacterState.IDLE;
                        }

                    } else {
                        state = CharacterState.IDLE;
                    }

                } else {

                    //Depending on the current coordinates of this peasant and of the house, this will rush
                    //to it or enter into it.
                    if(myHouse.coordinates.equals(coordinates)){
                        myHouse.enter(hasEatenEnough());
                        isAsleep = true;
                    } else {
                        coordinates = rush(coordinates, myHouse.coordinates);
                    }

                }
            }

            case IDLE -> state = CharacterState.NORMAL;
        }
    }

}