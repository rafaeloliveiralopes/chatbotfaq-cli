# Repository Guidelines for Agents

Este documento define regras mínimas para atuação de agentes (e humanos) neste repositório.
O objetivo é preservar o escopo, evitar overengineering e garantir entregas previsíveis.

O projeto é ChatbotFAQ CLI, um chatbot simples em Java, executado via console, baseado em palavras-chave e base externa em JSON.

---

## Escopo do Projeto

* Aplicação Java via console (CLI)
* Chatbot de pré-atendimento e qualificação de leads
* Respostas baseadas em matching de palavras-chave
* Base de conhecimento externa em JSON
* Comandos suportados: /ajuda, /reiniciar, /sair
* Resposta padrão (fallback) quando não houver correspondência
* README com instruções de execução e exemplos de conversa:

    * Orçamento
    * Agendamento
    * Atendimento humano
* Sem uso de NLP, LLMs, embeddings, banco de dados ou integrações externas

Qualquer ação fora desse escopo é considerada fora de contrato, salvo solicitação explícita.

---

## Estrutura do Projeto

Estrutura padrão Maven:

* `src/main/java/`
  Código-fonte da aplicação
* `src/main/resources/`
  Recursos estáticos
* `data/`
  Base de conhecimento externa (ex.: `intents.json`)
* `src/test/java/`
  Testes unitários
* `README.md`
  Instruções de execução e uso
* `PROJECT_SCOPE.md`
  Contrato de entrega e critérios de aceite
* `AGENTS.md`
  Este arquivo

IMPORTANTE: NUNCA, NUNCA, NUNCA criar pastas sem necessidade real e validada.

---

## Build, Execução e Testes

Ferramentas permitidas:

* Java 21
* Maven
* JUnit 5
* Jackson
* SLF4J + Logback

Comandos padrão:

* `./mvnw clean package`
* `./mvnw test`
* `java -jar target/chatbotfaq-cli.jar`

Não introduzir ‘scripts’ auxiliares, Makefile ou ferramentas externas.

---

## Convenções de Código

* Linguagem: Java
* Indentação: 4 espaços
* Código simples, direto e legível
* Evitar abstrações desnecessárias
* Sem frameworks adicionais
* Sem uso de reflexão, proxies ou padrões complexos

Regras de documentação:

* Comentários devem ser objetivos e usados apenas quando agregarem clareza real.
* Classes, métodos públicos e comportamentos relevantes devem ser documentados com JavaDoc.
* JavaDoc deve ser curto, direto e focado em:

    * intenção do código
    * parâmetros de entrada
    * valor de retorno
    * efeitos colaterais relevantes, quando existirem

---

## Base de Conhecimento (JSON)

Regras obrigatórias:

* A base deve permanecer externa ao JAR
* Estrutura mínima por intenção:

    * intent
    * keywords
    * response
    * priority
* Nenhuma lógica de negócio deve depender de valores hardcoded no código.

Agentes não devem alterar o formato do JSON sem autorização explícita.

---

## Commits e Controle de Mudanças

Padrão obrigatório de commit:

* Conventional Commits
* Idioma: inglês
* Formato: `type(scope): subject`

Exemplos:

* `feat(cli): add fallback response when no intent matches`
* `fix(parser): handle empty user input safely`

Não agrupar mudanças não relacionadas no mesmo commit.
Importante: antes de começar qualquer atividade suga o seguinte fluxo:
* Verifique em qual branch está trabalhando.
* Crie uma branch nova se não existir uma mais adequada para a atividade.

---

## Guardrails Obrigatórios

### Regra 0: Bloqueio de escopo

* Se a mudança não estiver prevista no PROJECT_SCOPE.md, pare
* Não adicionar funcionalidades aproveitando o embalo
* Não antecipar evoluções futuras (API, Web, IA, banco etc.)

---

### Regra 1: Sem overengineering

É proibido:

* Criar camadas extras sem necessidade
* Introduzir padrões complexos (clean architecture, hexagonal, DDD etc.)
* Criar ‘interfaces’ para o futuro
* Generalizar prematuramente

Este projeto é intencionalmente simples.

---

### Regra 2: Sem workaround silencioso

* Não engolir exceções
* Não mascarar erros com fallbacks silenciosos
* Falhas devem ser tratadas de forma clara e previsível

---

### Regra 3: Segurança básica

* Não registrar entradas completas do usuário em logs
* Não persistir dados sensíveis
* Logs apenas para diagnóstico técnico
* Nenhuma dependência nova sem autorização

---

### Regra 4: Definition of Done (mínimo)

Uma mudança só é considerada pronta se:

* Respeita integralmente o escopo definido
* Não quebra funcionalidades existentes
* Possui testes quando aplicável
* Não adiciona complexidade desnecessária
* Mantém o funcionamento via CLI intacto
* Atende aos critérios de aceite definidos em PROJECT_SCOPE.md

---

## Regra Final para Agentes

Se houver dúvida entre:

* fazer mais
* ou manter simples

mantenha simples.

Se houver dúvida entre:

* evoluir arquitetura
* ou entregar o combinado

entregue o combinado.
