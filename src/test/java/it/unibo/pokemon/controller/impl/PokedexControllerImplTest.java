package it.unibo.pokemon.controller.impl;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.TrainerProgress;
import it.unibo.pokemon.core.impl.PokedexImpl;
import it.unibo.pokemon.core.impl.TrainerProgressImpl;
import it.unibo.pokemon.view.DisplayedPokemon;
import it.unibo.pokemon.view.PokedexView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static it.unibo.pokemon.core.EncounterStatus.CAUGHT;
import static it.unibo.pokemon.core.EncounterStatus.SEEN;
import static it.unibo.pokemon.core.EncounterStatus.UNSEEN;
import static it.unibo.pokemon.core.PokemonFixtures.CHARMANDER;
import static it.unibo.pokemon.core.PokemonFixtures.SQUIRTLE;

/**
 * Since the view is an interface and the controller has no hidden side effects,
 * the controller can be tested without JavaFX, using a fake view that records what it is asked to show.
 */
public class PokedexControllerImplTest {

    private FakeView view;
    private Pokedex pokedex;
    private TrainerProgress progress;

    @BeforeEach
    public void setup() {
        view = new FakeView();
        pokedex = new PokedexImpl(Set.of(SQUIRTLE, CHARMANDER));
        progress = new TrainerProgressImpl(pokedex);
        progress.markAsSeen(CHARMANDER);
    }

    private PokedexController startedController() {
        final PokedexController controller = new PokedexControllerImpl(pokedex, progress, view);
        view.setController(controller);
        controller.start();
        return controller;
    }

    @Test
    public void testConstructorHasNoSideEffectsOnTheView() {
        new PokedexControllerImpl(pokedex, progress, view);
        Assertions.assertTrue(view.lists.isEmpty());
        Assertions.assertTrue(view.details.isEmpty());
    }

    @Test
    public void testProgressMustReferToTheSamePokedex() {
        final TrainerProgress otherProgress = new TrainerProgressImpl(new PokedexImpl(Set.of(SQUIRTLE)));
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new PokedexControllerImpl(pokedex, otherProgress, view)
        );
    }

    @Test
    public void testStartShowsAllPokemonsOrderedAndTheFirstOne() {
        final PokedexController controller = new PokedexControllerImpl(pokedex, progress, view);
        controller.start();
        Assertions.assertEquals(
            List.of(new DisplayedPokemon(CHARMANDER, SEEN), new DisplayedPokemon(SQUIRTLE, UNSEEN)),
            view.lists.getLast()
        );
        Assertions.assertEquals(new DisplayedPokemon(CHARMANDER, SEEN), view.details.getLast());
    }

    @Test
    public void testSearchingAnUnknownNameShowsAnError() {
        final PokedexController controller = startedController();
        controller.searchPokemon("Pikachu");
        Assertions.assertEquals(1, view.errors.size());
    }

    @Test
    public void testMarkAsCaughtUpdatesModelAndView() {
        final PokedexController controller = startedController();
        controller.markAsCaught(SQUIRTLE.getIndex());
        Assertions.assertEquals(CAUGHT, progress.getStatus(SQUIRTLE));
        Assertions.assertEquals(new DisplayedPokemon(SQUIRTLE, CAUGHT), view.details.getLast());
        Assertions.assertTrue(view.lists.getLast().contains(new DisplayedPokemon(SQUIRTLE, CAUGHT)));
    }

    private static final class FakeView implements PokedexView {
        private final List<List<DisplayedPokemon>> lists = new ArrayList<>();
        private final List<DisplayedPokemon> details = new ArrayList<>();
        private final List<String> errors = new ArrayList<>();
        private PokedexController controller;

        @Override
        public void setController(final PokedexController controller) {
            this.controller = controller;
        }

        @Override
        public PokedexController getController() {
            return controller;
        }

        @Override
        public void show() { }

        @Override
        public void showPokemon(final DisplayedPokemon pokemon) {
            details.add(pokemon);
        }

        @Override
        public void showPokemons(final List<DisplayedPokemon> pokemons) {
            lists.add(pokemons);
        }

        @Override
        public void showError(final String message) {
            errors.add(message);
        }
    }
}
