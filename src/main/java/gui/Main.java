package main.java.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.components.*;
import main.java.components.eatable.Carrot;
import main.java.components.eatable.Eatable;
import main.java.utilities.Coordinates;
import main.java.utilities.Period;

import java.util.*;

/**
 * Main class of {@code CodeBHSimulation}, allowing to launch the final program
 *
 * @author Sami Kabbaj (393773)
 * @author Salim Chaoui El Faiz (396504)
 */
public final class Main extends Application {
    private static final int TILE_SIZE = 20;
    private static final int MAP_WIDTH = 40;
    private static final int MAP_HEIGHT = 30;

    private boolean remove = false;

    public static void main(String[] args) {
        System.out.println("CodeBHSimulation ~ Done with <3 by Sami & Salim");
        launch(args);
    }
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
        House h1 = new House(new Coordinates(23, 16));
        House h2 = new House(new Coordinates(10, 10));
        Eatable e1 = new Carrot(Coordinates.posOf(25, 20));
        Eatable e2 = new Carrot(Coordinates.posOf(7, 0));
        Eatable e3 = new Carrot(Coordinates.posOf(15, 15));
        Eatable e4 = new Carrot(Coordinates.posOf(15, 3));
        Eatable e5 = new Carrot(Coordinates.posOf(3, 25));
        Eatable e6 = new Carrot(Coordinates.posOf(25, 13));

        Game game = new Game(Set.of(h1, h2), Set.of(e1, e2, e3, e4, e5, e6), Period.HELIOS);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(300), e ->
                        update(panes, game)
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(mapGrid, 1000, 700);
        scene.setFill(Color.rgb(193, 225, 193));
        primaryStage.setScene(scene);
        primaryStage.setTitle("CodeBHSimulation");
        primaryStage.setTitle("Map Example");
        primaryStage.show();

    }
    private void update(Group[][] cases, Game game){
        clear(cases);
        game.update();
        draw(cases, game);
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
            cases[x.coordinates.getY()][x.coordinates.getX()].getChildren().add(
                    getDrawing(x)
            );
        });
        game.allEatable.forEach(x ->
                cases[x.coordinates.getY()][x.coordinates.getX()].getChildren().add(
                        getDrawing(x)
                )
        );
        game.allHouses.forEach(x -> {
            cases[x.coordinates.getY()][x.coordinates.getX()].getChildren().add(
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
