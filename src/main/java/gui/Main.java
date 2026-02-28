package main.java.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
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

import java.awt.*;
import java.net.URL;
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

    private BorderPane borderPane;

    //private boolean hadesLaunched = false;
    private GridPane mapGrid;

    private StackPane centerPane; // champ de classe


    @Override
    public void start(Stage primaryStage) throws Exception {



        this.mapGrid = new GridPane();
        mapGrid.setAlignment(Pos.CENTER);
        mapGrid.setBorder(Border.stroke(Color.BLACK));
        mapGrid.setBorder(new Border(
                new BorderStroke(
                        Color.BLACK,                       // couleur de la bordure
                        BorderStrokeStyle.SOLID,           // style de bordure
                        new CornerRadii(5),               // rayon des coins (ici 10 pixels)
                        new BorderWidths(3)                // largeur de la bordure
                )
        ));
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
        BorderPane rightPanel = new BorderPane();
        rightPanel.setPadding(new Insets(20));

        Button pauseButton = new Button("Start the game");
        pauseButton.setOnAction(
                e -> {
                    pause = !pause;
                    if(pause){
                        pauseButton.setText("Play");
                    } else {
                        pauseButton.setText("Pause");
                    }
                }
        );
        pauseButton.setMaxWidth(Double.MAX_VALUE); // prend toute la largeur
        pauseButton.setPrefHeight(50);
        rightPanel.setBottom(pauseButton);

        infoLabel.setWrapText(true);
        infoLabel.setText("Simulation statistics");

        //ici c'est les données que on écrit
        VBox statsBox = new VBox(10);
        statsBox.setAlignment(Pos.TOP_LEFT);

        statsBox.getChildren().add(infoLabel);

        VBox staticStats = new VBox(5);
        statsBox.setAlignment(Pos.TOP_LEFT);

        VBox dynamicStats = new VBox(5);
        dynamicStats.setAlignment(Pos.TOP_LEFT);

        statsBox.getChildren().add(staticStats);
        statsBox.getChildren().add(dynamicStats);



        staticStats.getStyleClass().add("sim-stats-box");
        dynamicStats.getStyleClass().add("sim-stats-box");

        nbHouse.getStyleClass().add("sim-label");
        maxPeasantPerHouse.getStyleClass().add("sim-label");
        eatablePerHouse.getStyleClass().add("sim-label");
        currentDay.getStyleClass().add("sim-label");
        currentPeriod.getStyleClass().add("sim-label");
        currentPop.getStyleClass().add("sim-label");

        infoLabel.getStyleClass().add("sim-title");


        rightPanel.setTop(statsBox);
        BorderPane.setAlignment(statsBox, Pos.TOP_LEFT);



        //---------------------------------------------

        nbHouse.setWrapText(true);
        staticStats.getChildren().add(nbHouse); //house count
        maxPeasantPerHouse.setWrapText(true);
        staticStats.getChildren().add(maxPeasantPerHouse); //maxpeasant per house
        eatablePerHouse.setWrapText(true);
        staticStats.getChildren().add(eatablePerHouse); //eatableration
        currentDay.setWrapText(true);
        dynamicStats.getChildren().add(currentDay); //day
        currentPeriod.setWrapText(true);
        dynamicStats.getChildren().add(currentPeriod); // period
        currentPop.setWrapText(true);
        dynamicStats.getChildren().add(currentPop); //totalpopulation




        // --- SplitPane horizontal ---
        this.borderPane = new BorderPane();
        borderPane.setBackground(Background.fill(Color.LIGHTGREY ));

        borderPane.setRight(rightPanel);
        //borderPane.setCenter(mapGrid);
        // Dans start() au lieu de borderPane.setCenter(mapGrid);
        centerPane = new StackPane(mapGrid);
        borderPane.setCenter(centerPane);

        mapGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        mapGrid.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.1));
        mapGrid.prefHeightProperty().bind(borderPane.heightProperty().multiply(0.1));




        Scene scene = new Scene(borderPane);
        scene.setFill(Color.rgb(193, 225, 193));

        scene.getStylesheets().add(
                getClass().getResource("/style/css/simulation.css").toExternalForm()
        );

        pauseButton.getStyleClass().add("sim-button");
        pauseButton.setMaxWidth(Double.MAX_VALUE);
        pauseButton.setPrefHeight(55);

        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CodeBHSimulation");
        primaryStage.setTitle("Map Example");
        primaryStage.show();
    }

    private void setDataText(Game game){
        nbHouse.setText("Number of houses: " + game.getAllHouses().size());
        maxPeasantPerHouse.setText("Max peasant per house: " + Game.MAX_PEASANT_PER_HOUSE);
        eatablePerHouse.setText("Eatable generated per house: " + Game.EATABLE_RATIO);
        currentDay.setText("Day: " + game.getCurrentDay());
        currentPeriod.setText("Period: " + game.getPeriod());
        if(game.newLoggerDay){
            currentPop.setText("Total population: " + Logger.getTotalPopulation(game));
        }
    }

    private Timeline getTimeline(Group[][] panes) {
        Game game = new Game();
        setDataText(game);
        currentPop.setText("Total population: " + Logger.getTotalPopulation(game));

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
        setDataText(game);
        draw(cases, game);
    }


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
        Rectangle peasant = new Rectangle(0.6*TILE_SIZE, 0.6*TILE_SIZE);
        peasant.setFill(Color.BLUE);
        return peasant;
    }

    private Rectangle createHouse() {
        Rectangle house = new Rectangle(TILE_SIZE, TILE_SIZE);
        house.setFill(Color.DARKBLUE);
        return house;
    }

    public static Group createCarrot() {
        // Corps de la carotte
        Circle body = new Circle(0.3*TILE_SIZE);
        body.setFill(Color.ORANGE);

        // Feuilles (étoile verte plus petite)
        Polygon leaves = new Polygon();
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

        leaves.setFill(Color.GREEN);

        return new Group(body, leaves);
    }



}
