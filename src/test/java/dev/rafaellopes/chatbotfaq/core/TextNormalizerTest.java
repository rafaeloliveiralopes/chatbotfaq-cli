package dev.rafaellopes.chatbotfaq.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextNormalizerTest {

    @Test
    void shouldConvertToLowercase() {
        assertEquals("hello", TextNormalizer.normalize("HELLO"));
        assertEquals("hello", TextNormalizer.normalize("HeLLo"));
    }

    @Test
    void shouldRemoveAccents() {
        assertEquals("automacao", TextNormalizer.normalize("automação"));
        assertEquals("e", TextNormalizer.normalize("é"));
        assertEquals("e", TextNormalizer.normalize("ê"));
        assertEquals("a", TextNormalizer.normalize("á"));
        assertEquals("a", TextNormalizer.normalize("à"));
        assertEquals("a", TextNormalizer.normalize("ã"));
        assertEquals("o", TextNormalizer.normalize("ó"));
        assertEquals("o", TextNormalizer.normalize("ô"));
        assertEquals("o", TextNormalizer.normalize("õ"));
    }

    @Test
    void shouldNormalizeWhitespace() {
        assertEquals("hello world", TextNormalizer.normalize("hello  world"));
        assertEquals("hello world", TextNormalizer.normalize("  hello   world  "));
        assertEquals("hello world", TextNormalizer.normalize("hello\tworld"));
    }

    @Test
    void shouldHandleComplexText() {
        assertEquals("o que e automacao", TextNormalizer.normalize("O que é automação"));
        assertEquals("o que e automacao?", TextNormalizer.normalize("o que é automação?"));
        assertEquals("definicao de automacao", TextNormalizer.normalize("Definição de Automação"));
    }

    @Test
    void shouldHandleNullAndEmpty() {
        assertEquals("", TextNormalizer.normalize(null));
        assertEquals("", TextNormalizer.normalize(""));
        assertEquals("", TextNormalizer.normalize("   "));
    }

    @Test
    void shouldHandleSpecialCharacters() {
        assertEquals("cafe", TextNormalizer.normalize("café"));
        assertEquals("naive", TextNormalizer.normalize("naïve"));
        assertEquals("senor", TextNormalizer.normalize("Señor"));
    }

    @Test
    void shouldBeIdempotent() {
        String text = "O que é Automação?";
        String normalized = TextNormalizer.normalize(text);
        String normalizedTwice = TextNormalizer.normalize(normalized);
        assertEquals(normalized, normalizedTwice);
    }
}
