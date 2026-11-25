package it.unibo.pokemon.view.demo;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.view.PokedexView;
import it.unibo.pokemon.view.impl.PokedexViewImpl;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Starts the JavaFX view with a fake controller and fake data:
 * the view can be developed and tried without the model.
 */
public final class ViewDemo extends Application {

    @Override
    public void start(final Stage primaryStage) {
        final PokedexView view = new PokedexViewImpl(primaryStage);
        final PokedexController controller = new StubController(view);
        view.setController(controller);
        controller.start();
        view.show();
    }
}
