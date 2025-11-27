package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonType;

public enum PokemonTypeEnum implements PokemonType {
    GRASS, FIRE, WATER, POISON;

    @Override
    public String getName() {
        return this.name();
    }
}
