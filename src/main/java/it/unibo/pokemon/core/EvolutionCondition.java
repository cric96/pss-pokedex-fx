package it.unibo.pokemon.core;

import java.util.Objects;

/**
 * The condition under which a pokemon evolves: reaching a level, using an item, or being traded.
 * Each kind of condition is a record with its own data and constraints.
 */
public interface EvolutionCondition {

    int MIN_LEVEL = 1;
    int MAX_LEVEL = 100;

    /**
     * Evolves when reaching the given level.
     * @param level the level to reach, between {@link #MIN_LEVEL} and {@link #MAX_LEVEL}
     */
    record LevelUp(int level) implements EvolutionCondition {
        public LevelUp {
            if (level < MIN_LEVEL || level > MAX_LEVEL) {
                throw new IllegalArgumentException("Level must be in [" + MIN_LEVEL + ", " + MAX_LEVEL + "]: " + level);
            }
        }
    }

    /**
     * Evolves when the given item is used on it.
     * @param item the name of the item (e.g., "Thunder Stone")
     */
    record UseItem(String item) implements EvolutionCondition {
        public UseItem {
            if (Objects.requireNonNull(item).isBlank()) {
                throw new IllegalArgumentException("The item must have a name");
            }
        }
    }

    /**
     * Evolves when traded with another trainer.
     */
    record Trade() implements EvolutionCondition { }
}
