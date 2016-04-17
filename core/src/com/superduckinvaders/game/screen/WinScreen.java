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
import com.superduckinvaders.game.assets.Assets;

/**
 * Screen for displaying when a player has won.
 */
public class WinScreen extends BaseScreen {

    /**
     * Stage for containing the button.
     */
    private Stage stage;

    /**
     * The final score to display on the WinScreen.
     */
    private int score;

    /**
     * Initialises this WinScreen to display the final score.
     *
     * @param game the game the screen is associated with
     * @param score the final score to display
     */
    public WinScreen(DuckGame game, int score) {
        super(game);
        this.score = score;
    }

    /**
     * Shows this GameScreen. Called by libGDX to set up the graphics.
     */
    @Override
    public void show() {
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Drawable drawable = new TextureRegionDrawable(Assets.button);

        Button backButton = new Button(new Button.ButtonStyle(drawable, drawable, drawable));
        backButton.setPosition((stage.getWidth() - backButton.getPrefWidth()) / 2, 220);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new StartScreen(getGame()));
            }
        });

        Button nextLevelButton = new Button(new Button.ButtonStyle(drawable, drawable, drawable));
        nextLevelButton.setPosition((stage.getWidth() - backButton.getPrefWidth()) / 2, 320);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MapScreen(getGame()));
            }
        });


        Label.LabelStyle green = new Label.LabelStyle(Assets.font, Color.GREEN);
        Label.LabelStyle white = new Label.LabelStyle(Assets.font, Color.WHITE);

        Label titleLabel = new Label("You win!", green);
        titleLabel.setPosition((stage.getWidth() - titleLabel.getPrefWidth()) / 2, 500);

        Label scoreLabel = new Label("Final score: " + score, green);
        scoreLabel.setPosition((stage.getWidth() - scoreLabel.getPrefWidth()) / 2, 460);

        Label backLabel = new Label("Back to start screen", white);
        backLabel.setPosition((stage.getWidth() - backLabel.getPrefWidth()) / 2, 235);
        backLabel.setTouchable(Touchable.disabled);

        Label nextLevelLabel = new Label("Next Level", white);
        nextLevelLabel.setPosition((stage.getWidth() - backLabel.getPrefWidth())* 55 / 100, 335);
        nextLevelLabel.setTouchable(Touchable.disabled);

        stage.addActor(backButton);
        stage.addActor(titleLabel);
        stage.addActor(scoreLabel);
        stage.addActor(backLabel);
        stage.addActor(nextLevelButton);
        stage.addActor(nextLevelLabel);
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
     * Called to dispose libGDX objects used by this GameScreen.
     */
    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }
}
