package it.unibo.pokemon.core;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The pokedex: the catalogue of all the known pokemon species,
 * together with the state of the user (which pokemon have been seen).
 */
public interface Pokedex {
    int getTotalPokemonNumber();
    Set<PokemonEntry> getAllPokemon();
    Optional<PokemonEntry> searchPokemonByName(String name);
    List<PokemonEntry> searchPokemonByType(PokemonType type);
    Optional<PokemonEntry> searchPokemonById(int id);

    /**
     * @param entry a pokemon of this pokedex
     * @return the pokemon from which the given one evolves, if any
     */
    Optional<PokemonEntry> getPreEvolution(PokemonEntry entry);

    /**
     * @param entry a pokemon of this pokedex
     * @return the pokemon into which the given one evolves, ordered by index (empty if none)
     */
    List<PokemonEntry> getPostEvolutions(PokemonEntry entry);

    /**
     * @param entry a pokemon of this pokedex
     * @return true if the user has already seen the given pokemon
     */
    boolean isSeen(PokemonEntry entry);

    /**
     * Records that the user has seen the given pokemon.
     * @param entry a pokemon of this pokedex
     * @throws IllegalArgumentException if the pokemon does not belong to this pokedex
     */
    void markAsSeen(PokemonEntry entry);
}
