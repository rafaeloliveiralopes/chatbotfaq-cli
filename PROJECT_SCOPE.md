# Serviços de Automação CLI (Java)

## 1. Objetivo

* Desenvolver um chatbot básico via console (CLI) que responde perguntas do usuário sobre “Serviços de automação com chatbot”
* O chatbot deve funcionar como FAQ interativo, respondendo com base num conjunto fixo de respostas pré-definidas em arquivo JSON externo
* O chatbot não deve qualificar leads, coletar dados do usuário, nem conduzir fluxo em etapas (fora de escopo)

## 2. Entregáveis

* Aplicação Java via console (CLI) interativo (‘loop’ até o usuário sair)
* Base de conhecimento externa em JSON (intents.json)
* Comandos:

  * /ajuda
  * /reiniciar
  * /sair
* Resposta padrão (fallback) quando não houver correspondência
* README com:

  * Como executar
  * Como editar a base JSON
  * Exemplos de perguntas e respostas (FAQ) sobre automação e chatbot

## 3. Tecnologias e dependências

* Java 21
* Maven
* Jackson (leitura do JSON)
* Java Collections (Map, List) para estruturas internas
* Normalização de texto: java.text. Normalizer (minúsculas e remoção de acentos)
* Testes: JUnit 5 (matcher e carregamento do JSON)
* Logs básicos: SLF4J + Logback

## 4. Decisões de Arquitetura

* Layout padrão do Maven para estrutura de código e testes (src/main, src/test)
* Base de conhecimento em JSON como formato único, para suportar lista de keywords e prioridade
* Base de conhecimento mantida fora do JAR em data/intents.json para permitir atualização sem recompilar
* Separação leve entre core e ‘interface’:

  * Core (normalização, matching e seleção de resposta) não depende de console
  * CLI atua como adaptador de entrada e saída (lê do usuário e imprime resposta)
* Logging apenas para diagnóstico básico, sem registrar dados sensíveis do usuário

# 5. Base de conhecimento (JSON)

* Arquivo externo (ex.: data/intents.json)
* Estrutura mínima por intenção:

  * intent: string
  * keywords: array de strings
  * response: string
  * priority: number
* Conteúdo esperado (exemplos de temas, não limitado a isso):

  * O que é automação
  * O que é chatbot
  * O que é automação com chatbot
  * O que inclui o serviço
  * Como funciona uma conversa com chatbot
  * Limitações e expectativas (o que não faz)

# 6. Regras de funcionamento (motor de matching)

* Fluxo:

  * ler mensagem do usuário
  * se for comando, executar comando
  * senão, normalizar texto e aplicar matching
* Normalização:

  * converter para minúsculas
  * remover acentos
  * reduzir espaços duplicados
* Matching:

  * contar ocorrências de keywords por intenção
  * selecionar a intenção com maior score
  * em empate, usar maior priority
  * persistindo empate, usar a primeira intenção definida no JSON
* Fallback:

  * se nenhum match ocorrer, responder com mensagem padrão orientando a reformular ou usar /ajuda

## 7. Critérios de aceite (checklist)

* Executa via CLI e mantém ‘loop’ até sair
* Mostra uma apresentação inicial do chatbot alinhada ao tema “Serviços de automação com chatbot”
* Comandos /ajuda, /reiniciar e /sair funcionam
* /ajuda lista exemplos de perguntas suportadas ou temas disponíveis
* Lê a base por arquivo JSON externo (sem hardcode das intenções no código)
* Aplica normalização + matching por keywords com desempate por priority
* Retorna fallback quando não entender
* Não quebra com entrada vazia ou caracteres inesperados
* README com explicação objetiva de como executar e como atualizar o intents.json, incluindo exemplos de perguntas e respostas

## 8. Fora de escopo

* Qualificação de leads, triagem comercial, roteiros de orçamento, agendamento ou atendimento humano
* Coleta estruturada de dados do usuário (nome, WhatsApp, dia, horário) e confirmação em etapas
* Interface web ou GUI
* API HTTP e integrações (WhatsApp, CRM, banco de dados, Google Calendar, etc.)
* NLP/LLM/embeddings/RAG
* Persistência de histórico de conversas

## 9. Opcional, somente se sobrar tempo (não bloqueia aceite)

* Exportar um resumo do uso da sessão para arquivo local (ex.: ‘logs’ de intents acionados e contagem de fallbacks)
* Se falhar ao salvar, o chatbot continua funcionando e ainda mostra o resumo no console
