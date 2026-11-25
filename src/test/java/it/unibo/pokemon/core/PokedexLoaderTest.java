package it.unibo.pokemon.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PokedexLoaderTest {

    private final Pokedex pokedex = PokedexLoader.loadPokedex();

    private PokemonEntry byName(final String name) {
        return pokedex.searchPokemonByName(name).orElseThrow();
    }

    @Test
    public void testDefaultPokedexIsLoaded() {
        Assertions.assertEquals(12, pokedex.getTotalPokemonNumber());
    }

    @Test
    public void testWholeEvolutionChainIsNavigable() {
        final PokemonEntry bulbasaur = byName("Bulbasaur");
        final PokemonEntry ivysaur = pokedex.getPostEvolutions(bulbasaur).getFirst();
        Assertions.assertEquals("Ivysaur", ivysaur.getName());
        Assertions.assertEquals(List.of(byName("Venusaur")), pokedex.getPostEvolutions(ivysaur));
        Assertions.assertEquals(bulbasaur, pokedex.getPreEvolution(ivysaur).orElseThrow());
    }

    @Test
    public void testSeenStateIsLoaded() {
        Assertions.assertTrue(pokedex.isSeen(byName("Charmander")));
        Assertions.assertFalse(pokedex.isSeen(byName("Bulbasaur")));
    }

    @Test
    public void testUtf8DescriptionsAreLoaded() {
        Assertions.assertTrue(byName("Bulbasaur").getDescription().contains("Pokémon"));
    }
}
