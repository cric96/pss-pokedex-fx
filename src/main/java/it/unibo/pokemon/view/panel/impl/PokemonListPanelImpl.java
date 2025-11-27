package it.unibo.pokemon.view.panel.impl;

import it.unibo.pokemon.core.PokemonEntry;
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

    private final VBox root = new VBox();
    private final ListView<PokemonEntry> listView = new ListView<>();
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button("Search");
    private final Button clearButton = new Button("Clear");
    private Consumer<String> searchListener;

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
            if (searchListener != null) {
                searchListener.accept("");
            }
        });
    }

    private HBox createSearchBar() {
        final HBox searchBar = new HBox(5);
        searchBar.setPadding(new Insets(5));
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBar.getChildren().addAll(searchField, searchButton, clearButton);
        return searchBar;
    }

    private void performSearch() {
        if (searchListener != null) {
            searchListener.accept(searchField.getText());
        }
    }

    private void setupListView() {
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(final PokemonEntry item, final boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item.isSeen()) {
                    setText(String.format("#%03d %s", item.getIndex(), item.getName()));
                } else {
                    setText(String.format("#%03d ???", item.getIndex()));
                }
            }
        });
    }

    @Override
    public Region getRoot() {
        return root;
    }

    @Override
    public void setPokemons(final List<PokemonEntry> pokemons) {
        listView.getItems().setAll(pokemons);
    }

    @Override
    public void setOnPokemonSelected(final Consumer<PokemonEntry> listener) {
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                listener.accept(newV);
            }
        });
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
    public void selectPokemon(PokemonEntry pokemon) {
        this.listView.getSelectionModel().select(pokemon);
    }
}
