package it.unibo.pokemon.core;

import it.unibo.pokemon.core.EvolutionCondition.LevelUp;
import it.unibo.pokemon.core.EvolutionCondition.Trade;
import it.unibo.pokemon.core.EvolutionCondition.UseItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static it.unibo.pokemon.core.PokemonFixtures.CHARMANDER;

public class EvolutionTest {

    @Test
    public void testPokemonCannotEvolveIntoItself() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Evolution(CHARMANDER, CHARMANDER, new Trade()));
    }

    @Test
    public void testLevelMustBeBetweenOneAndOneHundred() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LevelUp(0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LevelUp(101));
        Assertions.assertDoesNotThrow(() -> new LevelUp(1));
        Assertions.assertDoesNotThrow(() -> new LevelUp(100));
    }

    @Test
    public void testItemMustHaveAName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new UseItem(" "));
    }
}
