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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
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
import main.java.components.eatable.Eatable;
import main.java.utilities.Coordinates;

import java.util.*;
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

        CheckBox pauseBox = new CheckBox("Pause");
        pauseBox.setOnAction(e -> pause = pauseBox.isSelected());

        infoLabel.setWrapText(true);

        rightPanel.getChildren().addAll(pauseBox, infoLabel);

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

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(DELTA_TIME_MS),
                        e -> {
                            if (!pause) update(panes, game);
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    private void update(Group[][] cases, Game game){
        infoLabel.setText("Current generation : " + game.getCurrentDay() + "\n" + "Total population: " + game.getTotalPopulation());
        clear(cases);
        game.update();
        updateBackground(cases, game.getPeriod());
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

    private Rectangle createHouse() {
        Rectangle peasant = new Rectangle(20, 20);
        peasant.setFill(Color.DARKBLUE);
        return peasant;
    }
}
