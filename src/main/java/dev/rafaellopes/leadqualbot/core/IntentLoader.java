package dev.rafaellopes.leadqualbot.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Loads intents from an external JSON file outside the JAR.
 */
public class IntentLoader {

    private static final TypeReference<List<Intent>> INTENT_LIST = new TypeReference<>() {};

    private final ObjectMapper objectMapper;

    public IntentLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Intent> load(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("path must not be null");
        }
        if (!Files.exists(path)) {
            throw new IllegalStateException("knowledge base file not found: " + path.toAbsolutePath());
        }
        if (!Files.isRegularFile(path)) {
            throw new IllegalStateException("knowledge base path is not a file: " + path.toAbsolutePath());
        }

        try {
            return objectMapper.readValue(path.toFile(), INTENT_LIST);
        } catch (IOException e) {
            throw new IllegalStateException("failed to load knowledge base JSON: " + path.toAbsolutePath(), e);
        }
    }
}
