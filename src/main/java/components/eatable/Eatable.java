package main.java.components.eatable;

import java.util.Set;
import main.java.Game;
import main.java.components.House;
import main.java.components.Element;
import main.java.utilities.Coordinates;
import java.util.concurrent.ThreadLocalRandom;
import static main.java.utilities.Preconditions.ensure;
import static main.java.utilities.Preconditions.require;

/**
 * Abstract class representing any eatable {@link Element} in the survival simulation.
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 *
 */
public abstract class Eatable extends Element {

    //The health of the corresponding eatable
    protected int health;

    /**
     * Function public accessible for each one that has access to the eatable. It is a function meant to be used by the others so
     * they can eat this {@code Eatable}. To do so, it reduces {@code health} and if it passes under 0, the {@code Eatable}
     * is considered dead and completely eaten.
     *
     * To notify the eater if the {@code Eatable} is still "alive" or not the function returns a boolean.
     *
     * @return true if the health is under or equal to 0, false otherwise.
     */
    public boolean eat(){
        health -= 1;
        return !hasNotBeenEaten() ;
    }

    /**
     * Utility function informing on the "living status" of this {@code Eatable}.
     *
     * @return true if the eatable is alive (health strictly bigger than 0), false otherwise.
     */
    public boolean hasNotBeenEaten(){ return health > 0; }

    /**
     * Abstract function meant to allow each {@code Eatable} that will be implemented to have its own MAX_HEALTH.
     * The function doesn't require the presence in each child of a static constant named MAX_HEALTH, but it encourages
     * it with the signature of the function. Forcing the presence of a final static constant that would need to be defined
     * in each child is not possible in Java, so this is a possible solution.
     *
     * @return the maximal health of the {@code Eatable}.
     */
    protected abstract int getMAX_HEALTH();

    /**
     * Revives this {@code Eatable}. Reviving implies :
     * - new {@link Coordinates} which have to be :
     *      - obviously inside the map.
     *      - not the same as a {@link House} (superposition on a {@link main.java.components.Peasant or annother
     *      {@code Eatable} is accepted}).
     * - restoring {@code health} to its maximum.
     *
     * @param allHouses is the {@link Set} of all {@link House} in this simulation instance.
     * @throws IllegalStateException if :
     *  - we try to revive an {@code Eatable} still alive
     *  - the respawn {@link Coordinates} are out of bound
     */
    public void revive(Set<House> allHouses){
        ensure(!hasNotBeenEaten(), "An eatable that is still alive can't be revived.");
        int a,b;
        Coordinates c;
        boolean d;

        do{
            //Generate new coordinates
            a = ThreadLocalRandom.current().nextInt(0, Game.COLUMNS);
            b = ThreadLocalRandom.current().nextInt(0, Game.LINES);
            ensure(a >= 0 && a <= Game.COLUMNS, "The X coordinate chosen for an eatable respawn is out of bound : " + a);
            ensure(b >= 0 && b <= Game.LINES, "The Y coordinate chosen for an eatable respawn is out of bound : " + b);
            c = new Coordinates(a, b);
            Coordinates finalC = c;

            //Check if the generated coordinates are the same as a house's ones
            d = allHouses.stream().noneMatch(x -> x.getCoordinates().isSuperposed(finalC));
        } while (!d);

        coordinates = c;
        health = getMAX_HEALTH();
    }
}