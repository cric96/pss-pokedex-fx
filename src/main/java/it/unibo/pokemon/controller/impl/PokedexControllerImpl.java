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

    public PokedexControllerImpl(final Pokedex model, final PokedexView view) {

    }

    @Override
    public void detailsFor(final int index) {

    }

    @Override
    public void searchPokemon(final String name) {

    }

    @Override
    public void searchPokemonByType(final PokemonType type) {

    }

    @Override
    public void showAllPokemons() {

    }

    @Override
    public Optional<PokemonEntry> getCurrentPokemon() {
        return Optional.ofNullable(null);
    }

    @Override
    public int getCurrentPokemonIndex() {
        return 1;
    }

    @Override
    public InputStream loadPokemonImageStream(final PokemonEntry entry) {
        final String imageUrl = entry.getImageUrl();
        return getClass().getResourceAsStream(imageUrl);
    }
}
