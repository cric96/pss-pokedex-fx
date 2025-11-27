package it.unibo.pokemon.view.panel.impl;

import it.unibo.pokemon.controller.PokedexController;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.view.panel.PokemonDetailPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PokemonDetailPanelImpl implements PokemonDetailPanel {

    private final VBox root = new VBox(10);
    private final ImageView imageView = new ImageView();
    private final Label indexLabel = new Label();
    private final Label nameLabel = new Label();
    private final Label typeLabel = new Label();
    private final Label heightWeightLabel = new Label();
    private final Label descriptionLabel = new Label();
    private PokedexController controller;

    public PokemonDetailPanelImpl() {
        setupStyles();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(imageView, indexLabel, nameLabel, typeLabel, heightWeightLabel, descriptionLabel);
    }

    private void setupStyles() {
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        indexLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        typeLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        heightWeightLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(250);
    }

    @Override
    public Region getRoot() {
        return root;
    }

    @Override
    public void setController(final PokedexController controller) {
        this.controller = controller;
    }

    @Override
    public void setPokemon(final PokemonEntry pokemon) {
        indexLabel.setText(String.format("#%03d", pokemon.getIndex()));
        if (pokemon.isSeen()) {
            showSeenPokemon(pokemon);
        } else {
            showUnseenPokemon(pokemon);
        }
    }

    private void showSeenPokemon(final PokemonEntry pokemon) {
        nameLabel.setText(pokemon.getName());
        
        final String types = pokemon.getSecondaryType()
            .map(secondary -> pokemon.getPrimaryType().getName() + " / " + secondary.getName())
            .orElse(pokemon.getPrimaryType().getName());
        typeLabel.setText("Type: " + types);
        
        heightWeightLabel.setText(String.format("Height: %.1fm  Weight: %.1fkg", pokemon.getHeight(), pokemon.getWeight()));
        descriptionLabel.setText(pokemon.getDescription());
        
        if (controller != null) {
            final var imageStream = controller.loadPokemonImageStream(pokemon);
            if (imageStream != null) {
                final Image image = new Image(imageStream);
                imageView.setImage(image);
                imageView.setOpacity(1.0);
            }
        }
    }

    private void showUnseenPokemon(final PokemonEntry pokemon) {
        nameLabel.setText("???");
        typeLabel.setText("Type: ???");
        heightWeightLabel.setText("Height: ???  Weight: ???");
        descriptionLabel.setText("This Pokémon has not been seen yet.");

        // Show silhouette (dark image)
        if (controller != null) {
            imageView.setImage(null);
        }
    }
}
