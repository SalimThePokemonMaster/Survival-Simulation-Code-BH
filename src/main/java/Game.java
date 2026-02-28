package main.java;

import main.java.components.House;
import main.java.components.Peasant;
import main.java.components.eatable.Carrot;
import main.java.components.eatable.Eatable;
import main.java.utilities.Coordinates;
import main.java.utilities.Movable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static main.java.utilities.Preconditions.ensure;

/**
 * Final class representing a {@code Game}. This is the main part of the simulation. The {@code Game} is the simulation,
 * has all the necessary data, instantiates all the parts of it, can update them and handle their behaviours.
 * However, the {@code Game} does not work alone, it counts on another file, another class to instantiate it and to
 * run its functions such as the update one.
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 */
public final class Game {

    // Game generation
    private Period period = Period.HELIOS;
    private final Set<House> allHouses = generateHouses();
    private final Set<Eatable> allEatable = generateEatables(allHouses);

    // Game constants
    public final static int LINES = 40;
    public final static int COLUMNS = 60;

    public final static int EATABLE_RATIO = 15;
    public final static int MAX_PEASANT_PER_HOUSE = 10;

    final static int CYCLES_BEFORE_GENERATING = 3;
    final static int CYCLES_BEFORE_SLEEPING = 100;

    final static int MIN_NUMBER_OF_HOUSES = 1;
    final static int MAX_NUMBER_OF_HOUSES = 16;
    final static int MIN_NUMBER_OF_PEASANT = 2;
    final static int UPPER_BOUND_MAX_NUMBER_OF_PEASANT = 11;
    final static double REPRODUCTION_FACTOR = 1.5;

    // Game mutable
    int currentCycle = 0;
    private int currentDay;
    private Set<Peasant> peasantSet = new HashSet<>();

    // Boolean meant to be used by the Logger
    public boolean newLoggerDay;

    /**
     * Represents the different periods of a {@code Game}, of a simulation.
     *
     * <p>Each constant corresponds to a different phase of the simulation, mostly periods of a day.</p>
     *
     * <ul>
     *     <li>{@link #HELIOS}: represents a day.</li>
     *     <li>{@link #ARTEMIS}: represents a night.</li>
     *     <li>{@link #HADES}: special state reached only if the entire population is dead.</li>
     * </ul>
     */
    public enum Period {
        HELIOS,
        ARTEMIS,
        HADES
    }

    /**
     * Getter for all houses.
     *
     * @return the {@link Set} of all houses.
     */
    public Set<House> getAllHouses() {
        return allHouses;
    }

    /**
     * Getter for all eatables.
     *
     * @return the {@link Set} of all eatables.
     */
    public Set<Eatable> getAllEatable() {
        return allEatable;
    }

    /**
     * Getter for all peasants.
     *
     * @return the {@link Set} of all peasants.
     */
    public Set<Peasant> getPeasant() {
        return peasantSet;
    }

    /**
     * Getter for the current period.
     *
     * @return the current period.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Getter for the current day.
     *
     * @return the current day.
     */
    public int getCurrentDay() {
        return currentDay;
    }

    /**
     * Getter for the actual total population of peasants.
     *
     * @return the number of peasants.
     */
    public int getTotalPopulation() {
        return peasantSet.size();
    }

    /**
     * Utility function for the generation of a {@code Game}. Generates a random number of {@link House}.
     *
     * @return a {@link Set} of random generated {@link House}.
     * @throws IllegalStateException if the number of houses to generate and generated isn't coherent.
     */
    private static Set<House> generateHouses(){
        Set<House> a = new HashSet<>();
        int n = ThreadLocalRandom.current().nextInt(MIN_NUMBER_OF_HOUSES, MAX_NUMBER_OF_HOUSES);
        for(int i = 0; i < n; i++){
            Coordinates b = generateCoordinates(a);
            int n2 = ThreadLocalRandom.current().nextInt(MIN_NUMBER_OF_PEASANT, UPPER_BOUND_MAX_NUMBER_OF_PEASANT);
            House house = new House(b, n2, MAX_PEASANT_PER_HOUSE);
            a.add(house);
        }
        ensure(a.size() == n, "The outcome of the house generating process is not coherent.");
        return a;
    }

    /**
     * Utility function for the generation of a {@code Game}. Generates a random number of {@link Eatable}.
     *
     * @return a {@link Set} of random generated {@link Eatable}.
     * @throws IllegalStateException if the number of eatables to generate and generated isn't coherent.
     */
    private static Set<Eatable> generateEatables(Set<House> allHouses){
        Set<Eatable> a = new HashSet<>();
        int n = allHouses.size() * EATABLE_RATIO;
        for(int i = 0; i < n; i++){
            Coordinates b = generateCoordinates(allHouses);
            Carrot carrot = new Carrot(b);
            a.add(carrot);
        }
        ensure(a.size() == n, "The outcome of the eatable generating process is not coherent.");
        return a;
    }

    /**
     * Utility function for the generation of a {@code Game}. Generates a random {@link Coordinates}.
     *
     * @return a random generated {@link Coordinates}.
     * @throws IllegalStateException if generated coordinates are out of bound.
     */
    private static Coordinates generateCoordinates(Set<House> allHouses){
        boolean b;
        int x,y;
        Coordinates d;
        do{
            x = ThreadLocalRandom.current().nextInt(0, COLUMNS);
            y = ThreadLocalRandom.current().nextInt(1, LINES - 1);
            ensure(x >= 0 && x <= COLUMNS, "The X coordinate chosen for a carrot respawn is out of bound : " + x);
            ensure(y >= 0 && y < LINES, "The Y coordinate chosen for a carrot respawn is out of bound : " + y);
            d = new Coordinates(x, y);
            Coordinates finalD = d;
            b = allHouses.stream().noneMatch(k -> k.getCoordinates().isSuperposed(finalD));
        } while (!b);

        return d;
    }


    /**
     * Main evolution function of the {@code Game}. the update function handles all the behaviour and evolutions of the
     * compounds, of the {@link main.java.components.Element} of this {@code Game}, counts the clock, the cycles and
     * passes from a {@link Period} to another.
     * The actions to do in each {@link Period} depends individually on each compound, on each {@link main.java.components.Element}
     * that is updated individually and to which all necessary information is provided.
     */
    public void update(){
        switch (period){

            /*
                        During the day
             */
            case HELIOS -> {

                //If a generating peasant cycle clock is finished the houses that can generate, generate a peasant
                if (clock(currentCycle, CYCLES_BEFORE_GENERATING)) {
                    allHouses.forEach(e -> {
                        if (e.canGenerate()) peasantSet.add(e.generate());
                    });
                }

                //Each peasant searches and goes for the nearest eatable
                peasantSet.forEach(p -> p.update(allEatable.stream().filter(Eatable::hasNotBeenEaten).collect(Collectors.toSet()), period));

                //Eaten eatables are revived to random coordinates
                allEatable.stream()
                        .filter(a -> !a.hasNotBeenEaten()).collect(Collectors.toSet())
                        .forEach(a -> a.revive(allHouses));

                //This cycle is finished and we pass to the next one
                currentCycle++;

                //If the finished cycle corresponds to the finish of the period clock, the period passes to
                // the night, to ARTEMIS
                if (clock(currentCycle, CYCLES_BEFORE_SLEEPING)) {
                    period = Period.ARTEMIS;
                }
            }

            /*
                        During the night
             */
            case ARTEMIS -> {

                //The cycles are reset
                currentCycle = 0;

                //Each peasant reaches to his house and enters it
                peasantSet = peasantSet.stream()
                        .filter(p -> {
                            p.update(allEatable, period);
                            return !p.isAsleep();
                        })
                        .collect(Collectors.toSet());

                //When all the peasant are asleep, they are removed, the new number of peasant per house is calculated
                //depending on the reproduction factor and the number of peasant that have eaten enough. Then, the period
                //transitions to day, to HELIOS if there are still alive peasants and transitions to HADES, if the entire
                //population is dead.
                if (allHouses.stream().allMatch(House::isEveryoneSleeping)){
                    allHouses.forEach(e -> e.setToGenerateThisDay((int) (e.getPeasantHasEatenEnough() * REPRODUCTION_FACTOR)));
                    if (allHouses.stream().allMatch(h -> h.getToGenerateThisDay() == 0)) {
                        period = Period.HADES;
                    } else {
                        period = Period.HELIOS;
                        currentDay++;
                        newLoggerDay = true;
                    }
                }
            }

            /*
                        If the entire peasant population is dead
             */
            case HADES -> {
                currentDay++;
                newLoggerDay = true;
            }
        }

    }

    /**
     * Checks if the current cycle corresponds to a clock period of a behaviour of the {@code Game}.
     *
     * @param currentCycle actual cycle of the {@code Game}.
     * @param cyclePeriod clock period of the behaviour we want to check.
     * @return true if a clock period just finished ot started, false otherwise.
     */
    public boolean clock(int currentCycle, int cyclePeriod){
        return currentCycle % cyclePeriod == 0;
    }
}
