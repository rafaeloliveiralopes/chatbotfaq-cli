package dev.rafaellopes.chatbotfaq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

/**
 * Reads lines from console with charset auto-detection.
 * Strategy:
 * - Try strict UTF-8 decode (REPORT errors). If OK, candidate UTF-8.
 * - Decode with system default charset. Candidate default.
 * - Pick the best candidate using a small heuristic (avoids mojibake like "Ã©").
 */
final class ConsoleLineReader implements AutoCloseable {

    private final InputStream in;

    ConsoleLineReader(InputStream in) {
        this.in = in;
    }

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
            return null;
        }

        byte[] bytes = buffer.toByteArray();

        String utf8 = tryDecodeUtf8Strict(bytes);
        String def = new String(bytes, Charset.defaultCharset());

        // Remove CR from CRLF
        if (utf8 != null) utf8 = utf8.replace("\r", "");
        def = def.replace("\r", "");

        // If UTF-8 failed strictly, use default.
        if (utf8 == null) {
            return def;
        }

        // Heuristic: prefer the version with fewer mojibake markers.
        int utf8Score = mojibakeScore(utf8);
        int defScore = mojibakeScore(def);

        return (utf8Score <= defScore) ? utf8 : def;
    }

    private static String tryDecodeUtf8Strict(byte[] bytes) {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);

        try {
            CharBuffer cb = decoder.decode(ByteBuffer.wrap(bytes));
            return cb.toString();
        } catch (CharacterCodingException e) {
            return null;
        }
    }

    private static int mojibakeScore(String s) {
        if (s == null || s.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        int score = 0;

        // Replacement char means something went wrong in some decode path.
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\uFFFD') {
                score += 5;
            }
        }

        // Common mojibake markers when UTF-8 is decoded as cp1252.
        // "Ã", "Â" are frequent in PT-BR accented text corruption.
        score += countChar(s, 'Ã') * 2;
        score += countChar(s, 'Â') * 2;

        return score;
    }

    private static int countChar(String s, char target) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == target) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
