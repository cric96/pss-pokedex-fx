package it.unibo.pokemon.core;

/**
 * The progress of a trainer: which species of a {@link Pokedex} have been seen or caught.
 * Unlike the pokedex, which is fixed, the progress changes over time, and each trainer has their own.
 */
public interface TrainerProgress {

    /**
     * @return the pokedex this progress refers to
     */
    Pokedex getPokedex();

    /**
     * @param entry a pokemon of the pokedex
     * @return the current status of the pokemon
     * @throws IllegalArgumentException if the pokemon does not belong to the pokedex
     */
    EncounterStatus getStatus(PokemonEntry entry);

    /**
     * Records that the trainer has seen the pokemon. It has no effect if the pokemon was already seen or caught.
     * @param entry a pokemon of the pokedex
     * @throws IllegalArgumentException if the pokemon does not belong to the pokedex
     */
    void markAsSeen(PokemonEntry entry);

    /**
     * Records that the trainer has caught the pokemon (and so has seen it).
     * @param entry a pokemon of the pokedex
     * @throws IllegalArgumentException if the pokemon does not belong to the pokedex
     */
    void markAsCaught(PokemonEntry entry);

    /**
     * @return how many species have been seen (caught ones included)
     */
    int getSeenCount();

    /**
     * @return how many species have been caught
     */
    int getCaughtCount();
}
