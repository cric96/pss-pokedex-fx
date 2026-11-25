package it.unibo.pokemon.core;

import it.unibo.pokemon.core.EvolutionCondition.LevelUp;
import it.unibo.pokemon.core.impl.PokemonEntryImpl;

import static it.unibo.pokemon.core.impl.PokemonTypeEnum.ELECTRIC;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.FIRE;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.FLYING;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.GRASS;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.POISON;
import static it.unibo.pokemon.core.impl.PokemonTypeEnum.WATER;

/**
 * Species and evolutions shared by the tests.
 */
public final class PokemonFixtures {

    public static final PokemonEntry BULBASAUR = entry(1, "Bulbasaur", GRASS, POISON);
    public static final PokemonEntry CHARMANDER = entry(4, "Charmander", FIRE, null);
    public static final PokemonEntry CHARMELEON = entry(5, "Charmeleon", FIRE, null);
    public static final PokemonEntry CHARIZARD = entry(6, "Charizard", FIRE, FLYING);
    public static final PokemonEntry SQUIRTLE = entry(7, "Squirtle", WATER, null);
    /** Not part of {@link #CATALOGUE}. */
    public static final PokemonEntry PIKACHU = entry(25, "Pikachu", ELECTRIC, null);

    public static final Evolution CHARMANDER_TO_CHARMELEON = new Evolution(CHARMANDER, CHARMELEON, new LevelUp(16));
    public static final Evolution CHARMELEON_TO_CHARIZARD = new Evolution(CHARMELEON, CHARIZARD, new LevelUp(36));

    private PokemonFixtures() { }

    public static PokemonEntry entry(final int index, final String name, final PokemonType primary, final PokemonType secondary) {
        return new PokemonEntryImpl(index, name, "The pokemon " + name, 1.0, 1.0, primary, secondary, null);
    }
}
