package org.vaadin.marcus.langchain4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vaadin.marcus.data.BookingStatus;
import org.vaadin.marcus.service.BookingDetails;
import org.vaadin.marcus.service.FlightService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for LangChain4jTools.
 * Tests verify that tool methods correctly delegate to FlightService.
 */
@ExtendWith(MockitoExtension.class)
class LangChain4jToolsTest {

    @Mock
    private FlightService mockFlightService;

    private LangChain4jTools tools;

    @BeforeEach
    void setUp() {
        tools = new LangChain4jTools(mockFlightService);
    }

    @Test
    void testConstructor_WithFlightService_InitializesSuccessfully() {
        // Arrange & Act
        LangChain4jTools newTools = new LangChain4jTools(mockFlightService);

        // Assert
        assertNotNull(newTools, "LangChain4jTools should be created successfully");
    }

    @Test
    void testGetBookingDetails_WithValidData_DelegatesToFlightService() {
        // Arrange
        String bookingNumber = "101";
        String firstName = "John";
        String lastName = "Doe";
        BookingDetails expectedDetails = new BookingDetails(
            bookingNumber, firstName, lastName, LocalDate.now().plusDays(5),
            BookingStatus.CONFIRMED, "LAX", "JFK", "ECONOMY"
        );
        when(mockFlightService.getBookingDetails(bookingNumber, firstName, lastName))
            .thenReturn(expectedDetails);

        // Act
        BookingDetails actualDetails = tools.getBookingDetails(bookingNumber, firstName, lastName);

        // Assert
        assertNotNull(actualDetails, "Booking details should not be null");
        assertEquals(expectedDetails, actualDetails, "Should return details from FlightService");
        verify(mockFlightService, times(1)).getBookingDetails(bookingNumber, firstName, lastName);
    }

    @Test
    void testGetBookingDetails_WithInvalidBooking_ThrowsException() {
        // Arrange
        String invalidBookingNumber = "999";
        String firstName = "Jane";
        String lastName = "Smith";
        when(mockFlightService.getBookingDetails(invalidBookingNumber, firstName, lastName))
            .thenThrow(new IllegalArgumentException("Booking not found"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tools.getBookingDetails(invalidBookingNumber, firstName, lastName),
            "Should throw exception for invalid booking"
        );
        assertEquals("Booking not found", exception.getMessage());
        verify(mockFlightService).getBookingDetails(invalidBookingNumber, firstName, lastName);
    }

    @Test
    void testChangeBooking_WithValidData_DelegatesToFlightService() {
        // Arrange
        String bookingNumber = "102";
        String firstName = "Alice";
        String lastName = "Johnson";
        LocalDate newFlightDate = LocalDate.now().plusDays(10);
        String newDepartureAirport = "SFO";
        String newArrivalAirport = "ORD";

        // Act
        tools.changeBooking(bookingNumber, firstName, lastName, newFlightDate, newDepartureAirport, newArrivalAirport);

        // Assert
        verify(mockFlightService, times(1)).changeBooking(
            bookingNumber, firstName, lastName, newFlightDate, newDepartureAirport, newArrivalAirport
        );
    }

    @Test
    void testChangeBooking_Within24Hours_ThrowsException() {
        // Arrange
        String bookingNumber = "103";
        String firstName = "Bob";
        String lastName = "Williams";
        LocalDate newFlightDate = LocalDate.now().plusDays(5);
        String newDepartureAirport = "ATL";
        String newArrivalAirport = "DEN";
        
        doThrow(new IllegalArgumentException("Booking cannot be changed within 24 hours of the start date."))
            .when(mockFlightService).changeBooking(
                bookingNumber, firstName, lastName, newFlightDate, newDepartureAirport, newArrivalAirport
            );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tools.changeBooking(bookingNumber, firstName, lastName, newFlightDate, newDepartureAirport, newArrivalAirport),
            "Should throw exception for booking within 24 hours"
        );
        assertTrue(exception.getMessage().contains("within 24 hours"));
        verify(mockFlightService).changeBooking(
            bookingNumber, firstName, lastName, newFlightDate, newDepartureAirport, newArrivalAirport
        );
    }

    @Test
    void testChangeBooking_WithAirportCodes_PassesCorrectly() {
        // Arrange
        String bookingNumber = "104";
        String firstName = "Charlie";
        String lastName = "Brown";
        LocalDate newDate = LocalDate.of(2026, 12, 25);
        String departure = "JFK"; // 3-letter code
        String arrival = "LAX";   // 3-letter code

        // Act
        tools.changeBooking(bookingNumber, firstName, lastName, newDate, departure, arrival);

        // Assert
        verify(mockFlightService).changeBooking(bookingNumber, firstName, lastName, newDate, departure, arrival);
    }

    @Test
    void testCancelBooking_WithValidData_DelegatesToFlightService() {
        // Arrange
        String bookingNumber = "105";
        String firstName = "David";
        String lastName = "Wilson";

        // Act
        tools.cancelBooking(bookingNumber, firstName, lastName);

        // Assert
        verify(mockFlightService, times(1)).cancelBooking(bookingNumber, firstName, lastName);
    }

    @Test
    void testCancelBooking_Within48Hours_ThrowsException() {
        // Arrange
        String bookingNumber = "106";
        String firstName = "Eve";
        String lastName = "Davis";
        
        doThrow(new IllegalArgumentException("Booking cannot be cancelled within 48 hours of the start date."))
            .when(mockFlightService).cancelBooking(bookingNumber, firstName, lastName);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tools.cancelBooking(bookingNumber, firstName, lastName),
            "Should throw exception for booking within 48 hours"
        );
        assertTrue(exception.getMessage().contains("within 48 hours"));
        verify(mockFlightService).cancelBooking(bookingNumber, firstName, lastName);
    }

    @Test
    void testCancelBooking_WithInvalidBooking_ThrowsException() {
        // Arrange
        String invalidBookingNumber = "999";
        String firstName = "Frank";
        String lastName = "Miller";
        
        doThrow(new IllegalArgumentException("Booking not found"))
            .when(mockFlightService).cancelBooking(invalidBookingNumber, firstName, lastName);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tools.cancelBooking(invalidBookingNumber, firstName, lastName),
            "Should throw exception for invalid booking"
        );
        assertEquals("Booking not found", exception.getMessage());
        verify(mockFlightService).cancelBooking(invalidBookingNumber, firstName, lastName);
    }

    @Test
    void testGetBookingDetails_MultipleCalls_EachDelegatesCorrectly() {
        // Arrange
        String booking1 = "101";
        String booking2 = "102";
        BookingDetails details1 = new BookingDetails(
            booking1, "Alice", "A", LocalDate.now(), BookingStatus.CONFIRMED, "LAX", "JFK", "ECONOMY"
        );
        BookingDetails details2 = new BookingDetails(
            booking2, "Bob", "B", LocalDate.now(), BookingStatus.CONFIRMED, "SFO", "ORD", "BUSINESS"
        );
        
        when(mockFlightService.getBookingDetails(booking1, "Alice", "A")).thenReturn(details1);
        when(mockFlightService.getBookingDetails(booking2, "Bob", "B")).thenReturn(details2);

        // Act
        BookingDetails result1 = tools.getBookingDetails(booking1, "Alice", "A");
        BookingDetails result2 = tools.getBookingDetails(booking2, "Bob", "B");

        // Assert
        assertEquals(details1, result1);
        assertEquals(details2, result2);
        verify(mockFlightService).getBookingDetails(booking1, "Alice", "A");
        verify(mockFlightService).getBookingDetails(booking2, "Bob", "B");
    }

    @Test
    void testChangeBooking_WithNullDate_PropagatesNull() {
        // Arrange
        String bookingNumber = "107";
        String firstName = "Grace";
        String lastName = "Taylor";
        LocalDate nullDate = null;
        String departure = "ATL";
        String arrival = "MIA";

        // Act
        tools.changeBooking(bookingNumber, firstName, lastName, nullDate, departure, arrival);

        // Assert
        verify(mockFlightService).changeBooking(bookingNumber, firstName, lastName, nullDate, departure, arrival);
    }

    @Test
    void testCancelBooking_CaseVariations_PassThroughToService() {
        // Arrange
        String bookingNumber = "108";
        String firstName = "HENRY"; // uppercase
        String lastName = "anderson"; // lowercase

        // Act
        tools.cancelBooking(bookingNumber, firstName, lastName);

        // Assert
        verify(mockFlightService).cancelBooking(bookingNumber, firstName, lastName);
    }

    @Test
    void testAllMethods_VerifyNoAdditionalLogic_PureDelegation() {
        // This test ensures tools are pure wrappers without side effects
        // Arrange
        String bookingNumber = "109";
        String firstName = "Test";
        String lastName = "User";
        BookingDetails details = new BookingDetails(
            bookingNumber, firstName, lastName, LocalDate.now(), 
            BookingStatus.CONFIRMED, "SEA", "DEN", "ECONOMY"
        );
        when(mockFlightService.getBookingDetails(bookingNumber, firstName, lastName)).thenReturn(details);

        // Act - Call getBookingDetails
        tools.getBookingDetails(bookingNumber, firstName, lastName);
        
        // Act - Call changeBooking
        tools.changeBooking(bookingNumber, firstName, lastName, LocalDate.now().plusDays(5), "LAX", "JFK");
        
        // Act - Call cancelBooking
        tools.cancelBooking(bookingNumber, firstName, lastName);

        // Assert - Verify each method was called exactly once with correct parameters
        verify(mockFlightService, times(1)).getBookingDetails(bookingNumber, firstName, lastName);
        verify(mockFlightService, times(1)).changeBooking(
            eq(bookingNumber), eq(firstName), eq(lastName), any(LocalDate.class), eq("LAX"), eq("JFK")
        );
        verify(mockFlightService, times(1)).cancelBooking(bookingNumber, firstName, lastName);
        verifyNoMoreInteractions(mockFlightService);
    }
}
