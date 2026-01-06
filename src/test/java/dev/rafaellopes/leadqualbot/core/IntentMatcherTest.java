package dev.rafaellopes.leadqualbot.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IntentMatcherTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<List<Intent>> INTENT_LIST = new TypeReference<>() {};
    private final IntentMatcher matcher = new IntentMatcher();

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchingCases")
    void shouldSelectBestIntent(String caseName, String intentsJson, String userMessage, String expectedIntent) throws Exception {
        List<Intent> intents = parseIntents(intentsJson);

        var result = matcher.findBestIntent(userMessage, intents);

        assertTrue(result.isPresent());
        assertEquals(expectedIntent, result.get().getIntent());
    }

    private static Stream<Arguments> matchingCases() {
        return Stream.of(
                Arguments.of(
                        "selects highest score",
                        """
                        [
                          {"intent":"orcamento","keywords":["orcamento","preco","valor"],"response":"r1","priority":1},
                          {"intent":"agendamento","keywords":["agenda","agendar","horario"],"response":"r2","priority":10}
                        ]
                        """,
                        "quero preco e orcamento",
                        "orcamento"
                ),
                Arguments.of(
                        "tie-breaks by higher priority",
                        """
                        [
                          {"intent":"a","keywords":["x"],"response":"r1","priority":1},
                          {"intent":"b","keywords":["x"],"response":"r2","priority":10}
                        ]
                        """,
                        "x",
                        "b"
                ),
                Arguments.of(
                        "keeps JSON order when still tied",
                        """
                        [
                          {"intent":"first","keywords":["x"],"response":"r1","priority":5},
                          {"intent":"second","keywords":["x"],"response":"r2","priority":5}
                        ]
                        """,
                        "x",
                        "first"
                )
        );
    }

    private static List<Intent> parseIntents(String json) throws Exception {
        return OBJECT_MAPPER.readValue(json, INTENT_LIST);
    }
}
