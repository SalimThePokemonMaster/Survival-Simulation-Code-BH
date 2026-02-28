package main.java.utilities;

import java.util.Objects;

/**
 * Final class representing {@code Coordinates}.
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 */
public final class Coordinates {

    //Coordinates' attributes
    private int x;
    private int y;

    /**
     * {@code Coordinates} constructor.
     *
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Override of equals function defining how to compare to say two {@code Coordinates} are equal.
     *
     * @param o is the object we want to know if it is equal to this.
     * @return true if {@code o} is equal to {@code this}, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    /**
     * Hash function of {@code Coordinates}.
     *
     * @return {@code this} hashed {@code Coordinates}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Getter for attribute x.
     *
     * @return attribute x.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for attribute y.
     *
     * @return attribute y.
     */
    public int getY() {
        return y;
    }

    /**
     * Gives the distance between two {@code Coordinates}.
     *
     * @param that is the other {@code Coordinates} we want the distance to.
     * @return the distance between {@code this} and {@code that}.
     */
    public double distanceTo(Coordinates that){
        return Math.sqrt(Math.pow(this.x - that.x,2) + Math.pow(this.y - that.y, 2));
    }

    /**
     * Says if two {@code Coordinates} are superposed, in other words, if they are equal.
     *
     * @param that is the {@code Coordinates} we want to know if it is superposed on {@code this}.
     * @return true if the coordinates are equal, false otherwise.
     */
    public boolean isSuperposed(Coordinates that){
        return equals(that);
    }

    /**
     * Gives new coordinates that are a translation of this.
     *
     * @param x2 is the x coordinate translation to do.
     * @param y2 is the y coordinate translation to do.
     * @return new {@code Coordinates} translated by the given parameter coordinates.
     */
    public Coordinates translated(int x2, int y2){
        return new Coordinates(x + x2, y + y2);
    }

    /**
     * Gives new coordinates that are a translation of this.
     *
     * @param that are the coordinates to translate.
     * @return new {@code Coordinates} translated by the given parameter coordinates.
     */
    public Coordinates translated(Coordinates that){
        return translated(that.getX(), that.getY());
    }

}
