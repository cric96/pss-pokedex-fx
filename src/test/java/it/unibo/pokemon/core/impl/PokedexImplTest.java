package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonEntryFactory;
import it.unibo.pokemon.core.PokemonEntryFull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Set;

import static it.unibo.pokemon.core.impl.PokemonTypeEnum.*;

public class PokedexImplTest {
    private static final PokemonEntryFull CHARMANDER =
        PokemonEntryFactory.create(4, "charmander", "A lizard", 1, 2)
            .setPrimaryType(FIRE)
            .build();
    private static final PokemonEntry SQUIRTLE =
        PokemonEntryFactory.create(7, "squirtle", "A turtle", 1, 2)
            .setPrimaryType(WATER)
            .build();
    private static final PokemonEntry BULBASAUR =
        PokemonEntryFactory.create(1, "bulbasaur", "A dinosaur", 1, 2)
            .setPrimaryType(GRASS)
            .setSecondaryType(POISON)
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
}
