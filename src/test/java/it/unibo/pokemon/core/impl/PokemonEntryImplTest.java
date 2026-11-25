package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static it.unibo.pokemon.core.PokemonFixtures.entry;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.ELECTRIC;

public class PokemonEntryImplTest {

    @Test
    public void testSecondaryTypeEqualToPrimaryIsIgnored() {
        final PokemonEntry pikachu = entry(25, "Pikachu", ELECTRIC, ELECTRIC);
        Assertions.assertTrue(pikachu.getSecondaryType().isEmpty());
    }

    @Test
    public void testSpeciesAreIdentifiedByTheirIndex() {
        final PokemonEntry pikachu = entry(25, "Pikachu", ELECTRIC, null);
        final PokemonEntry sameIndex = entry(25, "Another name", ELECTRIC, null);
        Assertions.assertEquals(pikachu, sameIndex);
    }
}
