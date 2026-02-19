package main.java.utilities;

import main.java.components.eatable.Eatable;

import java.util.Set;

public interface Movable {
    public abstract void move(Set<Eatable> allEatable);
}
