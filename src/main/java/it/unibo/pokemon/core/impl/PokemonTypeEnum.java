package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonType;

public enum PokemonTypeEnum implements PokemonType {
    NORMAL, FIRE, WATER, GRASS, ELECTRIC, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DRAGON, STEEL, DARK, FAIRY;

    @Override
    public String getName() {
        return this.name();
    }
}
