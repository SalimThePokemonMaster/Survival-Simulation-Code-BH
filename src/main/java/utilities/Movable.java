package main.java.utilities;

import main.java.Game;
import java.util.stream.Stream;
import static main.java.utilities.Preconditions.require;

/**
 * Interface meant to be implemented by any {@link main.java.components.Element} that will have to move during
 * the simulation. To that purpose, offers and defines a default rush moving function that makes a move of one unit
 * in the direction we want to go.
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 */
public interface Movable {

    /**
     * Rush implemented by default function. Given the start {@link Coordinates} and the ending ones, simulates one step,
     * one move on the start {@link Coordinates} and returns the result {@link Coordinates}.
     *
     * @param myCoordinates are the start {@link Coordinates}.
     * @param toGo are the ending {@link Coordinates}.
     * @return {@code myCoordinates} translated by one step in the direction of {@code toGo}.
     * @throws IllegalArgumentException if the given {@link Coordinates} to go to are out of bound.
     */
    default Coordinates rush(Coordinates myCoordinates, Coordinates toGo){
        require(toGo.getX() >= 0 && toGo.getX() <= Game.COLUMNS);
        require(toGo.getY() >= 0 && toGo.getY() <= Game.LINES);

        return myCoordinates.translated(
            Stream.of(
                new Coordinates(0, 0),
                new Coordinates(1, 0),
                new Coordinates(-1, 0),
                new Coordinates(0, 1),
                new Coordinates(0, -1)
            ).reduce(
                (a, b) -> {
                    if (myCoordinates.translated(a).distanceTo(toGo) <= myCoordinates.translated(b).distanceTo(toGo)) {
                        return a;
                    } else {
                        return b;
                    }
                }
            ).get()
        );
    }
}
