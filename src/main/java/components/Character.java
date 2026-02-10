package main.java.components;

import main.java.utilities.*;

import java.util.List;
import java.util.stream.Stream;

public class Character extends Constituent {
    private final House myHouse;
    private State state;

    public Character(Coordinates coordinates, House house){
        this.coordinates = coordinates;
        this.movable = Movable.MOVABLE;
        this.myHouse = house;
        this.state = State.NORMAL;
    }

    private double distanceToNextEatable(List<Eatable> allEatable){
        return allEatable.stream().map(e -> e.coordinates).reduce(
                (a, b) -> {
                    if (coordinates.distanceBetween(a) <= coordinates.distanceBetween(b)) {
                        return a;
                    } else {
                        return b;
                    }
                }
        ).get().distanceBetween(this.coordinates);
    }

    @Override
    void move(List<Eatable> allEatable, List<Constituent> allConstituent) {
        rush(
            allEatable.stream().map(e -> e.coordinates).reduce(
                (a, b) -> {
                    if (coordinates.distanceBetween(a) <= coordinates.distanceBetween(b)) {
                        return a;
                    } else {
                        return b;
                    }
                }
            ).orElse(coordinates),

            allConstituent
        );
    }

    @Override
    void update(Wrapper inf) {
        switch (state) {
            case NORMAL -> {
               if(inf.period == Period.ARTEMIS){
                   recover(inf.allConstituents);
               } else {

               }
            }
            case IDLE -> {}
        }

    }

    public boolean eat(Eatable toEat){
        toEat.
    }

    public void recover(List<Constituent> allConstituent){ rush(myHouse.coordinates, allConstituent); }

    public void rush(Coordinates to, List<Constituent> allConstituent){
        coordinates = coordinates.translated(
            Stream.of(
                new Coordinates(1, 0),
                new Coordinates(-1, 0),
                new Coordinates(0, 1),
                new Coordinates(0, -1)
            ).reduce(
                (a, b) -> {
                    if (coordinates.translated(a).distanceBetween(to) <= coordinates.translated(b).distanceBetween(to)) {
                        return a;
                    } else {
                        return b;
                    }
                }
            ).orElse(new Coordinates(0,0))
        );
    }
}