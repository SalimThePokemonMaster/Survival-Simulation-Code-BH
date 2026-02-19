package main.java.components;

import main.java.components.eatable.Eatable;
import main.java.utilities.Coordinates;
import main.java.utilities.Period;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Game {
    public Set<House> allHouses;
    public Set<Eatable> allEatable;
    public Period period;

    int cyclesPerPeasant = 5;
    int currentCycle = 0;

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
        if (currentCycle % cyclesPerPeasant == 0) {
            allHouses.forEach(e -> {
                    if (e.canGenerate()) peasantSet.add(e.generate());
            });
        }
        currentCycle++;

        allEatable = allEatable.stream()
                .filter(e -> !e.isDead())
                .collect(Collectors.toSet());

        peasantSet.forEach(p -> p.update(allEatable));
    }
}
