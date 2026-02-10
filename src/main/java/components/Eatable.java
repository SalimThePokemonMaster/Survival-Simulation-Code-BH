package main.java.components;

import main.java.utilities.Coordinates;

public abstract class Eatable extends Constituent{
    abstract boolean reduceHealth(Character by);
}