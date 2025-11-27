package it.unibo.pokemon.controller.impl;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonType;
import it.unibo.pokemon.view.PokedexView;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PokedexControllerImpl implements PokedexController {

    private final Pokedex model;
    private final PokedexView view;
    private int currentPokemonIndex = 1;
    private PokemonEntry currentPokemon;

    public PokedexControllerImpl(final Pokedex model, final PokedexView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this);
        showAllPokemons();
    }

    @Override
    public void showPokemon(final int index) {
        currentPokemonIndex = index;
        model.searchPokemonById(index).ifPresentOrElse(
            entry -> {
                currentPokemon = entry;
                view.showPokemon(entry);
            },
            () -> view.showError("Pokemon not found")
        );
    }

    @Override
    public void searchPokemon(final String name) {
        if (name == null || name.isBlank()) {
            showAllPokemons();
            return;
        }
        final Optional<PokemonEntry> pokemon = model.searchPokemonByName(name);
        pokemon.ifPresentOrElse(entry -> {
            currentPokemon = entry;
            currentPokemonIndex = entry.getIndex();
            view.showPokemon(entry);
        }, () -> view.showError("Pokemon not found with name: " + name));

    }

    @Override
    public void searchPokemonByType(final PokemonType type) {
        if (type == null) {
            showAllPokemons();
            return;
        }
        final List<PokemonEntry> filtered = model.searchPokemonByType(type).stream()
                .sorted(Comparator.comparingInt(PokemonEntry::getIndex))
                .toList();
        if (filtered.isEmpty()) {
            view.showError("No Pokemon found with type: " + type.getName());
        } else {
            view.showPokemons(filtered);
            currentPokemon = filtered.get(0);
            currentPokemonIndex = currentPokemon.getIndex();
            view.showPokemon(currentPokemon);
        }
    }

    @Override
    public void showAllPokemons() {
        final List<PokemonEntry> allPokemons = model.getAllPokemon().stream()
                .sorted(Comparator.comparingInt(PokemonEntry::getIndex))
                .toList();
        view.showPokemons(allPokemons);
    }

    @Override
    public Optional<PokemonEntry> getCurrentPokemon() {
        return Optional.ofNullable(currentPokemon);
    }

    @Override
    public int getCurrentPokemonIndex() {
        return currentPokemonIndex;
    }

    @Override
    public InputStream loadPokemonImageStream(final PokemonEntry entry) {
        final String imageUrl = entry.getImageUrl();
        return getClass().getResourceAsStream(imageUrl);
    }
}
