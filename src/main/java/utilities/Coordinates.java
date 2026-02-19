package main.java.utilities;

import java.util.Objects;

public class Coordinates {

    int x;
    int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static Coordinates posOf(int x, int y){
        return new Coordinates(x, y);
    }

    public double distanceBetween(Coordinates that){
        return Math.sqrt(Math.pow(this.x - that.x,2) + Math.pow(this.y - that.y, 2));
    }

    public boolean isNext(Coordinates that){
        return distanceBetween(that) <= 1 && !isSuperposed(that);
    }

    public boolean isSuperposed(Coordinates that){
        return this.x == that.x && this.y == that.y;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getY() {
        return y;
    }

    public void move(int x2, int y2){
        x += x2;
        y += y2;
    }

    public Coordinates translated(int x2, int y2){
        return new Coordinates(x + x2, y + y2);
    }

    public Coordinates translated(Coordinates that){
        return new Coordinates(x + that.x, y + that.y);
    }



}
