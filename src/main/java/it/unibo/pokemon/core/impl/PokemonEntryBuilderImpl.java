package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonEntryBuilder;
import it.unibo.pokemon.core.PokemonEntryFull;
import it.unibo.pokemon.core.PokemonType;

import java.util.ArrayList;
import java.util.List;

public class PokemonEntryBuilderImpl implements PokemonEntryBuilder {
    private int index;
    private String name;
    private String description;
    private PokemonType primaryType;
    private PokemonType secondaryType;
    private PokemonEntry preEvolution;
    private List<PokemonEntry> postEvolution;
    private double weight;
    private double height;

    public PokemonEntryBuilderImpl() {
        this.postEvolution = new ArrayList<>();
    }

    @Override
    public PokemonEntryBuilder setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public PokemonEntryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public PokemonEntryBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public PokemonEntryBuilder setPrimaryType(PokemonType type) {
        this.primaryType = type;
        return this;
    }

    @Override
    public PokemonEntryBuilder setSecondaryType(PokemonType type) {
        this.secondaryType = type;
        return this;
    }

    @Override
    public PokemonEntryBuilder addPreEvolution(PokemonEntry pokemonEntry) {
        this.preEvolution = pokemonEntry;
        return this;
    }

    @Override
    public PokemonEntryBuilder addPostEvolution(PokemonEntry pokemonEntry) {
        this.postEvolution.add(pokemonEntry);
        return this;
    }

    @Override
    public PokemonEntryBuilder setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public PokemonEntryBuilder setHeight(double height) {
        this.height = height;
        return this;
    }

    @Override
    public PokemonEntryFull build() {
        return new PokemonEntryImpl(
            index, name, description, weight, height, primaryType, secondaryType, preEvolution, postEvolution
        );
    }
}
