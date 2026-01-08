package dev.rafaellopes.chatbotfaq.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntentLoaderTest {

    @Test
    void shouldLoadIntentsFromFixtureJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        IntentLoader loader = new IntentLoader(mapper);

        Path fixturePath = copyFixtureToTempFile();

        List<Intent> intents = loader.load(fixturePath);

        assertNotNull(intents);
        assertEquals(2, intents.size());

        Intent first = intents.getFirst();
        assertEquals("orcamento", first.getIntent());
        assertEquals("Resposta orçamento", first.getResponse());
        assertEquals(10, first.getPriority());
        assertNotNull(first.getKeywords());
        assertTrue(first.getKeywords().contains("quanto custa"));

        Intent second = intents.get(1);
        assertEquals("agendamento", second.getIntent());
        assertEquals("Resposta agendamento", second.getResponse());
        assertEquals(5, second.getPriority());
        assertNotNull(second.getKeywords());
        assertTrue(second.getKeywords().contains("agendar"));
    }

    @Test
    void shouldFailWhenJsonFileDoesNotExist() {
        ObjectMapper mapper = new ObjectMapper();
        IntentLoader loader = new IntentLoader(mapper);

        Path missing = Path.of("data/does-not-exist.json");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> loader.load(missing));
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("file not found")
                || ex.getMessage().toLowerCase().contains("not found"));
    }

    @Test
    void shouldFailWhenJsonIsInvalid() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        IntentLoader loader = new IntentLoader(mapper);

        Path temp = Files.createTempFile("invalid-intents", ".json");
        Files.writeString(temp, "{ invalid json");

        assertThrows(IllegalStateException.class, () -> loader.load(temp));
    }

    private static Path copyFixtureToTempFile() throws IOException {
        try (InputStream in = IntentLoaderTest.class.getClassLoader().getResourceAsStream("fixtures/intents.json")) {
            if (in == null) {
                throw new FileNotFoundException("Fixture not found on classpath: " + "fixtures/intents.json");
            }

            Path temp = Files.createTempFile("intent-fixture-", ".json");
            Files.copy(in, temp, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            temp.toFile().deleteOnExit();
            return temp;
        }
    }
}
