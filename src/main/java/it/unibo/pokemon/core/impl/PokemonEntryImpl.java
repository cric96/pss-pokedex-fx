package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonEntryFull;
import it.unibo.pokemon.core.PokemonType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PokemonEntryImpl extends PokemonEntryBaseImpl implements PokemonEntryFull {
    private final String name;
    private final String description;
    private final double weight;
    private final double height;
    private final PokemonType primaryType;
    private final PokemonType secondaryType;
    private final PokemonEntry preEvolution;
    private final List<PokemonEntry> postEvolutions;

    public PokemonEntryImpl(
        final int index,
        final String name,
        final String description,
        final double weight,
        final double height,
        final PokemonType primaryType,
        final PokemonType secondaryType,
        final PokemonEntry preEvolution,
        final List<PokemonEntry> postEvolutions
    ) {
        super(index);
        if(postEvolutions.contains(this)) {
            throw new IllegalArgumentException("the post evolution cannot contains myself");
        }
        if(preEvolution == this) {
            throw new IllegalArgumentException("The pre evolution cannot be the same of this pokemon!");
        }
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.height = height;
        this.primaryType = primaryType;
        if(primaryType != secondaryType) {
            this.secondaryType = secondaryType;
        } else {
            this.secondaryType = null;
        }
        this.preEvolution = preEvolution;
        this.postEvolutions = Collections.unmodifiableList(postEvolutions);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public PokemonType getPrimaryType() {
        return this.primaryType;
    }

    @Override
    public Optional<PokemonType> getSecondaryType() {
        return Optional.ofNullable(this.secondaryType);
    }

    @Override
    public Optional<PokemonEntry> getPreEvolution() {
        return Optional.ofNullable(this.preEvolution);
    }

    @Override
    public List<PokemonEntry> getPostEvolution() {
        return this.postEvolutions;
    }
}
