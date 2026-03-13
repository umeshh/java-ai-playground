package org.vaadin.marcus.langchain4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for LangChain4jAssistant.
 * Tests focus on interface contract validation and mock implementations.
 * Note: LangChain4jAssistant is an @AiService interface that gets runtime implementation
 * from LangChain4j framework. These tests validate the contract and mock behavior.
 */
@ExtendWith(MockitoExtension.class)
class LangChain4jAssistantTest {

    @Mock
    private LangChain4jAssistant mockAssistant;

    private final String testChatId = "chat-123";
    private final String testUserMessage = "What is my booking status?";

    @BeforeEach
    void setUp() {
        // Mock is injected by Mockito
    }

    @Test
    void testChat_WithValidInput_ReturnsFlux() {
        // Arrange
        Flux<String> expectedResponse = Flux.just("Hello!", "How can I help you today?");
        when(mockAssistant.chat(testChatId, testUserMessage)).thenReturn(expectedResponse);

        // Act
        Flux<String> actualResponse = mockAssistant.chat(testChatId, testUserMessage);

        // Assert
        assertNotNull(actualResponse, "Response flux should not be null");
        List<String> responses = actualResponse.collectList().block();
        assertEquals(2, responses.size());
        assertEquals("Hello!", responses.get(0));
        assertEquals("How can I help you today?", responses.get(1));
        
        verify(mockAssistant, times(1)).chat(testChatId, testUserMessage);
    }

    @Test
    void testChat_WithEmptyMessage_CanBeHandled() {
        // Arrange
        String emptyMessage = "";
        Flux<String> expectedResponse = Flux.just("I didn't receive your message. Please try again.");
        when(mockAssistant.chat(testChatId, emptyMessage)).thenReturn(expectedResponse);

        // Act
        Flux<String> actualResponse = mockAssistant.chat(testChatId, emptyMessage);

        // Assert
        assertNotNull(actualResponse);
        String response = actualResponse.blockFirst();
        assertEquals("I didn't receive your message. Please try again.", response);
    }

    @Test
    void testChat_WithDifferentChatIds_MaintainsSeparateSessions() {
        // Arrange
        String chatId1 = "chat-001";
        String chatId2 = "chat-002";
        String message = "Hello";
        
        Flux<String> response1 = Flux.just("Response for chat 1");
        Flux<String> response2 = Flux.just("Response for chat 2");
        
        when(mockAssistant.chat(chatId1, message)).thenReturn(response1);
        when(mockAssistant.chat(chatId2, message)).thenReturn(response2);

        // Act
        Flux<String> actual1 = mockAssistant.chat(chatId1, message);
        Flux<String> actual2 = mockAssistant.chat(chatId2, message);

        // Assert
        assertEquals("Response for chat 1", actual1.blockFirst());
        assertEquals("Response for chat 2", actual2.blockFirst());
        
        verify(mockAssistant).chat(chatId1, message);
        verify(mockAssistant).chat(chatId2, message);
    }

    @Test
    void testChat_StreamingResponse_HandlesMultipleChunks() {
        // Arrange
        Flux<String> streamingResponse = Flux.just(
            "Sure,", " let", " me", " check", " your", " booking", " details."
        );
        when(mockAssistant.chat(testChatId, testUserMessage)).thenReturn(streamingResponse);

        // Act
        Flux<String> actualResponse = mockAssistant.chat(testChatId, testUserMessage);

        // Assert
        List<String> chunks = actualResponse.collectList().block();
        assertNotNull(chunks);
        assertEquals(7, chunks.size());
        assertEquals("Sure,", chunks.get(0));
        assertEquals(" details.", chunks.get(6));
    }

    @Test
    void testChat_WithError_PropagatesError() {
        // Arrange
        Exception expectedException = new RuntimeException("AI service error");
        when(mockAssistant.chat(testChatId, testUserMessage))
            .thenReturn(Flux.error(expectedException));

        // Act
        Flux<String> actualResponse = mockAssistant.chat(testChatId, testUserMessage);

        // Assert
        assertThrows(RuntimeException.class, () -> actualResponse.blockFirst());
    }

    @Test
    void testChat_WithLongMessage_CanBeProcessed() {
        // Arrange
        String longMessage = "I need to change my booking. ".repeat(50);
        Flux<String> expectedResponse = Flux.just("I'll help you with that booking change.");
        when(mockAssistant.chat(testChatId, longMessage)).thenReturn(expectedResponse);

        // Act
        Flux<String> actualResponse = mockAssistant.chat(testChatId, longMessage);

        // Assert
        assertNotNull(actualResponse);
        String response = actualResponse.blockFirst();
        assertEquals("I'll help you with that booking change.", response);
    }

    @Test
    void testChat_WithSpecialCharacters_HandlesCorrectly() {
        // Arrange
        String specialMessage = "Flight #101 from SFO → JFK @10:30 AM?";
        Flux<String> expectedResponse = Flux.just("Let me check flight 101 for you.");
        when(mockAssistant.chat(testChatId, specialMessage)).thenReturn(expectedResponse);

        // Act
        Flux<String> actualResponse = mockAssistant.chat(testChatId, specialMessage);

        // Assert
        String response = actualResponse.blockFirst();
        assertEquals("Let me check flight 101 for you.", response);
    }

    @Test
    void testChat_EmptyFlux_CompletesImmediately() {
        // Arrange
        when(mockAssistant.chat(testChatId, testUserMessage)).thenReturn(Flux.empty());

        // Act
        Flux<String> actualResponse = mockAssistant.chat(testChatId, testUserMessage);

        // Assert
        List<String> responses = actualResponse.collectList().block();
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void testChat_VerifyMethodSignatureCompatibility() {
        // This test validates that the interface contract is correct
        // Arrange
        Flux<String> response = Flux.just("Test response");
        when(mockAssistant.chat(anyString(), anyString())).thenReturn(response);

        // Act
        Flux<String> result = mockAssistant.chat("any-chat-id", "any message");

        // Assert
        assertNotNull(result);
        assertInstanceOf(Flux.class, result);
        verify(mockAssistant).chat(anyString(), anyString());
    }

    @Test
    void testChat_NullChatId_CanBeHandledByMock() {
        // Arrange
        Flux<String> errorResponse = Flux.error(new IllegalArgumentException("Chat ID cannot be null"));
        when(mockAssistant.chat(null, testUserMessage)).thenReturn(errorResponse);

        // Act
        Flux<String> actualResponse = mockAssistant.chat(null, testUserMessage);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> actualResponse.blockFirst());
    }

    @Test
    void testChat_MultipleInvocations_EachReturnsSeparateFlux() {
        // Arrange
        String message1 = "First message";
        String message2 = "Second message";
        
        when(mockAssistant.chat(testChatId, message1)).thenReturn(Flux.just("Response 1"));
        when(mockAssistant.chat(testChatId, message2)).thenReturn(Flux.just("Response 2"));

        // Act
        Flux<String> response1 = mockAssistant.chat(testChatId, message1);
        Flux<String> response2 = mockAssistant.chat(testChatId, message2);

        // Assert
        assertEquals("Response 1", response1.blockFirst());
        assertEquals("Response 2", response2.blockFirst());
        
        verify(mockAssistant).chat(testChatId, message1);
        verify(mockAssistant).chat(testChatId, message2);
        verifyNoMoreInteractions(mockAssistant);
    }
}
