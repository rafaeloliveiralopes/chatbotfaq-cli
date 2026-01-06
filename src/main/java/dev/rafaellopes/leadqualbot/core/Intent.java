package dev.rafaellopes.leadqualbot.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents one intent entry loaded from the external knowledge base JSON.
 */
public class Intent {

    @JsonProperty("intent")
    private String intentName;

    private List<String> keywords;
    private String response;
    private int priority;

    public Intent() {
        // Default constructor required by Jackson
    }

    public String getIntent() {
        return intentName;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getResponse() {
        return response;
    }

    public int getPriority() {
        return priority;
    }
}
