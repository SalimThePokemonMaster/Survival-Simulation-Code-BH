package main.java.utilities;


/**
 * Final non instantiable utility class containing static functions to make checks.
 *
 * @author Sami Kabbaj
 * @author Salim Chaoui El Faiz
 */
public final class Preconditions {
    /**
     * Private constructor
     */
    private Preconditions(){}

    /**
     * Throws an exception if the condition involved is not respected. Meant to be used as a check on the
     * given arguments of a function.
     *
     * @param shouldBeTrue condition that we want to be respected.
     * @throws IllegalArgumentException if the condition that is checked is not respected (or is illegal).
     */
    public static void require(boolean shouldBeTrue){
        if(!shouldBeTrue){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Throws an exception if the condition involved is not respected. Meant to be used as a check on the current state
     * of a process, of a class, of an object.
     *
     * @param shouldBeTrue condition that we want to be respected
     * @param message to be displayed in case of an error.
     * @throws IllegalStateException  if the condition that is checked is not respected (or is illegal).
     */
    public static void ensure(boolean shouldBeTrue, String message){
        if(!shouldBeTrue){
            throw new IllegalStateException(message);
        }
    }

}
