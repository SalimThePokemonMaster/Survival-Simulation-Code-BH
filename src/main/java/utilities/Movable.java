package main.java.utilities;

import java.util.stream.Stream;

public interface Movable {

    default Coordinates rush(Coordinates myCoordinates, Coordinates toGo){
        return myCoordinates.translated(
                Stream.of(
                        new Coordinates(0, 0),
                        new Coordinates(1, 0),
                        new Coordinates(-1, 0),
                        new Coordinates(0, 1),
                        new Coordinates(0, -1)
                ).reduce(
                        (a, b) -> {
                            if (myCoordinates.translated(a).distanceBetween(toGo) <= myCoordinates.translated(b).distanceBetween(toGo)) {
                                return a;
                            } else {
                                return b;
                            }
                        }
                ).get()
        );
    }
}
