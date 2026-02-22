package main.java.utilities;

public final class Preconditions {
    /**
     * Private constructor
     */
    private Preconditions(){}

    /**
     * Throws an exception if the condition involved is not respected
     *
     * @param shouldBeTrue condition that we want to be respected
     * @throws IllegalArgumentException if the condition that is checked is not respected (or is illegal)
     */
    public static void require(boolean shouldBeTrue){
        if(!shouldBeTrue){
            throw new IllegalArgumentException();
        }
    }

    public static void ensure(boolean shouldBeTrue, String message){
        if(!shouldBeTrue){
            throw new IllegalArgumentException(message);
        }
    }
}
