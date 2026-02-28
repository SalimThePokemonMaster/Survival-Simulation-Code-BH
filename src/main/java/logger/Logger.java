package main.java.logger;

import main.java.Game;
import main.java.components.House;
import main.java.components.Peasant;
import main.java.components.eatable.Eatable;
import main.java.utilities.Movable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static main.java.Game.EATABLE_RATIO;
import static main.java.Game.MAX_PEASANT_PER_HOUSE;

/**
 * Final class representing a {@code Logger}. This {@code Logger} is meant to write the logs of a simulation in two formats,
 * a more verbal one for people lecture and a second one for data extraction and to use those data.
 * ALl the logs are registered in the {@code logs} directory in {@code resources}.
 *
 * The {@code Logger} has a main function to make this file possible to be launched. That allows to make a simulation of 100
 * days without using the UI, being consequently incredibly faster.
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 *
 */
public final class Logger {

    //StringBuilder of the verbal log text
    static StringBuilder log = new StringBuilder();
    //StringBuilder of the data log text
    static StringBuilder Clog = new StringBuilder();

    //Constants at the creation of the logger
    static final LocalDateTime timestamp = LocalDateTime.now();
    static final String threadName = Thread.currentThread().getName();
    static final String javaVersion = System.getProperty("java.version");

    /**
     * {@code Logger} entry point. Creates a {@link Game} and initialises the {@code Logger}, then simulates, plays 100
     * days of simulation and recovers the logs in the log files.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {

        Game game = new Game();

        initializer(game);

        double populationRecorder = getTotalPopulation(game);

        while (game.getCurrentDay() < 100){
            game.update();
            populationRecorder = update(game, populationRecorder);
        }
        write();
    }

    /**
     * Utility function that helps to count the total population inf the simulation but, considering the special
     * usage of the {@code Logger}, it has a special way to count that is meant to work during the night ({@code ARTEMIS})
     * when all {@link Peasant} are asleep.
     *
     * @param game is the simulation {@link Game}.
     * @return the total population is the given {@code game}.
     */
    static public int getTotalPopulation(Game game){
        return game.getAllHouses().stream().map(House::getToGenerateThisDay).reduce(Integer::sum).get();
    }

    /**
     * Initialises the logs with their headers.
     *
     * @param game is the simulation {@link Game}.
     */
    static public void initializer(Game game) {
        /*
                    Verbal log header
         */
        log.append("============================================================\n")
                .append("                SIMULATION ENGINE — STARTUP\n")
                .append("============================================================\n\n")
                .append("Simulation ID   : ").append(timestamp.toString()).append("\n")
                .append("Launch Time     : ").append(timestamp.toString()).append("\n")
                .append("Status          : INITIALIZING\n\n")

                .append("--- WORLD SNAPSHOT -----------------------------------------\n\n")
                .append("Houses          : " + game.getAllHouses().size()).append("\n")
                .append("Max peasant per house : " + MAX_PEASANT_PER_HOUSE).append("\n");

        game.getAllHouses().forEach(h ->
                log.append("(")
                        .append(h.getCoordinates().getX())
                        .append(",")
                        .append(h.getCoordinates().getY())
                        .append(");")
        );
        log.append("\n");
        log.append("Population      : " + getTotalPopulation(game)).append("\n")
                .append("Quantity to eat : " + Peasant.QUANTITY_TO_EAT).append("\n\n")
                .append("Eatables        : " + game.getAllEatable().size()).append("\n")
                .append("Eatable ratio   : " + EATABLE_RATIO).append("\n")
                .append("Map Size        : " + Game.COLUMNS + " x " + Game.LINES).append("\n")
                .append("Period          : " + game.getPeriod().toString()).append("\n\n")

                .append("--- SYSTEM INFO --------------------------------------------\n\n")
                .append("Thread          : ").append(threadName).append("\n")
                .append("Java Version    : ").append(javaVersion).append("\n")
                .append("============================================================\n");

        /*
                    Data log header
         */
        Clog.setLength(0);
        Clog.append("# Simulation Metadata\n")
            .append("# simulation_id=").append(timestamp.toString()).append("\n")
            .append("# houses_count=").append(game.getAllHouses().size()).append("\n")
            .append("# max_peasant_per_house=").append(MAX_PEASANT_PER_HOUSE).append("\n")
            .append("# eatables_ratio=").append(EATABLE_RATIO).append("\n")
            .append("# houses_coordinates=");

        game.getAllHouses().forEach(h ->
                Clog.append("(")
                        .append(h.getCoordinates().getX())
                        .append(",")
                        .append(h.getCoordinates().getY())
                        .append(");")
        );

        Clog.append("\n");

        Clog.append("day,period,total_population,delta_population_percent\n");

        /*
                Verbal log day 0 initialised
         */
        log.append("                        Day ").append(game.getCurrentDay()).append("\n")
                .append("============================================================\n\n")

                .append("Period          : ").append(game.getPeriod().toString()).append("\n")
                .append("Status          : RUNNING\n\n")
                .append("--- POPULATION ---------------------------------------------\n\n")
                .append("Total Population: ").append(getTotalPopulation(game)).append("\n")
                .append("============================================================\n");

        /*
                Data log day 0 initialised
         */
        Clog.append(game.getCurrentDay())
                .append(",")
                .append(game.getPeriod())
                .append(",")
                .append(getTotalPopulation(game))
                .append("\n");
    }

    /**
     * Update function that checks if it is a new day and if so, records all necessary data in the {@code Logger}
     * texts.
     *
     * @param game is the simulation {@link Game}.
     * @param populationRecorder is the actual total population before the new day.
     * @return the new total population.
     */
    static public double update(Game game, double populationRecorder){
        double newPopulationRecorder = populationRecorder;
        if(game.newLoggerDay){

            //Disables the game attribute that informs the logger that it is a new day
            game.newLoggerDay = false;

            /*
                        Verbal log new day data
             */
            log.append("                        Day ").append(game.getCurrentDay()).append("\n")
                    .append("============================================================\n")

                    .append("Period          : ").append(game.getPeriod().toString()).append("\n")
                    .append("Status          : RUNNING\n")
                    .append("--- POPULATION ---------------------------------------------\n")
                    .append("Total Population: ").append(getTotalPopulation(game)).append("\n");
            if(populationRecorder == 0){
                log.append("Population evolution: 0 %").append("\n");
            } else {
                log.append("Population evolution: ").append(( (double)(getTotalPopulation(game) - populationRecorder) / populationRecorder) * 100.0).append(" % ").append("\n\n");
            }

            newPopulationRecorder = getTotalPopulation(game);

            log.append("============================================================\n");

            //Computes the variation of population between last day and the new one
            double deltaPopulationPercent;
            if(populationRecorder == 0){
                deltaPopulationPercent = 0; Clog.append(" DeltaPop=0%").append("\n");
            } else {
                deltaPopulationPercent =  (getTotalPopulation(game) - populationRecorder) / populationRecorder * 100.0;
            }

            /*
                        Data log new day data
             */
            Clog.append(game.getCurrentDay())
                    .append(",")
                    .append(game.getPeriod())
                    .append(",")
                    .append(getTotalPopulation(game))
                    .append(",")
                    .append(deltaPopulationPercent)
                    .append("\n");


        }
        return newPopulationRecorder;
    }

    /**
     * Writing function that saves the logs int the corresponding files.
     */
    static public void write(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String safeTimestamp = timestamp.format(formatter);

        writer(Path.of("resources/logs/verbal/" + safeTimestamp + ".txt"), log.toString());
        writer(Path.of("resources/logs/data_exploitable/" + safeTimestamp + ".txt"), Clog.toString());
    }

    /**
     * Utility function that writes a given {@link String} into a file.
     *
     * @param filePath is the file path to write in.
     * @param message is the text we want to write.
     */
    static private void writer(Path filePath, String message){
        try {
            Files.writeString(filePath, message, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error during the writing process in the file.", e);
        }
    }
}
