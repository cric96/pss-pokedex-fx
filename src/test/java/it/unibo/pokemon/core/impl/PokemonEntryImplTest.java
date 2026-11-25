package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PokemonEntryImplTest {

    @Test
    public void pokemonEntryTypeShouldNeverBeTheSame() {
        final PokemonEntry pokemon = new PokemonEntryBuilderImpl()
            .withIndex(25)
            .withName("pikachu")
            .withPrimaryType(PokemonTypeEnum.ELECTRIC)
            .withSecondaryType(PokemonTypeEnum.ELECTRIC)
            .build();
        Assertions.assertTrue(pokemon.getSecondaryType().isEmpty());
    }

    @Test
    public void pokemonEntryShouldNeverEvolveFromItself() {
        final var pokemonBuilder = new PokemonEntryBuilderImpl()
            .withIndex(25)
            .withName("pikachu")
            .withPrimaryType(PokemonTypeEnum.ELECTRIC)
            .withPreEvolution(25);
        Assertions.assertThrows(IllegalArgumentException.class, pokemonBuilder::build);
    }

    @Test
    public void pokemonEntryShouldExposeItsPreEvolution() {
        final PokemonEntry raichu = new PokemonEntryBuilderImpl()
            .withIndex(26)
            .withName("raichu")
            .withPrimaryType(PokemonTypeEnum.ELECTRIC)
            .withPreEvolution(25)
            .build();
        Assertions.assertEquals(25, raichu.getPreEvolutionIndex().orElseThrow());
    }
}
