package dev.rafaellopes.leadqualbot.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Matches a user message against intents using keyword occurrence scoring.
 * Rules (PROJECT_SCOPE.md):
 * - Count keyword occurrences per intent
 * - Pick the highest score
 * - Tie-break by higher priority
 * - If still tied, pick the first intent as defined in JSON order
 */
public class IntentMatcher {

    /**
     * Finds the best matching intent for a given user message.
     *
     * @param userMessage raw user input (may be null)
     * @param intents list of intents in JSON order (may be null/empty)
     * @return Optional of the best intent, empty when score == 0 or no intents
     */
    public Optional<Intent> findBestIntent(String userMessage, List<Intent> intents) {
        if (intents == null || intents.isEmpty()) {
            return Optional.empty();
        }

        List<String> messageTokens = tokenize(userMessage);

        Intent bestIntent = null;
        int bestScore = 0;
        int bestPriority = Integer.MIN_VALUE;

        for (Intent intent : intents) {
            if (intent == null) {
                continue;
            }

            int score = score(messageTokens, intent.getKeywords());

            if (score > bestScore) {
                bestScore = score;
                bestPriority = intent.getPriority();
                bestIntent = intent;
            } else if (score == bestScore && score > 0) {
                int priority = intent.getPriority();
                if (priority > bestPriority) {
                    bestPriority = priority;
                    bestIntent = intent;
                }
                // If still tied (same score and same priority), keep the earlier one (JSON order)
            }
        }

        if (bestScore == 0) {
            return Optional.empty();
        }
        return Optional.of(bestIntent);

    }

    private int score(List<String> messageTokens, List<String> keywords) {
        if (messageTokens.isEmpty() || keywords == null || keywords.isEmpty()) {
            return 0;
        }

        int total = 0;

        for (String keyword : keywords) {
            List<String> keywordTokens = tokenize(keyword);
            if (keywordTokens.isEmpty()) {
                continue;
            }
            total += countOccurrences(messageTokens, keywordTokens);
        }

        return total;
    }

    /**
     * Tokenizes text for matching:
     * - Reuses TextNormalizer (lowercase, remove accents, collapse spaces)
     * - Converts non-alphanumeric to spaces
     * - Splits by spaces
     */
    private List<String> tokenize(String text) {
        String normalized = TextNormalizer.normalize(text);
        if (normalized.isEmpty()) {
            return List.of();
        }

        String sanitized = normalized.replaceAll("[^a-z0-9]+", " ").trim();
        if (sanitized.isEmpty()) {
            return List.of();
        }

        String[] parts = sanitized.split("\\s+");
        List<String> tokens = new ArrayList<>(parts.length);
        for (String part : parts) {
            if (!part.isBlank()) {
                tokens.add(part);
            }
        }

        return tokens;
    }

    /**
     * Counts how many times a keyword token sequence appears in the message token list.
     * Example:
     * - message: ["falar","com","humano","agora"]
     * - keyword: ["falar","com","humano"]
     * => 1 occurrence
     */
    private int countOccurrences(List<String> messageTokens, List<String> keywordTokens) {
        int m = messageTokens.size();
        int k = keywordTokens.size();

        if (k == 0 || k > m) {
            return 0;
        }

        int count = 0;
        int i = 0;

        while (i <= m - k) {
            boolean match = true;

            for (int j = 0; j < k; j++) {
                if (!messageTokens.get(i + j).equals(keywordTokens.get(j))) {
                    match = false;
                    break;
                }
            }

            if (match) {
                count++;
                i += k; // Move past this occurrence
            } else {
                i++;
            }
        }

        return count;
    }
}
