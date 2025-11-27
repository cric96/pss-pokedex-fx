package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;

import java.util.Objects;

public class PokemonEntryBaseImpl implements PokemonEntry {
    private final int index;

    public PokemonEntryBaseImpl(final int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PokemonEntryBaseImpl that = (PokemonEntryBaseImpl) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(index);
    }
}
