package it.unibo.pokemon.core;

import java.util.Objects;

/**
 * An evolution: the species {@code from} evolves into {@code to} when {@code condition} holds.
 * Evolutions are the edges of a graph whose nodes are the species: the species do not know their evolutions,
 * so the graph can be built from immutable objects without circular references.
 * The constraints involving several evolutions (e.g., no cycles) are checked by the {@link Pokedex}.
 * @param from the species that evolves
 * @param to the species it evolves into
 * @param condition when the evolution happens
 */
public record Evolution(PokemonEntry from, PokemonEntry to, EvolutionCondition condition) {
    public Evolution {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Objects.requireNonNull(condition);
        if (from.equals(to)) {
            throw new IllegalArgumentException(from + " cannot evolve into itself");
        }
    }
}
