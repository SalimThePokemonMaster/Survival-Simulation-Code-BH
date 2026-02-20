package main.java.utilities;

import main.java.components.House;
import main.java.components.eatable.Eatable;

import java.util.Set;

public class InfGroup {
    private Set<House> allHouses;
    private Set<Eatable> allEatable;
    private Period period;

    public Set<House> getAllHouses() {
        return Set.copyOf(allHouses);
    }

    public Set<Eatable> getAllEatable() {
        return Set.copyOf(allEatable);
    }

    public Period getPeriod() {
        return period;
    }

    public InfGroup(Set<House> allHouses, Set<Eatable> allEatable, Period period) {
        this.allEatable = allEatable;
        this.allHouses = allHouses;
        this.period = period;
    }
}
