package main.java.components.eatable;

import main.java.components.Element;
import main.java.components.Peasant;

public abstract class Eatable extends Element {
    abstract boolean reduceHealth(Peasant by);
    public abstract boolean canBeEaten();
}