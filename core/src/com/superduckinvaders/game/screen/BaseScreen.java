package com.superduckinvaders.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.superduckinvaders.game.DuckGame;

import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * A Base screen that holds common objects to reduce boilerplate code.
 */
abstract public class BaseScreen extends ScreenAdapter {

    private DuckGame game;
    
    /**
     * The game camera.
     */
    protected OrthographicCamera camera;
    protected FitViewport viewport;

    /**
     * Start off the BaseScreen
     * @see BaseScreen#getGame
     * @param game The main game class
     */
    public BaseScreen(DuckGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(DuckGame.GAME_WIDTH, DuckGame.GAME_HEIGHT, camera);
    }

    /**
     * A helper method to get the game. If you find yourself using this often,
     * consider another utility method in BaseScreen.
     * @return The main game class
     */
    protected DuckGame getGame() {
        return this.game;
    }

    public Viewport getViewport() {
        return viewport;
    }
    
    @Override
    public void resize(int width, int height){
        viewport.update(width, height, false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
    }
}