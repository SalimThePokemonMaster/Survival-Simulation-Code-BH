package main.java.components;

import main.java.components.eatable.Eatable;
import main.java.utilities.*;

import java.util.Optional;
import java.util.Set;

public class Peasant extends Element implements Movable {
    private CharacterState state;
    private int eaten;
    private final House myHouse;
    private boolean isAsleep;

    public Peasant(Coordinates coordinates, House house){
        this.coordinates = coordinates;
        this.state = CharacterState.NORMAL;
        this.myHouse = house;
        this.eaten = 0;
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

    public void update(Set<Eatable> allEatable, Game.Period actualPeriod) {
        switch (state){
            case NORMAL -> {
                if(actualPeriod == Game.Period.HELIOS){
                    if(Math.random() > 0.4){
                        Optional<Eatable> targetO = target(allEatable);

                        if (targetO.isPresent()){
                            Eatable target = targetO.get();

                            if (target.coordinates.equals(coordinates)) {
                                if(target.eat()){
                                    eaten++;
                                }
                            } else {
                                coordinates = rush(coordinates, target.coordinates);
                            }

                        } else {
                            state = CharacterState.IDLE;
                        }

                    } else {

                    }

                } else {

                    if(myHouse.coordinates.equals(coordinates)){
                        myHouse.enter(hasEatenEnough());
                        isAsleep = true;
                    } else {
                        coordinates = rush(coordinates, myHouse.coordinates);
                    }

                }
            }
            case IDLE -> {
                if(actualPeriod == Game.Period.HELIOS){
                    if(Math.random() > 0.3) {
                        state = CharacterState.NORMAL;
                    }
                } else {
                    state = CharacterState.NORMAL;
                }
            }
        }
    }

    public boolean hasEatenEnough(){
        return eaten > 1;
    }

    public boolean isAsleep() {
        return isAsleep;
    }
}