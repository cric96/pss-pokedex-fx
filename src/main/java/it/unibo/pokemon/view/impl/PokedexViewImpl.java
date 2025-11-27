package it.unibo.pokemon.view.impl;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.controller.impl.PokedexControllerImpl;
import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokedexLoader;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.core.impl.PokedexImpl;
import it.unibo.pokemon.view.MockPokemons;
import it.unibo.pokemon.view.PokedexView;
import it.unibo.pokemon.view.panel.PokemonDetailPanel;
import it.unibo.pokemon.view.panel.PokemonListPanel;
import it.unibo.pokemon.view.panel.impl.PokemonDetailPanelImpl;
import it.unibo.pokemon.view.panel.impl.PokemonListPanelImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.util.List;

public class PokedexViewImpl extends Application implements PokedexView {

    private PokedexController controller;
    private PokemonListPanel listPanel;
    private PokemonDetailPanel detailPanel;

    @Override
    public void start(final Stage primaryStage) {
        // View preparation
        this.listPanel = new PokemonListPanelImpl();
        this.detailPanel = new PokemonDetailPanelImpl();
        final SplitPane root = new SplitPane(listPanel.getRoot(), detailPanel.getRoot());
        root.setDividerPositions(0.35);
        final Scene scene = new Scene(root, 900, 500);
        primaryStage.setTitle("Pokedex");
        primaryStage.setScene(scene);
        // Create controller
        this.showPokemons(MockPokemons.sample().stream().toList());
        this.detailPanel.setPokemon(MockPokemons.sample().stream().filter(PokemonEntry::isSeen).findFirst().get());
        // Set up event handlers
        // listPanel.setOnPokemonSelected(pokemon -> controller.showPokemon(pokemon.getIndex()));
        // listPanel.setOnSearch(controller::searchPokemon);
        // Show stage
        primaryStage.show();
    }

    @Override
    public void setController(final PokedexController controller) {
        this.controller = controller;
        // Set controller on detail panel for image loading
        detailPanel.setController(controller);
        // Show first pokemon
        this.controller.showPokemon(1);
    }

    @Override
    public PokedexController getController() {
        return controller;
    }

    @Override
    public void showPokemon(final PokemonEntry entry) {
        detailPanel.setPokemon(entry);
        listPanel.selectPokemon(entry);
    }

    @Override
    public void showPokemons(final List<PokemonEntry> entries) {
        listPanel.setPokemons(entries);
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
