package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.TrainerProgress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static it.unibo.pokemon.core.EncounterStatus.CAUGHT;
import static it.unibo.pokemon.core.EncounterStatus.SEEN;
import static it.unibo.pokemon.core.EncounterStatus.UNSEEN;
import static it.unibo.pokemon.core.PokemonFixtures.BULBASAUR;
import static it.unibo.pokemon.core.PokemonFixtures.CHARMANDER;
import static it.unibo.pokemon.core.PokemonFixtures.PIKACHU;
import static it.unibo.pokemon.core.PokemonFixtures.SQUIRTLE;

public class TrainerProgressImplTest {

    private Pokedex pokedex;
    private TrainerProgress progress;

    @BeforeEach
    public void setup() {
        pokedex = new PokedexImpl(Set.of(BULBASAUR, CHARMANDER, SQUIRTLE));
        progress = new TrainerProgressImpl(pokedex);
    }

    @Test
    public void testNewTrainerHasNotSeenAnything() {
        pokedex.getAllPokemon().forEach(entry -> Assertions.assertEquals(UNSEEN, progress.getStatus(entry)));
        Assertions.assertEquals(0, progress.getSeenCount());
        Assertions.assertEquals(0, progress.getCaughtCount());
    }

    @Test
    public void testSeeingAndCatching() {
        progress.markAsSeen(BULBASAUR);
        progress.markAsCaught(CHARMANDER);
        Assertions.assertEquals(SEEN, progress.getStatus(BULBASAUR));
        Assertions.assertEquals(CAUGHT, progress.getStatus(CHARMANDER));
        Assertions.assertEquals(UNSEEN, progress.getStatus(SQUIRTLE));
        Assertions.assertEquals(2, progress.getSeenCount()); // a caught pokemon has been seen too
        Assertions.assertEquals(1, progress.getCaughtCount());
    }

    @Test
    public void testProgressNeverGoesBack() {
        progress.markAsCaught(CHARMANDER);
        progress.markAsSeen(CHARMANDER);
        Assertions.assertEquals(CAUGHT, progress.getStatus(CHARMANDER));
    }

    @Test
    public void testEachTrainerHasTheirOwnProgress() {
        final TrainerProgress other = new TrainerProgressImpl(pokedex);
        progress.markAsSeen(SQUIRTLE);
        Assertions.assertEquals(UNSEEN, other.getStatus(SQUIRTLE));
    }

    @Test
    public void testPokemonNotInThePokedexShouldBeRejected() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> progress.getStatus(PIKACHU));
        Assertions.assertThrows(IllegalArgumentException.class, () -> progress.markAsSeen(PIKACHU));
        Assertions.assertThrows(IllegalArgumentException.class, () -> progress.markAsCaught(PIKACHU));
    }
}
