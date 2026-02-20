package main.java.components;

import main.java.components.eatable.Eatable;
import main.java.utilities.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class Peasant extends Element implements Movable {
    private CharacterState state;
    private int eaten;
    private final House myHouse;

    public Peasant(Coordinates coordinates, House house){
        this.coordinates = coordinates;
        this.state = CharacterState.NORMAL;
        this.myHouse = house;
        this.eaten = 0;
    }

    //@Override
    public void update(InfGroup inf) {
        switch (state){
            case NORMAL -> {
                Optional<Eatable> targetO = target(inf.getAllEatable());

                if (targetO.isPresent()){
                    Eatable target = targetO.get();
                    if (target.coordinates.equals(coordinates)) {
                        eat(target);
                    } else {
                        rush(target.coordinates);
                    }
                } else {
                    state = CharacterState.IDLE;
                }

            }
            case SLEEP -> {}
            case IDLE -> {
               // if()
            }
            case DEAD -> {}
        }
    }

    public void update(Set<Eatable> allEatable) {
        switch (state){
            case NORMAL -> {}
            case SLEEP -> {}
            case IDLE -> {}
            case DEAD -> {}
        }
        move(allEatable);
    }

    private Optional<Eatable> target(Set<Eatable> allEatable){
        return allEatable.stream()
                .reduce((a, b) -> {
                    if (coordinates.distanceBetween(a.coordinates) <= coordinates.distanceBetween(b.coordinates)) {
                        return a;
                    } else {
                        return b;
                    }
                });
    }

    @Override
    public void move(Set<Eatable> allEatable) {


        /*if (targetO.isPresent()){
            Eatable target = targetO.get();
            if (target.coordinates.equals(coordinates)) {
                eat(target);
            } else {
                rush(target.coordinates);
            }
        } else {
            rush(myHouse.coordinates);
        }*/
    }

    public void eat(Eatable eatable){
        //eatable.reduce();
    }

    public void rush(Coordinates toGo){
        if (state == CharacterState.NORMAL) {
            if(Math.random() > 0.3) {
                coordinates = coordinates.translated(
                    Stream.of(
                        new Coordinates(0, 0),
                        new Coordinates(1, 0),
                        new Coordinates(-1, 0),
                        new Coordinates(0, 1),
                        new Coordinates(0, -1)
                    ).reduce(
                        (a, b) -> {
                            if (coordinates.translated(a).distanceBetween(toGo) <= coordinates.translated(b).distanceBetween(toGo)) {
                                return a;
                            } else {
                                return b;
                            }
                        }
                    ).get()
                );
            }
        }
    }


}