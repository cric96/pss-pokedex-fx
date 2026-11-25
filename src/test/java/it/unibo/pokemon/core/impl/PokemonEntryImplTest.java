package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.PokemonEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static it.unibo.pokemon.core.PokemonFixtures.entry;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.ELECTRIC;

public class PokemonEntryImplTest {

    @Test
    public void testSecondaryTypeCannotBeEqualToPrimary() {
        final PokemonEntry pikachu = entry(25, "Pikachu", ELECTRIC, ELECTRIC);
        Assertions.assertTrue(pikachu.getSecondaryType().isEmpty());
    }

    @Test
    public void testMandatoryFields() {
        Assertions.assertThrows(NullPointerException.class, () -> entry(25, null, ELECTRIC, null));
        Assertions.assertThrows(NullPointerException.class, () -> entry(25, "Pikachu", null, null));
    }

    @Test
    public void testSpeciesAreIdentifiedByIndex() {
        Assertions.assertEquals(entry(25, "Pikachu", ELECTRIC, null), entry(25, "Pikachu", ELECTRIC, null));
        Assertions.assertNotEquals(entry(25, "Pikachu", ELECTRIC, null), entry(26, "Raichu", ELECTRIC, null));
    }
}
