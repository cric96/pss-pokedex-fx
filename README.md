# Pokédex FX

Esempio guidato di **analisi, progettazione e implementazione** di una piccola applicazione desktop in Java + JavaFX.

## Il problema

Nel mondo dei Pokémon, un allenatore porta con sé un **Pokédex**: un'enciclopedia tascabile che cataloga tutte le specie
di Pokémon esistenti e tiene traccia di quali l'allenatore ha già **avvistato** durante il suo viaggio.

Vogliamo realizzare un'applicazione che riproduca questo strumento.

Ogni specie di Pokémon è descritta da:

- un **numero** che la identifica nel Pokédex (es. `#001`);
- un **nome** (es. *Bulbasaur*);
- una breve **descrizione**;
- uno o due **tipi** elementali (es. *Erba* / *Veleno*);
- **altezza** e **peso**;
- un'**immagine**;
- le sue **relazioni evolutive**: un Pokémon può evolversi da un altro (es. *Ivysaur* si evolve da *Bulbasaur*)
  e può evolversi in uno o più altri.

L'utente deve poter **sfogliare** l'elenco completo dei Pokémon in ordine di numero, **selezionarne** uno per vederne la
scheda di dettaglio e **cercarlo** per nome.
Come nel Pokédex originale, però, le informazioni di un Pokémon **non ancora avvistato** devono rimanere nascoste:
di lui si conosce solo il numero, mentre nome, tipi, descrizione e immagine sono mostrati come `???`.
L'utente può infine **registrare l'avvistamento** di un Pokémon, rendendone visibili tutte le informazioni.

Il catalogo delle specie, compreso lo stato iniziale degli avvistamenti, viene caricato da un file JSON distribuito
con l'applicazione (`src/main/resources/it/unibo/pokemon/pokedex.json`).

## Percorso

L'esempio segue le fasi classiche dello sviluppo:

1. **Analisi**: cosa deve fare il sistema e quali sono le entità del dominio → [`docs/analisi.md`](docs/analisi.md)
2. **Progettazione**: come organizzare il sistema, partendo dal modello del dominio (`it.unibo.pokemon.core`)
   e arrivando all'architettura MVC (model / view / controller)
3. **Implementazione**: realizzazione in Java, test con JUnit e interfaccia grafica con JavaFX

I branch del repository:

- `main`: problema, analisi e **modello del dominio** (con i relativi test): il punto di partenza
- `solution`: l'applicazione completa, con controller e interfaccia JavaFX

## Compilazione ed esecuzione

Serve un JDK 25 (Gradle lo scarica automaticamente se non presente).

```bash
./gradlew test   # esegue i test
./gradlew run    # avvia l'applicazione (branch solution)
```
