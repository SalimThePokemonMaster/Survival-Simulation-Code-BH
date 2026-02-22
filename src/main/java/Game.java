package main.java;

import main.java.components.House;
import main.java.components.Peasant;
import main.java.components.eatable.Carrot;
import main.java.components.eatable.Eatable;
import main.java.utilities.Coordinates;
import main.java.utilities.Utilities;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static main.java.utilities.Preconditions.ensure;

public class Game {
    private Set<House> allHouses;
    private Set<Eatable> allEatable;
    private Set<Peasant> peasantSet = new HashSet<>();

    private Period period;

    private int day;

    public boolean newLoggerDay;

    public enum Period {
        HELIOS,
        ARTEMIS,
        HADES
    }

    int currentCycle = 0;
    final static int CYCLES_BEFORE_GENERATING = 1;
    final static int CYCLES_BEFORE_SLEEPING = 100;


    public Game(Set<House> houses, Set<Eatable> eatables, Period period) {
        this.allHouses = houses;
        this.allEatable = eatables;
        this.period = period;
    }

    public Game(){
        Set<House> houses = generateHouses();
        Set<Eatable> eatables = generateEatables(houses);
        this(houses, eatables, Period.HELIOS);
    }

    public Set<House> getAllHouses() {
        return allHouses;
    }

    public Set<Eatable> getAllEatable() {
        return allEatable;
    }

    public Period getPeriod() {
        return period;
    }

    public Set<Peasant> getPeasant() {
        return peasantSet;
    }

    public int getDay() {
        return day;
    }

    private static Set<House> generateHouses(){
        Set<House> a = new HashSet<>();
        int n = ThreadLocalRandom.current().nextInt(1, 16);
        for(int i = 0; i < n; i++){
            Coordinates b = generateCoordinates(a);
            int n2 = ThreadLocalRandom.current().nextInt(2, 11);
            House house = new House(b, n2);
            a.add(house);
        }
        ensure(a.size() == n, "The outcome of the house generating process is unsuccessfull.");
        return a;
    }

    private static Set<Eatable> generateEatables(Set<House> allHouses){
        Set<Eatable> a = new HashSet<>();
        int n = allHouses.size() * 3 * 10;
        for(int i = 0; i < n; i++){
            Coordinates b = generateCoordinates(allHouses);
            Carrot carrot = new Carrot(b);
            a.add(carrot);
        }
        ensure(a.size() == n, "The outcome of the eatable generating process is unsuccessfull.");
        return a;
    }

    private static Coordinates generateCoordinates(Set<House> allHouses){
        boolean b;
        int x,y;
        Coordinates d;
        do{
            x = ThreadLocalRandom.current().nextInt(0, Utilities.MAP_WIDTH);
            y = ThreadLocalRandom.current().nextInt(1, Utilities.MAP_HEIGTH - 1);
            ensure(x >= 0 && x <= Utilities.MAP_WIDTH, "The X coordinate chosen for a carrot respawn is out of bound : " + x);
            ensure(y >= 0 && y < Utilities.MAP_HEIGTH, "The Y coordinate chosen for a carrot respawn is out of bound : " + y);
            d = new Coordinates(x, y);
            Coordinates finalD = d;
            b = allHouses.stream().noneMatch(k -> k.getCoordinates().isSuperposed(finalD));
        } while (!b);

        return d;
    }


    public void update(){
        switch (period){
            case HELIOS -> {

                if (clock(currentCycle, CYCLES_BEFORE_GENERATING)) {
                    allHouses.forEach(e -> {
                        if (e.canGenerate()) peasantSet.add(e.generate());
                    });
                }

                peasantSet.forEach(p -> p.update(allEatable.stream().filter(Eatable::hasNotBeenEaten).collect(Collectors.toSet()), period));

                allEatable.stream()
                        .filter(a -> !a.hasNotBeenEaten()).collect(Collectors.toSet())
                        .forEach(a -> a.revive(allHouses));

                currentCycle++;

                if (clock(currentCycle, CYCLES_BEFORE_SLEEPING)) {
                    period = Period.ARTEMIS;
                }
            }

            case ARTEMIS -> {
                currentCycle = 0;

                peasantSet = peasantSet.stream()
                        .filter(p -> {
                            p.update(allEatable, period);
                            return !p.isAsleep();
                        })
                        .collect(Collectors.toSet());

                if (allHouses.stream().allMatch(House::isEveryoneSleeping)){
                    allHouses.forEach(e -> e.setToGenerateThisDay((int) (e.getPeasantHasEatenEnough()*1.5)));
                    if (allHouses.stream().allMatch(h -> h.getToGenerateThisDay() == 0)) {
                        period = Period.HADES;
                    } else {
                        period = Period.HELIOS;
                        day++;
                        newLoggerDay = true;
                    }
                }
            }

            case HADES -> {
                day++;
                newLoggerDay = true;
            }
        }

    }

    public boolean clock(int currentCycle, int cyclePeriod){
        return currentCycle % cyclePeriod == 0;
    }

    public int getTotalPopulation(){
        return allHouses.stream().map(House::getToGenerateThisDay).reduce(Integer::sum).get();
    }
}
