package it.unibo.pokemon.core;

public interface PokemonEntryBuilder {
    PokemonEntryBuilder withIndex(int index);
    PokemonEntryBuilder withName(String name);
    PokemonEntryBuilder withDescription(String description);
    PokemonEntryBuilder withPrimaryType(PokemonType type);
    PokemonEntryBuilder withSecondaryType(PokemonType type);
    PokemonEntryBuilder withPreEvolution(PokemonEntry pokemonEntry);
    PokemonEntryBuilder withPostEvolution(PokemonEntry pokemonEntry);
    PokemonEntryBuilder withWeight(double weight);
    PokemonEntryBuilder withHeight(double setHeight);
    PokemonEntryBuilder isSeen();
    PokemonEntryBuilder withImageUrl(String imageUrl);
    PokemonEntry build();
}
