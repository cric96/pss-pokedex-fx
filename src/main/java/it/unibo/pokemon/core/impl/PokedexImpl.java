package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.Evolution;
import it.unibo.pokemon.core.EvolutionCondition;
import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonType;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class PokedexImpl implements Pokedex {
    private final Set<PokemonEntry> pokemon;
    private final Set<Evolution> evolutions;
    /** Each species has at most one pre-evolution: we index evolutions by the evolved species. */
    private final Map<PokemonEntry, Evolution> preEvolutions = new HashMap<>();

    /**
     * Creates a pokedex without evolutions.
     * @param pokemon the known pokemon species
     */
    public PokedexImpl(final Collection<PokemonEntry> pokemon) {
        this(pokemon, Set.of());
    }

    /**
     * @param pokemon the known pokemon species
     * @param evolutions the evolutions between them
     * @throws IllegalArgumentException if any constraint of the pokedex is violated
     */
    public PokedexImpl(final Collection<PokemonEntry> pokemon, final Collection<Evolution> evolutions) {
        checkUnique(pokemon);
        this.pokemon = Set.copyOf(pokemon);
        this.evolutions = Set.copyOf(evolutions);
        for (final Evolution evolution : this.evolutions) {
            if (!contains(evolution.from()) || !contains(evolution.to())) {
                throw new IllegalArgumentException(evolution + " involves a pokemon not in the pokedex");
            }
            if (preEvolutions.put(evolution.to(), evolution) != null) {
                throw new IllegalArgumentException(evolution.to() + " has more than one pre-evolution");
            }
        }
        this.pokemon.forEach(this::checkNoCycles);
        this.evolutions.forEach(this::checkIncreasingLevels);
    }

    private static void checkUnique(final Collection<PokemonEntry> pokemon) {
        if (pokemon.stream().map(PokemonEntry::getIndex).distinct().count() != pokemon.size()
            || pokemon.stream().map(PokemonEntry::getName).distinct().count() != pokemon.size()) {
            throw new IllegalArgumentException("Indexes and names must be unique in a pokedex");
        }
    }

    /*
     * Walks back the chain of pre-evolutions: since each species has at most one pre-evolution,
     * the chain is a path, and it is a cycle if it meets an already visited species.
     */
    private void checkNoCycles(final PokemonEntry start) {
        final Set<PokemonEntry> visited = new HashSet<>();
        Optional<PokemonEntry> current = Optional.of(start);
        while (current.isPresent()) {
            if (!visited.add(current.get())) {
                throw new IllegalArgumentException("Evolution cycle involving " + start);
            }
            current = getPreEvolution(current.get()).map(Evolution::from);
        }
    }

    /*
     * Along a chain, two consecutive evolutions by level must require increasing levels
     * (e.g., Charmander -16-> Charmeleon -36-> Charizard).
     */
    private void checkIncreasingLevels(final Evolution evolution) {
        final Optional<Evolution> previous = getPreEvolution(evolution.from());
        if (previous.isPresent()
            && previous.get().condition() instanceof EvolutionCondition.LevelUp previousLevelUp
            && evolution.condition() instanceof EvolutionCondition.LevelUp levelUp
            && levelUp.level() <= previousLevelUp.level()) {
            throw new IllegalArgumentException(evolution + " requires a level not greater than " + previous.get());
        }
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
    public boolean contains(final PokemonEntry entry) {
        return pokemon.contains(entry);
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
    public Optional<Evolution> getPreEvolution(final PokemonEntry entry) {
        checkContained(entry);
        return Optional.ofNullable(preEvolutions.get(entry));
    }

    @Override
    public List<Evolution> getPostEvolutions(final PokemonEntry entry) {
        checkContained(entry);
        return evolutions.stream()
            .filter(evolution -> evolution.from().equals(entry))
            .sorted(Comparator.comparingInt(evolution -> evolution.to().getIndex()))
            .toList();
    }

    private void checkContained(final PokemonEntry entry) {
        if (!contains(entry)) {
            throw new IllegalArgumentException(entry + " does not belong to this pokedex");
        }
    }
}
