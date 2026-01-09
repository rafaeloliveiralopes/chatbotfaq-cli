package dev.rafaellopes.chatbotfaq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Reads lines from console with automatic charset detection.
 * Tries UTF-8 first, falls back to system default if replacement character is found.
 * This handles the common Windows scenario where terminal encoding differs from system default.
 */
final class ConsoleLineReader implements AutoCloseable {

    private final InputStream in;

    ConsoleLineReader(InputStream in) {
        this.in = in;
    }

    /**
     * Reads a line from input, auto-detecting charset.
     *
     * @return the line read, or null if EOF
     * @throws IOException if an I/O error occurs
     */
    String readLine() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(128);

        int b;
        while ((b = in.read()) != -1) {
            if (b == '\n') {
                break;
            }
            buffer.write(b);
        }

        if (b == -1 && buffer.size() == 0) {
            return null; // EOF
        }

        byte[] bytes = buffer.toByteArray();

        // Try UTF-8 first
        String utf8 = new String(bytes, StandardCharsets.UTF_8);
        if (utf8.indexOf('\uFFFD') >= 0) {
            // Contains replacement character - UTF-8 decoding failed, use system default
            return new String(bytes, Charset.defaultCharset()).replace("\r", "");
        }

        return utf8.replace("\r", "");
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}

