package main.java;

import main.java.components.Peasant;
import main.java.components.House;
import main.java.utilities.Coordinates;
import main.java.utilities.Utilities;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    public static void main(String[] args) {
        Game game = new Game();

        StringBuilder log = new StringBuilder();
        StringBuilder Clog = new StringBuilder();
        String timestamp = LocalDateTime.now().toString();
        String threadName = Thread.currentThread().getName();
        String javaVersion = System.getProperty("java.version");

        log.append("============================================================\n")
                .append("                SIMULATION ENGINE — STARTUP\n")
                .append("============================================================\n\n")
                .append("Simulation ID   : ").append(timestamp).append("\n")
                .append("Launch Time     : ").append(timestamp).append("\n")
                .append("Status          : INITIALIZING\n\n")

                .append("--- WORLD SNAPSHOT -----------------------------------------\n\n")
                .append("Houses          : " + game.getAllHouses().size()).append("\n")
                .append("Population      : " + game.getTotalPopulation()).append("\n")
                .append("Quantity to eat : " + Peasant.QUANTITY_TO_EAT).append("\n\n")
                .append("Eatables        : " + game.getAllEatable().size()).append("\n")
                .append("Map Size        : " + Utilities.MAP_WIDTH + " x " + Utilities.MAP_HEIGTH).append("\n")
                .append("Period          : " + game.getPeriod().toString()).append("\n\n")

                .append("--- SYSTEM INFO --------------------------------------------\n\n")
                .append("Thread          : ").append(threadName).append("\n")
                .append("Java Version    : ").append(javaVersion).append("\n")
                .append("============================================================\n");

        Clog.append(log);

        double populationRecorder = game.getTotalPopulation();
        double CpopulationRecorder = game.getTotalPopulation();

        while (game.getDay() < 100){
            game.update();
            if(game.newLoggerDay){
                game.newLoggerDay = false;
                log.append("                        Day ").append(game.getDay()).append("\n")
                        .append("============================================================\n\n")

                        .append("Period          : ").append(game.getPeriod().toString()).append("\n")
                        .append("Status          : RUNNING\n\n")

                        .append("--- POPULATION ---------------------------------------------\n\n")
                        .append("Total Population: ").append(game.getTotalPopulation()).append("\n");
                if(populationRecorder == 0){
                    log.append("Population evolution: 0 %").append("\n\n");
                } else {
                    log.append("Population evolution: ").append(( (double)(game.getTotalPopulation() - populationRecorder) / populationRecorder) * 100.0).append(" % ").append("\n\n");
                }

                populationRecorder = game.getTotalPopulation();

                log.append("============================================================\n");


                Clog.append("[Day ")
                        .append(game.getDay())
                        .append("] Period=")
                        .append(game.getPeriod())
                        .append(" Pop=")
                        .append(game.getTotalPopulation());

                if(CpopulationRecorder == 0){
                    Clog.append(" DeltaPop=0%").append("\n");
                } else {
                    Clog.append(" DeltaPop=").append((double)(game.getTotalPopulation() - CpopulationRecorder) / CpopulationRecorder * 100.0).append("%").append("\n");
                }
                CpopulationRecorder = game.getTotalPopulation();


            }
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String safeTimestamp = now.format(formatter);

        writer(Path.of("resources/logs/" + safeTimestamp + ".txt"), log.toString());
        writer(Path.of("resources/logs/C" + safeTimestamp + ".txt"), Clog.toString());
    }

    protected static void writer(Path filePath, String message){
        try {
            Files.writeString(filePath, message, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error during the writing process in the file", e);
        }
    }

    protected static String reader(Path filePath) throws IOException {
        return Files.readString(filePath);
    }
}
