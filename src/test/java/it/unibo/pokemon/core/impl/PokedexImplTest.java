package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.Evolution;
import it.unibo.pokemon.core.EvolutionCondition.LevelUp;
import it.unibo.pokemon.core.EvolutionCondition.UseItem;
import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static it.unibo.pokemon.core.PokemonFixtures.BULBASAUR;
import static it.unibo.pokemon.core.PokemonFixtures.CHARIZARD;
import static it.unibo.pokemon.core.PokemonFixtures.CHARMANDER;
import static it.unibo.pokemon.core.PokemonFixtures.CHARMANDER_TO_CHARMELEON;
import static it.unibo.pokemon.core.PokemonFixtures.CHARMELEON;
import static it.unibo.pokemon.core.PokemonFixtures.CHARMELEON_TO_CHARIZARD;
import static it.unibo.pokemon.core.PokemonFixtures.PIKACHU;
import static it.unibo.pokemon.core.PokemonFixtures.SQUIRTLE;
import static it.unibo.pokemon.core.PokemonFixtures.entry;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.FIRE;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.POISON;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.WATER;

public class PokedexImplTest {

    private static final Set<PokemonEntry> SPECIES = Set.of(BULBASAUR, CHARMANDER, CHARMELEON, CHARIZARD, SQUIRTLE);

    private Pokedex pokedex;

    @BeforeEach
    public void setup() {
        pokedex = new PokedexImpl(SPECIES, Set.of(CHARMANDER_TO_CHARMELEON, CHARMELEON_TO_CHARIZARD));
    }

    @Test
    public void testPokedexNumberShouldBeAlignedWithTotalPokemon() {
        Assertions.assertEquals(5, pokedex.getTotalPokemonNumber());
        Assertions.assertEquals(SPECIES, pokedex.getAllPokemon());
    }

    @Test
    public void testSearchByName() {
        Assertions.assertEquals(Optional.of(CHARMANDER), pokedex.searchPokemonByName("Charmander"));
        Assertions.assertTrue(pokedex.searchPokemonByName("Gianlucamon").isEmpty());
    }

    @Test
    public void testSearchByTypeAlsoConsidersTheSecondaryType() {
        Assertions.assertEquals(Set.of(CHARMANDER, CHARMELEON, CHARIZARD), Set.copyOf(pokedex.searchPokemonByType(FIRE)));
        Assertions.assertEquals(List.of(BULBASAUR), pokedex.searchPokemonByType(POISON));
    }

    @Test
    public void testEvolutionChainShouldBeNavigableInBothDirections() {
        Assertions.assertEquals(List.of(CHARMANDER_TO_CHARMELEON), pokedex.getPostEvolutions(CHARMANDER));
        Assertions.assertEquals(List.of(CHARMELEON_TO_CHARIZARD), pokedex.getPostEvolutions(CHARMELEON));
        Assertions.assertEquals(Optional.of(CHARMELEON_TO_CHARIZARD), pokedex.getPreEvolution(CHARIZARD));
        Assertions.assertEquals(Optional.of(CHARMANDER_TO_CHARMELEON), pokedex.getPreEvolution(CHARMELEON));
        Assertions.assertTrue(pokedex.getPreEvolution(CHARMANDER).isEmpty());
        Assertions.assertTrue(pokedex.getPostEvolutions(CHARIZARD).isEmpty());
    }

    @Test
    public void testASpeciesCanEvolveIntoSeveralOnes() {
        final PokemonEntry eevee = entry(133, "Eevee", PokemonTypeEnum.NORMAL, null);
        final PokemonEntry vaporeon = entry(134, "Vaporeon", WATER, null);
        final PokemonEntry flareon = entry(136, "Flareon", FIRE, null);
        final Evolution toFlareon = new Evolution(eevee, flareon, new UseItem("Fire Stone"));
        final Evolution toVaporeon = new Evolution(eevee, vaporeon, new UseItem("Water Stone"));
        final Pokedex eeveelutions = new PokedexImpl(Set.of(eevee, vaporeon, flareon), Set.of(toFlareon, toVaporeon));
        Assertions.assertEquals(List.of(toVaporeon, toFlareon), eeveelutions.getPostEvolutions(eevee));
    }

    @Test
    public void testIndexesAndNamesShouldBeUnique() {
        final PokemonEntry fakeCharmander = entry(99, "Charmander", FIRE, null);
        final PokemonEntry fakeSquirtle = entry(7, "Not Squirtle", WATER, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PokedexImpl(List.of(CHARMANDER, fakeCharmander)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PokedexImpl(List.of(SQUIRTLE, fakeSquirtle)));
    }

    @Test
    public void testEvolutionsMustInvolveSpeciesOfThePokedex() {
        final Evolution evolution = new Evolution(CHARMANDER, PIKACHU, new LevelUp(20));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PokedexImpl(SPECIES, Set.of(evolution)));
    }

    @Test
    public void testASpeciesHasAtMostOnePreEvolution() {
        final Evolution other = new Evolution(SQUIRTLE, CHARMELEON, new LevelUp(20));
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new PokedexImpl(SPECIES, Set.of(CHARMANDER_TO_CHARMELEON, other))
        );
    }

    @Test
    public void testEvolutionsCannotFormCycles() {
        final Evolution backToCharmander = new Evolution(CHARIZARD, CHARMANDER, new UseItem("Time Machine"));
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new PokedexImpl(SPECIES, Set.of(CHARMANDER_TO_CHARMELEON, CHARMELEON_TO_CHARIZARD, backToCharmander))
        );
    }

    @Test
    public void testLevelsMustIncreaseAlongAChain() {
        final Evolution tooEarly = new Evolution(CHARMELEON, CHARIZARD, new LevelUp(10));
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new PokedexImpl(SPECIES, Set.of(CHARMANDER_TO_CHARMELEON, tooEarly))
        );
        // The constraint only concerns consecutive evolutions by level
        final Evolution byItem = new Evolution(CHARMELEON, CHARIZARD, new UseItem("Fire Stone"));
        Assertions.assertDoesNotThrow(() -> new PokedexImpl(SPECIES, Set.of(CHARMANDER_TO_CHARMELEON, byItem)));
    }

    @Test
    public void testPokemonNotInThePokedexShouldBeRejected() {
        Assertions.assertFalse(pokedex.contains(PIKACHU));
        Assertions.assertThrows(IllegalArgumentException.class, () -> pokedex.getPreEvolution(PIKACHU));
        Assertions.assertThrows(IllegalArgumentException.class, () -> pokedex.getPostEvolutions(PIKACHU));
    }
}
