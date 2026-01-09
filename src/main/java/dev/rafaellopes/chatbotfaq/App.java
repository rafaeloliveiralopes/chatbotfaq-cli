package dev.rafaellopes.chatbotfaq;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rafaellopes.chatbotfaq.core.Intent;
import dev.rafaellopes.chatbotfaq.core.IntentLoader;
import dev.rafaellopes.chatbotfaq.core.IntentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * CLI entrypoint for the ChatbotFAQ chatbot.
 * Runs an interactive loop reading user input and responding based on keyword intent matching.
 */
@SuppressWarnings({"java:S106"}) // CLI uses System.out/System.err by design
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static final Path CURRENT_DIR = Path.of("").toAbsolutePath();

    private static final String FALLBACK_MESSAGE =
            "Não tenho uma resposta para isso. :( \nDigite: /ajuda, para ver exemplos de perguntas ou tente reformular a sua pergunta.";

    private static final String HELP_MESSAGE = """
            Exemplos de perguntas sobre automação com chatbots que você pode me fazer:
            - O que é automação?
            - O que é chatbot?
            - O que é automação com chatbot?
            - O que inclui um serviço de automação com chatbot?
            - Como funciona uma conversa com chatbot?

            Comandos disponíveis:
            - /ajuda: mostra esta mensagem
            - /reiniciar: reinicia a conversa
            - /sair: encerra o chatbot
            """;

    public static void main(String[] args) {
        log.info("Starting ChatbotFAQ");

        ObjectMapper objectMapper = new ObjectMapper();
        IntentLoader loader = new IntentLoader(objectMapper);
        IntentMatcher matcher = new IntentMatcher();

        Path kbPath = resolveKbPath(args);
        log.info("Knowledge base path: {}", kbPath.toAbsolutePath());

        List<Intent> intents;
        try {
            intents = loader.load(kbPath);
        } catch (Exception e) {
            log.error("Failed to load knowledge base: {}", kbPath.toAbsolutePath(), e);

            System.out.println("\n⚠️  Desculpe, estou com dificuldades técnicas no momento.");
            System.out.println("Não consegui carregar a base de conhecimento (intents.json).");
            System.out.println("Tente novamente mais tarde.");
            System.exit(1);
            return;
        }

        log.info("Knowledge base loaded: intents={}", intents.size());

        if (intents.isEmpty()) {
            log.warn("Knowledge base loaded but contains no intents!");
            System.out.println("\n⚠️  Aviso: A base de conhecimento está vazia.");
            System.out.println("Verifique se o arquivo data/intents.json contém perguntas configuradas.\n");
        }

        printWelcome();

        // Prefer native console when available (best Unicode behavior on Windows).
        var console = System.console();
        if (console != null) {
            runWithNativeConsole(console, intents, matcher);
            return;
        }

        // Fallback for IDE terminals (System.console() is usually null).
        runWithByteConsole(intents, matcher);
    }

    private static void runWithNativeConsole(java.io.Console console, List<Intent> intents, IntentMatcher matcher) {
        boolean running = true;

        while (running) {
            String userMessage = console.readLine("> ");
            if (userMessage == null) {
                running = false;
            } else {
                running = processUserMessage(userMessage, intents, matcher);
            }
        }
    }

    private static void runWithByteConsole(List<Intent> intents, IntentMatcher matcher) {
        try (ConsoleLineReader reader = new ConsoleLineReader(System.in)) {
            boolean running = true;

            while (running) {
                System.out.print("> ");
                System.out.flush();

                String userMessage = reader.readLine();
                if (userMessage == null) {
                    running = false;
                } else {
                    running = processUserMessage(userMessage, intents, matcher);
                }
            }
        } catch (Exception e) {
            log.error("Error reading user input", e);
        }
    }

    /**
     * Process user message and return whether to continue running.
     *
     * @param userMessage the user input
     * @param intents the list of intents
     * @param matcher the intent matcher
     * @return true to continue, false to exit
     */
    private static boolean processUserMessage(String userMessage, List<Intent> intents, IntentMatcher matcher) {
        if (userMessage.isBlank()) {
            log.debug("Blank input received");
            System.out.println(FALLBACK_MESSAGE + "\n");
            return true;
        }

        String trimmed = userMessage.trim();

        switch (trimmed) {
            case "/sair" -> {
                log.debug("Command received: /sair");
                System.out.println("Conversa encerrada. Obrigado pela visita!");
                return false;
            }
            case "/ajuda" -> {
                log.debug("Command received: /ajuda");
                System.out.println(HELP_MESSAGE);
            }
            case "/reiniciar" -> {
                log.debug("Command received: /reiniciar");
                System.out.println("Conversa reiniciada.\n");
                printWelcome();
            }
            default -> {
                Optional<Intent> bestIntent = findBestIntentSafe(trimmed, intents, matcher);

                if (bestIntent.isPresent()) {
                    log.info("Selected intent: {}", bestIntent.get().getIntent());
                } else {
                    log.info("No intent matched (fallback)");
                }

                String response = bestIntent.map(Intent::getResponse).orElse(FALLBACK_MESSAGE);
                System.out.println(response + "\n");
            }
        }

        return true;
    }

    private static void printWelcome() {
        System.out.println("Bem-vindo(a) ao ChatbotFAQ!");
        System.out.println("A nossa empresa trabalha com Serviços de Automação com chatbot.\n");
        System.out.println("Me diga o que você gostaria de saber sobre automações com chatbot.");
        System.out.println("Dica: digite /ajuda para ver exemplos de perguntas e comandos.\n");
    }

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

    private static Optional<Intent> findBestIntentSafe(String userMessage, List<Intent> intents, IntentMatcher matcher) {
        if (userMessage == null || userMessage.isBlank()) {
            return Optional.empty();
        }
        if (intents == null || intents.isEmpty()) {
            return Optional.empty();
        }
        try {
            return matcher.findBestIntent(userMessage, intents);
        } catch (Exception e) {
            log.warn("Intent matching failed, using fallback: {}", e.getClass().getSimpleName());
            return Optional.empty();
        }
    }

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

        Path cwdPath = CURRENT_DIR.resolve("data").resolve("intents.json");
        if (Files.exists(cwdPath)) {
            return cwdPath;
        }

        Path jarDir = getJarDir();
        return jarDir.resolve("data").resolve("intents.json");
    }

    private static Path getJarDir() {
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
