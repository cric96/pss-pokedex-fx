package it.unibo.pokemon.view;

import it.unibo.pokemon.core.EncounterStatus;
import it.unibo.pokemon.core.PokemonEntry;

/**
 * What the view needs to display a pokemon: its (immutable) species data and how far the trainer has got with it.
 * The view is passive: it does not query the model, it receives everything it has to show.
 * @param entry the pokemon species
 * @param status the encounter status (if UNSEEN, its details must be hidden)
 */
public record DisplayedPokemon(PokemonEntry entry, EncounterStatus status) {

    /**
     * @return true if the details of the pokemon can be shown
     */
    public boolean isSeen() {
        return status.isSeen();
    }
}
