package it.unibo.pokemon.core;

/**
 * How far the trainer has got with a species. The status can only move forward:
 * UNSEEN -> SEEN -> CAUGHT (catching a pokemon also means having seen it).
 */
public enum EncounterStatus {
    UNSEEN, SEEN, CAUGHT;

    /**
     * @return the status after the pokemon has been seen (never goes back)
     */
    public EncounterStatus seen() {
        return this == UNSEEN ? SEEN : this;
    }

    /**
     * @return the status after the pokemon has been caught
     */
    public EncounterStatus caught() {
        return CAUGHT;
    }

    /**
     * @return true if the pokemon has been seen (a caught pokemon has been seen too)
     */
    public boolean isSeen() {
        return this != UNSEEN;
    }
}
