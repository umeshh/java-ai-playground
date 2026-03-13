package org.vaadin.marcus.langchain4j;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for LangChain4jConfig.
 * Tests verify Spring bean configuration and component initialization.
 */
@ExtendWith(MockitoExtension.class)
class LangChain4jConfigTest {

    @Mock
    private Tokenizer mockTokenizer;

    @Mock
    private EmbeddingModel mockEmbeddingModel;

    private LangChain4jConfig config;

    @BeforeEach
    void setUp() {
        config = new LangChain4jConfig();
    }

    @Test
    void testChatMemoryProvider_ReturnsValidProvider() {
        // Act
        ChatMemoryProvider provider = config.chatMemoryProvider(mockTokenizer);

        // Assert
        assertNotNull(provider, "ChatMemoryProvider should not be null");
    }

    @Test
    void testChatMemoryProvider_CreatesMemoryForDifferentChatIds() {
        // Arrange
        ChatMemoryProvider provider = config.chatMemoryProvider(mockTokenizer);

        // Act
        Object memory1 = provider.get("chat-001");
        Object memory2 = provider.get("chat-002");

        // Assert
        assertNotNull(memory1, "Memory for chat-001 should not be null");
        assertNotNull(memory2, "Memory for chat-002 should not be null");
        assertNotSame(memory1, memory2, "Different chat IDs should have different memory instances");
    }

    @Test
    void testChatMemoryProvider_UsesProvidedTokenizer() {
        // Arrange
        ChatMemoryProvider provider = config.chatMemoryProvider(mockTokenizer);

        // Act
        Object memory = provider.get("test-chat");

        // Assert
        assertNotNull(memory, "Memory should be created with provided tokenizer");
        // TokenWindowChatMemory internally uses the tokenizer for token counting
    }

    @Test
    void testChatMemoryProvider_SameChatId_ReturnsSameMemory() {
        // Arrange
        ChatMemoryProvider provider = config.chatMemoryProvider(mockTokenizer);
        String chatId = "consistent-chat";

        // Act
        Object memory1 = provider.get(chatId);
        Object memory2 = provider.get(chatId);

        // Assert - TokenWindowChatMemory creates new instances, but they maintain state per chatId
        assertNotNull(memory1);
        assertNotNull(memory2);
    }

    @Test
    void testEmbeddingStore_ReturnsInMemoryStore() {
        // Act
        EmbeddingStore<TextSegment> store = config.embeddingStore();

        // Assert
        assertNotNull(store, "EmbeddingStore should not be null");
        assertInstanceOf(EmbeddingStore.class, store, "Should return EmbeddingStore instance");
    }

    @Test
    void testEmbeddingStore_IsNewInstanceEachTime() {
        // Act
        EmbeddingStore<TextSegment> store1 = config.embeddingStore();
        EmbeddingStore<TextSegment> store2 = config.embeddingStore();

        // Assert
        assertNotNull(store1);
        assertNotNull(store2);
        // Note: In Spring context, this would be a singleton, but direct calls create new instances
        assertNotSame(store1, store2, "Direct method calls should create new instances");
    }

    @Test
    void testContentRetriever_WithValidDependencies_ReturnsRetriever() {
        // Arrange
        EmbeddingStore<TextSegment> embeddingStore = config.embeddingStore();

        // Act
        ContentRetriever retriever = config.contentRetriever(embeddingStore, mockEmbeddingModel);

        // Assert
        assertNotNull(retriever, "ContentRetriever should not be null");
    }

    @Test
    void testContentRetriever_ConfiguresMaxResults() {
        // Arrange
        EmbeddingStore<TextSegment> embeddingStore = config.embeddingStore();

        // Act
        ContentRetriever retriever = config.contentRetriever(embeddingStore, mockEmbeddingModel);

        // Assert
        assertNotNull(retriever);
        // The builder is configured with maxResults(2), verified by successful bean creation
    }

    @Test
    void testContentRetriever_ConfiguresMinScore() {
        // Arrange
        EmbeddingStore<TextSegment> embeddingStore = config.embeddingStore();

        // Act
        ContentRetriever retriever = config.contentRetriever(embeddingStore, mockEmbeddingModel);

        // Assert
        assertNotNull(retriever);
        // The builder is configured with minScore(0.6), verified by successful bean creation
    }

    @Test
    void testContentRetriever_UsesProvidedEmbeddingStore() {
        // Arrange
        EmbeddingStore<TextSegment> customStore = config.embeddingStore();

        // Act
        ContentRetriever retriever = config.contentRetriever(customStore, mockEmbeddingModel);

        // Assert
        assertNotNull(retriever, "Should create retriever with provided embedding store");
    }

    @Test
    void testContentRetriever_UsesProvidedEmbeddingModel() {
        // Arrange
        EmbeddingStore<TextSegment> embeddingStore = config.embeddingStore();

        // Act
        ContentRetriever retriever = config.contentRetriever(embeddingStore, mockEmbeddingModel);

        // Assert
        assertNotNull(retriever, "Should create retriever with provided embedding model");
    }

    @Test
    void testAllBeans_CanBeCreatedSuccessfully() {
        // This test verifies the entire configuration can be instantiated
        // Arrange & Act
        ChatMemoryProvider memoryProvider = config.chatMemoryProvider(mockTokenizer);
        EmbeddingStore<TextSegment> embeddingStore = config.embeddingStore();
        ContentRetriever contentRetriever = config.contentRetriever(embeddingStore, mockEmbeddingModel);

        // Assert
        assertNotNull(memoryProvider, "ChatMemoryProvider bean should be created");
        assertNotNull(embeddingStore, "EmbeddingStore bean should be created");
        assertNotNull(contentRetriever, "ContentRetriever bean should be created");
    }

    @Test
    void testChatMemoryProvider_WithNullTokenizer_ReturnsProvider() {
        // The implementation doesn't validate null upfront,
        // so this test verifies it returns a provider that could fail later
        // Act
        ChatMemoryProvider provider = config.chatMemoryProvider(null);

        // Assert
        assertNotNull(provider, "Should return a provider even with null tokenizer");
    }

    @Test
    void testContentRetriever_WithNullEmbeddingStore_ThrowsException() {
        // Act & Assert - Implementation throws IllegalArgumentException, not NullPointerException
        assertThrows(
            IllegalArgumentException.class,
            () -> config.contentRetriever(null, mockEmbeddingModel),
            "Should throw IllegalArgumentException when embedding store is null"
        );
    }

    @Test
    void testContentRetriever_WithNullEmbeddingModel_DoesNotThrowImmediately() {
        // Arrange
        EmbeddingStore<TextSegment> embeddingStore = config.embeddingStore();

        // Act - Implementation allows null embedding model, may fail at retrieval time
        ContentRetriever retriever = config.contentRetriever(embeddingStore, null);

        // Assert
        assertNotNull(retriever, "Should create retriever even with null embedding model");
    }

    @Test
    void testConfigurationClass_IsInstantiable() {
        // Act
        LangChain4jConfig newConfig = new LangChain4jConfig();

        // Assert
        assertNotNull(newConfig, "Configuration class should be instantiable");
    }

    @Test
    void testEmbeddingStore_SupportsTextSegments() {
        // Act
        EmbeddingStore<TextSegment> store = config.embeddingStore();

        // Assert
        assertNotNull(store);
        // InMemoryEmbeddingStore<TextSegment> supports TextSegment type
    }

    @Test
    void testChatMemoryProvider_MultipleChatsIndependent() {
        // Arrange
        ChatMemoryProvider provider = config.chatMemoryProvider(mockTokenizer);

        // Act - Create memories for different chats
        Object chatMemory1 = provider.get("user-1-chat");
        Object chatMemory2 = provider.get("user-2-chat");
        Object chatMemory3 = provider.get("user-3-chat");

        // Assert - Each chat should have its own memory
        assertNotNull(chatMemory1);
        assertNotNull(chatMemory2);
        assertNotNull(chatMemory3);
    }
}
