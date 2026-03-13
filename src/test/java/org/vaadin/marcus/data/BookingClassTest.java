package org.vaadin.marcus.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link BookingClass} enum.
 * Tests values(), valueOf(), and all enum constants.
 */
@DisplayName("BookingClass Enum Tests")
class BookingClassTest {

    /**
     * Tests that the values() method returns all enum constants.
     */
    @Test
    @DisplayName("values() should return all enum constants")
    void testValues() {
        // Act
        BookingClass[] values = BookingClass.values();

        // Assert
        assertNotNull(values);
        assertEquals(3, values.length);
        assertArrayEquals(
                new BookingClass[]{BookingClass.ECONOMY, BookingClass.PREMIUM_ECONOMY, BookingClass.BUSINESS},
                values
        );
    }

    /**
     * Tests the valueOf() method with valid constant names.
     */
    @Test
    @DisplayName("valueOf() should return correct enum constant for valid names")
    void testValueOf() {
        // Act & Assert
        assertEquals(BookingClass.ECONOMY, BookingClass.valueOf("ECONOMY"));
        assertEquals(BookingClass.PREMIUM_ECONOMY, BookingClass.valueOf("PREMIUM_ECONOMY"));
        assertEquals(BookingClass.BUSINESS, BookingClass.valueOf("BUSINESS"));
    }

    /**
     * Tests that valueOf() throws exception for invalid constant name.
     */
    @Test
    @DisplayName("valueOf() should throw IllegalArgumentException for invalid name")
    void testValueOfInvalidName() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            BookingClass.valueOf("INVALID");
        });
    }

    /**
     * Tests that valueOf() throws exception for null.
     */
    @Test
    @DisplayName("valueOf() should throw NullPointerException for null")
    void testValueOfNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            BookingClass.valueOf(null);
        });
    }

    /**
     * Tests the ECONOMY constant.
     */
    @Test
    @DisplayName("ECONOMY constant should exist and have correct name")
    void testEconomyConstant() {
        // Act
        BookingClass bookingClass = BookingClass.ECONOMY;

        // Assert
        assertNotNull(bookingClass);
        assertEquals("ECONOMY", bookingClass.name());
        assertEquals(0, bookingClass.ordinal());
    }

    /**
     * Tests the PREMIUM_ECONOMY constant.
     */
    @Test
    @DisplayName("PREMIUM_ECONOMY constant should exist and have correct name")
    void testPremiumEconomyConstant() {
        // Act
        BookingClass bookingClass = BookingClass.PREMIUM_ECONOMY;

        // Assert
        assertNotNull(bookingClass);
        assertEquals("PREMIUM_ECONOMY", bookingClass.name());
        assertEquals(1, bookingClass.ordinal());
    }

    /**
     * Tests the BUSINESS constant.
     */
    @Test
    @DisplayName("BUSINESS constant should exist and have correct name")
    void testBusinessConstant() {
        // Act
        BookingClass bookingClass = BookingClass.BUSINESS;

        // Assert
        assertNotNull(bookingClass);
        assertEquals("BUSINESS", bookingClass.name());
        assertEquals(2, bookingClass.ordinal());
    }

    /**
     * Tests enum equality.
     */
    @Test
    @DisplayName("Enum constants should be equal to themselves")
    void testEnumEquality() {
        // Act & Assert
        assertEquals(BookingClass.ECONOMY, BookingClass.ECONOMY);
        assertEquals(BookingClass.PREMIUM_ECONOMY, BookingClass.PREMIUM_ECONOMY);
        assertEquals(BookingClass.BUSINESS, BookingClass.BUSINESS);

        assertNotEquals(BookingClass.ECONOMY, BookingClass.PREMIUM_ECONOMY);
        assertNotEquals(BookingClass.ECONOMY, BookingClass.BUSINESS);
        assertNotEquals(BookingClass.PREMIUM_ECONOMY, BookingClass.BUSINESS);
    }

    /**
     * Tests enum toString() method.
     */
    @Test
    @DisplayName("toString() should return the name of the constant")
    void testToString() {
        // Act & Assert
        assertEquals("ECONOMY", BookingClass.ECONOMY.toString());
        assertEquals("PREMIUM_ECONOMY", BookingClass.PREMIUM_ECONOMY.toString());
        assertEquals("BUSINESS", BookingClass.BUSINESS.toString());
    }

    /**
     * Tests enum switch statement compatibility.
     */
    @Test
    @DisplayName("Enum should work correctly in switch statements")
    void testSwitchStatement() {
        // Act & Assert
        assertEquals("Economy Class", getClassDescription(BookingClass.ECONOMY));
        assertEquals("Premium Economy Class", getClassDescription(BookingClass.PREMIUM_ECONOMY));
        assertEquals("Business Class", getClassDescription(BookingClass.BUSINESS));
    }

    /**
     * Helper method to test enum in switch statements.
     *
     * @param bookingClass the booking class
     * @return a description of the booking class
     */
    private String getClassDescription(BookingClass bookingClass) {
        return switch (bookingClass) {
            case ECONOMY -> "Economy Class";
            case PREMIUM_ECONOMY -> "Premium Economy Class";
            case BUSINESS -> "Business Class";
        };
    }
}
