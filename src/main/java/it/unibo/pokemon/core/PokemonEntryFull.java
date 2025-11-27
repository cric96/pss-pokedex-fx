package it.unibo.pokemon.core;

import java.util.List;
import java.util.Optional;

public interface PokemonEntryFull extends PokemonEntry {
    String getName();
    String getDescription();
    double getWeight();
    double getHeight();
    PokemonType getPrimaryType();
    Optional<PokemonType> getSecondaryType();
    Optional<PokemonEntry> getPreEvolution();
    List<PokemonEntry> getPostEvolution();
}
