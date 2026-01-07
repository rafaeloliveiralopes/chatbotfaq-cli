package dev.rafaellopes.leadqualbot.export;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a summary of a chatbot session for export purposes.
 * Contains basic interaction data without storing full user messages.
 */
public class SessionSummary {

    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private final List<String> selectedIntents;
    private int fallbackCount;
    private int totalInteractions;

    public SessionSummary() {
        this.startTime = LocalDateTime.now();
        this.selectedIntents = new ArrayList<>();
        this.fallbackCount = 0;
        this.totalInteractions = 0;
    }

    public void recordIntent(String intentName) {
        if (intentName != null && !intentName.isBlank()) {
            selectedIntents.add(intentName);
            totalInteractions++;
        }
    }

    public void recordFallback() {
        fallbackCount++;
        totalInteractions++;
    }

    public void endSession() {
        this.endTime = LocalDateTime.now();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<String> getSelectedIntents() {
        return new ArrayList<>(selectedIntents);
    }

    public int getFallbackCount() {
        return fallbackCount;
    }

    public int getTotalInteractions() {
        return totalInteractions;
    }

    public boolean isEmpty() {
        return totalInteractions == 0;
    }
}

