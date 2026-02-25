package main.java.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.Game;
import main.java.components.*;
import main.java.components.eatable.Carrot;
import main.java.logger.Logger;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * Main class of {@code CodeBHSimulation}, allowing to launch the final program
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 */
public final class Main extends Application {
    private static final int TILE_SIZE = 20;
    private static final int DELTA_TIME_MS = 20;
    private boolean pause;

    public static void main(String[] args) {
        System.out.println("CodeBHSimulation ~ Done with <3 by Sami & Salim");
        launch(args);
    }

    Label infoLabel = new Label();
    Label nbHouse = new Label();
    Label maxPeasantPerHouse = new Label();
    Label eatablePerHouse = new Label();
    Label currentDay = new Label();
    Label currentPeriod = new Label();
    Label currentPop = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane mapGrid = new GridPane();
        mapGrid.setAlignment(Pos.CENTER);

        Group[][] panes = new Group[Game.LINES][Game.COLUMNS];


        for (int y = 0; y < Game.LINES; y++) {
            for (int x = 0; x < Game.COLUMNS; x++) {
                StackPane tile = createTile();
                Group children = new Group();
                tile.getChildren().add(children);
                panes[y][x] = children;
                mapGrid.add(tile, x, y);
            }
        }

        Timeline timeline = getTimeline(panes);
        timeline.play();

        // --- Panel droit ---
        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setAlignment(Pos.TOP_CENTER);

        Button pauseBox = new Button("Pause");
        pauseBox.setOnAction(e -> pause = !pause);
        Group k = new Group();

        infoLabel.setWrapText(true);
        infoLabel.setText("Simulation statistics:");
        VBox statsBox = new VBox();
        nbHouse.setWrapText(true);
        statsBox.getChildren().add(nbHouse); //house count
        maxPeasantPerHouse.setWrapText(true);
        statsBox.getChildren().add(maxPeasantPerHouse); //maxpeasant per house
        eatablePerHouse.setWrapText(true);
        statsBox.getChildren().add(eatablePerHouse); //eatableration
        currentDay.setWrapText(true);
        statsBox.getChildren().add(currentDay); //day
        currentPeriod.setWrapText(true);
        statsBox.getChildren().add(currentPeriod); // period
        currentPop.setWrapText(true);
        statsBox.getChildren().add(currentPop); //totalpopulation
        infoLabel.setLabelFor(statsBox);
        k.getChildren().add(infoLabel);
        k.getChildren().add(statsBox);

        rightPanel.getChildren().addAll(pauseBox, k);

        // --- SplitPane horizontal ---
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(mapGrid, rightPanel);

        splitPane.setDividerPositions(0.8);
        splitPane.setBackground(Background.fill(Color.LIGHTGREY ));
        Scene scene = new Scene(splitPane);
        scene.setFill(Color.rgb(193, 225, 193));
        primaryStage.setScene(scene);
        primaryStage.setTitle("CodeBHSimulation");
        primaryStage.setTitle("Map Example");
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }

    private Timeline getTimeline(Group[][] panes) {
        Game game = new Game();
        Logger logger = new Logger();
        logger.initializer(game);
        logger.write();
        AtomicReference<Double> populationRecorder = new AtomicReference<>((double) logger.getTotalPopulation(game));

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(DELTA_TIME_MS),
                        e -> {
                            if (!pause){
                                update(panes, game);
                                logger.update(game, populationRecorder.get());
                                populationRecorder.set((double) logger.getTotalPopulation(game));
                                logger.write();

                            }
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    private void update(Group[][] cases, Game game){
        clear(cases);
        game.update();
        updateBackground(cases, game.getPeriod());
        nbHouse.setText("Number of houses: " + game.getAllHouses().size());
        maxPeasantPerHouse.setText("Max peasant per house: " + Game.MAX_PEASANT_PER_HOUSE);
        eatablePerHouse.setText("Eatable generated per house: " + Game.EATABLE_RATIO);
        currentDay.setText("Current day: " + game.getCurrentDay());
        currentPeriod.setText("Current period: " + game.getPeriod());
        if(game.newLoggerDay){
            currentPop.setText("Current total population: " + Logger.getTotalPopulation(game));
        }
        draw(cases, game);
    }

    private void updateBackground(Group[][] cases, Game.Period period) {
        Color color = switch (period) {
            case HELIOS -> Color.BEIGE;
            case ARTEMIS -> Color.BLACK;
            case HADES -> Color.RED;
        };

        for (int y = 0; y < Game.LINES; y++) {
            for (int x = 0; x < Game.COLUMNS; x++) {
                StackPane tile = (StackPane) cases[y][x].getParent();
                Rectangle background = (Rectangle) tile.getChildren().getFirst();
                background.setFill(color);
            }
        }
    }

    private void clear(Group[][] cases){
        for (int y = 0; y < Game.LINES; y++) {
            for (int x = 0; x < Game.COLUMNS; x++) {
                cases[y][x].getChildren().clear();
            }
        }
    }

    private void draw(Group[][] cases, Game game){
        Stream.concat(
                Stream.concat(game.getPeasant().stream(), game.getAllEatable().stream()
            ), game.getAllHouses().stream()
        ).forEach(x -> {
            cases[x.getCoordinates().getY()][x.getCoordinates().getX()].getChildren().add(
                    getDrawing(x)
            );
        });
    }

    private Node getDrawing(Element x){
        return
            switch (x) {
                case Peasant p -> createPeasant();
                case Carrot c -> createCarrot();
                case House h -> createHouse();
                default -> throw new IllegalStateException();
            };
    }

    private StackPane createTile() {
        Rectangle background = new Rectangle(TILE_SIZE, TILE_SIZE);
        background.setFill(Color.BEIGE);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(0.05);

        return new StackPane(background);
    }

    private Rectangle createPeasant() {
        Rectangle peasant = new Rectangle(12, 12);
        peasant.setFill(Color.BLUE);
        return peasant;
    }

    private Rectangle createHouse() {
        Rectangle house = new Rectangle(20, 20);
        house.setFill(Color.DARKBLUE);
        return house;
    }

    public static Group createCarrot() {
        // Corps de la carotte
        Circle body = new Circle(6);
        body.setFill(Color.ORANGE);

        // Feuilles (étoile verte plus petite)
        Polygon leaves = new Polygon();
        double centerX = 0;
        double centerY = 0;
        double outerRadius = 4;
        double innerRadius = 2;

        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i;
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;
            double x = centerX + Math.cos(angle - Math.PI / 2) * radius;
            double y = centerY + Math.sin(angle - Math.PI / 2) * radius;
            leaves.getPoints().addAll(x, y);
        }

        leaves.setFill(Color.GREEN);

        return new Group(body, leaves);
    }
}
