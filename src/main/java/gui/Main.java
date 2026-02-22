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

/**
 * Main class of {@code CodeBHSimulation}, allowing to launch the final program
 *
 * @author Sami Kabbaj (393773)
 * @author Salim Chaoui El Faiz (396504)
 */
public final class Main extends Application {
    private static final int TILE_SIZE = 20;
    private static final int MAP_WIDTH = 60;
    private static final int MAP_HEIGHT = 40;
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

        Group[][] panes = new Group[MAP_HEIGHT][MAP_WIDTH];


        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
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
        House h1 = new House(new Coordinates(23, 16), 2);
        House h2 = new House(new Coordinates(45, 25), 2);
        House h3 = new House(new Coordinates(38, 37), 2);
        House h4 = new House(new Coordinates(3, 4), 4);
        House h5 = new House(new Coordinates(14, 26), 8);
        House h6 = new House(new Coordinates(35, 27), 3);
        House h7 = new House(new Coordinates(45, 3), 8);
        Set<House> houses = Set.of(h1, h2, h3, h4, h5, h6, h7);

        Eatable e1 = new Carrot(Coordinates.posOf(25, 20));
        Eatable e2 = new Carrot(Coordinates.posOf(33, 0));
        Eatable e3 = new Carrot(Coordinates.posOf(15, 15));
        Eatable e4 = new Carrot(Coordinates.posOf(15, 3));
        Eatable e5 = new Carrot(Coordinates.posOf(49, 25));
        Eatable e6 = new Carrot(Coordinates.posOf(25, 13));
        Eatable e7  = new Carrot(Coordinates.posOf(12, 8));
        Eatable e8  = new Carrot(Coordinates.posOf(30, 22));
        Eatable e9  = new Carrot(Coordinates.posOf(5, 27));
        Eatable e10 = new Carrot(Coordinates.posOf(18, 9));
        Eatable e11 = new Carrot(Coordinates.posOf(34, 35));
        Eatable e12 = new Carrot(Coordinates.posOf(57, 38));
        Eatable e13 = new Carrot(Coordinates.posOf(27, 14));
        Eatable e14 = new Carrot(Coordinates.posOf(9, 3));
        Eatable e15 = new Carrot(Coordinates.posOf(21, 37));
        Eatable e16 = new Carrot(Coordinates.posOf(4, 12));
        Eatable e17 = new Carrot(Coordinates.posOf(22, 5));
        Eatable e18 = new Carrot(Coordinates.posOf(47, 30));
        Eatable e19 = new Carrot(Coordinates.posOf(13, 18));
        Eatable e20 = new Carrot(Coordinates.posOf(39, 7));
        Eatable e21 = new Carrot(Coordinates.posOf(6, 34));
        Eatable e22 = new Carrot(Coordinates.posOf(28, 16));
        Eatable e23 = new Carrot(Coordinates.posOf(50, 2));
        Eatable e24 = new Carrot(Coordinates.posOf(17, 29));
        Eatable e25 = new Carrot(Coordinates.posOf(31, 11));
        Eatable e26 = new Carrot(Coordinates.posOf(9, 21));
        Eatable e27 = new Carrot(Coordinates.posOf(44, 14));
        Eatable e28 = new Carrot(Coordinates.posOf(20, 33));
        Eatable e29 = new Carrot(Coordinates.posOf(2, 9));
        Eatable e30 = new Carrot(Coordinates.posOf(36, 24));
        Eatable e31 = new Carrot(Coordinates.posOf(14, 6));
        Eatable e32 = new Carrot(Coordinates.posOf(48, 19));
        Eatable e33 = new Carrot(Coordinates.posOf(25, 35));
        Eatable e34 = new Carrot(Coordinates.posOf(11, 27));
        Eatable e35 = new Carrot(Coordinates.posOf(33, 4));
        Eatable e36 = new Carrot(Coordinates.posOf(7, 15));
        Eatable e37 = new Carrot(Coordinates.posOf(41, 28));
        Eatable e38 = new Carrot(Coordinates.posOf(18, 2));
        Eatable e39 = new Carrot(Coordinates.posOf(29, 31));
        Eatable e40 = new Carrot(Coordinates.posOf(45, 10));
        Set<Eatable> eatables = Set.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13,
                e14, e15, e16, e17, e18, e19, e20, e21, e22, e23, e24, e25,
                e26, e27, e28, e29, e30, e31, e32, e33, e34, e35,
                e36, e37, e38, e39, e40);

        //Game game = new Game(houses, eatables, Game.Period.ARTEMIS);
        Game game = new Game();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(DELTA_TIME_MS), e -> {
                        if (!pause) update(panes, game);

                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    private void update(Group[][] cases, Game game){
        infoLabel.setText("Current generation : " + game.getDay());
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

        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                StackPane tile = (StackPane) cases[y][x].getParent();
                Rectangle background = (Rectangle) tile.getChildren().getFirst();
                background.setFill(color);
            }
        }
    }

    private void clear(Group[][] cases){
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                cases[y][x].getChildren().clear();
            }
        }
    }
    private void draw(Group[][] cases, Game game){
        game.getPeasant().forEach(x -> {
            cases[x.getCoordinates().getY()][x.getCoordinates().getX()].getChildren().add(
                    getDrawing(x)
            );
        });
        game.getAllEatable().forEach(x ->
                cases[x.getCoordinates().getY()][x.getCoordinates().getX()].getChildren().add(
                        getDrawing(x)
                )
        );
        game.getAllHouses().forEach(x -> {
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
