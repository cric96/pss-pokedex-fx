# Design dettagliato

## 1. Catalogo e progressi dell'allenatore

```mermaid
classDiagram
    direction LR
    class Pokedex {
        <<interface>>
        +getAllPokemon() Set~PokemonEntry~
        +contains(PokemonEntry entry) boolean
    }
    class PokemonEntry {
        <<interface>>
        +getIndex() int
        +getName() String
        +getPrimaryType() PokemonType
        +getSecondaryType() Optional~PokemonType~
    }
    class PokemonType {
        <<interface>>
        +getName() String
    }
    class TrainerProgress {
        <<interface>>
        +getPokedex() Pokedex
        +getStatus(PokemonEntry entry) EncounterStatus
        +markAsSeen(PokemonEntry entry)
        +markAsCaught(PokemonEntry entry)
    }
    class EncounterStatus {
        <<enumeration>>
        UNSEEN
        SEEN
        CAUGHT
        +seen() EncounterStatus
        +caught() EncounterStatus
        +isSeen() boolean
    }
    Pokedex o-- "*" PokemonEntry
    PokemonEntry --> PokemonType
    TrainerProgress --> Pokedex : refers to
    TrainerProgress ..> EncounterStatus
```

**Problema.** L'applicazione deve sapere quali specie l'allenatore ha avvistato o catturato (RF2–RF6). A differenza
delle specie, questa informazione cambia nel tempo, è diversa per ogni allenatore e deve rispettare le regole
V9–V11 dell'analisi.

**Soluzione.** Lo stato di incontro sta in un'entità separata, `TrainerProgress`, riferita a un `Pokedex`.
Così `PokemonEntry` e `Pokedex` restano **immutabili**: descrivono com'è fatta una specie, non a che punto è un
allenatore.
Le regole sull'avanzamento (V10) stanno nell'enum `EncounterStatus`. `seen()` e `caught()` restituiscono lo stato
successivo, e non tornano mai indietro. `TrainerProgressImpl` si limita a memorizzare lo stato delle specie incontrate
e a rifiutare quelle che non appartengono al suo Pokédex (V9). Le regole stanno così in un solo punto, e si
verificano senza costruire né un Pokédex né i progressi.

Il prezzo della separazione è un oggetto in più da creare e passare a chi lo usa: `PokedexApp` crea entrambi,
e il controller li riceve entrambi.

## 2. Evoluzioni e condizioni di evoluzione

```mermaid
classDiagram
    direction LR
    class Pokedex {
        <<interface>>
        +getPreEvolution(PokemonEntry entry) Optional~Evolution~
        +getPostEvolutions(PokemonEntry entry) List~Evolution~
    }
    class Evolution {
        <<record>>
        +from() PokemonEntry
        +to() PokemonEntry
        +condition() EvolutionCondition
    }
    class PokemonEntry {
        <<interface>>
    }
    class EvolutionCondition {
        <<interface>>
    }
    class LevelUp {
        <<record>>
        +level() int
    }
    class UseItem {
        <<record>>
        +item() String
    }
    class Trade {
        <<record>>
    }
    Pokedex o-- "*" Evolution
    Evolution --> "2" PokemonEntry : from / to
    Evolution --> EvolutionCondition
    EvolutionCondition <|.. LevelUp
    EvolutionCondition <|.. UseItem
    EvolutionCondition <|.. Trade
```

**Problema.** Un'evoluzione collega due specie e ha dati propri: una condizione, che può essere di tre tipi con dati
diversi (un livello, uno strumento, nulla). Le evoluzioni formano catene da navigare nei due versi, e devono
rispettare vincoli che riguardano l'insieme (V5, V6, V8), non una sola evoluzione.

**Soluzione.** `Evolution` è un record immutabile `(from, to, condition)`. Le specie **non conoscono** le proprie
evoluzioni: è il `Pokedex` a contenerle tutte e a ricavare entrambi i versi, con `getPreEvolution` e
`getPostEvolutions`. Ogni relazione è quindi memorizzata una sola volta, e il grafo si costruisce in modo semplice:
prima le specie, poi le evoluzioni.
I vincoli sull'insieme sono verificati dal costruttore di `PokedexImpl`, l'unico oggetto che vede tutte le specie e
tutte le evoluzioni. Così un `Pokedex` che esiste è sempre valido. I vincoli su una singola evoluzione (V3, V7) li
verificano invece i costruttori dei record.

Le condizioni sono un'interfaccia, `EvolutionCondition`, con un'implementazione per ogni tipo: `LevelUp`, `UseItem` e
`Trade`. Ciascuna ha solo i propri dati e verifica i propri vincoli: un livello tra 1 e 100, uno strumento con un
nome (V7).
