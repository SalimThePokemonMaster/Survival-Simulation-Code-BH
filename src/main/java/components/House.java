package main.java.components;

import main.java.Game;
import main.java.utilities.Coordinates;

import static main.java.utilities.Preconditions.ensure;
import static main.java.utilities.Preconditions.require;

public final class House extends Element {

    private int peasantEntered;
    private int peasantHasEatenEnough;

    private int peasantGenerated;
    private int toGenerateThisDay;

    private final static int MAX_CAPACITY  = 10;
    private final static int DEFAULT_CREATION_GENERATED  = 2;
    private final Coordinates spawnPos;

    public House(Coordinates coordinates, int toGenerateThisDay){
        require(coordinates.getX() >= 0 && coordinates.getX() <= Game.COLUMNS);
        require(coordinates.getY() > 0 && coordinates.getY() <= Game.COLUMNS);
        this.coordinates = coordinates;
        this.toGenerateThisDay = toGenerateThisDay;
        this.spawnPos = coordinates.translated(0,1);
    }

    public House(Coordinates coordinates){
        this(coordinates, DEFAULT_CREATION_GENERATED);
    }

    public int getPeasantEntered() {
        return peasantEntered;
    }

    public int getPeasantHasEatenEnough() {
        return peasantHasEatenEnough;
    }

    public int getPeasantGenerated() {
        return peasantGenerated;
    }

    public int getToGenerateThisDay() {
        return toGenerateThisDay;
    }


    public void setToGenerateThisDay(int toGenerate){
        toGenerateThisDay = toGenerate;
        peasantEntered = 0;
        peasantGenerated = 0;
        peasantHasEatenEnough = 0;
    }

    public Peasant generate(){
        peasantGenerated++;
        return new Peasant(spawnPos, this);
    }

    //si on fait le système qui fait les nouvelles maisons alors ici ca va un peu poser problème
    public boolean canGenerate(){
        ensure(
                peasantGenerated <= MAX_CAPACITY,
                "State and content of house : " + this.toString() + ", is not coherent."
        );
        return peasantGenerated < toGenerateThisDay && peasantGenerated < MAX_CAPACITY;
    }

    public boolean isEveryoneSleeping(){
        ensure(
                peasantGenerated >= peasantEntered,
                "State and content of house : " + this.toString() + ", is not coherent."
        );
        return peasantEntered == peasantGenerated;
    }

    public void enter(boolean hasEatenEnough){
        peasantEntered++;
        if(hasEatenEnough) peasantHasEatenEnough++;
        ensure(
                peasantHasEatenEnough <= peasantEntered && peasantEntered <= MAX_CAPACITY,
                "State and content of house : " + this.toString() + ", is not coherent."
        );
    }
}
