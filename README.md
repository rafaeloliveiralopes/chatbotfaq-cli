# ChatbotFAQ CLI

> Chatbot simples em Java (CLI) que responde perguntas sobre serviços de automação com chatbot usando matching por palavras-chave e uma base JSON externa.

---

## O que é este projeto?

O ChatbotFAQ é um chatbot baseado em palavras-chave que roda no terminal (CLI) como um FAQ interativo sobre "Serviços de automação com chatbot".

Ele responde perguntas usando um sistema simples de matching e uma base de conhecimento em JSON.

### O que ele faz:
- Responde perguntas sobre automação e chatbots
- Comandos: /ajuda, /reiniciar, /sair
- Resposta padrão (fallback) quando não entende a pergunta

### O que ele não faz:
- Não qualifica leads nem faz triagem comercial
- Não coleta dados do usuário (nome, telefone, etc.)
- Não conduz fluxos em etapas
- Não usa IA, NLP, banco de dados ou integrações externas

## Stack:
- Java 21
- Maven
- Jackson (parse de JSON)
- SLF4J + Logback (logs)

---

## Como compilar e executar

### Pré-requisitos
- Java 21 ou superior
- Maven (ou use o wrapper ./mvnw)

### Compilar

```bash
./mvnw clean package
````

Isso gera dois JARs em target/:

* chatbotfaq-cli-0.1.0-SNAPSHOT.jar (sem dependências)
* chatbotfaq-cli-0.1.0-SNAPSHOT-jar-with-dependencies.jar (recomendado)

### Executar

```bash
java -jar target/chatbotfaq-cli-0.1.0-SNAPSHOT-jar-with-dependencies.jar
```

O chatbot inicia e exibe uma mensagem de boas-vindas. Digite suas perguntas e pressione Enter.

Comandos disponíveis:

* /ajuda: mostra ajuda com exemplos de perguntas e comandos
* /reiniciar: reinicia a conversa
* /sair: encerra o chatbot

### Caminho customizado para a base (opcional)

Por padrão, o chatbot carrega **data/intents.json** na mesma pasta onde o JAR está sendo executado.

Para usar outro arquivo:

```bash
java -jar target/chatbotfaq-cli-0.1.0-SNAPSHOT-jar-with-dependencies.jar --kb /caminho/para/intents.json
```

---

## Como editar a base de conhecimento

A base de conhecimento fica em:

```text
 data/intents.json
```

### Estrutura do JSON

#### Cada intent possui:

* intent: identificador único (string)
* keywords: lista de palavras/frases para casar (array de strings)
* response: resposta retornada quando casar (string)
* priority: critério de desempate (número, maior ganha)

Exemplo:

```json
{
  "intent": "o_que_e_automacao",
  "keywords": [
    "o que e automacao",
    "o que é automacao",
    "automacao",
    "automatizar"
  ],
  "response": "Automação é usar tecnologia para executar tarefas repetitivas com menos esforço manual.",
  "priority": 20
}
```

### Como adicionar uma nova intent

* Abra data/intents.json
* Adicione um novo objeto no array seguindo a estrutura acima
* Salve o arquivo
* Reinicie o chatbot (não precisa recompilar)

Dicas:

* Prefira keywords normalizadas (minúsculas, sem acentos)
* Inclua variações comuns da mesma pergunta
* Use priority maior para intents mais importantes
* Teste rodando o chatbot e fazendo perguntas reais

---

## Exemplos de conversa

### Exemplo 1: Perguntas gerais

```
Bem-vindo ao ChatbotFAQ!
A nossa empresa trabalha com Serviços de Automação com chatbot.

Me diga o que você gostaria de saber sobre automações com chatbot.
Dica: digite /ajuda para ver exemplos de perguntas e comandos.

> o que é automação?
Automação é usar tecnologia para executar tarefas repetitivas com menos esforço manual. Em negócios, isso pode incluir responder clientes automaticamente, organizar pedidos, enviar mensagens, registrar dados e integrar ferramentas para reduzir retrabalho.

> o que é chatbot?
Chatbot é um programa que simula uma conversa para responder perguntas e orientar o usuário. Ele pode funcionar por regras e palavras-chave (como este exemplo) ou por IA, dependendo do projeto.

> como funciona uma conversa com chatbot?
Uma conversa com chatbot funciona assim: você faz uma pergunta, o bot identifica palavras-chave e retorna a resposta pré-definida mais adequada. Se não encontrar correspondência, ele responde com fallback e sugere exemplos de perguntas.

> /sair
Conversa encerrada. Obrigado pela visita!
```

### Exemplo 2: Benefícios e limitações

```
> quais são os benefícios?
Os principais benefícios são: atendimento mais rápido, redução de tarefas repetitivas, padronização de respostas e mais organização no primeiro contato.

> e quais são as limitações?
Um chatbot baseado em palavras-chave é ótimo para perguntas previsíveis, mas pode falhar com textos muito diferentes do esperado. Por isso, é importante manter a base de perguntas e respostas bem escrita e ampliar keywords conforme surgirem novas dúvidas.

> o que inclui o serviço?
Em geral, serviços de automação com chatbot incluem: definição das perguntas e respostas, regras de atendimento, mensagens de fallback, comandos (ex.: ajuda e sair), testes básicos e documentação para você atualizar o conteúdo da base de conhecimento.
```

### Exemplo 3: Preço e ajuda

```
> quanto custa?
O valor costuma depender do escopo: quantidade de perguntas e respostas, número de fluxos, canal de atendimento e nível de personalização. Se quiser, pergunte também: "o que inclui o serviço", "limitações" ou "como funciona uma conversa com chatbot".

> /ajuda
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
```

---

## Executar testes

```bash
./mvnw test
```

Os testes cobrem:

* Normalização de texto (minúsculas, remoção de acentos)
* Carregamento e validação do JSON
* Matching de intents e desempate por priority
* Tratamento de comandos no CLI

---

## Documentação

* PROJECT_SCOPE.md: escopo e critérios de aceite
* AGENTS.md: regras internas do projeto
* LICENSE: licença MIT

---

## Estrutura do projeto

```
chatbotfaq-cli/
├── data/
│   └── intents.json
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── dev/rafaellopes/chatbotfaq/
│   │   │       ├── App.java
│   │   │       └── core/
│   │   │           ├── Intent.java
│   │   │           ├── IntentLoader.java
│   │   │           ├── IntentMatcher.java
│   │   │           └── TextNormalizer.java
│   │   └── resources/
│   │       └── logback.xml
│   └── test/
│       ├── java/
│       │   └── dev/rafaellopes/chatbotfaq/
│       │       ├── AppTest.java
│       │       └── core/
│       │           ├── IntentLoaderTest.java
│       │           ├── IntentMatcherTest.java
│       │           ├── IntentTest.java
│       │           └── TextNormalizerTest.java
│       └── resources/
│           └── fixtures/
│               └── intents.json
├── target/
├── pom.xml
└── README.md
```

---

## Troubleshooting

Falha ao carregar a base de conhecimento:

* Verifique se data/intents.json existe na mesma pasta do JAR
* Ou use --kb para informar um caminho customizado
* Verifique se o JSON está válido

Java não encontrado:

* Instale Java 21 ou superior (ex.: Adoptium)

Build falhando:

* Confirme a versão do Java: java -version
* Tente: ./mvnw clean install
