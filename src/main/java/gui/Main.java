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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * Main class of {@code CodeBHSimulation}, allowing to launch the final program
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 */
public final class Main extends Application {
    private static final int TILE_SIZE = 15;
    private static final int DELTA_TIME_MS = 50;
    private boolean pause = true;

    private final BorderPane borderPane = new BorderPane();
    private final Label infoLabel = new Label();
    private final Label nbHouse = new Label();
    private final Label maxPeasantPerHouse = new Label();
    private final Label eatablePerHouse = new Label();
    private final Label currentDay = new Label();
    private final Label currentPeriod = new Label();
    private final Label currentPop = new Label();

    public static void main(String[] args) {
        System.out.println("CodeBHSimulation ~ Done with <3 by Sami & Salim");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // ************************************
        // Setting the grid for the game
        // ************************************

        GridPane mapGrid = new GridPane();
        mapGrid.setAlignment(Pos.CENTER);
        mapGrid.setBorder(Border.stroke(Color.BLACK));
        mapGrid.setBorder(new Border(
                new BorderStroke(
                        Color.WHITESMOKE,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(5),
                        new BorderWidths(3)
                )
        ));

        // ************************************
        // Setting the right panel for the logs
        // ************************************

        BorderPane rightPanel = new BorderPane();
        rightPanel.setPadding(new Insets(20));

        // ************************************
        // Adding each pixel in a group for performance
        // ************************************

        Group[][] panes = new Group[Game.LINES][Game.COLUMNS];
        for (int y = 0; y < Game.LINES; y++) {
            for (int x = 0; x < Game.COLUMNS; x++) {
                StackPane tile = createTile();

                tile.setMinSize(TILE_SIZE, TILE_SIZE);
                tile.setMaxSize(TILE_SIZE, TILE_SIZE);
                tile.setPrefSize(TILE_SIZE, TILE_SIZE);

                Group children = new Group();
                tile.getChildren().add(children);
                panes[y][x] = children;
                mapGrid.add(tile, x, y);
            }
        }

        // ************************************
        // Creating a timeline to update the game automatically
        // ************************************

        Timeline timeline = getTimeline(panes);
        timeline.play();

        // ************************************
        // Adding a pause button
        // ************************************

        Button pauseButton = new Button("Start the game");
        pauseButton.setOnAction(
                _ -> {
                    pause = !pause;
                    if(pause){
                        pauseButton.setText("Play");
                    } else {
                        pauseButton.setText("Pause");
                    }
                }
        );
        pauseButton.getStyleClass().add("sim-button");
        pauseButton.setMaxWidth(Double.MAX_VALUE);
        pauseButton.setPrefHeight(55);
        rightPanel.setBottom(pauseButton);

        // ************************************
        // Adding the labels to the right panel
        // ************************************

        VBox statsBox = new VBox(10);
        statsBox.setAlignment(Pos.TOP_LEFT);

        infoLabel.setWrapText(true);
        infoLabel.setText("Simulation statistics");
        infoLabel.getStyleClass().add("sim-title");
        statsBox.getChildren().add(infoLabel);

        VBox staticStats = new VBox(5);
        staticStats.setAlignment(Pos.TOP_LEFT);
        statsBox.getChildren().add(staticStats);
        staticStats.getStyleClass().add("sim-stats-box");

        VBox dynamicStats = new VBox(5);
        dynamicStats.setAlignment(Pos.TOP_LEFT);
        statsBox.getChildren().add(dynamicStats);
        dynamicStats.getStyleClass().add("sim-stats-box");

        nbHouse.getStyleClass().add("sim-label");
        maxPeasantPerHouse.getStyleClass().add("sim-label");
        eatablePerHouse.getStyleClass().add("sim-label");
        currentDay.getStyleClass().add("sim-label");
        currentPeriod.getStyleClass().add("sim-label");
        currentPop.getStyleClass().add("sim-label");

        rightPanel.setTop(statsBox);
        BorderPane.setAlignment(statsBox, Pos.TOP_LEFT);

        // ************************************
        // Adding the text for the labels
        // ************************************

        addTextTo(staticStats, nbHouse);
        addTextTo(staticStats, maxPeasantPerHouse);
        addTextTo(staticStats, eatablePerHouse);
        addTextTo(dynamicStats, currentDay);
        addTextTo(dynamicStats, currentPeriod);
        addTextTo(dynamicStats, currentPop);

        // ************************************
        // Adding everything to the borderPane
        // ************************************

        borderPane.setBackground(Background.fill(Color.LIGHTGREY));
        borderPane.setRight(rightPanel);
        borderPane.setCenter(new StackPane(mapGrid));

        mapGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        mapGrid.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.1));
        mapGrid.prefHeightProperty().bind(borderPane.heightProperty().multiply(0.1));

        // ************************************
        // Displays the whole simulation
        // ************************************

        Scene scene = new Scene(borderPane);
        scene.setFill(Color.rgb(193, 225, 193));
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/css/simulation.css")).toExternalForm()
        );

        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CodeBHSimulation");
        primaryStage.setTitle("Map Example");
        primaryStage.show();
    }

    /**
     * Utility method that renders the current logs into the right panel.
     *
     * @param game represents the current {@link Game}
     */
    private void setDataText(Game game){
        nbHouse.setText("Number of houses: " + game.getAllHouses().size());
        maxPeasantPerHouse.setText("Max peasant per house: " + Game.MAX_PEASANT_PER_HOUSE);
        eatablePerHouse.setText("Eatables generated per house: " + Game.EATABLE_RATIO);
        currentDay.setText("Day: " + game.getCurrentDay());
        currentPeriod.setText("Period: " + game.getPeriod());
        if(game.newLoggerDay){
            currentPop.setText("Total population: " + Logger.getTotalPopulation(game));
        }
    }

    /**
     * Utility method that creates a timeline for the update function
     *
     * @param cases represents the pixels
     */
    private Timeline getTimeline(Group[][] cases) {
        Game game = new Game();
        setDataText(game);
        currentPop.setText("Total population: " + Logger.getTotalPopulation(game));

        Logger logger = new Logger();
        Logger.initializer(game);
        logger.write();
        AtomicReference<Double> populationRecorder = new AtomicReference<>((double) logger.getTotalPopulation(game));

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(DELTA_TIME_MS),
                        _ -> {
                            if (!pause){
                                update(cases, game);
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

    /**
     * Utility method that updates the whole simulation.
     *
     * @param cases represents the pixels
     * @param game represents the current {@link main.java.Game}
     */
    private void update(Group[][] cases, Game game){
        clear(cases);
        game.update();
        updateBackground(cases, game.getPeriod());
        setDataText(game);
        draw(cases, game);
    }

    /**
     * Utility method that updates the background.
     *
     * @param cases represents the pixels
     * @param period represents the {@link main.java.Game.Period}
     */
    private void updateBackground(Group[][] cases, Game.Period period) {
        Color tileColor;
        Color generalColor;
        switch (period) {
            case ARTEMIS -> { tileColor = Color.rgb(19, 38, 49, 1.0); generalColor = Color.DARKSLATEBLUE; }
            case HADES -> { tileColor = Color.RED; generalColor = Color.DARKRED; }
            default -> { tileColor = Color.BEIGE; generalColor = Color.LIGHTGREY; }
        }
        borderPane.setBackground(new Background(new BackgroundFill(generalColor, null, null)));
        for (int y = 0; y < Game.LINES; y++) {
            for (int x = 0; x < Game.COLUMNS; x++) {
                StackPane tile = (StackPane) cases[y][x].getParent();
                Rectangle background = (Rectangle) tile.getChildren().getFirst();
                background.setFill(tileColor);
            }
        }
    }

    /**
     * Utility method that clears the map.
     *
     * @param cases represents the pixels
     */
    private void clear(Group[][] cases){
        for (int y = 0; y < Game.LINES; y++) {
            for (int x = 0; x < Game.COLUMNS; x++) {
                cases[y][x].getChildren().clear();
            }
        }
    }

    /**
     * Utility method that draws each pixel
     *
     * @param cases the group of all pixels
     * @param game the current game
     */
    private void draw(Group[][] cases, Game game){
        Stream.concat(
                Stream.concat(game.getAllEatable().stream(), game.getPeasant().stream()
                    ), game.getAllHouses().stream()
        ).forEach(x -> cases[x.getCoordinates().getY()][x.getCoordinates().getX()].getChildren().add(
                getDrawing(x)
        ));
    }

    /**
     * Utility method that displays an element.
     *
     * @return a {@link Node} representing a {@link Element}
     */
    private Node getDrawing(Element x){
        Group g = new Group();

        switch (x) {
            case Peasant _ -> g.getChildren().add(createPeasant());
            case Carrot _ -> g.getChildren().add(createCarrot());
            case House _ -> g.getChildren().add(createHouse());
            default -> throw new IllegalStateException();
        }

        return g;
    }

    /**
     * Utility method for the display.
     *
     * @return a {@link StackPane} representing a pixel
     */
    private StackPane createTile() {
        Rectangle background = new Rectangle(TILE_SIZE, TILE_SIZE);
        background.setFill(Color.BEIGE);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(0.05);

        return new StackPane(background);
    }

    /**
     * Utility method for the display.
     *
     * @return a {@link Group} representing a {@link Peasant}
     */
    private Group createPeasant() {
        double coef = 1;

        Rectangle body = new Rectangle(-0.15 * TILE_SIZE * coef, -0.1 * TILE_SIZE * coef,
                0.3 * TILE_SIZE * coef, 0.4 * TILE_SIZE * coef);
        Circle head = new Circle(0, -0.35 * TILE_SIZE * coef, 0.12 * TILE_SIZE * coef);
        Rectangle hatBase = new Rectangle(-0.18 * TILE_SIZE * coef, -0.48 * TILE_SIZE * coef,
                0.36 * TILE_SIZE * coef, 0.08 * TILE_SIZE * coef);
        Rectangle hatTop = new Rectangle(-0.1 * TILE_SIZE * coef, -0.56 * TILE_SIZE * coef,
                0.2 * TILE_SIZE * coef, 0.12 * TILE_SIZE * coef);
        Rectangle leftArm = new Rectangle(-0.28 * TILE_SIZE * coef, -0.05 * TILE_SIZE * coef,
                0.14 * TILE_SIZE * coef, 0.07 * TILE_SIZE * coef);
        Rectangle rightArm = new Rectangle(0.14 * TILE_SIZE * coef, -0.05 * TILE_SIZE * coef,
                0.14 * TILE_SIZE * coef, 0.07 * TILE_SIZE * coef);
        Rectangle leftLeg = new Rectangle(-0.12 * TILE_SIZE * coef, 0.28 * TILE_SIZE * coef,
                0.1 * TILE_SIZE * coef, 0.18 * TILE_SIZE * coef);
        Rectangle rightLeg = new Rectangle(0.02 * TILE_SIZE * coef, 0.28 * TILE_SIZE * coef,
                0.1 * TILE_SIZE * coef, 0.18 * TILE_SIZE * coef);

        body.setFill(Color.SADDLEBROWN);
        head.setFill(Color.BEIGE);
        hatBase.setFill(Color.DARKRED);
        hatTop.setFill(Color.DARKRED);
        leftArm.setFill(Color.SADDLEBROWN);
        rightArm.setFill(Color.SADDLEBROWN);
        leftLeg.setFill(Color.BLACK);
        rightLeg.setFill(Color.BLACK);

        return new Group(body, head, hatBase, hatTop, leftArm, rightArm, leftLeg, rightLeg);
    }

    /**
     * Utility method for the display.
     *
     * @return a {@link Group} representing a {@link House}
     */
    private Group createHouse() {
        Rectangle house = new Rectangle(TILE_SIZE, TILE_SIZE);
        house.setFill(Color.DARKBLUE);
        return new Group(house);
    }

    /**
     * Utility method for the display.
     *
     * @return a {@link Group} representing a {@link Carrot}
     */
    public static Group createCarrot() {
        Circle body = new Circle(0.3*TILE_SIZE);
        body.setFill(Color.ORANGE);

        Polygon leaves = new Polygon();
        leaves.setFill(Color.GREEN);
        double centerX = 0;
        double centerY = 0;
        double outerRadius = 0.2*TILE_SIZE;
        double innerRadius = 0.1*TILE_SIZE;

        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i;
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;
            double x = centerX + Math.cos(angle - Math.PI / 2) * radius;
            double y = centerY + Math.sin(angle - Math.PI / 2) * radius;
            leaves.getPoints().addAll(x, y);
        }
        return new Group(body, leaves);
    }

    /**
     * Utility method that adds the label to the VBox.
     * @param to where to add the text
     * @param text the text to add
     */
    private void addTextTo(VBox to, Label text){
        text.setWrapText(true);
        to.getChildren().add(text);
    }
}
