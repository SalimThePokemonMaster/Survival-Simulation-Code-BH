package main.java.components.eatable;

import main.java.components.Element;
import main.java.components.House;
import main.java.utilities.Coordinates;
import main.java.utilities.Utilities;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static main.java.utilities.Preconditions.ensure;

public abstract class Eatable extends Element {
    protected int health;

    public boolean eat(){
        health -= 1;
        return !hasNotBeenEaten() ;
    }

    public boolean hasNotBeenEaten(){
        return health > 0;
    }

    protected abstract int getMAX_HEALTH();

    public void revive(Set<House> allHouses){
        int a,b;
        Coordinates c;
        boolean d;
        do{
            a = ThreadLocalRandom.current().nextInt(0, Utilities.MAP_WIDTH);
            b = ThreadLocalRandom.current().nextInt(0, Utilities.MAP_HEIGTH);
            ensure(a >= 0 && a <= Utilities.MAP_WIDTH, "The X coordinate chosen for a carrot respawn is out of bound : " + a);
            ensure(b >= 0 && b <= Utilities.MAP_HEIGTH, "The Y coordinate chosen for a carrot respawn is out of bound : " + b);
            c = new Coordinates(a, b);
            Coordinates finalC = c;
            d = allHouses.stream().noneMatch(x -> x.getCoordinates().isSuperposed(finalC));
        } while (!d);

        coordinates = c;
        health = getMAX_HEALTH();
    }
}