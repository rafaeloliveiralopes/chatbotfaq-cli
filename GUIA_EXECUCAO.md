# CHATBOT FAQ CLI - Guia de Execução

## 📋 Pré-requisitos

**Requisito obrigatório:** Java 21 ou superior instalado no computador.

### Verificar se o Java está instalado

Abra o terminal (Linux/Mac) ou Prompt de Comando (Windows) e digite:

```bash
java -version
```

Você deve ver algo como:
```
openjdk version "21.0.x" ...
```

Se não tiver o Java instalado, baixe em: https://adoptium.net/

---

## 🚀 Como Executar o Chatbot

### Passo 1: Verifique os arquivos

Certifique-se de que você tem estes arquivos na mesma pasta:

```
📁 pasta-do-chatbot/
  ├── chatbotfaq-cli-0.1.0-SNAPSHOT-jar-with-dependencies.jar
  └── data/
      └── intents.json
```

### Passo 2: Abra o terminal na pasta do chatbot

- **Windows:** Shift + Botão direito na pasta → "Abrir janela de comando aqui" ou "Abrir no Terminal"
- **Linux/Mac:** Botão direito na pasta → "Abrir Terminal aqui"

### Passo 3: Execute o chatbot

Digite o comando:

```bash
java -jar chatbotfaq-cli-0.1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 💬 Como Usar

### Mensagem de Boas-vindas

Ao iniciar, você verá:

```
Bem-vindo(a) ao ChatbotFAQ!
A nossa empresa trabalha com Serviços de Automação com chatbot.

Me diga o que você gostaria de saber sobre automações com chatbot.
Dica: digite /ajuda para ver exemplos de perguntas e comandos.

>
```

### Comandos Disponíveis

| Comando | Descrição |
|---------|-----------|
| `/ajuda` | Mostra exemplos de perguntas que você pode fazer |
| `/reiniciar` | Reinicia a conversa |
| `/sair` | Encerra o chatbot |

### Exemplos de Perguntas

Você pode fazer perguntas como:

- ✅ "O que é automação?"
- ✅ "O que é chatbot?"
- ✅ "Quando usar chatbot?"
- ✅ "Quanto custa?"
- ✅ "Quais são os benefícios?"
- ✅ "Quais as limitações?"

O chatbot vai responder com base nas palavras-chave que reconhecer.

---

## 🎯 Exemplo de Uso

```
> o que é chatbot
Chatbot é um programa que simula uma conversa para responder perguntas 
e orientar o usuário. Ele pode funcionar por regras e palavras-chave 
(como este exemplo) ou por IA, dependendo do projeto.

> quanto custa
O valor costuma depender do escopo: quantidade de perguntas e respostas, 
número de fluxos, canal de atendimento e nível de personalização.

> /sair
Conversa encerrada. Obrigado pela visita!
```

---

## 🔧 Personalizar o Conteúdo

Para adicionar ou modificar perguntas e respostas, edite o arquivo `data/intents.json`.

### Estrutura de um Intent

```json
{
  "intent": "nome_do_intent",
  "keywords": [
    "palavra chave 1",
    "palavra chave 2"
  ],
  "response": "Resposta que o chatbot vai dar",
  "priority": 20
}
```

**Importante:** 
- `keywords`: palavras ou frases que disparam essa resposta
- `priority`: quanto maior, mais prioritário (use para desambiguar)
- Após editar, salve o arquivo e reinicie o chatbot

---

## ❓ Problemas Comuns

### "Command not found" ou "java não é reconhecido"

**Solução:** Java não está instalado ou não está no PATH do sistema.
- Instale o Java 21: https://adoptium.net/
- Ou verifique as variáveis de ambiente

### "Could not find or load main class"

**Solução:** Certifique-se de estar executando o JAR correto:
```bash
java -jar chatbotfaq-cli-0.1.0-SNAPSHOT-jar-with-dependencies.jar
```

### "Failed to load knowledge base"

**Solução:** O arquivo `data/intents.json` não está na pasta correta.
- Verifique se existe a pasta `data/` no mesmo diretório do JAR
- Verifique se o arquivo `intents.json` está dentro dela

---

## 📊 Funcionalidades Implementadas

✅ Interface CLI interativa  
✅ Matching de perguntas por palavras-chave  
✅ 9 intents pré-configurados sobre automação com chatbot  
✅ Normalização de texto (remove acentos, converte para minúsculas)  
✅ Sistema de prioridades para desambiguação  
✅ Mensagens de fallback para perguntas não reconhecidas  
✅ Comandos: /ajuda, /reiniciar, /sair  
✅ Base de conhecimento externa (JSON)  
✅ Logs para debugging (arquivo chatbotfaq.log)  

---

## 📞 Suporte

Este chatbot foi desenvolvido como uma solução simples e eficiente para 
responder perguntas frequentes sobre automação com chatbots.

Para questões técnicas ou dúvidas sobre o projeto, entre em contato com:

**Rafael Lopes**  
Email: [seu-email]  
GitHub: [seu-github]

---

## 📝 Informações Técnicas

- **Linguagem:** Java 21
- **Build:** Maven 3.9+
- **Dependências:** Jackson (JSON), SLF4J + Logback (Logging), JUnit 5 (Testes)
- **Versão:** 0.1.0-SNAPSHOT
