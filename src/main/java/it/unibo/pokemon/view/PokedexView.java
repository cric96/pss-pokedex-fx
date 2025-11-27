package it.unibo.pokemon.view;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.core.PokemonEntry;

import java.util.List;

/**
 * The view of the pokedex.
 */
public interface PokedexView {

    /**
     * Sets the controller.
     * @param controller the controller to set
     */
    void setController(PokedexController controller);

    /**
     * Gets the controller.
     * @return the controller
     */
    PokedexController getController();

    /**
     * Shows a pokemon.
     * @param entry the pokemon to show
     */
    void showPokemon(PokemonEntry entry);

    /**
     * Shows a list of pokemons.
     * @param entries the list of pokemons to show
     */
    void showPokemons(List<PokemonEntry> entries);

    /**
     * Shows an error.
     * @param message the error message
     */
    void showError(String message);
}
