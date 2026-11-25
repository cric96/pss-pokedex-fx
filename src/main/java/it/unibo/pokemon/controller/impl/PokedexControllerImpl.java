package it.unibo.pokemon.controller.impl;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonType;
import it.unibo.pokemon.core.TrainerProgress;
import it.unibo.pokemon.view.DisplayedPokemon;
import it.unibo.pokemon.view.PokedexView;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class PokedexControllerImpl implements PokedexController {

    private final Pokedex pokedex;
    private final TrainerProgress progress;
    private final PokedexView view;
    private List<PokemonEntry> displayedPokemons = List.of();
    private PokemonEntry currentPokemon;

    /**
     * Creates the controller. The constructor has no side effects (it does not touch the view):
     * the interaction begins with {@link #start()}.
     * @param pokedex the pokedex
     * @param progress the progress of the trainer (referring to the same pokedex)
     * @param view the view to update
     */
    public PokedexControllerImpl(final Pokedex pokedex, final TrainerProgress progress, final PokedexView view) {
        this.pokedex = Objects.requireNonNull(pokedex);
        this.progress = Objects.requireNonNull(progress);
        this.view = Objects.requireNonNull(view);
        if (progress.getPokedex() != pokedex) {
            throw new IllegalArgumentException("The progress must refer to the given pokedex");
        }
    }

    @Override
    public void start() {
        showAllPokemons();
        displayedPokemons.stream().findFirst().ifPresent(this::select);
    }

    @Override
    public void detailsForPokemon(final int index) {
        pokedex.searchPokemonById(index).ifPresentOrElse(
            this::select,
            () -> view.showError("Pokemon not found")
        );
    }

    @Override
    public void searchPokemon(final String name) {
        if (name == null || name.isBlank()) {
            showAllPokemons();
            return;
        }
        pokedex.searchPokemonByName(name).ifPresentOrElse(
            this::select,
            () -> view.showError("Pokemon not found with name: " + name)
        );
    }

    @Override
    public void searchPokemonByType(final PokemonType type) {
        if (type == null) {
            showAllPokemons();
            return;
        }
        final List<PokemonEntry> filtered = sortedByIndex(pokedex.searchPokemonByType(type));
        if (filtered.isEmpty()) {
            view.showError("No Pokemon found with type: " + type.getName());
        } else {
            showList(filtered);
            select(filtered.getFirst());
        }
    }

    @Override
    public void showAllPokemons() {
        showList(sortedByIndex(pokedex.getAllPokemon()));
    }

    @Override
    public void markAsSeen(final int index) {
        updateProgress(index, progress::markAsSeen);
    }

    @Override
    public void markAsCaught(final int index) {
        updateProgress(index, progress::markAsCaught);
    }

    private void updateProgress(final int index, final Consumer<PokemonEntry> update) {
        pokedex.searchPokemonById(index).ifPresentOrElse(
            entry -> {
                update.accept(entry);
                showList(displayedPokemons); // the list depends on the progress: refresh it
                select(entry);
            },
            () -> view.showError("Pokemon not found")
        );
    }

    @Override
    public Optional<PokemonEntry> getCurrentPokemon() {
        return Optional.ofNullable(currentPokemon);
    }

    @Override
    public int getCurrentPokemonIndex() {
        return getCurrentPokemon().map(PokemonEntry::getIndex).orElse(-1);
    }

    private void select(final PokemonEntry entry) {
        currentPokemon = entry;
        view.showPokemon(toDisplayed(entry));
    }

    private void showList(final List<PokemonEntry> entries) {
        displayedPokemons = entries;
        view.showPokemons(entries.stream().map(this::toDisplayed).toList());
    }

    private DisplayedPokemon toDisplayed(final PokemonEntry entry) {
        return new DisplayedPokemon(entry, progress.getStatus(entry));
    }

    private static List<PokemonEntry> sortedByIndex(final Collection<PokemonEntry> entries) {
        return entries.stream()
            .sorted(Comparator.comparingInt(PokemonEntry::getIndex))
            .toList();
    }
}
