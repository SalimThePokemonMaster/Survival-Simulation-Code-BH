package main.java.utilities;

import main.java.components.House;
import main.java.components.eatable.Eatable;

import java.util.Set;

public class Burger {
    private Set<House> allHouses;
    private Set<Eatable> allEatable;
    private Period period;

    public Set<House> getAllHouses() {
        return allHouses;
    }

    public Set<Eatable> getAllEatable() {
        return allEatable;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Burger(Set<House> allHouses, Set<Eatable> allEatable, Period period) {
        this.allEatable = allEatable;
        this.allHouses = allHouses;
        this.period = period;
    }

    public InfBurger toInfBurger(){
        return new InfBurger(Set.copyOf(allHouses), Set.copyOf(allEatable), period);
    }
}
