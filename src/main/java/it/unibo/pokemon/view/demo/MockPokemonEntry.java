package it.unibo.pokemon.view.demo;

import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.PokemonType;

import java.util.Optional;

/**
 * A fake species, used to develop the view while the model is not available yet.
 */
final class MockPokemonEntry implements PokemonEntry {

    private static final String IMAGES_PATH = "/it/unibo/pokemon/images/%04d.png";

    private final int index;
    private final String name;
    private final PokemonType type;

    MockPokemonEntry(final int index, final String name, final String type) {
        this.index = index;
        this.name = name;
        this.type = () -> type;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "A fake description of " + name + ".";
    }

    @Override
    public double getWeight() {
        return 1.0;
    }

    @Override
    public double getHeight() {
        return 1.0;
    }

    @Override
    public PokemonType getPrimaryType() {
        return type;
    }

    @Override
    public Optional<PokemonType> getSecondaryType() {
        return Optional.empty();
    }

    @Override
    public String getImageUrl() {
        return String.format(IMAGES_PATH, index);
    }
}
