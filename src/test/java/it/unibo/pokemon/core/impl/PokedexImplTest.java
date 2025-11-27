package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.Pokedex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Set;

import static it.unibo.pokemon.core.impl.PokemonTypeEnum.*;

public class PokedexImplTest {
    private static final PokemonEntry CHARMANDER = new PokemonEntryBuilderImpl()
            .withIndex(4)
            .withName("charmander")
            .withDescription("A lizard")
            .withWeight(1)
            .withHeight(2)
            .withPrimaryType(FIRE)
            .build();
    private static final PokemonEntry SQUIRTLE = new PokemonEntryBuilderImpl()
            .withIndex(7)
            .withName("squirtle")
            .withDescription("A turtle")
            .withWeight(1)
            .withHeight(2)
            .withPrimaryType(WATER)
            .build();
    private static final PokemonEntry BULBASAUR = new PokemonEntryBuilderImpl()
            .withIndex(1)
            .withName("bulbasaur")
            .withDescription("A dinosaur")
            .withWeight(1)
            .withHeight(2)
            .withPrimaryType(GRASS)
            .withSecondaryType(POISON)
            .build();

    private Pokedex pokedex;

    @BeforeEach
    public void setup() {
        var pokemon = Set.of(CHARMANDER, SQUIRTLE, BULBASAUR);
        pokedex = new PokedexImpl(pokemon);
    }

    @Test
    public void testPokedexShouldNeverBeEmpty() {
        Assertions.assertTrue(pokedex.getTotalPokemonNumber() > 0);
    }

    @Test
    public void testPokedexNumberShouldBeAlignedWithTotalPokemon() {
        Assertions.assertSame(pokedex.getTotalPokemonNumber(), pokedex.getAllPokemon().size());
    }

    @Test
    public void testSearchShouldFindPokemonIfIsInside() {
        var pokemon = pokedex.searchPokemonByName(CHARMANDER.getName());
        Assertions.assertTrue(pokemon.isPresent());
        Assertions.assertEquals(pokemon, Optional.of(CHARMANDER));
    }

    @Test
    public void testSearchShouldNotFindPokemonIfDoesNotExit() {
        var pokemon = pokedex.searchPokemonByName("gianlucamon");
        Assertions.assertTrue(pokemon.isEmpty());
    }

    @Test
    public void testSearchTypeShouldReturnThePokemonWithTheRightType() {
        var allFirePokemon = pokedex.searchPokemonByType(FIRE);
        Assertions.assertFalse(allFirePokemon.isEmpty());
    }

    @Test
    public void testSearchTypeShouldReturnThePokemonWithTheRightTypeAlsoOnSecondaryType() {
        var allPokemon = pokedex.searchPokemonByType(POISON);
        Assertions.assertFalse(allPokemon.isEmpty());
        Assertions.assertEquals(allPokemon.get(0), BULBASAUR);
    }
}
