package main.java.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.java.components.Peasant;
import main.java.utilities.Coordinates;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

/**
 * Main class of {@code ReCHor}, allowing to launch the final program
 *
 * @author Sami Kabbaj (393773)
 * @author Salim Chaoui El Faiz (396504)
 */
public final class Main extends Application {
    private static final int TILE_SIZE = 20;
    private static final int MAP_WIDTH = 40;
    private static final int MAP_HEIGHT = 30;

    public static void main(String[] args) {
        System.out.println("ReCHor ~ Done with <3 by Sami & Salim");
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {



        GridPane mapGrid = new GridPane();
        mapGrid.setAlignment(Pos.CENTER);

//        Map<Peasant, Rectangle> mappingCharacters = Map.of();
//
//        mappingCharacters.put(
//                new Peasant(Coordinates.posOf(2, 2), null), createPeasant()
//        );
        StackPane[][] panes = new StackPane[MAP_HEIGHT][MAP_WIDTH];
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {

                StackPane tile = createTile();
                panes[y][x] = tile;
                mapGrid.add(tile, x, y);
            }
        }

        Scene scene = new Scene(mapGrid, 1000, 700);
        scene.setFill(Color.rgb(193, 225, 193));
        primaryStage.setScene(scene);
        primaryStage.setTitle("ReCHor");
        primaryStage.setTitle("Map Example");
        primaryStage.show();

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
        double centerY = 0; // légèrement au-dessus du centre
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
