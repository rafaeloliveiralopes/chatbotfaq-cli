# GitHub Copilot Instructions for chatbotfaq-cli

## Context
This is a simple Java CLI chatbot project with strict governance rules to prevent scope creep and overengineering.

## Before Starting ANY Task

**MANDATORY:** Read these files first:
1. `/AGENTS.md` - Complete governance rules
2. `/PROJECT_SCOPE.md` - Scope definition and acceptance criteria

## Key Rules

### What This Project IS
- Simple Java 21 CLI application
- Keyword-based chatbot (simple string matching)
- External JSON knowledge base
- Commands: /ajuda, /reiniciar, /sair

### What This Project IS NOT
- ❌ Web application or API
- ❌ NLP/LLM/AI-powered
- ❌ Database-backed
- ❌ Integrated with external services

### Development Principles
- **YAGNI** (You Aren't Gonna Need It)
- **Keep It Simple**
- **No overengineering**
- **No premature optimization**
- **No complex patterns** (Clean Architecture, DDD, Hexagonal, etc.)

### Code Style
- Java 21
- 4 spaces indentation
- Simple, readable code
- Minimal abstractions
- JavaDoc for public APIs only

### Commits
- Use Conventional Commits (English)
- Format: `type(scope): subject`
- Examples: `feat(cli): add command`, `fix(parser): handle empty input`

### Definition of Done
A change is only complete if it:
- ✅ Respects scope completely
- ✅ Doesn't break existing features
- ✅ Has tests when applicable
- ✅ Adds NO unnecessary complexity
- ✅ Keeps CLI working
- ✅ Meets acceptance criteria

## When in Doubt
- **Doubt between doing more or keeping simple?** → Keep simple
- **Doubt between evolving architecture or delivering agreed?** → Deliver agreed

## Blocked Actions
Never suggest or implement:
- Web/REST API
- Database integration
- NLP/LLM/embeddings
- Complex architecture patterns
- Multiple layers for simple operations
- Interfaces "for the future"
- Additional frameworks

---

**Full rules:** See `/AGENTS.md` and `/PROJECT_SCOPE.md`

