package it.unibo.pokemon.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unibo.pokemon.core.impl.PokedexImpl;
import it.unibo.pokemon.core.impl.PokemonEntryImpl;
import it.unibo.pokemon.core.impl.PokemonTypeEnum;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
        final Gson gson = new Gson();
        final Type pokemonListType = new TypeToken<List<PokemonJsonData>>() {}.getType();
        final List<PokemonJsonData> dataList = gson.fromJson(new InputStreamReader(inputStream), pokemonListType);

        final Map<String, PokemonEntry> byName = new HashMap<>();
        for (final PokemonJsonData data : dataList) {
            byName.put(data.name(), createEntry(data, Collections.emptyMap()));
        }

        final Set<PokemonEntry> entries = new HashSet<>();
        for (final PokemonJsonData data : dataList) {
            entries.add(createEntry(data, byName));
        }

        return new PokedexImpl(entries);
    }

    private PokemonEntry createEntry(final PokemonJsonData data, final Map<String, PokemonEntry> evolutions) {
        final PokemonType secondaryType = data.type2() == null ? null : PokemonTypeEnum.valueOf(data.type2());
        final PokemonEntry preEvolution = data.preEvolution() != null && evolutions.containsKey(data.preEvolution())
                ? evolutions.get(data.preEvolution())
                : null;
        final List<PokemonEntry> postEvolutions = data.postEvolutions() == null ? Collections.emptyList()
                : data.postEvolutions().stream()
                    .filter(evolutions::containsKey)
                    .map(evolutions::get)
                    .collect(Collectors.toList());

        return new PokemonEntryImpl(
            data.id(),
            data.name(),
            data.description(),
            data.weight(),
            data.height(),
            PokemonTypeEnum.valueOf(data.type1()),
            secondaryType,
            preEvolution,
            postEvolutions,
            IMAGES_PATH + String.format("%04d.png", data.id()),
            data.seen()
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
        List<String> postEvolutions,
        boolean seen
    ) {}
}
