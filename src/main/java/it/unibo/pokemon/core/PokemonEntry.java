package it.unibo.pokemon.core;

import java.util.List;
import java.util.Optional;

public interface PokemonEntry {
    int getIndex();
    String getName();
    String getDescription();
    double getWeight();
    double getHeight();
    PokemonType getPrimaryType();
    Optional<PokemonType> getSecondaryType();
    Optional<PokemonEntry> getPreEvolution();
    List<PokemonEntry> getPostEvolution();
    boolean isSeen();
    void markAsSeen();
    String getImageUrl();
    static PokemonEntryBuilder builder() {
        return new it.unibo.pokemon.core.impl.PokemonEntryBuilderImpl();
    }
}
