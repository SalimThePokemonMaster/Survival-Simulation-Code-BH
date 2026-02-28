package main.java.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyCoordinatesTest {

    @Test
    void constructorShouldInitializeValuesCorrectly() {
        Coordinates c = new Coordinates(3, 5);
        assertEquals(3, c.getX());
        assertEquals(5, c.getY());
    }

    @Test
    void gettersShouldReturnCorrectValuesWithNegativeNumbers() {
        Coordinates c = new Coordinates(-2, -7);
        assertEquals(-2, c.getX());
        assertEquals(-7, c.getY());
    }

    @Test
    void equalsShouldReturnTrueForSameCoordinates() {
        Coordinates c1 = new Coordinates(1, 2);
        Coordinates c2 = new Coordinates(1, 2);
        assertEquals(c1, c2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentCoordinates() {
        Coordinates c1 = new Coordinates(1, 2);
        Coordinates c2 = new Coordinates(2, 3);
        assertNotEquals(c1, c2);
    }

    @Test
    void hashCodeShouldBeEqualForEqualObjects() {
        Coordinates c1 = new Coordinates(4, 6);
        Coordinates c2 = new Coordinates(4, 6);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void hashCodeShouldDifferForDifferentObjects() {
        Coordinates c1 = new Coordinates(4, 6);
        Coordinates c2 = new Coordinates(7, 8);
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void distanceToShouldReturnZeroForSameCoordinates() {
        Coordinates c = new Coordinates(3, 3);
        assertEquals(0.0, c.distanceTo(new Coordinates(3, 3)));
    }

    @Test
    void distanceToShouldReturnCorrectDistance() {
        Coordinates c1 = new Coordinates(0, 0);
        Coordinates c2 = new Coordinates(3, 4);
        assertEquals(5.0, c1.distanceTo(c2));
    }

    @Test
    void isSuperposedShouldReturnTrueForEqualCoordinates() {
        Coordinates c1 = new Coordinates(5, 5);
        Coordinates c2 = new Coordinates(5, 5);
        assertTrue(c1.isSuperposed(c2));
    }

    @Test
    void isSuperposedShouldReturnFalseForDifferentCoordinates() {
        Coordinates c1 = new Coordinates(5, 5);
        Coordinates c2 = new Coordinates(1, 1);
        assertFalse(c1.isSuperposed(c2));
    }

    @Test
    void translatedWithIntsShouldReturnNewTranslatedCoordinates() {
        Coordinates c = new Coordinates(1, 1);
        Coordinates result = c.translated(2, 3);

        assertEquals(3, result.getX());
        assertEquals(4, result.getY());
    }

    @Test
    void translatedWithIntsShouldNotModifyOriginalObject() {
        Coordinates c = new Coordinates(1, 1);
        c.translated(5, 5);

        assertEquals(1, c.getX());
        assertEquals(1, c.getY());
    }

    @Test
    void translatedWithCoordinatesShouldReturnCorrectResult() {
        Coordinates c1 = new Coordinates(2, 2);
        Coordinates c2 = new Coordinates(3, 4);

        Coordinates result = c1.translated(c2);

        assertEquals(5, result.getX());
        assertEquals(6, result.getY());
    }

    @Test
    void translatedWithCoordinatesShouldHandleNegativeTranslation() {
        Coordinates c1 = new Coordinates(5, 5);
        Coordinates c2 = new Coordinates(-2, -3);

        Coordinates result = c1.translated(c2);

        assertEquals(3, result.getX());
        assertEquals(2, result.getY());
    }
}