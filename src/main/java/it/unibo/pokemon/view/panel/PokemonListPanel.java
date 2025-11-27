package it.unibo.pokemon.view.panel;

import it.unibo.pokemon.core.PokemonEntry;

import java.util.List;
import java.util.function.Consumer;

public interface PokemonListPanel extends Panel {
    /**
     * Sets the list of pokemons to display.
     * @param pokemons the list of pokemons
     */
    void setPokemons(List<PokemonEntry> pokemons);

    /**
     * Sets the callback for when a pokemon is selected.
     * @param listener the listener to call
     */
    void setOnPokemonSelected(Consumer<PokemonEntry> listener);

    /**
     * Sets the callback for when a search is performed.
     * @param listener the listener to call with the search query
     */
    void setOnSearch(Consumer<String> listener);

    /**
     * Clears the search field.
     */
    void clearSearch();

    /**
     * A pokemon has been selected from the controller
     * @param pokemon the pokemon to select
     */
    void selectPokemon(PokemonEntry pokemon);
}
