package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.Evolution;
import it.unibo.pokemon.core.EvolutionCondition.LevelUp;
import it.unibo.pokemon.core.EvolutionCondition.UseItem;
import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
import org.junit.jupiter.api.Assertions;
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
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.NORMAL;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.POISON;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.WATER;

public class PokedexImplTest {

    private static final Set<PokemonEntry> SPECIES = Set.of(BULBASAUR, CHARMANDER, CHARMELEON, CHARIZARD, SQUIRTLE);

    @Test
    public void testSearchByTypeAlsoConsidersTheSecondaryType() {
        final Pokedex pokedex = new PokedexImpl(SPECIES);

        Assertions.assertEquals(List.of(BULBASAUR), pokedex.searchPokemonByType(POISON));
    }

    @Test
    public void testEvolutionChainIsNavigableInBothDirections() {
        final Pokedex pokedex = new PokedexImpl(SPECIES, Set.of(CHARMANDER_TO_CHARMELEON, CHARMELEON_TO_CHARIZARD));

        Assertions.assertEquals(Optional.of(CHARMANDER_TO_CHARMELEON), pokedex.getPreEvolution(CHARMELEON));
        Assertions.assertEquals(List.of(CHARMELEON_TO_CHARIZARD), pokedex.getPostEvolutions(CHARMELEON));
    }

    @Test
    public void testASpeciesCanEvolveIntoSeveralOnes() {
        final PokemonEntry eevee = entry(133, "Eevee", NORMAL, null);
        final PokemonEntry vaporeon = entry(134, "Vaporeon", WATER, null);
        final PokemonEntry flareon = entry(136, "Flareon", FIRE, null);
        final Evolution toFlareon = new Evolution(eevee, flareon, new UseItem("Fire Stone"));
        final Evolution toVaporeon = new Evolution(eevee, vaporeon, new UseItem("Water Stone"));
        final Pokedex pokedex = new PokedexImpl(Set.of(eevee, vaporeon, flareon), Set.of(toFlareon, toVaporeon));

        Assertions.assertEquals(List.of(toVaporeon, toFlareon), pokedex.getPostEvolutions(eevee));
    }

    @Test
    public void testIndexesMustBeUnique() {
        final PokemonEntry sameIndexAsSquirtle = entry(SQUIRTLE.getIndex(), "Not Squirtle", WATER, null);

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new PokedexImpl(List.of(SQUIRTLE, sameIndexAsSquirtle))
        );
    }

    @Test
    public void testNamesMustBeUnique() {
        final PokemonEntry sameNameAsCharmander = entry(99, CHARMANDER.getName(), FIRE, null);

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new PokedexImpl(List.of(CHARMANDER, sameNameAsCharmander))
        );
    }

    @Test
    public void testEvolutionsMustInvolveSpeciesOfThePokedex() {
        final Evolution toPikachu = new Evolution(CHARMANDER, PIKACHU, new LevelUp(20));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new PokedexImpl(SPECIES, Set.of(toPikachu)));
    }

    @Test
    public void testASpeciesHasAtMostOnePreEvolution() {
        final Evolution squirtleToCharmeleon = new Evolution(SQUIRTLE, CHARMELEON, new LevelUp(20));

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new PokedexImpl(SPECIES, Set.of(CHARMANDER_TO_CHARMELEON, squirtleToCharmeleon))
        );
    }

    @Test
    public void testEvolutionsCannotFormCycles() {
        final Evolution backToCharmander = new Evolution(CHARIZARD, CHARMANDER, new UseItem("Time Machine"));
        final Set<Evolution> cycle = Set.of(CHARMANDER_TO_CHARMELEON, CHARMELEON_TO_CHARIZARD, backToCharmander);

        Assertions.assertThrows(IllegalArgumentException.class, () -> new PokedexImpl(SPECIES, cycle));
    }

    @Test
    public void testLevelsMustIncreaseAlongAChain() {
        final Evolution tooEarly = new Evolution(CHARMELEON, CHARIZARD, new LevelUp(10));

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new PokedexImpl(SPECIES, Set.of(CHARMANDER_TO_CHARMELEON, tooEarly))
        );
    }

    @Test
    public void testLevelConstraintIgnoresEvolutionsByItem() {
        final Evolution byItem = new Evolution(CHARMELEON, CHARIZARD, new UseItem("Fire Stone"));

        Assertions.assertDoesNotThrow(() -> new PokedexImpl(SPECIES, Set.of(CHARMANDER_TO_CHARMELEON, byItem)));
    }
}
