package main.java.components;

import main.java.utilities.Coordinates;

/**
 * Abstract class representing any compound of this simulation that will be created and displayed. Each one of them is
 * an {@code Element} if it has a presence on the map.
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 */
public abstract class Element {
    protected Coordinates coordinates;

    /**
     * Getter for the element's {@link Coordinates}.
     *
     * @return this element's {@link Coordinates}.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

}
