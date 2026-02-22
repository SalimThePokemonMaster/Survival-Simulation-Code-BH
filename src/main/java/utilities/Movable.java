package main.java.utilities;

import main.java.Game;
import java.util.stream.Stream;
import static main.java.utilities.Preconditions.require;

public interface Movable {

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
