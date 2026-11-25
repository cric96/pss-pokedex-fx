# Design dettagliato

## 1. La view

```mermaid
classDiagram
    direction TB
    class PokedexView {
        <<interface>>
        +setController(PokedexController controller)
        +showPokemons(List~DisplayedPokemon~ pokemons)
        +showPokemon(DisplayedPokemon pokemon)
        +showError(String message)
    }
    class PokedexController {
        <<interface>>
        +detailsForPokemon(int index)
        +searchPokemon(String name)
        +markAsSeen(int index)
        +markAsCaught(int index)
    }
    class PokedexViewImpl
    class Panel {
        <<interface>>
        +getRoot() Region
    }
    class PokemonListPanel {
        <<interface>>
        +setPokemons(List~DisplayedPokemon~ pokemons)
        +selectPokemon(DisplayedPokemon pokemon)
        +setOnPokemonSelected(Consumer~DisplayedPokemon~ listener)
        +setOnSearch(Consumer~String~ listener)
    }
    class PokemonDetailPanel {
        <<interface>>
        +setPokemon(DisplayedPokemon pokemon)
        +setOnMarkAsSeen(Consumer~PokemonEntry~ listener)
        +setOnMarkAsCaught(Consumer~PokemonEntry~ listener)
    }
    class DisplayedPokemon {
        <<record>>
        +entry() PokemonEntry
        +status() EncounterStatus
    }
    PokedexViewImpl ..|> PokedexView
    PokedexViewImpl --> PokedexController : forwards user actions
    PokedexViewImpl *-- PokemonListPanel
    PokedexViewImpl *-- PokemonDetailPanel
    PokemonListPanel --|> Panel
    PokemonDetailPanel --|> Panel
    PokedexView ..> DisplayedPokemon : shows
```

**Problema.** La view deve poter essere sostituita senza modificare il controller (architettura), e la finestra ha
due aree con comportamenti diversi: l'elenco con la ricerca e la scheda di dettaglio. Se un'unica classe le gestisse
entrambe e parlasse con il controller, diventerebbe grande e difficile da modificare.

**Soluzione.** Il controller conosce solo `PokedexView`. La sua implementazione JavaFX, `PokedexViewImpl`, è composta
da due pannelli, ciascuno dietro un'interfaccia: `PokemonListPanel` e `PokemonDetailPanel`. Entrambe estendono `Panel`,
che espone il nodo grafico da inserire nella finestra.
I pannelli **non conoscono il controller**: offrono metodi per registrare delle callback (`setOnPokemonSelected`,
`setOnSearch`, `setOnMarkAsSeen`, `setOnMarkAsCaught`). È `PokedexViewImpl`, in `setController`, a collegare ogni
callback al metodo corrispondente del controller. Ogni pannello si può quindi modificare o riusare indipendentemente
dal controller.
La view riceve tutto ciò che deve mostrare come `DisplayedPokemon` (una specie e il suo stato di incontro): decide
*come* nascondere un Pokémon non avvistato, ma non interroga il model per sapere *se* nasconderlo.

A differenza di `PokedexView`, le interfacce dei pannelli dipendono da JavaFX (`getRoot` restituisce un nodo
grafico). Non è un problema, perché sono un dettaglio interno all'implementazione JavaFX: sostituendo la view, si
sostituiscono anche loro.

**Pattern.**

**Observer**, per comunicare le azioni dell'utente dai pannelli al controller:

```mermaid
classDiagram
    direction LR
    class PokemonListPanel {
        <<interface>>
        +setOnPokemonSelected(Consumer~DisplayedPokemon~ listener)
        +setOnSearch(Consumer~String~ listener)
    }
    class PokemonDetailPanel {
        <<interface>>
        +setOnMarkAsSeen(Consumer~PokemonEntry~ listener)
        +setOnMarkAsCaught(Consumer~PokemonEntry~ listener)
    }
    class Consumer~T~ {
        <<interface>>
        +accept(T value)
    }
    class PokedexViewImpl {
        +setController(PokedexController controller)
    }
    class PokedexController {
        <<interface>>
        +detailsForPokemon(int index)
        +searchPokemon(String name)
        +markAsSeen(int index)
        +markAsCaught(int index)
    }
    PokemonListPanel --> Consumer : notifies
    PokemonDetailPanel --> Consumer : notifies
    PokedexViewImpl ..> Consumer : registers lambdas
    PokedexViewImpl --> PokedexController : the lambdas call
```

- gli *observable* sono i pannelli, `PokemonListPanel` e `PokemonDetailPanel`: i metodi `setOn...` registrano un
  observer per ciascun evento (selezione, ricerca, avvistamento, cattura);
- l'interfaccia dell'*observer* è `Consumer<T>` della libreria Java, e `accept` è il metodo di notifica;
- gli *observer* concreti sono le lambda che `PokedexViewImpl` crea e registra in `setController`. Ognuna chiama il
  metodo corrispondente del controller, per esempio `pokemon -> controller.markAsCaught(pokemon.getIndex())`.

Quando l'utente preme "Cattura", il pannello di dettaglio chiama `accept` sul suo observer, che a sua volta chiama
`markAsCaught` sul controller. Il pannello non sa chi lo osserva.
Ogni evento ha un solo observer: è la forma più semplice del pattern, spesso chiamata *callback*.
