package it.unibo.pokemon.view.impl;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.view.DisplayedPokemon;
import it.unibo.pokemon.view.PokedexView;
import it.unibo.pokemon.view.panel.PokemonDetailPanel;
import it.unibo.pokemon.view.panel.PokemonListPanel;
import it.unibo.pokemon.view.panel.impl.PokemonDetailPanelImpl;
import it.unibo.pokemon.view.panel.impl.PokemonListPanelImpl;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class PokedexViewImpl implements PokedexView {

    private static final String TITLE = "Pokedex";
    private static final double WINDOW_WIDTH = 900;
    private static final double WINDOW_HEIGHT = 500;
    /** Fraction of the window width taken by the list (the rest is for the details). */
    private static final double LIST_WIDTH_FRACTION = 0.35;

    private final Stage stage;
    private final PokemonListPanel listPanel = new PokemonListPanelImpl();
    private final PokemonDetailPanel detailPanel = new PokemonDetailPanelImpl();
    private PokedexController controller;

    public PokedexViewImpl(final Stage stage) {
        this.stage = Objects.requireNonNull(stage);
        final SplitPane root = new SplitPane(listPanel.getRoot(), detailPanel.getRoot());
        root.setDividerPositions(LIST_WIDTH_FRACTION);
        stage.setTitle(TITLE);
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    @Override
    public void setController(final PokedexController controller) {
        this.controller = Objects.requireNonNull(controller);
        listPanel.setOnPokemonSelected(pokemon -> controller.detailsForPokemon(pokemon.entry().getIndex()));
        listPanel.setOnSearch(controller::searchPokemon);
        detailPanel.setOnMarkAsSeen(pokemon -> controller.markAsSeen(pokemon.getIndex()));
        detailPanel.setOnMarkAsCaught(pokemon -> controller.markAsCaught(pokemon.getIndex()));
    }

    @Override
    public PokedexController getController() {
        return controller;
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void showPokemon(final DisplayedPokemon pokemon) {
        detailPanel.setPokemon(pokemon);
        listPanel.selectPokemon(pokemon);
    }

    @Override
    public void showPokemons(final List<DisplayedPokemon> pokemons) {
        listPanel.setPokemons(pokemons);
    }

    @Override
    public void showError(final String message) {
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
