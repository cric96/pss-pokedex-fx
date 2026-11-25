package it.unibo.pokemon.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unibo.pokemon.core.impl.PokedexImpl;
import it.unibo.pokemon.core.impl.PokemonEntryImpl;
import it.unibo.pokemon.core.impl.PokemonTypeEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builds a {@link Pokedex} from its JSON description.
 */
public class PokedexLoader {

    private static final String DEFAULT_POKEDEX_JSON_PATH = "/it/unibo/pokemon/pokedex.json";
    private static final String IMAGES_PATH = "/it/unibo/pokemon/images/";

    private final InputStream inputStream;

    public PokedexLoader(final InputStream inputStream) {
        this.inputStream = Objects.requireNonNull(inputStream, "Input stream cannot be null");
    }

    public static Pokedex loadPokedex() {
        final InputStream is = PokedexLoader.class.getResourceAsStream(DEFAULT_POKEDEX_JSON_PATH);
        if (is == null) {
            System.err.println("Cannot find pokedex.json");
            return new PokedexImpl(Collections.emptySet());
        }
        return new PokedexLoader(is).load();
    }

    /**
     * @return the pokedex described by the input stream
     * @throws IllegalStateException if the input cannot be read or is not a valid pokedex
     */
    public Pokedex load() {
        final Type pokemonListType = new TypeToken<List<PokemonJson>>() { }.getType();
        final List<PokemonJson> dataList;
        try (var reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            dataList = new Gson().fromJson(reader, pokemonListType);
        } catch (final IOException e) {
            throw new IllegalStateException("Cannot read the pokedex", e);
        }
        final Map<String, PokemonEntry> entriesByName = dataList.stream()
            .collect(Collectors.toMap(PokemonJson::name, PokedexLoader::createEntry));
        // In the JSON evolutions are referenced by name: resolve them to the entries.
        final List<Evolution> evolutions = dataList.stream()
            .filter(data -> data.evolvesFrom() != null)
            .map(data -> createEvolution(data, entriesByName::get))
            .toList();
        try {
            return new PokedexImpl(entriesByName.values(), evolutions);
        } catch (final IllegalArgumentException e) {
            throw new IllegalStateException("Invalid pokedex: " + e.getMessage(), e);
        }
    }

    private static PokemonEntry createEntry(final PokemonJson data) {
        return new PokemonEntryImpl(
            data.id(),
            data.name(),
            data.description(),
            data.weight(),
            data.height(),
            PokemonTypeEnum.valueOf(data.type1()),
            data.type2() == null ? null : PokemonTypeEnum.valueOf(data.type2()),
            IMAGES_PATH + String.format("%04d.png", data.id())
        );
    }

    private static Evolution createEvolution(final PokemonJson data, final Function<String, PokemonEntry> byName) {
        final EvolvesFromJson evolvesFrom = data.evolvesFrom();
        final PokemonEntry from = byName.apply(evolvesFrom.pokemon());
        if (from == null) {
            throw new IllegalStateException(data.name() + " evolves from an unknown pokemon: " + evolvesFrom.pokemon());
        }
        return new Evolution(from, byName.apply(data.name()), createCondition(data.name(), evolvesFrom));
    }

    private static EvolutionCondition createCondition(final String name, final EvolvesFromJson json) {
        final List<EvolutionCondition> conditions = Stream.<EvolutionCondition>of(
            json.level() == null ? null : new EvolutionCondition.LevelUp(json.level()),
            json.item() == null ? null : new EvolutionCondition.UseItem(json.item()),
            json.trade() ? new EvolutionCondition.Trade() : null
        ).filter(Objects::nonNull).toList();
        if (conditions.size() != 1) {
            throw new IllegalStateException("The evolution of " + name + " must have exactly one condition");
        }
        return conditions.getFirst();
    }

    private record PokemonJson(
        int id,
        String name,
        String description,
        String type1,
        String type2,
        double height,
        double weight,
        EvolvesFromJson evolvesFrom
    ) { }

    /**
     * Exactly one among level, item and trade must be set.
     */
    private record EvolvesFromJson(String pokemon, Integer level, String item, boolean trade) { }
}
