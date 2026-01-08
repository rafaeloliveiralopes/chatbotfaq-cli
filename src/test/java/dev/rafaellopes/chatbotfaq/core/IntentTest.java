package dev.rafaellopes.chatbotfaq.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IntentTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeFromJsonUsingIntentKey() throws Exception {
        String json = """
            {
              "intent": "orcamento",
              "keywords": ["preco", "orcamento", "valor"],
              "response": "Certo. Vamos falar sobre orçamento.",
              "priority": 10
            }
            """;

        Intent intent = objectMapper.readValue(json, Intent.class);

        assertNotNull(intent);
        assertEquals("orcamento", intent.getIntent());
        assertEquals(List.of("preco", "orcamento", "valor"), intent.getKeywords());
        assertEquals("Certo. Vamos falar sobre orçamento.", intent.getResponse());
        assertEquals(10, intent.getPriority());
    }
}
