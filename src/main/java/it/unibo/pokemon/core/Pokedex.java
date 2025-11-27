package it.unibo.pokemon.core;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Pokedex {
    int getTotalPokemonNumber();
    Set<PokemonEntry> getAllPokemon();
    Optional<PokemonEntry> searchPokemonByName(String name);
    List<PokemonEntry> searchPokemonByType(PokemonType type);
}
