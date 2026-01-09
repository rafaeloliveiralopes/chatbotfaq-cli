package dev.rafaellopes.chatbotfaq;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rafaellopes.chatbotfaq.core.Intent;
import dev.rafaellopes.chatbotfaq.core.IntentMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private static final String FALLBACK =
            "Desculpe, não entendi. Você pode reformular ou digitar /ajuda para ver as opções.";

    private IntentMatcher matcher;
    private List<Intent> testIntents;

    @BeforeEach
    void setUp() {
        matcher = new IntentMatcher();
        testIntents = createTestIntents();
    }

    @Test
    void shouldReturnIntentResponseWhenIntentMatches() {
        String userMessage = "quanto custa";
        String response = App.decideResponse(userMessage, testIntents, matcher, FALLBACK);

        assertNotNull(response);
        assertEquals("Resposta sobre orçamento", response);
    }

    @Test
    void shouldReturnFallbackWhenNoIntentMatches() {
        String userMessage = "xyzabc123";
        String response = App.decideResponse(userMessage, testIntents, matcher, FALLBACK);

        assertEquals(FALLBACK, response);
    }

    @ParameterizedTest
    @MethodSource("nullOrBlankUserMessages")
    void shouldReturnFallbackWhenInputIsNullOrBlank(String userMessage) {
        String response = App.decideResponse(userMessage, testIntents, matcher, FALLBACK);
        assertEquals(FALLBACK, response);
    }

    static Stream<Arguments> nullOrBlankUserMessages() {
        return Stream.of(
                Arguments.of((String) null),
                Arguments.of(""),
                Arguments.of("   "),
                Arguments.of("\t"),
                Arguments.of("\n"),
                Arguments.of(" \t ")
        );
    }

    @Test
    void shouldReturnFallbackForUnexpectedCharacters() {
        String response1 = App.decideResponse("😀🎉", testIntents, matcher, FALLBACK);
        assertEquals(FALLBACK, response1);

        String response2 = App.decideResponse("!@#$%^&*()", testIntents, matcher, FALLBACK);
        assertEquals(FALLBACK, response2);

        String response3 = App.decideResponse("????????", testIntents, matcher, FALLBACK);
        assertEquals(FALLBACK, response3);
    }

    @Test
    void shouldContinueProcessingAfterFallback() {
        String response1 = App.decideResponse("xyzabc", testIntents, matcher, FALLBACK);
        assertEquals(FALLBACK, response1);

        String response2 = App.decideResponse("quanto custa", testIntents, matcher, FALLBACK);
        assertEquals("Resposta sobre orçamento", response2);

        String response3 = App.decideResponse("!!!!", testIntents, matcher, FALLBACK);
        assertEquals(FALLBACK, response3);

        String response4 = App.decideResponse("agendar", testIntents, matcher, FALLBACK);
        assertEquals("Resposta sobre agendamento", response4);
    }

    private List<Intent> createTestIntents() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = """
                [
                  {
                    "intent": "orcamento",
                    "keywords": ["preco", "valor", "quanto custa", "orcamento"],
                    "response": "Resposta sobre orçamento",
                    "priority": 10
                  },
                  {
                    "intent": "agendamento",
                    "keywords": ["agendar", "marcar", "horario"],
                    "response": "Resposta sobre agendamento",
                    "priority": 10
                  }
                ]
                """;

            return mapper.readValue(json, new TypeReference<List<Intent>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test intents", e);
        }
    }
}
