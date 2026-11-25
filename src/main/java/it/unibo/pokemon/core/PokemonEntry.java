package it.unibo.pokemon.core;

import java.util.Optional;

/**
 * An entry of the pokedex, i.e., the (immutable) description of a pokemon species.
 * It knows neither its evolutions (see {@link Evolution}) nor the progress of the trainer
 * (see {@link TrainerProgress}).
 */
public interface PokemonEntry {
    int getIndex();
    String getName();
    String getDescription();
    double getWeight();
    double getHeight();
    PokemonType getPrimaryType();
    Optional<PokemonType> getSecondaryType();
    String getImageUrl();
}
