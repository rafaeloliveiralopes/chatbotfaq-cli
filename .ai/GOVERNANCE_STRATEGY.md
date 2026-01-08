# Estratégia de Governança - ChatbotFAQ CLI

## Problema
Como garantir que agentes de IA (GitHub Copilot, ChatGPT, etc.) respeitem as regras do projeto em NOVAS sessões de conversa, dado que LLMs não têm memória persistente entre sessões?

## Solução Implementada

### 1. **Documentação como Código**
Os arquivos de governança estão versionados no repositório:
- `AGENTS.md` - Regras completas de governança
- `PROJECT_SCOPE.md` - Escopo e critérios de aceite

### 2. **Múltiplos Pontos de Entrada**
Criamos redundância estratégica para maximizar a chance de que agentes leiam as regras:

```
.github/copilot-instructions.md  → GitHub Copilot lê automaticamente
.ai/instructions.md              → Convenção comum para instruções de IA
.ai/README.md                    → Aviso chamativo
README.md                        → Primeira parada de qualquer desenvolvedor/agente
AGENTS.md                        → Governança completa
PROJECT_SCOPE.md                 → Escopo detalhado
```

### 3. **Hierarquia de Informação**

**Nível 1 - Chamada para Ação:**
- `.ai/README.md` - Aviso curto e direto
- `README.md` - Seção destacada no topo

**Nível 2 - Referência Rápida:**
- `.github/copilot-instructions.md` - Resumo executivo
- `.ai/instructions.md` - Checklist prático

**Nível 3 - Documentação Completa:**
- `AGENTS.md` - Todas as regras e princípios
- `PROJECT_SCOPE.md` - Requisitos e critérios de aceite

### 4. **Convenções Específicas por Ferramenta**

#### GitHub Copilot
- Lê automaticamente `.github/copilot-instructions.md`
- Aparece nas instruções de contexto do Copilot Chat

#### ChatGPT / Claude / outros
- Desenvolvedor pode referenciar os arquivos no início da conversa
- README.md destaca a importância de ler a documentação

### 5. **Princípio da Redundância**
Mesmo que um agente ignore um arquivo, há múltiplas camadas:
- Se ignorar `.ai/README.md`, pode ver no `README.md`
- Se ignorar o resumo, pode ser direcionado ao documento completo
- Se começar a trabalhar, a revisão de código pode capturar desvios

## Como Funciona na Prática

### Primeira Interação (Nova Sessão)
1. Agente de IA é ativado no repositório
2. GitHub Copilot carrega `.github/copilot-instructions.md` automaticamente
3. OU: Desenvolvedor cola instruções do `.ai/instructions.md`
4. Agente lê `AGENTS.md` e `PROJECT_SCOPE.md` antes de agir

### Durante o Trabalho
- Agente consulta `PROJECT_SCOPE.md` para validar se mudança está no escopo
- Segue checklist do Definition of Done em `AGENTS.md`
- Usa Conventional Commits conforme especificado

### Pull Request / Code Review
- Humano revisa se mudanças respeitam `AGENTS.md`
- Valida contra critérios de aceite em `PROJECT_SCOPE.md`
- Rejeita mudanças que violem as regras

## Limitações Conhecidas

### ❌ O que NÃO funciona:
- **Memória entre sessões:** LLMs sempre começam do zero
- **Garantia 100%:** Agentes podem ignorar instruções
- **Enforcement automático:** Ainda depende de revisão humana

### ✅ O que SIM funciona:
- **Documentação como fonte da verdade**
- **Múltiplos pontos de entrada** aumentam chance de leitura
- **Instruções claras e objetivas** facilitam seguimento
- **Revisão de código** captura desvios

## Manutenção

### Quando atualizar estes documentos:
1. **Mudança de escopo** → Atualizar `PROJECT_SCOPE.md`
2. **Nova regra de governança** → Atualizar `AGENTS.md`
3. **Mudança de stack** → Atualizar ambos + resumos
4. **Lições aprendidas** → Adicionar em `AGENTS.md`

### Checklist de consistência:
- [ ] `AGENTS.md` e `PROJECT_SCOPE.md` estão sincronizados
- [ ] `.github/copilot-instructions.md` reflete mudanças principais
- [ ] `.ai/instructions.md` está atualizado
- [ ] `README.md` aponta para documentação correta

## Resultado Esperado

Com esta estratégia multi-camadas:
- ✅ Agentes têm **alta probabilidade** de ler as regras
- ✅ Documentação é **única fonte da verdade**
- ✅ Regras são **versionadas com o código**
- ✅ Futuras sessões podem **referenciar facilmente**
- ✅ Desenvolvedores humanos também se beneficiam

## Conclusão

**Não há como "memorizar" entre sessões de IA**, mas podemos criar um ambiente onde:
1. As regras estão sempre acessíveis
2. Múltiplos mecanismos incentivam a leitura
3. Documentação é clara e objetiva
4. Revisão humana valida o cumprimento

Esta é a melhor solução possível dentro das limitações técnicas atuais.

---

**Criado em:** 6 de Janeiro de 2025  
**Versão:** 1.0  
**Autor:** Rafael Lopes (com assistência de GitHub Copilot)

