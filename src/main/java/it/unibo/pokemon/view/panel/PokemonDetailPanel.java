package it.unibo.pokemon.view.panel;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.core.PokemonEntry;

public interface PokemonDetailPanel extends Panel {
    /**
     * Sets the pokemon to display.
     * @param pokemon the pokemon entry
     */
    void setPokemon(PokemonEntry pokemon);

    /**
     * Sets the controller for loading images.
     * @param controller the controller
     */
    void setController(PokedexController controller);
}
