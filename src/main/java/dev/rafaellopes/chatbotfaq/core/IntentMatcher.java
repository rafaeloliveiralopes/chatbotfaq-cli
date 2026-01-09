package dev.rafaellopes.chatbotfaq.core;

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
            int score = 0;

            if (intent != null) {
                score = score(messageTokens, intent.getKeywords());
            }

            if (intent == null || score == 0) {
                continue;
            }

            int priority = intent.getPriority();

            if (isBetterCandidate(score, priority, bestScore, bestPriority)) {
                bestIntent = intent;
                bestScore = score;
                bestPriority = priority;
            }
        }

        if (bestScore == 0) {
            return Optional.empty();
        }

        return Optional.of(bestIntent);
    }

    private boolean isBetterCandidate(int score, int priority, int bestScore, int bestPriority) {
        if (score > bestScore) {
            return true;
        }
        if (score < bestScore) {
            return false;
        }
        return priority > bestPriority;
    }

    private int score(List<String> messageTokens, List<String> keywords) {
        if (messageTokens.isEmpty() || keywords == null || keywords.isEmpty()) {
            return 0;
        }

        int total = 0;

        for (String keyword : keywords) {
            List<String> keywordTokens = tokenize(keyword);
            if (!keywordTokens.isEmpty()) {
                total += countOccurrences(messageTokens, keywordTokens);
            }
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
        String normalized = dev.rafaellopes.chatbotfaq.core.TextNormalizer.normalize(text);
        if (normalized.isEmpty()) {
            return List.of();
        }

        String alphaNumOnly = convertNonAlphanumericToSpaces(normalized);
        return extractTokens(alphaNumOnly);
    }

    private String convertNonAlphanumericToSpaces(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            boolean isAlphaNum = (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
            sb.append(isAlphaNum ? c : ' ');
        }
        return sb.toString();
    }

    private List<String> extractTokens(String text) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ') {
                if (!current.isEmpty()) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            tokens.add(current.toString());
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
                i += k;
            } else {
                i++;
            }
        }

        return count;
    }
}
