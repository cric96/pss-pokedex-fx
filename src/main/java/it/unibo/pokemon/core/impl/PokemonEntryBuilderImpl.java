package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonEntryBuilder;
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
    private final List<PokemonEntry> postEvolution;
    private double weight;
    private double height;
    private boolean seen;
    private String imageUrl;

    public PokemonEntryBuilderImpl() {
        this.postEvolution = new ArrayList<>();
    }

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
    public PokemonEntryBuilder withPreEvolution(final PokemonEntry pokemonEntry) {
        this.preEvolution = pokemonEntry;
        return this;
    }

    @Override
    public PokemonEntryBuilder withPostEvolution(final PokemonEntry pokemonEntry) {
        this.postEvolution.add(pokemonEntry);
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
    public PokemonEntryBuilder isSeen() {
        this.seen = true;
        return this;
    }

    @Override
    public PokemonEntryBuilder withImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Override
    public PokemonEntry build() {
        var entry = new PokemonEntryImpl(
            index, name, description, weight, height, primaryType, secondaryType, preEvolution, postEvolution, imageUrl, seen
        );
        return entry;
    }
}
