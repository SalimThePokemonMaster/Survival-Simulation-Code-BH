package main.java.utilities;

import main.java.components.Constituent;

import java.util.List;

public interface Utilities {

    public static boolean isFree(Coordinates toGo,  List<Constituent> allConstituent){
        for(Constituent constituent : allConstituent){
            if(constituent.coordinates == toGo){
                return false;
            }
        }
        return true;
    }


}
