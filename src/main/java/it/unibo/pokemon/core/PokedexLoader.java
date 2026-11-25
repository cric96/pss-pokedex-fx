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
import java.util.Set;
import java.util.stream.Collectors;

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

    public Pokedex load() {
        final Type pokemonListType = new TypeToken<List<PokemonJsonData>>() { }.getType();
        final List<PokemonJsonData> dataList;
        try (var reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            dataList = new Gson().fromJson(reader, pokemonListType);
        } catch (final IOException e) {
            throw new IllegalStateException("Cannot read the pokedex", e);
        }
        // In the JSON evolutions are referenced by name: resolve them to indexes.
        final Map<String, Integer> indexByName = dataList.stream()
            .collect(Collectors.toMap(PokemonJsonData::name, PokemonJsonData::id));
        final List<PokemonEntry> entries = dataList.stream()
            .map(data -> createEntry(data, indexByName))
            .toList();
        final Set<Integer> seen = dataList.stream()
            .filter(PokemonJsonData::seen)
            .map(PokemonJsonData::id)
            .collect(Collectors.toSet());
        return new PokedexImpl(entries, seen);
    }

    private PokemonEntry createEntry(final PokemonJsonData data, final Map<String, Integer> indexByName) {
        final PokemonType secondaryType = data.type2() == null ? null : PokemonTypeEnum.valueOf(data.type2());
        final Integer preEvolution = data.preEvolution() == null ? null : indexByName.get(data.preEvolution());
        return new PokemonEntryImpl(
            data.id(),
            data.name(),
            data.description(),
            data.weight(),
            data.height(),
            PokemonTypeEnum.valueOf(data.type1()),
            secondaryType,
            preEvolution,
            IMAGES_PATH + String.format("%04d.png", data.id())
        );
    }

    private record PokemonJsonData(
        int id,
        String name,
        String description,
        String type1,
        String type2,
        double height,
        double weight,
        String preEvolution,
        boolean seen
    ) { }
}
