package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonEntryBuilder;
import it.unibo.pokemon.core.PokemonType;

public class PokemonEntryBuilderImpl implements PokemonEntryBuilder {
    private int index;
    private String name;
    private String description = "";
    private PokemonType primaryType;
    private PokemonType secondaryType;
    private Integer preEvolutionIndex;
    private double weight;
    private double height;
    private String imageUrl;

    @Override
    public PokemonEntryBuilder withIndex(final int index) {
        this.index = index;
        return this;
    }

    @Override
    public PokemonEntryBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public PokemonEntryBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    @Override
    public PokemonEntryBuilder withPrimaryType(final PokemonType type) {
        this.primaryType = type;
        return this;
    }

    @Override
    public PokemonEntryBuilder withSecondaryType(final PokemonType type) {
        this.secondaryType = type;
        return this;
    }

    @Override
    public PokemonEntryBuilder withPreEvolution(final int preEvolutionIndex) {
        this.preEvolutionIndex = preEvolutionIndex;
        return this;
    }

    @Override
    public PokemonEntryBuilder withWeight(final double weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public PokemonEntryBuilder withHeight(final double height) {
        this.height = height;
        return this;
    }

    @Override
    public PokemonEntryBuilder withImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Override
    public PokemonEntry build() {
        return new PokemonEntryImpl(
            index, name, description, weight, height, primaryType, secondaryType, preEvolutionIndex, imageUrl
        );
    }
}
