package org.vaadin.marcus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vaadin.marcus.data.BookingStatus;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FlightService.
 * Tests cover booking retrieval, changes, and cancellations with various scenarios.
 */
class FlightServiceTest {

    private FlightService flightService;

    @BeforeEach
    void setUp() {
        // Create a new instance with demo data for each test
        flightService = new FlightService();
    }

    @Test
    void testGetBookings_ReturnsNonEmptyList() {
        // Arrange & Act
        List<BookingDetails> bookings = flightService.getBookings();

        // Assert
        assertNotNull(bookings, "Bookings list should not be null");
        assertFalse(bookings.isEmpty(), "Bookings list should contain demo data");
        assertEquals(5, bookings.size(), "Demo data should initialize 5 bookings");
    }

    @Test
    void testGetBookings_ContainsValidBookingDetails() {
        // Arrange & Act
        List<BookingDetails> bookings = flightService.getBookings();
        BookingDetails firstBooking = bookings.get(0);

        // Assert
        assertNotNull(firstBooking.bookingNumber(), "Booking number should not be null");
        assertNotNull(firstBooking.firstName(), "First name should not be null");
        assertNotNull(firstBooking.lastName(), "Last name should not be null");
        assertNotNull(firstBooking.date(), "Date should not be null");
        assertNotNull(firstBooking.bookingStatus(), "Booking status should not be null");
        assertNotNull(firstBooking.from(), "Departure airport should not be null");
        assertNotNull(firstBooking.to(), "Arrival airport should not be null");
        assertNotNull(firstBooking.bookingClass(), "Booking class should not be null");
    }

    @Test
    void testGetBookingDetails_WithValidCredentials_ReturnsBooking() {
        // Arrange
        List<BookingDetails> allBookings = flightService.getBookings();
        BookingDetails expectedBooking = allBookings.get(0);
        String bookingNumber = expectedBooking.bookingNumber();
        String firstName = expectedBooking.firstName();
        String lastName = expectedBooking.lastName();

        // Act
        BookingDetails actualBooking = flightService.getBookingDetails(
            bookingNumber, firstName, lastName
        );

        // Assert
        assertNotNull(actualBooking, "Booking should be found");
        assertEquals(expectedBooking.bookingNumber(), actualBooking.bookingNumber());
        assertEquals(expectedBooking.firstName(), actualBooking.firstName());
        assertEquals(expectedBooking.lastName(), actualBooking.lastName());
    }

    @Test
    void testGetBookingDetails_WithInvalidBookingNumber_ThrowsException() {
        // Arrange
        String invalidBookingNumber = "999";
        String firstName = "John";
        String lastName = "Doe";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> flightService.getBookingDetails(invalidBookingNumber, firstName, lastName),
            "Should throw IllegalArgumentException for invalid booking number"
        );
        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    void testGetBookingDetails_WithInvalidCustomerName_ThrowsException() {
        // Arrange
        List<BookingDetails> allBookings = flightService.getBookings();
        String bookingNumber = allBookings.get(0).bookingNumber();
        String invalidFirstName = "Invalid";
        String invalidLastName = "Name";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> flightService.getBookingDetails(bookingNumber, invalidFirstName, invalidLastName),
            "Should throw IllegalArgumentException for invalid customer name"
        );
        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    void testGetBookingDetails_CaseInsensitiveSearch() {
        // Arrange
        List<BookingDetails> allBookings = flightService.getBookings();
        BookingDetails expectedBooking = allBookings.get(0);
        String bookingNumber = expectedBooking.bookingNumber().toLowerCase();
        String firstName = expectedBooking.firstName().toUpperCase();
        String lastName = expectedBooking.lastName().toLowerCase();

        // Act
        BookingDetails actualBooking = flightService.getBookingDetails(
            bookingNumber, firstName, lastName
        );

        // Assert
        assertNotNull(actualBooking, "Case-insensitive search should find booking");
        assertEquals(expectedBooking.bookingNumber(), actualBooking.bookingNumber());
    }

    @Test
    void testChangeBooking_WithValidData_UpdatesBooking() {
        // Arrange - Use a booking far enough in the future (2 days or more)
        List<BookingDetails> allBookings = flightService.getBookings();
        // Demo data creates bookings at plusDays(0), plusDays(2), plusDays(4), etc.
        // Select booking at index 2 or higher (plusDays(4+) is well beyond 24 hours)
        BookingDetails originalBooking = allBookings.get(2);
        LocalDate newDate = LocalDate.now().plusDays(10);
        String newFrom = "ORD";
        String newTo = "ATL";

        // Act
        flightService.changeBooking(
            originalBooking.bookingNumber(),
            originalBooking.firstName(),
            originalBooking.lastName(),
            newDate,
            newFrom,
            newTo
        );

        // Assert
        BookingDetails updatedBooking = flightService.getBookingDetails(
            originalBooking.bookingNumber(),
            originalBooking.firstName(),
            originalBooking.lastName()
        );
        assertEquals(newDate, updatedBooking.date(), "Date should be updated");
        assertEquals(newFrom, updatedBooking.from(), "Departure airport should be updated");
        assertEquals(newTo, updatedBooking.to(), "Arrival airport should be updated");
    }

    @Test
    void testChangeBooking_Within24Hours_ThrowsException() {
        // Arrange - Use a booking far enough in the future to allow initial change
        List<BookingDetails> allBookings = flightService.getBookings();
        // Select booking at index 2 or higher (plusDays(4+) allows initial change)
        BookingDetails bookingToChange = allBookings.get(2);
        
        // First change the booking date to today (within 24 hours - at the boundary)
        LocalDate today = LocalDate.now();
        flightService.changeBooking(
            bookingToChange.bookingNumber(),
            bookingToChange.firstName(),
            bookingToChange.lastName(),
            today,
            "LAX",
            "JFK"
        );

        // Act & Assert - Try to change booking that's now within 24 hours
        LocalDate newDate = LocalDate.now().plusDays(5);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> flightService.changeBooking(
                bookingToChange.bookingNumber(),
                bookingToChange.firstName(),
                bookingToChange.lastName(),
                newDate,
                "SFO",
                "SEA"
            ),
            "Should throw exception when trying to change booking within 24 hours"
        );
        assertTrue(exception.getMessage().contains("within 24 hours"),
            "Exception message should mention 24 hours restriction");
    }

    @Test
    void testCancelBooking_WithValidData_CancelsBooking() {
        // Arrange - Use a booking far enough in the future (2 days or more)
        List<BookingDetails> allBookings = flightService.getBookings();
        // Demo data: index 2 has plusDays(4), well beyond 48 hours
        BookingDetails bookingToCancel = allBookings.get(2);

        // Act
        flightService.cancelBooking(
            bookingToCancel.bookingNumber(),
            bookingToCancel.firstName(),
            bookingToCancel.lastName()
        );

        // Assert
        BookingDetails cancelledBooking = flightService.getBookingDetails(
            bookingToCancel.bookingNumber(),
            bookingToCancel.firstName(),
            bookingToCancel.lastName()
        );
        assertEquals(BookingStatus.CANCELLED, cancelledBooking.bookingStatus(),
            "Booking status should be CANCELLED");
    }

    @Test
    void testCancelBooking_Within48Hours_ThrowsException() {
        // Arrange - Use a booking far enough in the future to allow initial change
        List<BookingDetails> allBookings = flightService.getBookings();
        // Select booking at index 3 or higher to ensure it's far enough
        BookingDetails bookingToCancel = allBookings.get(3);
        
        // First change the booking date to be within 48 hours
        LocalDate soonDate = LocalDate.now().plusDays(1);
        flightService.changeBooking(
            bookingToCancel.bookingNumber(),
            bookingToCancel.firstName(),
            bookingToCancel.lastName(),
            soonDate,
            "LAX",
            "JFK"
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> flightService.cancelBooking(
                bookingToCancel.bookingNumber(),
                bookingToCancel.firstName(),
                bookingToCancel.lastName()
            ),
            "Should throw exception when trying to cancel booking within 48 hours"
        );
        assertTrue(exception.getMessage().contains("within 48 hours"),
            "Exception message should mention 48 hours restriction");
    }

    @Test
    void testCancelBooking_WithInvalidBooking_ThrowsException() {
        // Arrange
        String invalidBookingNumber = "999";
        String firstName = "John";
        String lastName = "Doe";

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> flightService.cancelBooking(invalidBookingNumber, firstName, lastName),
            "Should throw exception for invalid booking"
        );
    }

    @Test
    void testChangeBooking_WithInvalidBooking_ThrowsException() {
        // Arrange
        String invalidBookingNumber = "999";
        String firstName = "John";
        String lastName = "Doe";
        LocalDate newDate = LocalDate.now().plusDays(10);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> flightService.changeBooking(
                invalidBookingNumber, firstName, lastName, newDate, "LAX", "JFK"
            ),
            "Should throw exception for invalid booking"
        );
    }

    @Test
    void testDemoDataInitialization_CreatesConsistentData() {
        // Arrange & Act
        FlightService service1 = new FlightService();
        FlightService service2 = new FlightService();

        // Assert
        assertEquals(service1.getBookings().size(), service2.getBookings().size(),
            "Both services should create same number of bookings");
    }
}
