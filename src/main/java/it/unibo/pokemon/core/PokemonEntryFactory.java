package it.unibo.pokemon.core;

import it.unibo.pokemon.core.impl.PokemonEntryBaseImpl;
import it.unibo.pokemon.core.impl.PokemonEntryBuilderImpl;

public class PokemonEntryFactory {
    private PokemonEntryFactory() {}

    public static PokemonEntry fromIndex(int index) {
        return new PokemonEntryBaseImpl(index);
    }
    public static PokemonEntryBuilder create(int index, String name, String description, double weight, double height) {
        return new PokemonEntryBuilderImpl()
            .setIndex(index)
            .setName(name)
            .setDescription(description)
            .setWeight(weight)
            .setHeight(height);
    }
}
