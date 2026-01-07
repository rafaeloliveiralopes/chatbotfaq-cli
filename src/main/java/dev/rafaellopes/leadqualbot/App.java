package dev.rafaellopes.leadqualbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rafaellopes.leadqualbot.core.Intent;
import dev.rafaellopes.leadqualbot.core.IntentLoader;
import dev.rafaellopes.leadqualbot.core.IntentMatcher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * CLI entrypoint for the LeadQualBot chatbot.
 * Runs an interactive loop reading user input and responding based on intent matching.
 */
@SuppressWarnings({"java:S106"}) // CLI uses System.out/System.err by design
public class App {

    private static final Path CURRENT_DIR = Path.of("").toAbsolutePath();

    private static final String FALLBACK_MESSAGE =
            "Desculpe, não entendi. Você pode reformular ou digitar /ajuda para ver as opções.";

    private static final String HELP_MESSAGE = """
            Comandos disponíveis:
            /ajuda - Mostra esta mensagem
            /reiniciar - Reinicia a conversa
            /sair - Encerra o chatbot

            Você também pode fazer perguntas sobre nossos serviços!
            """;

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        IntentLoader loader = new IntentLoader(objectMapper);
        IntentMatcher matcher = new IntentMatcher();

        Path kbPath = resolveKbPath(args);
        List<Intent> intents;
        try {
            intents = loader.load(kbPath);
        } catch (Exception e) {
            System.err.println("Erro ao carregar base de conhecimento: " + e.getMessage());
            System.err.println("Dica: use --kb <caminho-do-json> ou mantenha data/intents.json ao lado do jar.");
            System.exit(1);
            return;
        }

        System.out.println("Bem-vindo ao LeadQualBot!");
        System.out.println("Digite /ajuda para ver os comandos disponíveis.\n");

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running && scanner.hasNextLine()) {
                System.out.print("> ");

                String userMessage = scanner.nextLine();

                if (userMessage == null || userMessage.isBlank()) {
                    System.out.println(FALLBACK_MESSAGE + "\n");
                } else {
                    String trimmed = userMessage.trim();

                    switch (trimmed) {
                        case "/sair" -> {
                            System.out.println("Até logo!");
                            running = false;
                        }
                        case "/ajuda" -> System.out.println(HELP_MESSAGE);
                        case "/reiniciar" -> System.out.println("Conversa reiniciada. Como posso ajudar?\n");
                        default -> {
                            String response = decideResponse(trimmed, intents, matcher, FALLBACK_MESSAGE);
                            System.out.println(response + "\n");
                        }
                    }
                }
            }
        }
    }

    /**
     * Decides the response for a user message by finding the best matching intent.
     * Returns fallback message when no intent matches or processing fails.
     * This method is package-private and pure (no side effects) to enable testing.
     *
     * @param userMessage the user input message
     * @param intents list of available intents
     * @param matcher the intent matcher to use
     * @param fallbackMessage the fallback message to return when no match found
     * @return the response message to display
     */
    static String decideResponse(String userMessage, List<Intent> intents, IntentMatcher matcher, String fallbackMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return fallbackMessage;
        }

        try {
            Optional<Intent> bestIntent = matcher.findBestIntent(userMessage, intents);
            return bestIntent.map(Intent::getResponse).orElse(fallbackMessage);
        } catch (Exception e) {
            return fallbackMessage;
        }
    }

    /**
     * Resolves the knowledge base path from command-line arguments.
     * If --kb <path> is provided, uses that path.
     * Otherwise, resolves data/intents.json relative to the JAR directory or current directory.
     *
     * @param args the command-line arguments
     * @return the resolved path to the knowledge base file
     */
    private static Path resolveKbPath(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if ("--kb".equals(args[i]) && i + 1 < args.length) {
                    String value = args[i + 1];
                    if (value != null && !value.isBlank()) {
                        return Path.of(value.trim());
                    }
                }
            }
        }
        Path baseDir = getAppBaseDir();
        return baseDir.resolve("data").resolve("intents.json");
    }

    private static Path getAppBaseDir() {
        try {
            var uri = App.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            Path location = Path.of(uri);
            if (Files.isRegularFile(location)) {
                return location.getParent();
            }
            return CURRENT_DIR;
        } catch (Exception e) {
            return CURRENT_DIR;
        }
    }
}
