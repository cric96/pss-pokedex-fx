package it.unibo.pokemon;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.controller.impl.PokedexControllerImpl;
import it.unibo.pokemon.core.Pokedex;
import it.unibo.pokemon.core.PokedexLoader;
import it.unibo.pokemon.core.TrainerProgress;
import it.unibo.pokemon.core.impl.TrainerProgressImpl;
import it.unibo.pokemon.view.PokedexView;
import it.unibo.pokemon.view.impl.PokedexViewImpl;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The entry point of the application: the only place that knows the concrete classes
 * of model, view and controller, creates them and wires them together.
 * Neither the view nor the controller create their collaborators.
 */
public final class PokedexApp extends Application {

    @Override
    public void start(final Stage primaryStage) {
        final Pokedex pokedex = PokedexLoader.loadPokedex();
        final TrainerProgress progress = new TrainerProgressImpl(pokedex);
        final PokedexView view = new PokedexViewImpl(primaryStage);
        final PokedexController controller = new PokedexControllerImpl(pokedex, progress, view);
        view.setController(controller);
        controller.start();
        view.show();
    }
}
