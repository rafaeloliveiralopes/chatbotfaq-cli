package dev.rafaellopes.chatbotfaq.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Loads intents from an external JSON file outside the JAR.
 */
public class IntentLoader {

    private static final Logger log = LoggerFactory.getLogger(IntentLoader.class);

    private static final TypeReference<List<Intent>> INTENT_LIST = new TypeReference<>() {};

    private final ObjectMapper objectMapper;

    public IntentLoader(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
    }

    public List<Intent> load(Path path) {
        if (path == null) {
            log.warn("Knowledge base load failed: path is null");
            throw new IllegalArgumentException("path must not be null");
        }

        Path absolutePath = path.toAbsolutePath();
        log.info("Loading knowledge base from: {}", absolutePath);

        if (!Files.exists(path)) {
            log.warn("Knowledge base load failed: file not found: {}", absolutePath);
            throw new IllegalStateException("knowledge base file not found: " + absolutePath);
        }

        if (!Files.isRegularFile(path)) {
            log.warn("Knowledge base load failed: path is not a file: {}", absolutePath);
            throw new IllegalStateException("knowledge base path is not a file: " + absolutePath);
        }

        try {
            List<Intent> intents = objectMapper.readValue(path.toFile(), INTENT_LIST);
            log.info("Knowledge base loaded successfully: {} intents", intents == null ? 0 : intents.size());
            return intents;
        } catch (IOException e) {
            log.warn("Knowledge base load failed: invalid JSON or unreadable file: {}", absolutePath);
            throw new IllegalStateException("failed to load knowledge base JSON: " + absolutePath, e);
        }
    }
}
