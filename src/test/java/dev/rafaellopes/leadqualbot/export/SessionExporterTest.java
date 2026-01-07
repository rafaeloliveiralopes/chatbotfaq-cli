package dev.rafaellopes.leadqualbot.export;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SessionExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldExportNonEmptySession() throws Exception {
        Path exportPath = tempDir.resolve("leads.txt");
        SessionExporter exporter = new SessionExporter(exportPath);

        SessionSummary summary = new SessionSummary();
        summary.recordIntent("orcamento");
        summary.recordIntent("agendamento");
        summary.recordFallback();
        summary.endSession();

        boolean result = exporter.export(summary);

        assertTrue(result);
        assertTrue(Files.exists(exportPath));

        String content = Files.readString(exportPath);
        assertTrue(content.contains("LeadQualBot Session Summary"));
        assertTrue(content.contains("Total interactions: 3"));
        assertTrue(content.contains("Fallbacks: 1"));
        assertTrue(content.contains("orcamento"));
        assertTrue(content.contains("agendamento"));
    }

    @Test
    void shouldNotExportEmptySession() {
        Path exportPath = tempDir.resolve("leads.txt");
        SessionExporter exporter = new SessionExporter(exportPath);

        SessionSummary summary = new SessionSummary();

        boolean result = exporter.export(summary);

        assertFalse(result);
        assertFalse(Files.exists(exportPath));
    }

    @Test
    void shouldNotExportNullSession() {
        Path exportPath = tempDir.resolve("leads.txt");
        SessionExporter exporter = new SessionExporter(exportPath);

        boolean result = exporter.export(null);

        assertFalse(result);
        assertFalse(Files.exists(exportPath));
    }

    @Test
    void shouldAppendToExistingFile() throws Exception {
        Path exportPath = tempDir.resolve("leads.txt");
        SessionExporter exporter = new SessionExporter(exportPath);

        SessionSummary summary1 = new SessionSummary();
        summary1.recordIntent("orcamento");
        summary1.endSession();

        SessionSummary summary2 = new SessionSummary();
        summary2.recordIntent("agendamento");
        summary2.endSession();

        exporter.export(summary1);
        exporter.export(summary2);

        String content = Files.readString(exportPath);
        int occurrences = content.split("LeadQualBot Session Summary", -1).length - 1;
        assertEquals(2, occurrences);
    }

    @Test
    void shouldHandleExportFailureGracefully() {
        // Use invalid path to force failure
        Path invalidPath = Path.of("/invalid/path/that/does/not/exist/leads.txt");
        SessionExporter exporter = new SessionExporter(invalidPath);

        SessionSummary summary = new SessionSummary();
        summary.recordIntent("orcamento");

        boolean result = exporter.export(summary);

        assertFalse(result); // Should return false but not throw exception
    }

    @Test
    void shouldFormatEmptySessionForConsole() {
        SessionSummary summary = new SessionSummary();

        String formatted = SessionExporter.formatForConsole(summary);

        assertNotNull(formatted);
        assertTrue(formatted.contains("Nenhuma interação"));
    }

    @Test
    void shouldFormatNonEmptySessionForConsole() {
        SessionSummary summary = new SessionSummary();
        summary.recordIntent("orcamento");
        summary.recordIntent("agendamento");
        summary.recordFallback();

        String formatted = SessionExporter.formatForConsole(summary);

        assertNotNull(formatted);
        assertTrue(formatted.contains("Resumo da Sessão"));
        assertTrue(formatted.contains("Interações: 3"));
        assertTrue(formatted.contains("orcamento"));
        assertTrue(formatted.contains("agendamento"));
    }

    @Test
    void shouldFormatNullSessionForConsole() {
        String formatted = SessionExporter.formatForConsole(null);

        assertNotNull(formatted);
        assertTrue(formatted.contains("Nenhuma interação"));
    }
}

