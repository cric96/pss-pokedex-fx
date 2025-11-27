package it.unibo.pokemon;

import it.unibo.pokemon.view.impl.PokedexViewImpl;
import javafx.application.Application;

public class Launcher {
    static void main(final String[] args) {
        Application.launch(PokedexViewImpl.class, args);
    }
}
