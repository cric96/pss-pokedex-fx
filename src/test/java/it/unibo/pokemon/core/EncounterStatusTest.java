package it.unibo.pokemon.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static it.unibo.pokemon.core.EncounterStatus.CAUGHT;
import static it.unibo.pokemon.core.EncounterStatus.SEEN;
import static it.unibo.pokemon.core.EncounterStatus.UNSEEN;

public class EncounterStatusTest {

    @Test
    public void testStatusOnlyMovesForward() {
        Assertions.assertEquals(SEEN, UNSEEN.seen());
        Assertions.assertEquals(SEEN, SEEN.seen());
        Assertions.assertEquals(CAUGHT, CAUGHT.seen());
    }

    @Test
    public void testCatchingFromAnyStatus() {
        for (final EncounterStatus status : EncounterStatus.values()) {
            Assertions.assertEquals(CAUGHT, status.caught());
        }
    }

    @Test
    public void testCaughtImpliesSeen() {
        Assertions.assertFalse(UNSEEN.isSeen());
        Assertions.assertTrue(SEEN.isSeen());
        Assertions.assertTrue(CAUGHT.isSeen());
    }
}
