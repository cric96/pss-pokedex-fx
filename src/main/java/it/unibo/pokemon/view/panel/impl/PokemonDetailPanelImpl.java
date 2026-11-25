package it.unibo.pokemon.view.panel.impl;

import it.unibo.pokemon.core.EncounterStatus;
import it.unibo.pokemon.core.PokemonEntry;
import it.unibo.pokemon.view.DisplayedPokemon;
import it.unibo.pokemon.view.panel.PokemonDetailPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

public class PokemonDetailPanelImpl implements PokemonDetailPanel {

    private static final double SPACING = 10;
    private static final double IMAGE_SIZE = 150;
    private static final double DESCRIPTION_MAX_WIDTH = 250;
    private static final String FONT_FAMILY = "System";
    private static final double SMALL_FONT_SIZE = 12;
    private static final double MEDIUM_FONT_SIZE = 14;
    private static final double LARGE_FONT_SIZE = 18;

    private final VBox root = new VBox(SPACING);
    private final ImageView imageView = new ImageView();
    private final Label indexLabel = new Label();
    private final Label nameLabel = new Label();
    private final Label typeLabel = new Label();
    private final Label heightWeightLabel = new Label();
    private final Label descriptionLabel = new Label();
    private final Label statusLabel = new Label();
    private final Button markAsSeenButton = new Button("Mark as seen");
    private final Button markAsCaughtButton = new Button("Mark as caught");
    private Consumer<PokemonEntry> markAsSeenListener = pokemon -> { };
    private Consumer<PokemonEntry> markAsCaughtListener = pokemon -> { };
    private PokemonEntry displayed;

    public PokemonDetailPanelImpl() {
        setupStyles();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(SPACING));
        final HBox buttons = new HBox(SPACING, markAsSeenButton, markAsCaughtButton);
        buttons.setAlignment(Pos.CENTER);
        root.getChildren().addAll(
            imageView, indexLabel, nameLabel, typeLabel, heightWeightLabel, descriptionLabel, statusLabel, buttons
        );
        markAsSeenButton.setOnAction(e -> {
            if (displayed != null) {
                markAsSeenListener.accept(displayed);
            }
        });
        markAsCaughtButton.setOnAction(e -> {
            if (displayed != null) {
                markAsCaughtListener.accept(displayed);
            }
        });
        markAsSeenButton.setVisible(false);
        markAsCaughtButton.setVisible(false);
    }

    private void setupStyles() {
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);
        imageView.setPreserveRatio(true);
        indexLabel.setFont(Font.font(FONT_FAMILY, FontWeight.NORMAL, SMALL_FONT_SIZE));
        nameLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, LARGE_FONT_SIZE));
        typeLabel.setFont(Font.font(FONT_FAMILY, FontWeight.NORMAL, MEDIUM_FONT_SIZE));
        heightWeightLabel.setFont(Font.font(FONT_FAMILY, FontWeight.NORMAL, SMALL_FONT_SIZE));
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(DESCRIPTION_MAX_WIDTH);
    }

    @Override
    public Region getRoot() {
        return root;
    }

    @Override
    public void setOnMarkAsSeen(final Consumer<PokemonEntry> listener) {
        this.markAsSeenListener = listener;
    }

    @Override
    public void setOnMarkAsCaught(final Consumer<PokemonEntry> listener) {
        this.markAsCaughtListener = listener;
    }

    @Override
    public void setPokemon(final DisplayedPokemon pokemon) {
        this.displayed = pokemon.entry();
        indexLabel.setText(String.format("#%03d", displayed.getIndex()));
        markAsSeenButton.setVisible(pokemon.status() == EncounterStatus.UNSEEN);
        markAsCaughtButton.setVisible(pokemon.status() != EncounterStatus.CAUGHT);
        statusLabel.setText(switch (pokemon.status()) {
            case UNSEEN -> "Not seen yet";
            case SEEN -> "Seen";
            case CAUGHT -> "Caught";
        });
        if (pokemon.isSeen()) {
            showSeenPokemon(displayed);
        } else {
            showUnseenPokemon();
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

        imageView.setImage(null);
        if (pokemon.getImageUrl() != null) {
            final var imageStream = getClass().getResourceAsStream(pokemon.getImageUrl());
            if (imageStream != null) {
                imageView.setImage(new Image(imageStream));
            }
        }
    }

    private void showUnseenPokemon() {
        nameLabel.setText("???");
        typeLabel.setText("Type: ???");
        heightWeightLabel.setText("Height: ???  Weight: ???");
        descriptionLabel.setText("This Pokémon has not been seen yet.");
        imageView.setImage(null);
    }
}
