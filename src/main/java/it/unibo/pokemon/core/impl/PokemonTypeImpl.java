package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonType;

public enum PokemonTypeImpl implements PokemonType {
    NORMAL("Normal"),
    FIRE("Fire"),
    WATER("Water"),
    GRASS("Grass"),
    ELECTRIC("Electric"),
    ICE("Ice"),
    FIGHTING("Fighting"),
    POISON("Poison"),
    GROUND("Ground"),
    FLYING("Flying"),
    PSYCHIC("Psychic"),
    BUG("Bug"),
    ROCK("Rock"),
    GHOST("Ghost"),
    DRAGON("Dragon"),
    DARK("Dark"),
    STEEL("Steel"),
    FAIRY("Fairy");

    private final String name;

    PokemonTypeImpl(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
