package it.unibo.pokemon.view;

import it.unibo.pokemon.controller.PokedexController;

import java.util.List;

/**
 * The view of the pokedex.
 */
public interface PokedexView {

    /**
     * Sets the controller to which user actions are forwarded.
     * It only wires the events: it has no other side effect.
     * @param controller the controller to set
     */
    void setController(PokedexController controller);

    /**
     * Gets the controller.
     * @return the controller
     */
    PokedexController getController();

    /**
     * Makes the view visible.
     */
    void show();

    /**
     * Shows the details of a pokemon and selects it in the list.
     * @param pokemon the pokemon to show
     */
    void showPokemon(DisplayedPokemon pokemon);

    /**
     * Shows a list of pokemons.
     * @param pokemons the list of pokemons to show
     */
    void showPokemons(List<DisplayedPokemon> pokemons);

    /**
     * Shows an error.
     * @param message the error message
     */
    void showError(String message);
}
