package dev.rafaellopes.chatbotfaq.core;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Normalizes input text for consistent matching:
 * - Lowercase (Locale.ROOT)
 * - Unicode NFD normalization
 * - Remove combining marks (accents)
 * - Collapse whitespace
 */
public class TextNormalizer {

    private static final String ACCENTED =
            "áàãâäéèêëíìîïóòõôöúùûüçñÁÀÃÂÄÉÈÊËÍÌÎÏÓÒÕÔÖÚÙÛÜÇÑ";

    private static final String UNACCENTED =
            "aaaaaeeeeiiiiooooouuuucnAAAAAEEEEIIIIOOOOOUUUUCN";

    public static String normalize(String text) {
        if (text == null) {
            return "";
        }

        String normalized = text.toLowerCase(Locale.ROOT);

        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}+", "");

        normalized = removeAccentsByMapping(normalized);

        normalized = normalized.trim().replaceAll("\\s+", " ");
        return normalized;
    }

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
