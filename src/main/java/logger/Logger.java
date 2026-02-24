package main.java.logger;

import main.java.Game;
import main.java.components.House;
import main.java.components.Peasant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static main.java.Game.EATABLE_RATIO;
import static main.java.Game.MAX_PEASANT_PER_HOUSE;

public final class Logger {

    static StringBuilder log = new StringBuilder();
    static StringBuilder Clog = new StringBuilder();
    static String timestamp = LocalDateTime.now().toString();;
    static String threadName = Thread.currentThread().getName();
    static String javaVersion = System.getProperty("java.version");
    static Game game = new Game();

    private record Pair(double a,  double b){}

    public Logger() {}

    public static void main(String[] args) {

        Game game = new Game();

        initializer(game);

        double populationRecorder = getTotalPopulation(game);
        double CpopulationRecorder = getTotalPopulation(game);

        while (game.getCurrentDay() < 100){
            game.update();
            Pair k = update(game, populationRecorder, CpopulationRecorder);
            populationRecorder = k.a();
            CpopulationRecorder = k.b();
        }
        write();
    }

    public static int getTotalPopulation(Game game){
        return game.getAllHouses().stream().map(House::getToGenerateThisDay).reduce(Integer::sum).get();
    }

    public static void initializer(Game game) {
        log.append("============================================================\n")
                .append("                SIMULATION ENGINE — STARTUP\n")
                .append("============================================================\n\n")
                .append("Simulation ID   : ").append(timestamp).append("\n")
                .append("Launch Time     : ").append(timestamp).append("\n")
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

        Clog.setLength(0);
        Clog.append("# Simulation Metadata\n")
            .append("# simulation_id=").append(timestamp).append("\n")
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

        log.append("                        Day ").append(game.getCurrentDay()).append("\n")
                .append("============================================================\n\n")

                .append("Period          : ").append(game.getPeriod().toString()).append("\n")
                .append("Status          : RUNNING\n\n")
                .append("--- POPULATION ---------------------------------------------\n\n")
                .append("Total Population: ").append(getTotalPopulation(game)).append("\n")
                .append("============================================================\n");

        Clog.append(game.getCurrentDay())
                .append(",")
                .append(game.getPeriod())
                .append(",")
                .append(getTotalPopulation(game))
                .append("\n");
    }


    public static Pair update(Game game, double populationRecorder, double CpopulationRecorder){
        double newPopulationRecorder = populationRecorder;
        double newCpopulationRecorder = CpopulationRecorder;
        if(game.newLoggerDay){
            game.newLoggerDay = false;
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


            double deltaPopulationPercent = 0 ;
            if(CpopulationRecorder == 0){
                deltaPopulationPercent = 0; Clog.append(" DeltaPop=0%").append("\n");
            } else {
                deltaPopulationPercent =  (getTotalPopulation(game) - CpopulationRecorder) / CpopulationRecorder * 100.0;
            }
            newCpopulationRecorder = getTotalPopulation(game);

            Clog.append(game.getCurrentDay())
                    .append(",")
                    .append(game.getPeriod())
                    .append(",")
                    .append(getTotalPopulation(game))
                    .append(",")
                    .append(deltaPopulationPercent)
                    .append("\n");


        }
        return new Pair(newPopulationRecorder, newCpopulationRecorder);
    }

    public static void write(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String safeTimestamp = now.format(formatter);

        writer(Path.of("resources/logs/verbal/" + safeTimestamp + ".txt"), log.toString());
        writer(Path.of("resources/logs/data_exploitable/" + safeTimestamp + ".txt"), Clog.toString());
    }

    private static void writer(Path filePath, String message){
        try {
            Files.writeString(filePath, message, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error during the writing process in the file", e);
        }
    }
}
