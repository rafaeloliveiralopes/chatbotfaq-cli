package dev.rafaellopes.leadqualbot.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link TextNormalizer}.
 */
class TextNormalizerTest {

    @Test
    void shouldNormalizeAccentsAndCasing() {
        assertEquals("orcamento agora", TextNormalizer.normalize("OrÇaménto Agora"));
    }

    @Test
    void shouldConvertToLowercase() {
        assertEquals("upper case text", TextNormalizer.normalize("UPPER CASE TEXT"));
    }

    @Test
    void shouldRemoveVariousAccents() {
        assertEquals("aeiou aeiou aeiou ao n c",
                TextNormalizer.normalize("áéíóú àèìòù âêîôû ãõ ñ ç"));
    }

    @Test
    void shouldCollapseDuplicateSpaces() {
        assertEquals("multiple spaces here", TextNormalizer.normalize("multiple   spaces    here"));
    }

    @Test
    void shouldTrimLeadingAndTrailingSpaces() {
        assertEquals("trimmed", TextNormalizer.normalize("   trimmed   "));
    }

    @Test
    void shouldHandleNullInput() {
        assertEquals("", TextNormalizer.normalize(null));
    }

    @Test
    void shouldHandleEmptyString() {
        assertEquals("", TextNormalizer.normalize(""));
    }

    @Test
    void shouldHandleWhitespaceOnlyString() {
        assertEquals("", TextNormalizer.normalize("   \t  \n  "));
    }

    @Test
    void shouldHandleMixedAccentsSpacesAndCasing() {
        assertEquals("ola, mundo! com acentos",
                TextNormalizer.normalize("  Olá,   Múndo!  COM  açéntos  "));
    }

    @Test
    void shouldHandleSpecialCharacters() {
        assertEquals("texto@com#simbolos$especiais%",
                TextNormalizer.normalize("Texto@com#símbolos$especiais%"));
    }

    @Test
    void shouldHandleNumbersAndLetters() {
        assertEquals("agendamento 123 as 14h30",
                TextNormalizer.normalize("Agéndamento 123 às 14h30"));
    }
}
