package org.vaadin.marcus.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link BookingStatus} enum.
 * Tests values(), valueOf(), and all enum constants.
 */
@DisplayName("BookingStatus Enum Tests")
class BookingStatusTest {

    /**
     * Tests that the values() method returns all enum constants.
     */
    @Test
    @DisplayName("values() should return all enum constants")
    void testValues() {
        // Act
        BookingStatus[] values = BookingStatus.values();

        // Assert
        assertNotNull(values);
        assertEquals(3, values.length);
        assertArrayEquals(
                new BookingStatus[]{BookingStatus.CONFIRMED, BookingStatus.COMPLETED, BookingStatus.CANCELLED},
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
        assertEquals(BookingStatus.CONFIRMED, BookingStatus.valueOf("CONFIRMED"));
        assertEquals(BookingStatus.COMPLETED, BookingStatus.valueOf("COMPLETED"));
        assertEquals(BookingStatus.CANCELLED, BookingStatus.valueOf("CANCELLED"));
    }

    /**
     * Tests that valueOf() throws exception for invalid constant name.
     */
    @Test
    @DisplayName("valueOf() should throw IllegalArgumentException for invalid name")
    void testValueOfInvalidName() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            BookingStatus.valueOf("INVALID");
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
            BookingStatus.valueOf(null);
        });
    }

    /**
     * Tests the CONFIRMED constant.
     */
    @Test
    @DisplayName("CONFIRMED constant should exist and have correct name")
    void testConfirmedConstant() {
        // Act
        BookingStatus status = BookingStatus.CONFIRMED;

        // Assert
        assertNotNull(status);
        assertEquals("CONFIRMED", status.name());
        assertEquals(0, status.ordinal());
    }

    /**
     * Tests the COMPLETED constant.
     */
    @Test
    @DisplayName("COMPLETED constant should exist and have correct name")
    void testCompletedConstant() {
        // Act
        BookingStatus status = BookingStatus.COMPLETED;

        // Assert
        assertNotNull(status);
        assertEquals("COMPLETED", status.name());
        assertEquals(1, status.ordinal());
    }

    /**
     * Tests the CANCELLED constant.
     */
    @Test
    @DisplayName("CANCELLED constant should exist and have correct name")
    void testCancelledConstant() {
        // Act
        BookingStatus status = BookingStatus.CANCELLED;

        // Assert
        assertNotNull(status);
        assertEquals("CANCELLED", status.name());
        assertEquals(2, status.ordinal());
    }

    /**
     * Tests enum equality.
     */
    @Test
    @DisplayName("Enum constants should be equal to themselves")
    void testEnumEquality() {
        // Act & Assert
        assertEquals(BookingStatus.CONFIRMED, BookingStatus.CONFIRMED);
        assertEquals(BookingStatus.COMPLETED, BookingStatus.COMPLETED);
        assertEquals(BookingStatus.CANCELLED, BookingStatus.CANCELLED);

        assertNotEquals(BookingStatus.CONFIRMED, BookingStatus.COMPLETED);
        assertNotEquals(BookingStatus.CONFIRMED, BookingStatus.CANCELLED);
        assertNotEquals(BookingStatus.COMPLETED, BookingStatus.CANCELLED);
    }

    /**
     * Tests enum toString() method.
     */
    @Test
    @DisplayName("toString() should return the name of the constant")
    void testToString() {
        // Act & Assert
        assertEquals("CONFIRMED", BookingStatus.CONFIRMED.toString());
        assertEquals("COMPLETED", BookingStatus.COMPLETED.toString());
        assertEquals("CANCELLED", BookingStatus.CANCELLED.toString());
    }

    /**
     * Tests enum switch statement compatibility.
     */
    @Test
    @DisplayName("Enum should work correctly in switch statements")
    void testSwitchStatement() {
        // Act & Assert
        assertEquals("Confirmed", getStatusDescription(BookingStatus.CONFIRMED));
        assertEquals("Completed", getStatusDescription(BookingStatus.COMPLETED));
        assertEquals("Cancelled", getStatusDescription(BookingStatus.CANCELLED));
    }

    /**
     * Helper method to test enum in switch statements.
     *
     * @param status the booking status
     * @return a description of the status
     */
    private String getStatusDescription(BookingStatus status) {
        return switch (status) {
            case CONFIRMED -> "Confirmed";
            case COMPLETED -> "Completed";
            case CANCELLED -> "Cancelled";
        };
    }
}
