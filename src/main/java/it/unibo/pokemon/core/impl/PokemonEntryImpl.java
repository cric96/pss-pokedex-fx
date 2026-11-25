package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonType;

import java.util.Objects;
import java.util.Optional;

public final class PokemonEntryImpl implements PokemonEntry {
    private final int index;
    private final String name;
    private final String description;
    private final double weight;
    private final double height;
    private final PokemonType primaryType;
    private final PokemonType secondaryType;
    private final String imageUrl;

    /**
     * @param index the number of the species in the pokedex
     * @param name the name
     * @param description a short description
     * @param weight the weight, in kg
     * @param height the height, in m
     * @param primaryType the primary type
     * @param secondaryType the secondary type, or null if none (ignored if equal to the primary type)
     * @param imageUrl the classpath resource of the image
     */
    public PokemonEntryImpl(
        final int index,
        final String name,
        final String description,
        final double weight,
        final double height,
        final PokemonType primaryType,
        final PokemonType secondaryType,
        final String imageUrl
    ) {
        this.index = index;
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.weight = weight;
        this.height = height;
        this.primaryType = Objects.requireNonNull(primaryType);
        this.secondaryType = primaryType.equals(secondaryType) ? null : secondaryType;
        this.imageUrl = imageUrl;
    }

    @Override
    public int getIndex() {
        return this.index;
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
    public String getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PokemonEntryImpl that = (PokemonEntryImpl) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(index);
    }

    @Override
    public String toString() {
        return "#" + index + " " + name;
    }
}
