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
public class MapScreen extends BaseScreen {

    /**
     * A Scene2D stage to add UI elements to.
     */
    private Stage stage;

    public class CustomClickListener extends ClickListener{
        public int level;

        public CustomClickListener(int level){
            this.level = level;
        }

        @Override
        public void clicked(InputEvent event, float x, float y){
            dispose();
            DuckGame.session.setLevel(level);
            getGame().setScreen(new GameScreen(getGame(), new Round(getGame())));

        }
    }


    /**
     * Start off the BaseScreen
     *
     * @param game The main game class
     * @see BaseScreen#getGame
     */
    public MapScreen(DuckGame game) {
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
        startButton.setPosition((stage.getWidth() - startButton.getPrefWidth()) * 45 /100, 585);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                getGame().setScreen(new StartScreen(getGame()));
            }
        });

        Label startLabel = new Label("RETURN TO START", white);
        startLabel.setPosition((stage.getWidth() - startLabel.getPrefWidth()) * 46 / 100, 600);
        startLabel.setTouchable(Touchable.disabled);


        float[] columns = new float[]{
                2 / 10f,
                7 / 10f
        };

        float[] rows = new float[]{
                485f,
                385f,
                285f,
                185f
        };

        float[] labelColumns = new float[]{
                27 / 100f,
                65 / 100f
        };

        float[] labelRows = new float[]{
                500f,
                400f,
                300f,
                200f
        };

        float stageWidth = stage.getWidth();// - Assets.button.getRegionWidth();

        for (int i = 0 ; i < 8 ; i++){
            int column = i % 2;
            int row    = i / 2;
            String labelText;
            Label.LabelStyle labelColor;
            Button bttn = new Button(new Button.ButtonStyle(button, button, button));
            if (DuckGame.session.isUnlocked(i+1)) {
                bttn.addListener(new CustomClickListener(i+1));
                labelText = "LEVEL "+(i+1);
                labelColor = white;
            }
            else {
                labelText = "LOCKED";
                labelColor = red;
            }
            bttn.setPosition((stageWidth-bttn.getPrefWidth())*columns[column], rows[row]);

            Label label = new Label(labelText, labelColor);
            label.setPosition((stageWidth-label.getPrefWidth())*labelColumns[column], labelRows[row]);
            label.setTouchable(Touchable.disabled);

            stage.addActor(bttn);
            stage.addActor(label);
        }

        stage.addActor(startButton);
        stage.addActor(startLabel);
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
