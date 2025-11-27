package it.unibo.pokemon.core;

public interface PokemonEntryBuilder {
    PokemonEntryBuilder setIndex(int index);
    PokemonEntryBuilder setName(String name);
    PokemonEntryBuilder setDescription(String description);
    PokemonEntryBuilder setPrimaryType(PokemonType type);
    PokemonEntryBuilder setSecondaryType(PokemonType type);
    PokemonEntryBuilder addPreEvolution(PokemonEntry pokemonEntry);
    PokemonEntryBuilder addPostEvolution(PokemonEntry pokemonEntry);
    PokemonEntryBuilder setWeight(double weight);
    PokemonEntryBuilder setHeight(double setHeight);
    PokemonEntryFull build();
}
