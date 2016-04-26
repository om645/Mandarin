package com.superduckinvaders.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.assets.Assets;

public class StartScreen extends BaseScreen {

    /**
     * Stage for containing the button.
     */
    private Stage stage;

    /**
     * Initialises this StartScreen.
     * @param game the game the screen is associated with
     */
    public StartScreen(DuckGame game) {
        super(game);
    }

    /**
     * Shows this GameScreen. Called by libGDX to set up the graphics.
     */
    @Override
    public void show() {
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Image logoImage = new Image(Assets.logo);
        logoImage.setPosition((stage.getWidth() - logoImage.getPrefWidth()) / 2, 400);

        Drawable button = new TextureRegionDrawable(Assets.button);

        Button playButton = new Button(new Button.ButtonStyle(button, button, button));
        playButton.setPosition((stage.getWidth() - playButton.getPrefWidth()) / 2, 300);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                getGame().setScreen(new GameScreen(getGame(), new Round(getGame())));

            }
        });

        Button mapButton = new Button(new Button.ButtonStyle(button, button, button));
        mapButton.setPosition((stage.getWidth() - mapButton.getPrefWidth()) / 2, 250);
        mapButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                getGame().setScreen(new MapScreen(getGame()));

            }
        });
        /////////////////////NEW FOR ASSESMENT 4///////////////////////////
        Button settingsButton = new Button(new Button.ButtonStyle(button, button, button));
        settingsButton.setPosition((stage.getWidth() - mapButton.getPrefWidth()) / 2, 200);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                getGame().setScreen(new SettingsScreen(getGame()));

            }
        });
        ////////////////////////////////////////////////////////////////////////////
        Label.LabelStyle white = new Label.LabelStyle(Assets.font, Color.WHITE);

        Label playLabel = new Label("START", white);
        playLabel.setPosition((stage.getWidth() - playLabel.getPrefWidth()) / 2, 315);
        playLabel.setTouchable(Touchable.disabled);

        Label mapLabel = new Label("LEVEL SELECT", white);
        mapLabel.setPosition((stage.getWidth() - mapLabel.getPrefWidth()) / 2, 265);
        mapLabel.setTouchable(Touchable.disabled);
        /////////////////////NEW FOR ASSESMENT 4///////////////////////////
        Label settingsLabel = new Label("CHEATS", white);
        settingsLabel.setPosition((stage.getWidth() - settingsLabel.getPrefWidth()) / 2, 215);
        settingsLabel.setTouchable(Touchable.disabled);
        /////////////////////////////////////////////////////////////////

        stage.addActor(logoImage);
        stage.addActor(playButton);
        stage.addActor(playLabel);
        stage.addActor(mapButton);
        stage.addActor(mapLabel);
        /////////////////////NEW FOR ASSESMENT 4///////////////////////////
        stage.addActor(settingsButton);
        stage.addActor(settingsLabel);
        ///////////////////////////////////////////////////////////////
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
     * Called to dispose libGDX objects used by this StartScreen.
     */
    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }
}
