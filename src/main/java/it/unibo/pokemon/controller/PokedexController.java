package it.unibo.pokemon.controller;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonType;

import java.util.Optional;

/**
 * The controller of the pokedex.
 */
public interface PokedexController {

    /**
     * Starts the interaction: shows all the pokemons and the details of the first one.
     * It must be called once, after the controller has been attached to the view.
     */
    void start();

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
     * Records that the user has seen a pokemon, and updates the view accordingly.
     * @param index the index of the seen pokemon
     */
    void markAsSeen(int index);

    /**
     * Records that the user has caught a pokemon, and updates the view accordingly.
     * @param index the index of the caught pokemon
     */
    void markAsCaught(int index);

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
}
