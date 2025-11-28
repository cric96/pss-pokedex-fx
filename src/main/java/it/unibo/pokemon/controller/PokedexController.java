package it.unibo.pokemon.controller;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonType;

import java.io.InputStream;
import java.util.Optional;

/**
 * The controller of the pokedex.
 */
public interface PokedexController {

    /**
     * Shows a pokemon.
     * @param index the index of the pokemon to show
     */
    void detailsForPokemon(int index);

    /**
     * Searches a pokemon by name.
     * @param name the name of the pokemon to search
     */
    void searchPokemon(String name);

    /**
     * Searches pokemons by type and displays them in the list.
     * @param type the type of pokemons to search
     */
    void searchPokemonByType(PokemonType type);

    /**
     * Shows all pokemons in the list (resets any search filter).
     */
    void showAllPokemons();

    /**
     * Gets the currently displayed pokemon.
     * @return an Optional containing the current pokemon, or empty if none
     */
    Optional<PokemonEntry> getCurrentPokemon();

    /**
     * Gets the current pokemon index.
     * @return the current pokemon index
     */
    int getCurrentPokemonIndex();

    /**
     * Loads the image stream for a pokemon from the model.
     * @param entry the pokemon entry
     * @return the input stream for the pokemon image, or null if not found
     */
    InputStream loadPokemonImageStream(PokemonEntry entry);
}
