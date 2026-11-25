package it.unibo.pokemon.core;

import java.util.Optional;

/**
 * An entry of the pokedex, i.e., the (immutable) description of a pokemon species.
 * It does not contain any state of the user (e.g., whether the pokemon has been seen):
 * that information belongs to the {@link Pokedex}.
 */
public interface PokemonEntry {
    int getIndex();
    String getName();
    String getDescription();
    double getWeight();
    double getHeight();
    PokemonType getPrimaryType();
    Optional<PokemonType> getSecondaryType();

    /**
     * The evolutionary relation is stored only once, as a reference (by index) to the pre-evolution.
     * Post-evolutions are derived by the {@link Pokedex}, so the two directions can never be inconsistent.
     * @return the index of the pokemon this one evolves from, if any
     */
    Optional<Integer> getPreEvolutionIndex();

    String getImageUrl();

    static PokemonEntryBuilder builder() {
        return new it.unibo.pokemon.core.impl.PokemonEntryBuilderImpl();
    }
}
