package org.vaadin.marcus.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link BookingData} class.
 * Tests all getters and setters for customers and bookings lists.
 */
@DisplayName("BookingData Class Tests")
class BookingDataTest {

    /**
     * Tests the default constructor initializes empty lists.
     */
    @Test
    @DisplayName("Constructor should initialize with empty lists")
    void testConstructor() {
        // Act
        BookingData bookingData = new BookingData();

        // Assert
        assertNotNull(bookingData.getCustomers());
        assertNotNull(bookingData.getBookings());
        assertTrue(bookingData.getCustomers().isEmpty());
        assertTrue(bookingData.getBookings().isEmpty());
    }

    /**
     * Tests the getCustomers and setCustomers methods.
     */
    @Test
    @DisplayName("Should get and set customers list correctly")
    void testGetAndSetCustomers() {
        // Arrange
        BookingData bookingData = new BookingData();
        List<Customer> customers = new ArrayList<>();
        Customer customer1 = new Customer("John", "Doe");
        Customer customer2 = new Customer("Jane", "Smith");
        customers.add(customer1);
        customers.add(customer2);

        // Act
        bookingData.setCustomers(customers);

        // Assert
        assertEquals(customers, bookingData.getCustomers());
        assertEquals(2, bookingData.getCustomers().size());
        assertTrue(bookingData.getCustomers().contains(customer1));
        assertTrue(bookingData.getCustomers().contains(customer2));
    }

    /**
     * Tests the getBookings and setBookings methods.
     */
    @Test
    @DisplayName("Should get and set bookings list correctly")
    void testGetAndSetBookings() {
        // Arrange
        BookingData bookingData = new BookingData();
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = createTestBooking("BK001");
        Booking booking2 = createTestBooking("BK002");
        bookings.add(booking1);
        bookings.add(booking2);

        // Act
        bookingData.setBookings(bookings);

        // Assert
        assertEquals(bookings, bookingData.getBookings());
        assertEquals(2, bookingData.getBookings().size());
        assertTrue(bookingData.getBookings().contains(booking1));
        assertTrue(bookingData.getBookings().contains(booking2));
    }

    /**
     * Tests that the customers list is mutable.
     */
    @Test
    @DisplayName("Customers list should be mutable")
    void testCustomersListIsMutable() {
        // Arrange
        BookingData bookingData = new BookingData();
        Customer customer = new Customer("John", "Doe");

        // Act
        bookingData.getCustomers().add(customer);

        // Assert
        assertEquals(1, bookingData.getCustomers().size());
        assertEquals(customer, bookingData.getCustomers().get(0));
    }

    /**
     * Tests that the bookings list is mutable.
     */
    @Test
    @DisplayName("Bookings list should be mutable")
    void testBookingsListIsMutable() {
        // Arrange
        BookingData bookingData = new BookingData();
        Booking booking = createTestBooking("BK123");

        // Act
        bookingData.getBookings().add(booking);

        // Assert
        assertEquals(1, bookingData.getBookings().size());
        assertEquals(booking, bookingData.getBookings().get(0));
    }

    /**
     * Tests setting empty lists.
     */
    @Test
    @DisplayName("Should handle empty lists")
    void testEmptyLists() {
        // Arrange
        BookingData bookingData = new BookingData();
        List<Customer> emptyCustomers = new ArrayList<>();
        List<Booking> emptyBookings = new ArrayList<>();

        // Act
        bookingData.setCustomers(emptyCustomers);
        bookingData.setBookings(emptyBookings);

        // Assert
        assertNotNull(bookingData.getCustomers());
        assertNotNull(bookingData.getBookings());
        assertTrue(bookingData.getCustomers().isEmpty());
        assertTrue(bookingData.getBookings().isEmpty());
    }

    /**
     * Tests setting null values.
     */
    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Arrange
        BookingData bookingData = new BookingData();

        // Act
        bookingData.setCustomers(null);
        bookingData.setBookings(null);

        // Assert
        assertNull(bookingData.getCustomers());
        assertNull(bookingData.getBookings());
    }

    /**
     * Tests managing both customers and bookings simultaneously.
     */
    @Test
    @DisplayName("Should manage customers and bookings independently")
    void testIndependentManagement() {
        // Arrange
        BookingData bookingData = new BookingData();
        Customer customer = new Customer("John", "Doe");
        Booking booking = createTestBooking("BK001");

        // Act
        bookingData.getCustomers().add(customer);
        bookingData.getBookings().add(booking);

        // Assert
        assertEquals(1, bookingData.getCustomers().size());
        assertEquals(1, bookingData.getBookings().size());
        assertEquals(customer, bookingData.getCustomers().get(0));
        assertEquals(booking, bookingData.getBookings().get(0));
    }

    /**
     * Tests replacing lists with new ones.
     */
    @Test
    @DisplayName("Should replace lists with new ones")
    void testReplacingLists() {
        // Arrange
        BookingData bookingData = new BookingData();
        List<Customer> originalCustomers = new ArrayList<>();
        originalCustomers.add(new Customer("John", "Doe"));
        bookingData.setCustomers(originalCustomers);

        List<Customer> newCustomers = new ArrayList<>();
        newCustomers.add(new Customer("Jane", "Smith"));
        newCustomers.add(new Customer("Bob", "Johnson"));

        // Act
        bookingData.setCustomers(newCustomers);

        // Assert
        assertEquals(2, bookingData.getCustomers().size());
        assertEquals(newCustomers, bookingData.getCustomers());
        assertNotEquals(originalCustomers, bookingData.getCustomers());
    }

    /**
     * Helper method to create a test booking instance.
     *
     * @param bookingNumber the booking number
     * @return a new Booking instance with test data
     */
    private Booking createTestBooking(String bookingNumber) {
        return new Booking(
                bookingNumber,
                LocalDate.of(2026, 3, 15),
                new Customer("Test", "Customer"),
                BookingStatus.CONFIRMED,
                "New York",
                "London",
                BookingClass.ECONOMY
        );
    }
}
