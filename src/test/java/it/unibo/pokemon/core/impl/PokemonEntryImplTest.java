package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PokemonEntryImplTest {

    @Test
    public void pokemonEntryTypeShouldNeverBeTheSame() {
        PokemonEntry pokemon = new PokemonEntryBuilderImpl()
            .withIndex(0)
            .withName("pikachu")
            .withDescription("")
            .withWeight(1.0)
            .withHeight(2.0)
            .withPrimaryType(PokemonTypeEnum.FIRE)
            .withSecondaryType(PokemonTypeEnum.FIRE)
            .build();
        Assertions.assertTrue(pokemon.getSecondaryType().isEmpty());
    }

    @Test
    public void pokemonEntryTypeShouldNeverHavePrevolutionOfItself() {
        var pokemonBuilder = new PokemonEntryBuilderImpl()
            .withIndex(0)
            .withName("pikachu")
            .withDescription("")
            .withWeight(1.0)
            .withHeight(2.0)
            .withPrimaryType(PokemonTypeEnum.FIRE);
        var pikachu = pokemonBuilder.build();
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->  pokemonBuilder.withPreEvolution(pikachu).build()
        );
    }

    @Test
    public void pokemonEntryTypeShouldNeverHavePostevolutionOfItself() {
        var pokemonBuilder = new PokemonEntryBuilderImpl()
            .withIndex(0)
            .withName("pikachu")
            .withDescription("")
            .withWeight(1.0)
            .withHeight(2.0)
            .withPrimaryType(PokemonTypeEnum.FIRE);
        var pikachu = pokemonBuilder.build();
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->  pokemonBuilder.withPostEvolution(pikachu).build()
        );
    }

}
