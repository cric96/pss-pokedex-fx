package it.unibo.pokemon.view.panel.impl;

import it.unibo.pokemon.core.EncounterStatus;
import it.unibo.pokemon.view.DisplayedPokemon;
import it.unibo.pokemon.view.panel.PokemonListPanel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class PokemonListPanelImpl implements PokemonListPanel {

    private static final double SPACING = 5;

    private final VBox root = new VBox();
    private final ListView<DisplayedPokemon> listView = new ListView<>();
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button("Search");
    private final Button clearButton = new Button("Clear");
    private Consumer<String> searchListener = query -> { };
    private Consumer<DisplayedPokemon> selectionListener = pokemon -> { };

    public PokemonListPanelImpl() {
        setupSearchBar();
        setupListView();
        root.getChildren().addAll(createSearchBar(), listView);
        VBox.setVgrow(listView, Priority.ALWAYS);
    }

    private void setupSearchBar() {
        searchField.setPromptText("Search Pokemon...");
        searchField.setOnAction(e -> performSearch());
        searchButton.setOnAction(e -> performSearch());
        clearButton.setOnAction(e -> {
            searchField.clear();
            searchListener.accept("");
        });
    }

    private HBox createSearchBar() {
        final HBox searchBar = new HBox(SPACING);
        searchBar.setPadding(new Insets(SPACING));
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBar.getChildren().addAll(searchField, searchButton, clearButton);
        return searchBar;
    }

    private void performSearch() {
        searchListener.accept(searchField.getText());
    }

    private void setupListView() {
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(final DisplayedPokemon item, final boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    final String name = item.isSeen() ? item.entry().getName() : "???";
                    final String caught = item.status() == EncounterStatus.CAUGHT ? "  ●" : "";
                    setText(String.format("#%03d %s%s", item.entry().getIndex(), name, caught));
                }
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                selectionListener.accept(newV);
            }
        });
    }

    @Override
    public Region getRoot() {
        return root;
    }

    @Override
    public void setPokemons(final List<DisplayedPokemon> pokemons) {
        listView.getItems().setAll(pokemons);
    }

    @Override
    public void setOnPokemonSelected(final Consumer<DisplayedPokemon> listener) {
        this.selectionListener = listener;
    }

    @Override
    public void setOnSearch(final Consumer<String> listener) {
        this.searchListener = listener;
    }

    @Override
    public void clearSearch() {
        searchField.clear();
    }

    @Override
    public void selectPokemon(final DisplayedPokemon pokemon) {
        listView.getSelectionModel().select(pokemon);
        listView.scrollTo(pokemon);
    }
}
