package it.unibo.pokemon.view.demo;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.core.EncounterStatus;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonType;
import it.unibo.pokemon.view.DisplayedPokemon;
import it.unibo.pokemon.view.PokedexView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * A fake controller with a few pokemon in memory, used to develop and try the view
 * while the model and the real controller are not available yet.
 */
final class StubController implements PokedexController {

    private final PokedexView view;
    private final List<PokemonEntry> pokemons = List.of(
        new MockPokemonEntry(1, "Bulbasaur", "GRASS"),
        new MockPokemonEntry(4, "Charmander", "FIRE"),
        new MockPokemonEntry(6, "Charizard", "FIRE"),
        new MockPokemonEntry(7, "Squirtle", "WATER")
    );
    private final Map<PokemonEntry, EncounterStatus> statuses = new HashMap<>(Map.of(
        pokemons.get(0), EncounterStatus.CAUGHT,
        pokemons.get(1), EncounterStatus.SEEN
    ));
    private PokemonEntry current;

    StubController(final PokedexView view) {
        this.view = view;
    }

    @Override
    public void start() {
        showAllPokemons();
        detailsForPokemon(pokemons.getFirst().getIndex());
    }

    @Override
    public void detailsForPokemon(final int index) {
        pokemons.stream()
            .filter(pokemon -> pokemon.getIndex() == index)
            .findFirst()
            .ifPresent(this::select);
    }

    @Override
    public void searchPokemon(final String name) {
        pokemons.stream()
            .filter(pokemon -> pokemon.getName().equalsIgnoreCase(name))
            .findFirst()
            .ifPresentOrElse(this::select, () -> view.showError("Not found: " + name));
    }

    @Override
    public void searchPokemonByType(final PokemonType type) {
        showAllPokemons();
    }

    @Override
    public void showAllPokemons() {
        view.showPokemons(pokemons.stream().map(this::toDisplayed).toList());
    }

    @Override
    public void markAsSeen(final int index) {
        update(index, EncounterStatus::seen);
    }

    @Override
    public void markAsCaught(final int index) {
        update(index, EncounterStatus::caught);
    }

    @Override
    public Optional<PokemonEntry> getCurrentPokemon() {
        return Optional.ofNullable(current);
    }

    @Override
    public int getCurrentPokemonIndex() {
        return getCurrentPokemon().map(PokemonEntry::getIndex).orElse(-1);
    }

    private void update(final int index, final UnaryOperator<EncounterStatus> transition) {
        pokemons.stream()
            .filter(pokemon -> pokemon.getIndex() == index)
            .findFirst()
            .ifPresent(pokemon -> {
                statuses.put(pokemon, transition.apply(statuses.getOrDefault(pokemon, EncounterStatus.UNSEEN)));
                showAllPokemons();
                select(pokemon);
            });
    }

    private void select(final PokemonEntry pokemon) {
        current = pokemon;
        view.showPokemon(toDisplayed(pokemon));
    }

    private DisplayedPokemon toDisplayed(final PokemonEntry pokemon) {
        return new DisplayedPokemon(pokemon, statuses.getOrDefault(pokemon, EncounterStatus.UNSEEN));
    }
}
