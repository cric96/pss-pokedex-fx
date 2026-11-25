package it.unibo.pokemon.core;

import it.unibo.pokemon.core.EvolutionCondition.LevelUp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PokedexLoaderTest {

    private final Pokedex pokedex = PokedexLoader.loadPokedex();

    private PokemonEntry byName(final String name) {
        return pokedex.searchPokemonByName(name).orElseThrow();
    }

    private static Pokedex load(final String json) {
        return new PokedexLoader(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))).load();
    }

    @Test
    public void testDefaultPokedexIsLoaded() {
        Assertions.assertEquals(12, pokedex.getTotalPokemonNumber());
    }

    @Test
    public void testWholeEvolutionChainIsNavigable() {
        final PokemonEntry bulbasaur = byName("Bulbasaur");
        final PokemonEntry ivysaur = byName("Ivysaur");
        final PokemonEntry venusaur = byName("Venusaur");
        Assertions.assertEquals(
            List.of(new Evolution(bulbasaur, ivysaur, new LevelUp(16))),
            pokedex.getPostEvolutions(bulbasaur)
        );
        Assertions.assertEquals(
            List.of(new Evolution(ivysaur, venusaur, new LevelUp(32))),
            pokedex.getPostEvolutions(ivysaur)
        );
        Assertions.assertEquals(bulbasaur, pokedex.getPreEvolution(ivysaur).orElseThrow().from());
    }

    @Test
    public void testUtf8DescriptionsAreLoaded() {
        Assertions.assertTrue(byName("Bulbasaur").getDescription().contains("Pokémon"));
    }

    @Test
    public void testOtherEvolutionConditions() {
        final Pokedex loaded = load("""
            [
              {"id": 25, "name": "Pikachu", "description": "", "type1": "ELECTRIC", "height": 0.4, "weight": 6.0},
              {"id": 26, "name": "Raichu", "description": "", "type1": "ELECTRIC", "height": 0.8, "weight": 30.0,
               "evolvesFrom": {"pokemon": "Pikachu", "item": "Thunder Stone"}},
              {"id": 64, "name": "Kadabra", "description": "", "type1": "PSYCHIC", "height": 1.3, "weight": 56.5},
              {"id": 65, "name": "Alakazam", "description": "", "type1": "PSYCHIC", "height": 1.5, "weight": 48.0,
               "evolvesFrom": {"pokemon": "Kadabra", "trade": true}}
            ]
            """);
        final PokemonEntry raichu = loaded.searchPokemonByName("Raichu").orElseThrow();
        final PokemonEntry alakazam = loaded.searchPokemonByName("Alakazam").orElseThrow();
        Assertions.assertEquals(
            new EvolutionCondition.UseItem("Thunder Stone"),
            loaded.getPreEvolution(raichu).orElseThrow().condition()
        );
        Assertions.assertEquals(new EvolutionCondition.Trade(), loaded.getPreEvolution(alakazam).orElseThrow().condition());
    }

    @Test
    public void testInvalidPokedexIsRejected() {
        // Two conditions for the same evolution
        Assertions.assertThrows(IllegalStateException.class, () -> load("""
            [
              {"id": 25, "name": "Pikachu", "description": "", "type1": "ELECTRIC", "height": 0.4, "weight": 6.0},
              {"id": 26, "name": "Raichu", "description": "", "type1": "ELECTRIC", "height": 0.8, "weight": 30.0,
               "evolvesFrom": {"pokemon": "Pikachu", "item": "Thunder Stone", "level": 20}}
            ]
            """));
        // Evolution from an unknown pokemon
        Assertions.assertThrows(IllegalStateException.class, () -> load("""
            [
              {"id": 26, "name": "Raichu", "description": "", "type1": "ELECTRIC", "height": 0.8, "weight": 30.0,
               "evolvesFrom": {"pokemon": "Pikachu", "item": "Thunder Stone"}}
            ]
            """));
        // Cycle (a constraint of the pokedex, reported as invalid input)
        Assertions.assertThrows(IllegalStateException.class, () -> load("""
            [
              {"id": 1, "name": "A", "description": "", "type1": "NORMAL", "height": 1, "weight": 1,
               "evolvesFrom": {"pokemon": "B", "trade": true}},
              {"id": 2, "name": "B", "description": "", "type1": "NORMAL", "height": 1, "weight": 1,
               "evolvesFrom": {"pokemon": "A", "trade": true}}
            ]
            """));
    }
}
