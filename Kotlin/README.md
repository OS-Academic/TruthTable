# Kotlin TruthTable generator

TruthTable generator implemented in [Kotlin](https://kotlinlang.org) using [Antlr4 generator](http://antlr.org).

## TODO and Limitations

Notation should be typed with explicit groups (`(` and `)`), we/I have plans to correctly group cases like `a -> b & c` to `a -> (b & c)` (following the common rule), but at the moment you should write `a -> (b & c)` yourself, otherwise the parser will not corretly recognize the input.

## Building and running

### Manual

Cloning and building:

```bash
git clone https://github.com/OSAcademic/TruthTable.git
cd Kotlin
gradle build
```

Running (you should have Java 8 or superior installed):

```bash
java -jar build/libs/OSA-TruthTable-Kotlin-*-shaded.jar
```

Then enter the notation, example: `(p & q) <-> (c | v)`, to exit you should send interrupt signal (`^C`) or `EOF` (`^D`) or type `exit`.

### Automatic

**TODO**, see issue [Draft #1](https://github.com/OSAcademic/Draft/issues/1).

#### Notation

- `!`, `¬` e `~` for negatiob
- `&`, `^`, `∧` e `⋅` for conjunction
- `|`, `v` e `+` for disjunction
- `⊕` e `/=` for exclusive disjunction
- `->` for conditional
- `<->` for biconditional
- Alphabet characters (from A to Z) both upper and lower case for prepositions (also sequences of characters may be used).

# Pt-br

## Gerador de table verdade em Kotlin

Gerador de tabela verdade implemtada em [Kotlin](https://kotlinlang.org) usando o gerador de analisador [Antlr4](http://antlr.org).

### A-Fazer e limitações

A notação precisa ser digitada com grupos explicitos (`(` e `)`), temos/eu tenho planos para agrupar corretamente casos como `a -> b & c` para `a -> (b & c)` (Seguindo a regra comum), mas no momento você deve escrever `a -> (b & c)` por conta propria, se não o analisador não irá reconhecer a entrada corretamente.

### Construindo e rodando

#### Manualmente

Clonando e construindo:

```bash
git clone https://github.com/OSAcademic/TruthTable.git
cd Kotlin
gradle build
```

Rodando (assumindo que você tenha Java 8 ou superior instalado):

```bash
java -jar build/libs/OSA-TruthTable-Kotlin-*-shaded.jar
```

Então digite a notação, exemplo: `(p & q) <-> (c | v)`, para sair você precisa enviar o sinal de interrupção (em sistema *nix `^C`) ou sinal de final de arquivo (em sistemas *nix `^D`), ou digitar `exit` no lugar da notação.

#### Automatico

**A-Fazer**, veja [Draft #1](https://github.com/OSAcademic/Draft/issues/1).

#### Aviso & Informações

Tudo no projeto está em ingles, a saida de informações tambem são em ingles, o básico é (para quem não tem muito conhecimento):

- `T`/`true` para verdadeiro (mesmo que `V`/`1`)
- `F`/`false` para falso (mesmo que `F`/`0`)


#### Notação

- `!`, `¬` e `~` para negação
- `&`, `^`, `∧` e `⋅` para conjunção
- `|`, `v` e `+` para disjunção
- `⊕` e `/=` para disjunção exclusiva
- `->` para condicional
- `<->` para bicondicional
- Letas do alfabeto de A a Z tanto maiusculas quanto minusculas para preposição (podem ser sequencias de letras tambem)