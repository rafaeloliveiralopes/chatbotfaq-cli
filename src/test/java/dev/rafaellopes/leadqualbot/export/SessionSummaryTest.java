package dev.rafaellopes.leadqualbot.export;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SessionSummaryTest {

    @Test
    void shouldStartEmpty() {
        SessionSummary summary = new SessionSummary();

        assertTrue(summary.isEmpty());
        assertEquals(0, summary.getTotalInteractions());
        assertEquals(0, summary.getFallbackCount());
        assertTrue(summary.getSelectedIntents().isEmpty());
        assertNotNull(summary.getStartTime());
        assertNull(summary.getEndTime());
    }

    @Test
    void shouldRecordIntent() {
        SessionSummary summary = new SessionSummary();

        summary.recordIntent("orcamento");
        summary.recordIntent("agendamento");

        assertFalse(summary.isEmpty());
        assertEquals(2, summary.getTotalInteractions());
        assertEquals(0, summary.getFallbackCount());
        assertEquals(2, summary.getSelectedIntents().size());
        assertTrue(summary.getSelectedIntents().contains("orcamento"));
        assertTrue(summary.getSelectedIntents().contains("agendamento"));
    }

    @Test
    void shouldRecordFallback() {
        SessionSummary summary = new SessionSummary();

        summary.recordFallback();
        summary.recordFallback();

        assertFalse(summary.isEmpty());
        assertEquals(2, summary.getTotalInteractions());
        assertEquals(2, summary.getFallbackCount());
        assertTrue(summary.getSelectedIntents().isEmpty());
    }

    @Test
    void shouldRecordMixedInteractions() {
        SessionSummary summary = new SessionSummary();

        summary.recordIntent("orcamento");
        summary.recordFallback();
        summary.recordIntent("agendamento");
        summary.recordFallback();

        assertEquals(4, summary.getTotalInteractions());
        assertEquals(2, summary.getFallbackCount());
        assertEquals(2, summary.getSelectedIntents().size());
    }

    @Test
    void shouldIgnoreNullOrBlankIntentNames() {
        SessionSummary summary = new SessionSummary();

        summary.recordIntent(null);
        summary.recordIntent("");
        summary.recordIntent("   ");

        assertTrue(summary.isEmpty());
        assertEquals(0, summary.getTotalInteractions());
        assertTrue(summary.getSelectedIntents().isEmpty());
    }

    @Test
    void shouldEndSession() {
        SessionSummary summary = new SessionSummary();

        assertNull(summary.getEndTime());

        summary.endSession();

        assertNotNull(summary.getEndTime());
    }

    @Test
    void shouldReturnDefensiveCopyOfIntents() {
        SessionSummary summary = new SessionSummary();
        summary.recordIntent("orcamento");

        var intents = summary.getSelectedIntents();
        intents.add("fake");

        assertEquals(1, summary.getSelectedIntents().size());
        assertFalse(summary.getSelectedIntents().contains("fake"));
    }
}

