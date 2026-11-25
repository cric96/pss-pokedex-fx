package it.unibo.pokemon.view.panel;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.view.DisplayedPokemon;

import java.util.function.Consumer;

public interface PokemonDetailPanel extends Panel {
    /**
     * Sets the pokemon to display.
     * @param pokemon the pokemon to display
     */
    void setPokemon(DisplayedPokemon pokemon);

    /**
     * Sets the callback for when the user marks the displayed pokemon as seen.
     * @param listener the listener to call with the displayed pokemon
     */
    void setOnMarkAsSeen(Consumer<PokemonEntry> listener);

    /**
     * Sets the callback for when the user marks the displayed pokemon as caught.
     * @param listener the listener to call with the displayed pokemon
     */
    void setOnMarkAsCaught(Consumer<PokemonEntry> listener);
}
