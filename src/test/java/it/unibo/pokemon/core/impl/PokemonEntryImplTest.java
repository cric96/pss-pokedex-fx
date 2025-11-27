package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntryFull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PokemonEntryImplTest {

    @Test
    public void pokemonEntryTypeShouldNeverBeTheSame() {
        PokemonEntryFull pokemon = new PokemonEntryBuilderImpl()
            .setIndex(0)
            .setName("pikachu")
            .setDescription("")
            .setWeight(1.0)
            .setHeight(2.0)
            .setPrimaryType(PokemonTypeEnum.FIRE)
            .setSecondaryType(PokemonTypeEnum.FIRE)
            .build();
        Assertions.assertTrue(pokemon.getSecondaryType().isEmpty());
    }

    @Test
    public void pokemonEntryTypeShouldNeverHavePrevolutionOfItself() {
        var pokemonBuilder = new PokemonEntryBuilderImpl()
            .setIndex(0)
            .setName("pikachu")
            .setDescription("")
            .setWeight(1.0)
            .setHeight(2.0)
            .setPrimaryType(PokemonTypeEnum.FIRE);
        var pikachu = pokemonBuilder.build();
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->  pokemonBuilder.addPreEvolution(pikachu).build()
        );
    }

    @Test
    public void pokemonEntryTypeShouldNeverHavePostevolutionOfItself() {
        var pokemonBuilder = new PokemonEntryBuilderImpl()
            .setIndex(0)
            .setName("pikachu")
            .setDescription("")
            .setWeight(1.0)
            .setHeight(2.0)
            .setPrimaryType(PokemonTypeEnum.FIRE);
        var pikachu = pokemonBuilder.build();
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->  pokemonBuilder.addPostEvolution(pikachu).build()
        );
    }

}
