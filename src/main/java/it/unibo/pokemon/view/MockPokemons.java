package it.unibo.pokemon.view;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.impl.PokemonTypeEnum;

import java.util.List;
import java.util.Set;

public final class MockPokemons {

    private MockPokemons() { }

    public static Set<PokemonEntry> sample() {
        // Starter trio with simple evolutions mocked
        final PokemonEntry bulbasaur = PokemonEntry.builder()
            .withIndex(1)
            .withName("Bulbasaur")
            .withDescription("A strange seed was planted on its back at birth.")
            .withPrimaryType(PokemonTypeEnum.GRASS)
            .withSecondaryType(PokemonTypeEnum.POISON)
            .withWeight(6.9)
            .withHeight(0.7)
            .withImageUrl("/it/unibo/pokemon/images/0001.png")
            .isSeen()
            .build();

        final PokemonEntry ivysaur = PokemonEntry.builder()
            .withIndex(2)
            .withName("Ivysaur")
            .withDescription("When the bulb on its back grows large, it appears to lose the ability to stand on its hind legs.")
            .withPrimaryType(PokemonTypeEnum.GRASS)
            .withSecondaryType(PokemonTypeEnum.POISON)
            .withPreEvolution(bulbasaur)
            .withWeight(13.0)
            .withHeight(1.0)
            .withImageUrl("/it/unibo/pokemon/images/0002.png")
            .build();

        final PokemonEntry venusaur = PokemonEntry.builder()
            .withIndex(3)
            .withName("Venusaur")
            .withDescription("The plant blooms when it is absorbing solar energy. It stays on the move to seek sunlight.")
            .withPrimaryType(PokemonTypeEnum.GRASS)
            .withSecondaryType(PokemonTypeEnum.POISON)
            .withPreEvolution(ivysaur)
            .withWeight(100.0)
            .withHeight(2.0)
            .withImageUrl("/it/unibo/pokemon/images/0003.png")
            .build();

        final PokemonEntry charmander = PokemonEntry.builder()
            .withIndex(4)
            .withName("Charmander")
            .withDescription("Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail.")
            .withPrimaryType(PokemonTypeEnum.FIRE)
            .withWeight(8.5)
            .withHeight(0.6)
            .withImageUrl("/it/unibo/pokemon/images/0004.png")
            .build();

        final PokemonEntry squirtle = PokemonEntry.builder()
            .withIndex(7)
            .withName("Squirtle")
            .withDescription("After birth, its back swells and hardens into a shell. Powerfully sprays foam from its mouth.")
            .withPrimaryType(PokemonTypeEnum.WATER)
            .withWeight(9.0)
            .withHeight(0.5)
            .withImageUrl("/it/unibo/pokemon/images/0007.png")
            .build();

        return Set.of(bulbasaur, ivysaur, venusaur, charmander, squirtle);
    }
}
