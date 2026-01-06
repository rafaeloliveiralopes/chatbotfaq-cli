package dev.rafaellopes.leadqualbot.core;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Utility class for normalizing user input before keyword matching.
 *
 * <p>Rules:
 * <ul>
 *   <li>lowercase (Locale.ROOT)</li>
 *   <li>remove accents/diacritics</li>
 *   <li>collapse whitespace</li>
 *   <li>trim</li>
 * </ul>
 */
public class TextNormalizer {

    /**
     * Normalizes input text for matching. Returns empty string for null or empty input.
     *
     * @param text input text, may be null
     * @return normalized text, never null
     */
    public static String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        String normalized = text.toLowerCase(Locale.ROOT);
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{Mn}", "");
        normalized = normalized.replaceAll("\\s+", " ").trim();

        return normalized;
    }

    private TextNormalizer() {
    }
}
