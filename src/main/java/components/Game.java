package main.java.components;

import main.java.components.eatable.Eatable;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    public Set<House> allHouses;
    public Set<Eatable> allEatable;
    public Period period;

    public int generation;

    public enum Period {
        HELIOS,
        ARTEMIS,
        HADES
    }

    int currentCycle = 0;
    final static int CYCLES_BEFORE_GENERATING = 1;
    final static int CYCLES_BEFORE_SLEEPING = 100;

    Set<Peasant> peasantSet = new HashSet<>();

    public Game(Set<House> houses, Set<Eatable> eatable, Period period) {
        this.allHouses = houses;
        this.allEatable = eatable;
        this.period = period;
    }

    public Set<Peasant> getPeasant() {
        return peasantSet;
    }

    public void update(){
        System.out.println("----------- [CLOCK = "+currentCycle+"] -----------");
        switch (period){
            case HELIOS:
                if (isPlayable(currentCycle, CYCLES_BEFORE_GENERATING)) {
                    allHouses.forEach(e -> {
                        if (e.canGenerate()) peasantSet.add(e.generate());
                    });
                }
                peasantSet.forEach(p -> p.update(allEatable.stream().filter(Eatable::hasNotBeenEaten).collect(Collectors.toSet()), period));

                allEatable.stream()
                        .filter(a -> !a.hasNotBeenEaten()).collect(Collectors.toSet())
                        .forEach(Eatable::revive);

                allHouses.forEach(h -> {
                    System.out.println(h.toString() + " : ");
                    System.out.println("Number toGenerate : " + h.getToGenerateThisDay());
                    System.out.println("Number actually generated : " + h.getPeasantGenerated());
                });

                currentCycle++;

                if (isPlayable(currentCycle, CYCLES_BEFORE_SLEEPING)){
                    period = Period.ARTEMIS;
                }
                break;
            case ARTEMIS:
                currentCycle = 0;
                allHouses.forEach(h -> {
                    System.out.println(h.toString() + " : ");
                    System.out.println("Number entered : "  + h.getPeasantEntered());
                    System.out.println("Number hasEatenEnough : " + h.getPeasantHasEatenEnough());
                });

                peasantSet = peasantSet.stream()
                        .filter(p -> {
                            p.update(allEatable, period);
                            return !p.isAsleep();
                        })
                        .collect(Collectors.toSet());

                if (allHouses.stream().allMatch(House::isEveryoneSleeping)){
                    allHouses.forEach(e -> e.setToGenerateThisDay((int) ((int) e.getPeasantHasEatenEnough()*1.5)));
                    if (allHouses.stream().allMatch(h -> h.getToGenerateThisDay() == 0)) {
                        period = Period.HADES;
                    } else {
                        period = Period.HELIOS;
                        generation++;
                    }
                }
                break;
            case HADES:
                break;
        }

    }

    public boolean isPlayable(int currentCycle, int cyclePeriod){
        return currentCycle % cyclePeriod == 0;
    }
}
