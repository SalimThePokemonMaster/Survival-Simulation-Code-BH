package main.java.components;

import main.java.utilities.Coordinates;
import main.java.utilities.Wrapper;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public final class House extends Constituent {
    int registered;
    final int MAX_CAPACITY = 8;
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

    @Override
    void update(Wrapper inf) {
        switch (inf.period){
            case HELIOS -> {
                if(hasSleepers()){
                    wakeEveryone();
                }
            }
            case ARTEMIS -> {
                if(isFulledWithSleepers()){

                } else {

                }
            }
        }

    }

    public void enter(Peasant peasant){
        peasant.sleep();
        sleeping.push(peasant);
    }

    public void registeredDied(){
        registered--;
    }

    public boolean isFullyRegistered(){
        return registered == MAX_CAPACITY;
    }

    public boolean isFulledWithSleepers(){
        return sleeping.size() == registered;
    }

    public boolean hasSleepers(){
        return !sleeping.isEmpty();
    }

    private void wakeEveryone(){
        int a = sleeping.size();
        for(int i = 0; i < a; i++){
            Peasant b = sleeping.pop();
            b.awake(coordinates.translated(CONSTANT_SPAWNS.get(i)));
        }
    }
}
