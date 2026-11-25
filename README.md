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

## Cosa dobbiamo fare

Seguiamo le fasi classiche dello sviluppo: prima capiamo *cosa* serve, poi decidiamo *come* organizzarlo, solo alla
fine scriviamo il codice dell'interfaccia.

### 1. Analisi

Leggere il problema e ricavarne i requisiti e il modello del dominio: le entità, le loro relazioni, i vincoli che
devono sempre valere e gli aspetti critici.
Il risultato è in [`docs/analisi.md`](docs/analisi.md).
Prima di proseguire, controlliamo che ogni requisito funzionale si appoggi a qualcosa del dominio.
Per esempio, "nascondere i Pokémon non avvistati" richiede che il dominio sappia *chi* è stato avvistato.

### 2. Progettazione del dominio

Tradurre il modello del dominio in interfacce Java (package `it.unibo.pokemon.core`), indipendenti da grafica e file:

- `PokemonEntry`: la voce di una specie, **immutabile**;
- `PokemonType`: i tipi elementali;
- `Pokedex`: il catalogo; offre le ricerche, ricava le evoluzioni e tiene traccia degli avvistamenti;
- `PokedexLoader`: costruisce un `Pokedex` a partire dal file JSON.

Ogni vincolo individuato in analisi diventa un controllo nel codice **e** un test.
Su `main` questa fase è già svolta: è il punto di partenza.

### 3. Progettazione dell'applicazione (MVC)

Decidere chi fa cosa, separando tre ruoli:

- **Model**: il `Pokedex`. Non sa nulla di come verrà mostrato.
- **View** (`PokedexView`): passiva. Mostra ciò che le viene detto (un elenco di Pokémon, la scheda di un Pokémon,
  un messaggio di errore) e inoltra al controller le azioni dell'utente. È composta da due pannelli: l'elenco con la
  barra di ricerca e la scheda di dettaglio. Decide *come* nascondere un Pokémon non avvistato (`???`), ma non *se*
  nasconderlo: questa informazione le arriva dal controller.
- **Controller** (`PokedexController`): riceve le azioni dell'utente (seleziona un Pokémon, cerca per nome, segna come
  avvistato), interroga o aggiorna il model e dice alla view cosa mostrare.

Per ogni requisito funzionale va descritta l'interazione tra i tre. Per esempio, la selezione di un Pokémon (RF3):

> l'utente seleziona un Pokémon nell'elenco → la view chiama il controller con il numero selezionato →
> il controller lo cerca nel `Pokedex` e chiede se è stato avvistato → il controller chiede alla view di mostrare la
> scheda, indicando se il Pokémon è avvistato

Regole da rispettare:

- la view non conosce le classi concrete del model, e il controller non conosce JavaFX: deve essere testabile con una
  view finta;
- i costruttori non hanno effetti collaterali: costruire il controller non deve aggiornare la view;
- un solo punto del programma (la *composition root*) crea model, view e controller e li collega.

### 4. Implementazione

1. Il controller, con i suoi test (usando una view finta che registra cosa le viene chiesto di mostrare).
2. La view in JavaFX: i due pannelli e la finestra che li contiene.
3. La composition root e il `Launcher`, poi una verifica dell'applicazione a mano, requisito per requisito.

### 5. Estensioni (facoltative)

I requisiti opzionali dell'analisi: filtro per tipo (RFO1) e catena evolutiva nella scheda (RFO2).
Per ciascuno conviene chiedersi quale fase va toccata: il dominio li supporta già, o servono nuove operazioni?

## Branch

- `main`: problema, analisi e **modello del dominio** (con i relativi test): il punto di partenza
- `solution`: l'applicazione completa, con controller e interfaccia JavaFX

## Compilazione ed esecuzione

Serve un JDK 25 (Gradle lo scarica automaticamente se non presente).

```bash
./gradlew test   # esegue i test
./gradlew run    # avvia l'applicazione (branch solution)
```
