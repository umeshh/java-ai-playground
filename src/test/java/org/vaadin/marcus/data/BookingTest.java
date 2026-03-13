package org.vaadin.marcus.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Booking} class.
 * Tests all getters, setters, and constructor functionality.
 */
@DisplayName("Booking Class Tests")
class BookingTest {

    /**
     * Tests the constructor initializes all fields correctly.
     */
    @Test
    @DisplayName("Constructor should initialize all fields correctly")
    void testConstructor() {
        // Arrange
        String bookingNumber = "BK123456";
        LocalDate date = LocalDate.of(2026, 3, 15);
        Customer customer = new Customer("John", "Doe");
        BookingStatus status = BookingStatus.CONFIRMED;
        String from = "New York";
        String to = "London";
        BookingClass bookingClass = BookingClass.BUSINESS;

        // Act
        Booking booking = new Booking(bookingNumber, date, customer, status, from, to, bookingClass);

        // Assert
        assertEquals(bookingNumber, booking.getBookingNumber());
        assertEquals(date, booking.getDate());
        assertEquals(customer, booking.getCustomer());
        assertEquals(status, booking.getBookingStatus());
        assertEquals(from, booking.getFrom());
        assertEquals(to, booking.getTo());
        assertEquals(bookingClass, booking.getBookingClass());
    }

    /**
     * Tests the getBookingNumber and setBookingNumber methods.
     */
    @Test
    @DisplayName("Should get and set booking number correctly")
    void testGetAndSetBookingNumber() {
        // Arrange
        Booking booking = createTestBooking();
        String newBookingNumber = "BK999999";

        // Act
        booking.setBookingNumber(newBookingNumber);

        // Assert
        assertEquals(newBookingNumber, booking.getBookingNumber());
    }

    /**
     * Tests the getDate and setDate methods.
     */
    @Test
    @DisplayName("Should get and set date correctly")
    void testGetAndSetDate() {
        // Arrange
        Booking booking = createTestBooking();
        LocalDate newDate = LocalDate.of(2026, 12, 25);

        // Act
        booking.setDate(newDate);

        // Assert
        assertEquals(newDate, booking.getDate());
    }

    /**
     * Tests the getBookingTo and setBookingTo methods.
     */
    @Test
    @DisplayName("Should get and set booking-to date correctly")
    void testGetAndSetBookingTo() {
        // Arrange
        Booking booking = createTestBooking();
        LocalDate bookingTo = LocalDate.of(2026, 3, 20);

        // Act
        booking.setBookingTo(bookingTo);

        // Assert
        assertEquals(bookingTo, booking.getBookingTo());
    }

    /**
     * Tests the getCustomer and setCustomer methods.
     */
    @Test
    @DisplayName("Should get and set customer correctly")
    void testGetAndSetCustomer() {
        // Arrange
        Booking booking = createTestBooking();
        Customer newCustomer = new Customer("Jane", "Smith");

        // Act
        booking.setCustomer(newCustomer);

        // Assert
        assertEquals(newCustomer, booking.getCustomer());
        assertEquals("Jane", booking.getCustomer().getFirstName());
        assertEquals("Smith", booking.getCustomer().getLastName());
    }

    /**
     * Tests the getBookingStatus and setBookingStatus methods.
     */
    @Test
    @DisplayName("Should get and set booking status correctly")
    void testGetAndSetBookingStatus() {
        // Arrange
        Booking booking = createTestBooking();

        // Act & Assert - Test each status
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        assertEquals(BookingStatus.CONFIRMED, booking.getBookingStatus());

        booking.setBookingStatus(BookingStatus.COMPLETED);
        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());

        booking.setBookingStatus(BookingStatus.CANCELLED);
        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
    }

    /**
     * Tests the getFrom and setFrom methods.
     */
    @Test
    @DisplayName("Should get and set from location correctly")
    void testGetAndSetFrom() {
        // Arrange
        Booking booking = createTestBooking();
        String newFrom = "Paris";

        // Act
        booking.setFrom(newFrom);

        // Assert
        assertEquals(newFrom, booking.getFrom());
    }

    /**
     * Tests the getTo and setTo methods.
     */
    @Test
    @DisplayName("Should get and set to location correctly")
    void testGetAndSetTo() {
        // Arrange
        Booking booking = createTestBooking();
        String newTo = "Tokyo";

        // Act
        booking.setTo(newTo);

        // Assert
        assertEquals(newTo, booking.getTo());
    }

    /**
     * Tests the getBookingClass and setBookingClass methods.
     */
    @Test
    @DisplayName("Should get and set booking class correctly")
    void testGetAndSetBookingClass() {
        // Arrange
        Booking booking = createTestBooking();

        // Act & Assert - Test each class
        booking.setBookingClass(BookingClass.ECONOMY);
        assertEquals(BookingClass.ECONOMY, booking.getBookingClass());

        booking.setBookingClass(BookingClass.PREMIUM_ECONOMY);
        assertEquals(BookingClass.PREMIUM_ECONOMY, booking.getBookingClass());

        booking.setBookingClass(BookingClass.BUSINESS);
        assertEquals(BookingClass.BUSINESS, booking.getBookingClass());
    }

    /**
     * Tests that null values can be set and retrieved.
     */
    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Arrange
        Booking booking = createTestBooking();

        // Act
        booking.setBookingNumber(null);
        booking.setDate(null);
        booking.setBookingTo(null);
        booking.setCustomer(null);
        booking.setBookingStatus(null);
        booking.setFrom(null);
        booking.setTo(null);
        booking.setBookingClass(null);

        // Assert
        assertNull(booking.getBookingNumber());
        assertNull(booking.getDate());
        assertNull(booking.getBookingTo());
        assertNull(booking.getCustomer());
        assertNull(booking.getBookingStatus());
        assertNull(booking.getFrom());
        assertNull(booking.getTo());
        assertNull(booking.getBookingClass());
    }

    /**
     * Helper method to create a test booking instance.
     *
     * @return a new Booking instance with test data
     */
    private Booking createTestBooking() {
        return new Booking(
                "BK123456",
                LocalDate.of(2026, 3, 15),
                new Customer("John", "Doe"),
                BookingStatus.CONFIRMED,
                "New York",
                "London",
                BookingClass.BUSINESS
        );
    }
}
