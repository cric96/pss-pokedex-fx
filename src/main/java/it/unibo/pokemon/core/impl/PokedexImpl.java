package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonType;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PokedexImpl implements Pokedex {
    private final Set<PokemonEntry> pokemon;
    private final Set<Integer> seen;

    /**
     * Creates a pokedex in which no pokemon has been seen yet.
     * @param pokemon the known pokemon species
     */
    public PokedexImpl(final Collection<PokemonEntry> pokemon) {
        this(pokemon, Set.of());
    }

    /**
     * @param pokemon the known pokemon species
     * @param seenIndexes the indexes of the pokemon already seen by the user
     */
    public PokedexImpl(final Collection<PokemonEntry> pokemon, final Set<Integer> seenIndexes) {
        if (pokemon.stream().map(PokemonEntry::getIndex).distinct().count() != pokemon.size()
            || pokemon.stream().map(PokemonEntry::getName).distinct().count() != pokemon.size()) {
            throw new IllegalArgumentException("Indexes and names must be unique in a pokedex");
        }
        this.pokemon = Set.copyOf(pokemon);
        this.seen = new HashSet<>(seenIndexes);
    }

    @Override
    public int getTotalPokemonNumber() {
        return pokemon.size();
    }

    @Override
    public Set<PokemonEntry> getAllPokemon() {
        return pokemon;
    }

    @Override
    public Optional<PokemonEntry> searchPokemonByName(final String name) {
        return pokemon.stream()
            .filter(pokemonEntry -> pokemonEntry.getName().equals(name))
            .findAny();
    }

    @Override
    public List<PokemonEntry> searchPokemonByType(final PokemonType type) {
        return pokemon.stream()
            .filter(pokemonEntry -> pokemonEntry.getPrimaryType().equals(type)
                || pokemonEntry.getSecondaryType().filter(t -> t.equals(type)).isPresent())
            .toList();
    }

    @Override
    public Optional<PokemonEntry> searchPokemonById(final int id) {
        return pokemon.stream()
            .filter(pokemonEntry -> pokemonEntry.getIndex() == id)
            .findAny();
    }

    @Override
    public Optional<PokemonEntry> getPreEvolution(final PokemonEntry entry) {
        return checkContained(entry).getPreEvolutionIndex().flatMap(this::searchPokemonById);
    }

    @Override
    public List<PokemonEntry> getPostEvolutions(final PokemonEntry entry) {
        checkContained(entry);
        return pokemon.stream()
            .filter(candidate -> candidate.getPreEvolutionIndex().equals(Optional.of(entry.getIndex())))
            .sorted(Comparator.comparingInt(PokemonEntry::getIndex))
            .toList();
    }

    @Override
    public boolean isSeen(final PokemonEntry entry) {
        return seen.contains(checkContained(entry).getIndex());
    }

    @Override
    public void markAsSeen(final PokemonEntry entry) {
        seen.add(checkContained(entry).getIndex());
    }

    private PokemonEntry checkContained(final PokemonEntry entry) {
        if (!pokemon.contains(entry)) {
            throw new IllegalArgumentException(entry + " does not belong to this pokedex");
        }
        return entry;
    }
}
