package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonEntryFull;
import it.unibo.pokemon.core.PokemonType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PokedexImpl implements Pokedex {
    private final Set<PokemonEntry> pokemon;

    public PokedexImpl(final Set<PokemonEntry> pokemon) {
        this.pokemon = pokemon;
    }
    @Override
    public int getTotalPokemonNumber() {
        return pokemon.size();
    }

    @Override
    public Set<PokemonEntry> getAllPokemon() {
        return Set.copyOf(pokemon);
    }

    @Override
    public Optional<PokemonEntry> searchPokemonByName(String name) {
        return pokemon.stream()
            .filter(pokemon -> pokemon instanceof PokemonEntryFull)
            .map(pokemonEntry -> (PokemonEntryFull) pokemonEntry)
            .filter(pokemonEntry -> pokemonEntry.getName().equals(name))
            .findAny()
            .map(pokemonEntry -> (PokemonEntry) pokemonEntry);
    }

    @Override
    public List<PokemonEntry> searchPokemonByType(PokemonType type) {
        return pokemon.stream()
            .filter(pokemon -> pokemon instanceof PokemonEntryFull)
            .map(pokemonEntry -> (PokemonEntryFull) pokemonEntry)
            .filter(pokemonEntryFull -> pokemonEntryFull.getPrimaryType().equals(type))
            .map(pokemonEntry -> (PokemonEntry) pokemonEntry)
            .toList();
    }
}
