package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
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
            .filter(pokemonEntry -> pokemonEntry.getName().equals(name))
            .findAny();
    }

    @Override
    public List<PokemonEntry> searchPokemonByType(PokemonType type) {
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
}
