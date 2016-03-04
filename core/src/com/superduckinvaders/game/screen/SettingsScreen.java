package com.superduckinvaders.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.assets.Assets;

/**
 * Shows a list of buttons that take you to unlocked levels.
 */
public class SettingsScreen extends BaseScreen {

    /**
     * A Scene2D stage to add UI elements to.
     */
    private Stage stage;



    /**
     * Start off the BaseScreen
     *
     * @param game The main game class
     * @see BaseScreen#getGame
     */
    public SettingsScreen(DuckGame game) {
        super(game);
    }

    /**
     * Shows this GameScreen. Called by libGDX to set up the graphics.
     */
    @Override
    public void show() {
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Drawable button = new TextureRegionDrawable(Assets.button);

        Label.LabelStyle white = new Label.LabelStyle(Assets.font, Color.WHITE);
        Label.LabelStyle red = new Label.LabelStyle(Assets.font, Color.RED);

        Button startButton = new Button(new Button.ButtonStyle(button, button, button));
        startButton.setPosition((stage.getWidth() - startButton.getPrefWidth()) /2, 585);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                getGame().setScreen(new StartScreen(getGame()));
            }
        });

        Label startLabel = new Label("RETURN TO START", white);
        startLabel.setPosition((stage.getWidth() - startLabel.getPrefWidth()) / 2, 600);
        startLabel.setTouchable(Touchable.disabled);
        
        // Rapid fire cheat button
        Button shootingButton = new Button(new Button.ButtonStyle(button, button, button));
        shootingButton.setPosition((stage.getWidth() - shootingButton.getPrefWidth()) /2, 485);
        shootingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                getGame().session.shootingCheat=!getGame().session.shootingCheat;
                getGame().setScreen(new SettingsScreen(getGame()));
            }
        });
        
        String shootingLabelText="RAPID FIRE: ";
        
        if (getGame().session.shootingCheat) shootingLabelText+="ON";
        else shootingLabelText+="OFF";
        Label shootingLabel = new Label(shootingLabelText, white);
        shootingLabel.setPosition((stage.getWidth() - shootingLabel.getPrefWidth()) / 2, 500);
        shootingLabel.setTouchable(Touchable.disabled);
        
        
        // No hitbox cheat button
        Button noHitboxButton = new Button(new Button.ButtonStyle(button, button, button));
        noHitboxButton.setPosition((stage.getWidth() - noHitboxButton.getPrefWidth()) /2, 370);
        noHitboxButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                getGame().session.noHitboxCheat=!getGame().session.noHitboxCheat;
                getGame().setScreen(new SettingsScreen(getGame()));
            }
        });
        
        String noHitboxButtonText="WALK THROUGH WALLS: ";
        
        if (getGame().session.noHitboxCheat) noHitboxButtonText+="ON";
        else shootingLabelText+="OFF";
        Label noHitboxLabel = new Label(noHitboxButtonText, white);
        noHitboxLabel.setPosition((stage.getWidth() - shootingLabel.getPrefWidth()) / 2, 385);
        noHitboxLabel.setTouchable(Touchable.disabled);

        stage.addActor(startButton);
        stage.addActor(startLabel);
        stage.addActor(shootingButton);
        stage.addActor(shootingLabel);
        stage.addActor(noHitboxButton);
        stage.addActor(noHitboxLabel);
    }

    /**
     * Main screen loop.
     *
     * @param delta how much time has passed since the last update
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }

    /**
     * Called to dispose libGDX objects used by this LoseScreen.
     */
    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }
}
