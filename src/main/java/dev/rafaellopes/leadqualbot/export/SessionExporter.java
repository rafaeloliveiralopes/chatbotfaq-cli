package dev.rafaellopes.leadqualbot.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

/**
 * Exports session summaries to local files.
 * Failures are handled gracefully without breaking the application flow.
 */
public class SessionExporter {

    private static final Logger log = LoggerFactory.getLogger(SessionExporter.class);
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path outputPath;

    public SessionExporter(Path outputPath) {
        this.outputPath = outputPath;
    }

    /**
     * Exports the session summary to a text file.
     * If export fails, logs the error but does not throw exceptions.
     *
     * @param summary the session summary to export
     * @return true if export succeeded, false otherwise
     */
    public boolean export(SessionSummary summary) {
        if (summary == null || summary.isEmpty()) {
            log.debug("Session summary is empty, skipping export");
            return false;
        }

        try {
            String content = buildTextContent(summary);
            writeToFile(content);
            log.info("Session summary exported to: {}", outputPath.toAbsolutePath());
            return true;
        } catch (Exception e) {
            log.warn("Failed to export session summary: {}", e.getMessage());
            return false;
        }
    }

    private String buildTextContent(SessionSummary summary) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LeadQualBot Session Summary ===\n");
        sb.append("Start: ").append(summary.getStartTime().format(DATETIME_FORMAT)).append("\n");

        if (summary.getEndTime() != null) {
            sb.append("End: ").append(summary.getEndTime().format(DATETIME_FORMAT)).append("\n");
        }

        sb.append("Total interactions: ").append(summary.getTotalInteractions()).append("\n");
        sb.append("Fallbacks: ").append(summary.getFallbackCount()).append("\n");
        sb.append("\nIntents identified:\n");

        if (summary.getSelectedIntents().isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (String intent : summary.getSelectedIntents()) {
                sb.append("  - ").append(intent).append("\n");
            }
        }

        sb.append("\n");
        return sb.toString();
    }

    private void writeToFile(String content) throws IOException {
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Returns a formatted summary for console display.
     *
     * @param summary the session summary
     * @return formatted string for console output
     */
    public static String formatForConsole(SessionSummary summary) {
        if (summary == null || summary.isEmpty()) {
            return "Nenhuma interação registrada nesta sessão.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Resumo da Sessão ---\n");
        sb.append("Interações: ").append(summary.getTotalInteractions()).append("\n");
        sb.append("Intenções identificadas: ");

        if (summary.getSelectedIntents().isEmpty()) {
            sb.append("nenhuma");
        } else {
            sb.append(String.join(", ", summary.getSelectedIntents()));
        }

        return sb.toString();
    }
}

