package main.java.utilities;

import main.java.components.Constituent;
import main.java.components.Eatable;

import java.util.List;

public interface Movable {
    public abstract void move(List<Eatable> allEatable, List<Constituent> allConstituent);

}
