package dev.rafaellopes.leadqualbot.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntentLoaderTest {

    private final IntentLoader loader = new IntentLoader(new ObjectMapper());

    @Test
    void shouldLoadIntentsFromJsonFile(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("intents.json");

        String json = """
            [
              {
                "intent": "orcamento",
                "keywords": ["preco", "orcamento"],
                "response": "Vamos falar sobre orçamento.",
                "priority": 10
              }
            ]
            """;

        Files.writeString(file, json);

        List<Intent> intents = loader.load(file);

        assertEquals(1, intents.size());
        assertEquals("orcamento", intents.getFirst().getIntent());
        assertEquals(List.of("preco", "orcamento"), intents.getFirst().getKeywords());
        assertEquals("Vamos falar sobre orçamento.", intents.getFirst().getResponse());
        assertEquals(10, intents.getFirst().getPriority());
    }

    @Test
    void shouldFailWhenFileDoesNotExist(@TempDir Path tempDir) {
        Path missing = tempDir.resolve("missing.json");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> loader.load(missing));
        assertTrue(ex.getMessage().contains("file not found"));
    }

    @Test
    void shouldFailWhenJsonIsInvalid(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("intents.json");
        Files.writeString(file, "{ invalid json");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> loader.load(file));
        assertTrue(ex.getMessage().contains("failed to load"));
    }
}
