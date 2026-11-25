package it.unibo.pokemon.core;

import it.unibo.pokemon.core.EvolutionCondition.LevelUp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PokedexLoaderTest {

    private static Pokedex load(final String json) {
        return new PokedexLoader(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))).load();
    }

    @Test
    public void testShippedDataIsValid() {
        final Pokedex pokedex = PokedexLoader.loadPokedex();
        Assertions.assertEquals(12, pokedex.getTotalPokemonNumber());
    }

    @Test
    public void testEvolutionsAreResolvedByName() {
        final Pokedex pokedex = PokedexLoader.loadPokedex();
        final PokemonEntry bulbasaur = pokedex.searchPokemonByName("Bulbasaur").orElseThrow();
        final PokemonEntry ivysaur = pokedex.searchPokemonByName("Ivysaur").orElseThrow();
        Assertions.assertEquals(
            List.of(new Evolution(bulbasaur, ivysaur, new LevelUp(16))),
            pokedex.getPostEvolutions(bulbasaur)
        );
    }

    @Test
    public void testDataIsReadAsUtf8() {
        final Pokedex pokedex = PokedexLoader.loadPokedex();
        Assertions.assertTrue(pokedex.searchPokemonByName("Bulbasaur").orElseThrow().getDescription().contains("Pokémon"));
    }

    @Test
    public void testEvolutionByItemAndByTrade() {
        final String json = """
            [
              {"id": 25, "name": "Pikachu", "description": "", "type1": "ELECTRIC", "height": 0.4, "weight": 6.0},
              {"id": 26, "name": "Raichu", "description": "", "type1": "ELECTRIC", "height": 0.8, "weight": 30.0,
               "evolvesFrom": {"pokemon": "Pikachu", "item": "Thunder Stone"}},
              {"id": 64, "name": "Kadabra", "description": "", "type1": "PSYCHIC", "height": 1.3, "weight": 56.5},
              {"id": 65, "name": "Alakazam", "description": "", "type1": "PSYCHIC", "height": 1.5, "weight": 48.0,
               "evolvesFrom": {"pokemon": "Kadabra", "trade": true}}
            ]
            """;

        final Pokedex pokedex = load(json);

        final PokemonEntry raichu = pokedex.searchPokemonByName("Raichu").orElseThrow();
        final PokemonEntry alakazam = pokedex.searchPokemonByName("Alakazam").orElseThrow();
        Assertions.assertEquals(
            new EvolutionCondition.UseItem("Thunder Stone"),
            pokedex.getPreEvolution(raichu).orElseThrow().condition()
        );
        Assertions.assertEquals(new EvolutionCondition.Trade(), pokedex.getPreEvolution(alakazam).orElseThrow().condition());
    }

    @Test
    public void testEvolutionWithMoreThanOneConditionIsRejected() {
        final String json = """
            [
              {"id": 25, "name": "Pikachu", "description": "", "type1": "ELECTRIC", "height": 0.4, "weight": 6.0},
              {"id": 26, "name": "Raichu", "description": "", "type1": "ELECTRIC", "height": 0.8, "weight": 30.0,
               "evolvesFrom": {"pokemon": "Pikachu", "item": "Thunder Stone", "level": 20}}
            ]
            """;

        Assertions.assertThrows(IllegalStateException.class, () -> load(json));
    }

    @Test
    public void testEvolutionFromUnknownPokemonIsRejected() {
        final String json = """
            [
              {"id": 26, "name": "Raichu", "description": "", "type1": "ELECTRIC", "height": 0.8, "weight": 30.0,
               "evolvesFrom": {"pokemon": "Pikachu", "item": "Thunder Stone"}}
            ]
            """;

        Assertions.assertThrows(IllegalStateException.class, () -> load(json));
    }

    @Test
    public void testDataViolatingADomainConstraintIsRejected() {
        final String json = """
            [
              {"id": 1, "name": "A", "description": "", "type1": "NORMAL", "height": 1, "weight": 1,
               "evolvesFrom": {"pokemon": "B", "trade": true}},
              {"id": 2, "name": "B", "description": "", "type1": "NORMAL", "height": 1, "weight": 1,
               "evolvesFrom": {"pokemon": "A", "trade": true}}
            ]
            """;

        Assertions.assertThrows(IllegalStateException.class, () -> load(json));
    }
}
