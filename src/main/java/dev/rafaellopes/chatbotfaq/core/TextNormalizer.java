package dev.rafaellopes.chatbotfaq.core;

import java.text.Normalizer;

/**
 * Normalizes text for consistent matching across different platforms and encodings.
 * Performs lowercase conversion, accent removal, and whitespace normalization.
 */
public final class TextNormalizer {

    // Direct mapping for common Portuguese accented characters
    // This handles cases where NFD normalization fails (e.g., Windows cp1252 encoding)
    private static final String ACCENTED =   "àáâãäåèéêëìíîïòóôõöùúûüçñÀÁÂÃÄÅÈÉÊËÌÍÎÏÒÓÔÕÖÙÚÛÜÇÑ";
    private static final String UNACCENTED = "aaaaaaeeeeiiiiooooouuuucnAAAAAAEEEEIIIIOOOOOUUUUCN";

    private TextNormalizer() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Normalizes text by converting to lowercase, removing accents, and normalizing whitespace.
     * This ensures consistent matching regardless of platform encoding or user input variations.
     *
     * @param text the text to normalize
     * @return normalized text, or null if input is null
     */
    public static String normalize(String text) {
        if (text == null) {
            return "";
        }

        // Convert to lowercase using ROOT locale for consistent behavior
        String normalized = text.toLowerCase(java.util.Locale.ROOT);

        // Remove accents using Unicode normalization
        // NFD = Canonical Decomposition (separates base char from accent)
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);

        // Remove combining diacritical marks (accents)
        normalized = normalized.replaceAll("\\p{M}", "");

        // Fallback: direct character replacement for encodings where NFD doesn't work
        // (e.g., Windows PowerShell with cp1252)
        normalized = removeAccentsByMapping(normalized);

        // Normalize whitespace (replace multiple spaces with single space and trim)
        normalized = normalized.trim().replaceAll("\\s+", " ");

        return normalized;
    }

    /**
     * Removes accents by direct character mapping.
     * This is a fallback for when NFD normalization doesn't work due to encoding issues.
     */
    private static String removeAccentsByMapping(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int index = ACCENTED.indexOf(c);
            if (index >= 0) {
                sb.append(UNACCENTED.charAt(index));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
