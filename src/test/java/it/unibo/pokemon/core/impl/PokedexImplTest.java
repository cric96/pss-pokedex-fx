package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static it.unibo.pokemon.core.impl.PokemonTypeEnum.FIRE;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.FLYING;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.GRASS;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.POISON;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.WATER;

public class PokedexImplTest {
    private static final PokemonEntry BULBASAUR = new PokemonEntryBuilderImpl()
            .withIndex(1)
            .withName("bulbasaur")
            .withDescription("A dinosaur")
            .withPrimaryType(GRASS)
            .withSecondaryType(POISON)
            .build();
    private static final PokemonEntry CHARMANDER = new PokemonEntryBuilderImpl()
            .withIndex(4)
            .withName("charmander")
            .withDescription("A lizard")
            .withPrimaryType(FIRE)
            .build();
    private static final PokemonEntry CHARMELEON = new PokemonEntryBuilderImpl()
            .withIndex(5)
            .withName("charmeleon")
            .withDescription("A bigger lizard")
            .withPrimaryType(FIRE)
            .withPreEvolution(4)
            .build();
    private static final PokemonEntry CHARIZARD = new PokemonEntryBuilderImpl()
            .withIndex(6)
            .withName("charizard")
            .withDescription("A dragon (but not a dragon type)")
            .withPrimaryType(FIRE)
            .withSecondaryType(FLYING)
            .withPreEvolution(5)
            .build();
    private static final PokemonEntry SQUIRTLE = new PokemonEntryBuilderImpl()
            .withIndex(7)
            .withName("squirtle")
            .withDescription("A turtle")
            .withPrimaryType(WATER)
            .build();

    private Pokedex pokedex;

    @BeforeEach
    public void setup() {
        pokedex = new PokedexImpl(
            Set.of(BULBASAUR, CHARMANDER, CHARMELEON, CHARIZARD, SQUIRTLE),
            Set.of(CHARMANDER.getIndex())
        );
    }

    @Test
    public void testPokedexNumberShouldBeAlignedWithTotalPokemon() {
        Assertions.assertEquals(5, pokedex.getTotalPokemonNumber());
        Assertions.assertEquals(pokedex.getTotalPokemonNumber(), pokedex.getAllPokemon().size());
    }

    @Test
    public void testSearchShouldFindPokemonIfIsInside() {
        Assertions.assertEquals(Optional.of(CHARMANDER), pokedex.searchPokemonByName(CHARMANDER.getName()));
    }

    @Test
    public void testSearchShouldNotFindPokemonIfDoesNotExist() {
        Assertions.assertTrue(pokedex.searchPokemonByName("gianlucamon").isEmpty());
    }

    @Test
    public void testSearchTypeShouldReturnThePokemonWithTheRightType() {
        Assertions.assertEquals(Set.of(CHARMANDER, CHARMELEON, CHARIZARD), Set.copyOf(pokedex.searchPokemonByType(FIRE)));
    }

    @Test
    public void testSearchTypeShouldReturnThePokemonWithTheRightTypeAlsoOnSecondaryType() {
        Assertions.assertEquals(List.of(BULBASAUR), pokedex.searchPokemonByType(POISON));
    }

    @Test
    public void testEvolutionChainShouldBeNavigableInBothDirections() {
        Assertions.assertEquals(List.of(CHARMELEON), pokedex.getPostEvolutions(CHARMANDER));
        Assertions.assertEquals(List.of(CHARIZARD), pokedex.getPostEvolutions(CHARMELEON));
        Assertions.assertEquals(Optional.of(CHARMELEON), pokedex.getPreEvolution(CHARIZARD));
        Assertions.assertEquals(Optional.of(CHARMANDER), pokedex.getPreEvolution(CHARMELEON));
    }

    @Test
    public void testPokemonWithoutEvolutions() {
        Assertions.assertTrue(pokedex.getPreEvolution(SQUIRTLE).isEmpty());
        Assertions.assertTrue(pokedex.getPostEvolutions(SQUIRTLE).isEmpty());
    }

    @Test
    public void testSeenStateBelongsToThePokedex() {
        Assertions.assertTrue(pokedex.isSeen(CHARMANDER));
        Assertions.assertFalse(pokedex.isSeen(SQUIRTLE));
        pokedex.markAsSeen(SQUIRTLE);
        Assertions.assertTrue(pokedex.isSeen(SQUIRTLE));
        // Another pokedex with the same species has its own, independent, state
        final Pokedex other = new PokedexImpl(pokedex.getAllPokemon());
        Assertions.assertFalse(other.isSeen(SQUIRTLE));
    }

    @Test
    public void testIndexesAndNamesShouldBeUnique() {
        final PokemonEntry fakeCharmander = new PokemonEntryBuilderImpl()
            .withIndex(99)
            .withName(CHARMANDER.getName())
            .withPrimaryType(FIRE)
            .build();
        final PokemonEntry fakeSquirtle = new PokemonEntryBuilderImpl()
            .withIndex(SQUIRTLE.getIndex())
            .withName("not squirtle")
            .withPrimaryType(WATER)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PokedexImpl(List.of(CHARMANDER, fakeCharmander)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PokedexImpl(List.of(SQUIRTLE, fakeSquirtle)));
    }

    @Test
    public void testPokemonNotInThePokedexShouldBeRejected() {
        final PokemonEntry pikachu = new PokemonEntryBuilderImpl()
            .withIndex(25)
            .withName("pikachu")
            .withPrimaryType(PokemonTypeEnum.ELECTRIC)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> pokedex.markAsSeen(pikachu));
        Assertions.assertThrows(IllegalArgumentException.class, () -> pokedex.isSeen(pikachu));
    }
}
