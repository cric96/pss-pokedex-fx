package it.unibo.pokemon.core;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The pokedex: the (immutable) catalogue of all the known pokemon species and of their evolutions.
 * It guarantees the constraints that involve several species or evolutions: unique indexes and names,
 * at most one pre-evolution per species, no evolution cycles, increasing levels along a chain.
 */
public interface Pokedex {
    int getTotalPokemonNumber();
    Set<PokemonEntry> getAllPokemon();
    boolean contains(PokemonEntry entry);
    Optional<PokemonEntry> searchPokemonByName(String name);
    List<PokemonEntry> searchPokemonByType(PokemonType type);
    Optional<PokemonEntry> searchPokemonById(int id);
    Optional<Evolution> getPreEvolution(PokemonEntry entry);
    List<Evolution> getPostEvolutions(PokemonEntry entry);
}
