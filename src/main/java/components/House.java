package main.java.components;

import main.java.utilities.Coordinates;

public final class House extends Element {
    private int peasantGenerated;
    private int peasantEntered;
    private int peasantHasEatenEnough;
    private int toGenerateThisDay;

    final int MAX_CAPACITY  = 10;

    public House(Coordinates coordinates, int toGenerateThisDay){
        this.coordinates = coordinates;
        this.toGenerateThisDay = toGenerateThisDay;
    }

    public House(Coordinates coordinates){
        this(coordinates, 2);
    }

    public boolean isEveryoneSleeping(){
        return peasantEntered == peasantGenerated;
    }

    public void setToGenerateThisDay(int toGenerate){
        toGenerateThisDay = toGenerate;
        peasantEntered = 0;
        peasantGenerated = 0;
        peasantHasEatenEnough = 0;
    }

    public boolean canGenerate(){
        return peasantGenerated < toGenerateThisDay && peasantGenerated < MAX_CAPACITY;
    }
    
    public Peasant generate(){
        peasantGenerated++;
        return new Peasant(spawnPos(), this);
    }

    public int getPeasantGenerated() {
        return peasantGenerated;
    }

    public int getPeasantEntered() {
        return peasantEntered;
    }

    public int getPeasantHasEatenEnough() {
        return peasantHasEatenEnough;
    }

    public int getToGenerateThisDay() {
        return toGenerateThisDay;
    }

    private Coordinates spawnPos(){
        return coordinates.translated(0,1);
    }


    public void enter(boolean hasEatenEnough){
        peasantEntered++;
        if(hasEatenEnough) peasantHasEatenEnough++;
    }

}
