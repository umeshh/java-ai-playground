package org.vaadin.marcus.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Customer} class.
 * Tests all constructors, getters, and setters.
 */
@DisplayName("Customer Class Tests")
class CustomerTest {

    /**
     * Tests the no-argument constructor.
     */
    @Test
    @DisplayName("No-arg constructor should create customer with null fields and empty bookings list")
    void testNoArgConstructor() {
        // Act
        Customer customer = new Customer();

        // Assert
        assertNull(customer.getFirstName());
        assertNull(customer.getLastName());
        assertNotNull(customer.getBookings());
        assertTrue(customer.getBookings().isEmpty());
    }

    /**
     * Tests the two-argument constructor.
     */
    @Test
    @DisplayName("Two-arg constructor should initialize first and last name correctly")
    void testTwoArgConstructor() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";

        // Act
        Customer customer = new Customer(firstName, lastName);

        // Assert
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
        assertNotNull(customer.getBookings());
        assertTrue(customer.getBookings().isEmpty());
    }

    /**
     * Tests the getFirstName and setFirstName methods.
     */
    @Test
    @DisplayName("Should get and set first name correctly")
    void testGetAndSetFirstName() {
        // Arrange
        Customer customer = new Customer();
        String firstName = "Jane";

        // Act
        customer.setFirstName(firstName);

        // Assert
        assertEquals(firstName, customer.getFirstName());
    }

    /**
     * Tests the getLastName and setLastName methods.
     */
    @Test
    @DisplayName("Should get and set last name correctly")
    void testGetAndSetLastName() {
        // Arrange
        Customer customer = new Customer();
        String lastName = "Smith";

        // Act
        customer.setLastName(lastName);

        // Assert
        assertEquals(lastName, customer.getLastName());
    }

    /**
     * Tests the getBookings and setBookings methods.
     */
    @Test
    @DisplayName("Should get and set bookings list correctly")
    void testGetAndSetBookings() {
        // Arrange
        Customer customer = new Customer("John", "Doe");
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = createTestBooking("BK001");
        Booking booking2 = createTestBooking("BK002");
        bookings.add(booking1);
        bookings.add(booking2);

        // Act
        customer.setBookings(bookings);

        // Assert
        assertEquals(bookings, customer.getBookings());
        assertEquals(2, customer.getBookings().size());
        assertTrue(customer.getBookings().contains(booking1));
        assertTrue(customer.getBookings().contains(booking2));
    }

    /**
     * Tests that the bookings list is mutable.
     */
    @Test
    @DisplayName("Bookings list should be mutable")
    void testBookingsListIsMutable() {
        // Arrange
        Customer customer = new Customer("John", "Doe");
        Booking booking = createTestBooking("BK123");

        // Act
        customer.getBookings().add(booking);

        // Assert
        assertEquals(1, customer.getBookings().size());
        assertEquals(booking, customer.getBookings().get(0));
    }

    /**
     * Tests setting an empty bookings list.
     */
    @Test
    @DisplayName("Should handle empty bookings list")
    void testEmptyBookingsList() {
        // Arrange
        Customer customer = new Customer("John", "Doe");
        List<Booking> emptyList = new ArrayList<>();

        // Act
        customer.setBookings(emptyList);

        // Assert
        assertNotNull(customer.getBookings());
        assertTrue(customer.getBookings().isEmpty());
    }

    /**
     * Tests setting null values.
     */
    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Arrange
        Customer customer = new Customer("John", "Doe");

        // Act
        customer.setFirstName(null);
        customer.setLastName(null);
        customer.setBookings(null);

        // Assert
        assertNull(customer.getFirstName());
        assertNull(customer.getLastName());
        assertNull(customer.getBookings());
    }

    /**
     * Tests updating customer name after creation.
     */
    @Test
    @DisplayName("Should update customer name after creation")
    void testUpdateCustomerName() {
        // Arrange
        Customer customer = new Customer("John", "Doe");

        // Act
        customer.setFirstName("Jane");
        customer.setLastName("Smith");

        // Assert
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
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
