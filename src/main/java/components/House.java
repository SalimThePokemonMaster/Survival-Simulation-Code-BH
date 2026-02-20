package main.java.components;

import main.java.utilities.Coordinates;
import main.java.utilities.InfGroup;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;

public final class House extends Element {
    int registered = 0;
    final int MAX_CAPACITY = 2;
    Deque<Peasant> sleeping;

    List<Coordinates> CONSTANT_SPAWNS =
        List.of(
            new Coordinates(1, 0),
            new Coordinates(-1, 0),
            new Coordinates(0, 1),
            new Coordinates(0, -1),
            new Coordinates(1,1),
            new Coordinates(1, -1),
            new Coordinates(-1, 1),
            new Coordinates(-1, -1)
        );

    public House(Coordinates coordinates){
        this.coordinates = coordinates;
        this.registered = 0;
        this.sleeping = new ArrayDeque<>();
    }

    public boolean canGenerate(){
        return registered < MAX_CAPACITY;
    }
    public Peasant generate(){
        registered++;
        return new Peasant(spawnPos(), this);
    }

    public Coordinates spawnPos(){
        return coordinates.translated(0,1);
    }

    @Override
    public void update(InfGroup inf) {

    }
}
