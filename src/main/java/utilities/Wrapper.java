package main.java.utilities;

import main.java.components.Constituent;
import main.java.components.Eatable;

import java.util.List;

public class Wrapper {
    public List<Constituent> allConstituents;
    public List<Eatable> allEatable;
    public Period period;

    public Wrapper(List<Constituent> constituents, List<Eatable> eatable, Period period) {
        this.allConstituents = constituents;
        this.allEatable = eatable;
        this.period = period;
    }
}
