package main.java;

import main.java.components.Peasant;
import main.java.components.House;
import main.java.utilities.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {


        // créer une map

        // add dans la map la maison

        House maison = new House(new Coordinates(10, 10));
        List<Peasant> paysans = new ArrayList<>();

        while (true) {
            // update maison
            //Character nouveau = maison.update();
           /* if (nouveau != null) {
                paysans.add(nouveau);
                System.out.println("Nouveau paysan ! Total = " + paysans.size());
            }*/

            // update paysans
            for (Peasant p : paysans) {
               // p.update(eatableToAggro);
            }

            // vitesse de la simulation
            Thread.sleep(16); // ~60 updates par seconde
        }
    }
}
