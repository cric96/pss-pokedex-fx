package it.unibo.pokemon.core.impl;

import it.unibo.pokemon.core.EncounterStatus;
import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.TrainerProgress;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class TrainerProgressImpl implements TrainerProgress {
    private final Pokedex pokedex;
    /** Only the species that have been encountered are stored: the others are UNSEEN. */
    private final Map<PokemonEntry, EncounterStatus> statuses = new HashMap<>();

    /**
     * Creates the progress of a new trainer: no pokemon has been seen yet.
     * @param pokedex the pokedex the progress refers to
     */
    public TrainerProgressImpl(final Pokedex pokedex) {
        this.pokedex = Objects.requireNonNull(pokedex);
    }

    @Override
    public Pokedex getPokedex() {
        return pokedex;
    }

    @Override
    public EncounterStatus getStatus(final PokemonEntry entry) {
        checkContained(entry);
        return statuses.getOrDefault(entry, EncounterStatus.UNSEEN);
    }

    @Override
    public void markAsSeen(final PokemonEntry entry) {
        statuses.put(entry, getStatus(entry).seen());
    }

    @Override
    public void markAsCaught(final PokemonEntry entry) {
        statuses.put(entry, getStatus(entry).caught());
    }

    @Override
    public int getSeenCount() {
        return (int) statuses.values().stream().filter(EncounterStatus::isSeen).count();
    }

    @Override
    public int getCaughtCount() {
        return (int) statuses.values().stream().filter(status -> status == EncounterStatus.CAUGHT).count();
    }

    private void checkContained(final PokemonEntry entry) {
        if (!pokedex.contains(entry)) {
            throw new IllegalArgumentException(entry + " does not belong to the pokedex");
        }
    }
}
