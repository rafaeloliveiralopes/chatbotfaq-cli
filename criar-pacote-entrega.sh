#!/bin/bash

# Script para criar pacote de entrega do Chatbot FAQ CLI
# Apenas arquivos essenciais para execução

PACKAGE_DIR="chatbotfaq-cli-entrega"
ZIP_FILE="chatbotfaq-cli-entrega.zip"

echo "🎁 Criando pacote de entrega do Chatbot FAQ CLI..."
echo ""

# Limpar pacote anterior
rm -rf "$PACKAGE_DIR" "$ZIP_FILE"

# Criar estrutura
mkdir -p "$PACKAGE_DIR/data"

# Copiar JAR executável
echo "📦 Copiando JAR executável..."
cp target/chatbotfaq-cli-0.1.0-SNAPSHOT-jar-with-dependencies.jar "$PACKAGE_DIR/"

# Copiar base de conhecimento
echo "📚 Copiando base de conhecimento..."
cp data/intents.json "$PACKAGE_DIR/data/"

# Copiar README
echo "📖 Copiando README..."
cp README.md "$PACKAGE_DIR/"

# Copiar LICENSE
echo "📜 Copiando LICENSE..."
cp LICENSE "$PACKAGE_DIR/" 2>/dev/null || echo "LICENSE não encontrado (opcional)"

# Criar arquivo de instruções rápidas
echo "📝 Criando instruções de execução..."
cat > "$PACKAGE_DIR/COMO-EXECUTAR.txt" << 'EOF'
╔══════════════════════════════════════════════════════╗
║          CHATBOT FAQ CLI - Como Executar             ║
╚══════════════════════════════════════════════════════╝

📋 PRÉ-REQUISITO
---------------
Java 21 ou superior instalado

Verificar: java -version


🚀 EXECUTAR
-----------
java -jar chatbotfaq-cli-0.1.0-SNAPSHOT-jar-with-dependencies.jar


💬 COMANDOS
-----------
/ajuda      - Mostra exemplos de perguntas
/reiniciar  - Reinicia a conversa
/sair       - Encerra o chatbot


💡 EXEMPLOS DE PERGUNTAS
------------------------
- O que é automação?
- O que é chatbot?
- Quando usar chatbot?
- Quanto custa?
- Quais os benefícios?


📖 DOCUMENTAÇÃO COMPLETA
------------------------
Consulte o arquivo README.md para mais informações.


🔧 PERSONALIZAR
---------------
Edite o arquivo data/intents.json para adicionar ou
modificar perguntas e respostas.

EOF

# Criar arquivo ZIP (entrando na pasta para evitar subpasta aninhada)
echo "🗜️  Criando arquivo ZIP..."
cd "$PACKAGE_DIR" && zip -r "../$ZIP_FILE" . > /dev/null && cd ..

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ Pacote criado com sucesso!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📦 Arquivo: $ZIP_FILE"
echo ""
echo "📋 Conteúdo do ZIP:"
unzip -l "$ZIP_FILE" | head -15
echo ""

# Limpar pasta temporária (mantém apenas o .zip)
rm -rf "$PACKAGE_DIR"

echo "✨ Pronto para enviar!"

