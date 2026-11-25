# Pokédex FX

Nel mondo dei Pokémon, ogni allenatore porta con sé un **Pokédex**: un'enciclopedia tascabile che cataloga tutte le
specie di Pokémon esistenti e registra i **progressi** dell'allenatore, cioè quali specie ha già **avvistato** e quali
ha **catturato**.

Vogliamo realizzare un'applicazione desktop che riproduca questo strumento.

Ogni specie di Pokémon è descritta da:

- un **numero** che la identifica nel Pokédex (es. `#001`);
- un **nome** (es. *Bulbasaur*);
- una breve **descrizione**;
- uno o due **tipi** elementali (es. *Erba* / *Veleno*);
- **altezza** e **peso**;
- un'**immagine**.

Le specie sono legate tra loro dalle **evoluzioni**: una specie si evolve in un'altra quando si verifica una certa
**condizione**. Può essere il raggiungimento di un livello (Charmander si evolve in Charmeleon al livello 16), l'uso di
uno strumento (Pikachu si evolve in Raichu con la Pietratuono) oppure uno scambio con un altro allenatore.
Una specie si evolve da al più un'altra, ma può evolversi in più specie diverse (Eevee si evolve in Vaporeon, Jolteon
o Flareon, a seconda della pietra usata).
Le evoluzioni formano così delle **catene** (Charmander, Charmeleon e poi Charizard) che non possono mai chiudersi su sé
stesse. Lungo una catena, i livelli richiesti crescono.

L'utente deve poter **sfogliare** l'elenco completo dei Pokémon in ordine di numero, 
**selezionarne** uno per vederne la  scheda di dettaglio e **cercarlo** per nome.
Come nel Pokédex dei giochi, 
le informazioni di un Pokémon **non ancora avvistato** devono rimanere nascoste:
di lui si conosce solo il numero, mentre nome, tipi, descrizione e immagine sono mostrati come `???`.
L'utente può **registrare l'avvistamento** o **la cattura** di un Pokémon. 
Catturarne uno significa anche averlo  avvistato, e quanto registrato non si può annullare.

Il catalogo delle specie, con le loro evoluzioni, 
è fisso e viene caricato da un file distribuito con l'applicazione. 
I progressi dell'allenatore, invece, partono da zero a ogni avvio.
