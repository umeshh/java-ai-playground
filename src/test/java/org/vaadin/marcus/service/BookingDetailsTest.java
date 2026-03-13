package org.vaadin.marcus.service;

import org.junit.jupiter.api.Test;
import org.vaadin.marcus.data.BookingStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BookingDetails record.
 * Tests record properties, equality, and immutability.
 */
class BookingDetailsTest {

    @Test
    void testRecordConstruction() {
        // Arrange
        String bookingNumber = "101";
        String firstName = "John";
        String lastName = "Doe";
        LocalDate date = LocalDate.of(2026, 5, 15);
        BookingStatus status = BookingStatus.CONFIRMED;
        String from = "LAX";
        String to = "JFK";
        String bookingClass = "ECONOMY";

        // Act
        BookingDetails details = new BookingDetails(
            bookingNumber, firstName, lastName, date, status, from, to, bookingClass
        );

        // Assert
        assertNotNull(details, "BookingDetails should be created");
        assertEquals(bookingNumber, details.bookingNumber());
        assertEquals(firstName, details.firstName());
        assertEquals(lastName, details.lastName());
        assertEquals(date, details.date());
        assertEquals(status, details.bookingStatus());
        assertEquals(from, details.from());
        assertEquals(to, details.to());
        assertEquals(bookingClass, details.bookingClass());
    }

    @Test
    void testRecordEquality_SameValues() {
        // Arrange
        LocalDate date = LocalDate.of(2026, 6, 1);
        BookingDetails details1 = new BookingDetails(
            "102", "Jane", "Smith", date, BookingStatus.CONFIRMED, "SFO", "ORD", "BUSINESS"
        );
        BookingDetails details2 = new BookingDetails(
            "102", "Jane", "Smith", date, BookingStatus.CONFIRMED, "SFO", "ORD", "BUSINESS"
        );

        // Act & Assert
        assertEquals(details1, details2, "Records with same values should be equal");
        assertEquals(details1.hashCode(), details2.hashCode(), "Hash codes should be equal");
    }

    @Test
    void testRecordEquality_DifferentValues() {
        // Arrange
        LocalDate date = LocalDate.of(2026, 6, 1);
        BookingDetails details1 = new BookingDetails(
            "103", "Alice", "Johnson", date, BookingStatus.CONFIRMED, "ATL", "DEN", "ECONOMY"
        );
        BookingDetails details2 = new BookingDetails(
            "104", "Bob", "Williams", date, BookingStatus.CANCELLED, "LAX", "JFK", "BUSINESS"
        );

        // Act & Assert
        assertNotEquals(details1, details2, "Records with different values should not be equal");
    }

    @Test
    void testRecordToString_ContainsAllFields() {
        // Arrange
        LocalDate date = LocalDate.of(2026, 7, 4);
        BookingDetails details = new BookingDetails(
            "105", "Charlie", "Brown", date, BookingStatus.COMPLETED, "MIA", "SEA", "PREMIUM_ECONOMY"
        );

        // Act
        String toString = details.toString();

        // Assert
        assertNotNull(toString, "toString should not be null");
        assertTrue(toString.contains("105"), "toString should contain booking number");
        assertTrue(toString.contains("Charlie"), "toString should contain first name");
        assertTrue(toString.contains("Brown"), "toString should contain last name");
        assertTrue(toString.contains("COMPLETED"), "toString should contain status");
    }

    @Test
    void testRecordWithNullValues_BookingStatus() {
        // Arrange & Act
        BookingDetails details = new BookingDetails(
            "106", "David", "Wilson", LocalDate.now(), null, "LAX", "SFO", "ECONOMY"
        );

        // Assert
        assertNotNull(details, "Record should be created even with null status");
        assertNull(details.bookingStatus(), "Status should be null");
    }

    @Test
    void testRecordWithAllFieldTypes() {
        // Arrange - Test all BookingStatus values
        LocalDate date = LocalDate.of(2026, 8, 20);
        
        BookingDetails confirmed = new BookingDetails(
            "107", "Eve", "Davis", date, BookingStatus.CONFIRMED, "JFK", "LAX", "BUSINESS"
        );
        BookingDetails cancelled = new BookingDetails(
            "108", "Frank", "Miller", date, BookingStatus.CANCELLED, "ORD", "ATL", "ECONOMY"
        );
        BookingDetails completed = new BookingDetails(
            "109", "Grace", "Taylor", date, BookingStatus.COMPLETED, "DEN", "MIA", "PREMIUM_ECONOMY"
        );

        // Assert
        assertEquals(BookingStatus.CONFIRMED, confirmed.bookingStatus());
        assertEquals(BookingStatus.CANCELLED, cancelled.bookingStatus());
        assertEquals(BookingStatus.COMPLETED, completed.bookingStatus());
    }

    @Test
    void testRecordImmutability_DateField() {
        // Arrange
        LocalDate originalDate = LocalDate.of(2026, 9, 10);
        BookingDetails details = new BookingDetails(
            "110", "Henry", "Anderson", originalDate, BookingStatus.CONFIRMED, "SEA", "DEN", "ECONOMY"
        );

        // Act - Get the date and verify it's the same reference (LocalDate is immutable)
        LocalDate retrievedDate = details.date();

        // Assert
        assertEquals(originalDate, retrievedDate, "Date should match original");
        // LocalDate is immutable, so we can't modify it, but we verify the value is preserved
        assertEquals(LocalDate.of(2026, 9, 10), details.date());
    }

    @Test
    void testRecordWithEmptyStrings() {
        // Arrange & Act
        BookingDetails details = new BookingDetails(
            "", "", "", LocalDate.now(), BookingStatus.CONFIRMED, "", "", ""
        );

        // Assert
        assertNotNull(details, "Record should be created with empty strings");
        assertEquals("", details.bookingNumber());
        assertEquals("", details.firstName());
        assertEquals("", details.lastName());
        assertEquals("", details.from());
        assertEquals("", details.to());
        assertEquals("", details.bookingClass());
    }

    @Test
    void testRecordAccessors() {
        // Arrange
        LocalDate date = LocalDate.of(2026, 10, 31);
        BookingDetails details = new BookingDetails(
            "111", "Ivy", "Thomas", date, BookingStatus.CONFIRMED, "ATL", "ORD", "BUSINESS"
        );

        // Act & Assert - Test all accessor methods
        assertEquals("111", details.bookingNumber(), "bookingNumber() should return correct value");
        assertEquals("Ivy", details.firstName(), "firstName() should return correct value");
        assertEquals("Thomas", details.lastName(), "lastName() should return correct value");
        assertEquals(date, details.date(), "date() should return correct value");
        assertEquals(BookingStatus.CONFIRMED, details.bookingStatus(), "bookingStatus() should return correct value");
        assertEquals("ATL", details.from(), "from() should return correct value");
        assertEquals("ORD", details.to(), "to() should return correct value");
        assertEquals("BUSINESS", details.bookingClass(), "bookingClass() should return correct value");
    }
}
