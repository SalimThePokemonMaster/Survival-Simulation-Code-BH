package main.java.components;

import main.java.utilities.*;

import java.util.List;
import java.util.stream.Stream;

public class Peasant extends Constituent implements Movable {
    private final House myHouse;
    private CharacterState state;
    private int eaten;


    public Peasant(Coordinates coordinates, House house){
        this.coordinates = coordinates;
        this.myHouse = house;
        this.state = CharacterState.NORMAL;
        this.eaten = 0;
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

    private Eatable nextEatable(List<Eatable> allEatable){
        return allEatable.stream().reduce(
                (a, b) -> {
                    if (coordinates.distanceBetween(a.coordinates) <= coordinates.distanceBetween(b.coordinates)) {
                        return a;
                    } else {
                        return b;
                    }
                }
        ).get();
    }


    @Override
    void update(Wrapper inf) {
        switch (state) {
            case NORMAL -> {
                if(inf.period == Period.ARTEMIS){
                    if(isFull()){
                        recover(inf.allConstituents);
                    } else {
                        die();
                    }
                } else {
                    if(isFull()){
                        state = CharacterState.IDLE;
                    } else {
                        if(distanceToNextEatable(inf.allEatable) <= 1){
                            nextEatable(inf.allEatable).reduceHealth(this);
                        } else {
                            move(inf.allEatable, inf.allConstituents);
                        }
                    }
                }
            }
            case IDLE -> {
                if(inf.period == Period.ARTEMIS){
                    if(isFull()){
                        if(coordinates.distanceBetween(myHouse.coordinates) <= 1){
                            //il faut faire en sorte ici que les coordonnées soient modifiées pour ne pas bloquer l'entrée
                            myHouse.enter(this);
                        } else {
                            recover(inf.allConstituents);
                        }
                    } else {
                        die();
                    }
                }
            }
            case SLEEP -> {
                sleep();
            }
            case DEAD -> {}
        }
    }


    @Override
    public void move(List<Eatable> allEatable, List<Constituent> allConstituent) {
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


    public void eat(int amount){
        eaten += amount;
    }

    private boolean isFull(){
        return eaten > 3 ;
    }

    public void recover(List<Constituent> allConstituent){ rush(myHouse.coordinates, allConstituent); }

    //peut etre passé dans movable si je rajoute en argument mes propres coordonnées
    public void rush(Coordinates toGo, List<Constituent> allConstituent){
        Coordinates predictedNextPosition = coordinates.translated(
            Stream.of(
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

        //if(!Utilities.isFree(predictedNextPosition, allConstituent)){
            coordinates = predictedNextPosition;
        //}
    }

    private void die(){
        reduce(health);
        state = CharacterState.DEAD;
    }

    public void sleep(){
        state = CharacterState.NORMAL;
        eaten = 0;
    }

    public void awake(Coordinates coordinates){
        state = CharacterState.NORMAL;
        this.coordinates = coordinates;
    }
}