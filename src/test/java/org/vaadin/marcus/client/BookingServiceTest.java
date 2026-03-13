package org.vaadin.marcus.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vaadin.marcus.data.BookingStatus;
import org.vaadin.marcus.service.BookingDetails;
import org.vaadin.marcus.service.FlightService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookingService.
 * Uses Mockito to mock FlightService dependency.
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private FlightService mockFlightService;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(mockFlightService);
    }

    @Test
    void testGetBookings_ReturnsListFromFlightService() {
        // Arrange
        BookingDetails booking1 = new BookingDetails(
            "101",
            "John",
            "Doe",
            LocalDate.now().plusDays(5),
            BookingStatus.CONFIRMED,
            "LAX",
            "JFK",
            "ECONOMY"
        );
        BookingDetails booking2 = new BookingDetails(
            "102",
            "Jane",
            "Smith",
            LocalDate.now().plusDays(7),
            BookingStatus.CONFIRMED,
            "SFO",
            "ORD",
            "BUSINESS"
        );
        List<BookingDetails> expectedBookings = Arrays.asList(booking1, booking2);
        when(mockFlightService.getBookings()).thenReturn(expectedBookings);

        // Act
        List<BookingDetails> actualBookings = bookingService.getBookings();

        // Assert
        assertNotNull(actualBookings, "Bookings list should not be null");
        assertEquals(2, actualBookings.size(), "Should return 2 bookings");
        assertEquals(expectedBookings, actualBookings, "Should return exact list from FlightService");
        verify(mockFlightService, times(1)).getBookings();
    }

    @Test
    void testGetBookings_WhenNoBookings_ReturnsEmptyList() {
        // Arrange
        when(mockFlightService.getBookings()).thenReturn(Collections.emptyList());

        // Act
        List<BookingDetails> actualBookings = bookingService.getBookings();

        // Assert
        assertNotNull(actualBookings, "Bookings list should not be null");
        assertTrue(actualBookings.isEmpty(), "Should return empty list");
        verify(mockFlightService, times(1)).getBookings();
    }

    @Test
    void testGetBookings_CallsFlightServiceExactlyOnce() {
        // Arrange
        when(mockFlightService.getBookings()).thenReturn(Collections.emptyList());

        // Act
        bookingService.getBookings();

        // Assert
        verify(mockFlightService, times(1)).getBookings();
        verifyNoMoreInteractions(mockFlightService);
    }

    @Test
    void testGetBookings_PropagatesFlightServiceData() {
        // Arrange
        BookingDetails bookingWithSpecificData = new BookingDetails(
            "999",
            "Test",
            "User",
            LocalDate.of(2026, 12, 25),
            BookingStatus.CANCELLED,
            "ATL",
            "DEN",
            "PREMIUM_ECONOMY"
        );
        when(mockFlightService.getBookings())
            .thenReturn(Collections.singletonList(bookingWithSpecificData));

        // Act
        List<BookingDetails> result = bookingService.getBookings();

        // Assert
        assertEquals(1, result.size(), "Should have exactly one booking");
        BookingDetails actualBooking = result.get(0);
        assertEquals("999", actualBooking.bookingNumber());
        assertEquals("Test", actualBooking.firstName());
        assertEquals("User", actualBooking.lastName());
        assertEquals(LocalDate.of(2026, 12, 25), actualBooking.date());
        assertEquals(BookingStatus.CANCELLED, actualBooking.bookingStatus());
        assertEquals("ATL", actualBooking.from());
        assertEquals("DEN", actualBooking.to());
        assertEquals("PREMIUM_ECONOMY", actualBooking.bookingClass());
    }

    @Test
    void testConstructor_AcceptsFlightService() {
        // Arrange & Act
        BookingService service = new BookingService(mockFlightService);

        // Assert
        assertNotNull(service, "BookingService should be created successfully");
    }
}
