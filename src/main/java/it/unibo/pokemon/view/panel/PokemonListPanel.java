package it.unibo.pokemon.view.panel;

import it.unibo.pokemon.view.DisplayedPokemon;

import java.util.List;
import java.util.function.Consumer;

public interface PokemonListPanel extends Panel {
    /**
     * Sets the list of pokemons to display.
     * @param pokemons the list of pokemons
     */
    void setPokemons(List<DisplayedPokemon> pokemons);

    /**
     * Sets the callback for when a pokemon is selected.
     * @param listener the listener to call
     */
    void setOnPokemonSelected(Consumer<DisplayedPokemon> listener);

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
     * Selects a pokemon in the list.
     * @param pokemon the pokemon to select
     */
    void selectPokemon(DisplayedPokemon pokemon);
}
