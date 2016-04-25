package com.superduckinvaders.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.entity.Entity;
import com.superduckinvaders.game.entity.PhysicsEntity;
import com.superduckinvaders.game.entity.Player;
import com.superduckinvaders.game.entity.mob.Mob;

/**
 * Screen for interaction with the game.
 */
public class GameScreen extends BaseScreen {

    /**
     * The renderer for the tile map.
     */
    private OrthogonalTiledMapRenderer mapRenderer;
    private ShapeRenderer shapeRenderer;

    /**
     * The sprite batches for rendering.
     */
    private SpriteBatch spriteBatch, uiBatch;
    private OrthographicCamera uiCamera;
    private Viewport uiViewport;
    
    /**
     * Shaders
     */
    public ShaderProgram standardShader;
    public ShaderProgram dementedShader;

    /**
     * The Round this GameScreen renders.
     */
    private Round round;
    
    private float cameraMinX;
    private float cameraMinY;
    private float cameraMaxX;
    private float cameraMaxY;
    
    private float minimapCameraMinX;
    private float minimapCameraMinY;
    private float minimapCameraMaxX;
    private float minimapCameraMaxY;

    private int minimapX = 20;
    private int minimapY = 20;
    private int minimapWidth = 250;
    private int minimapHeight = 250;


    private OrthographicCamera minimapCamera;
    private Viewport minimapViewport;

    private float accumulator = 0f;
    private float step = 1/60f;

    
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;

    /**
     * Initialises this GameScreen for the specified round.
     *
     * @param game  the main game class
     * @param round the round to be displayed
     */
    public GameScreen(DuckGame game, Round round) {
        super(game);
        round.gameScreen = this;
        this.round = round;
    }

    /**
     * Converts screen coordinates to world coordinates.
     *
     * @param x the x coordinate on screen
     * @param y the y coordinate on screen
     * @return a Vector3 containing the world coordinates (x and y)
     */
    public Vector3 unproject(int x, int y) {
        return camera.unproject(new Vector3(x, y, 0));
    }

    /**
     * @return the Round currently on this GameScreen
     */
    public Round getRound() {
        return round;
    }

    /**
     * Shows this GameScreen. Called by libGDX to set up the graphics.
     */
    @Override
    public void show() {
        minimapCamera = new OrthographicCamera();
        minimapCamera.zoom = 2f;
        minimapViewport = new MinimapViewport();
        minimapViewport.setCamera(minimapCamera);

        viewport.setWorldSize(DuckGame.GAME_WIDTH / 2, DuckGame.GAME_HEIGHT / 2);
        minimapViewport.setWorldSize(DuckGame.GAME_HEIGHT / 2, DuckGame.GAME_HEIGHT / 2);
        
        /* These values are to get ensure the camera never shows
         * anything outside the map by preventing its position
         * being set closer to the edge than half of the screen
         */
        
        cameraMinX = viewport.getWorldWidth()  / 2;
        cameraMinY = viewport.getWorldHeight() / 2;
        
        cameraMaxX = round.getMapWidth() - cameraMinX;
        cameraMaxY = round.getMapHeight() - cameraMinY;
        
        minimapCameraMinX = minimapViewport.getWorldWidth();
        minimapCameraMinY = minimapViewport.getWorldHeight();
        
        minimapCameraMaxX = round.getMapWidth() - minimapCameraMinX;
        minimapCameraMaxY = round.getMapHeight() - minimapCameraMinY;

        spriteBatch = new SpriteBatch();

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false);

        uiBatch = new SpriteBatch();

        uiViewport = new FitViewport(DuckGame.GAME_WIDTH, DuckGame.GAME_HEIGHT, uiCamera);

        shapeRenderer = new ShapeRenderer();
        mapRenderer = new OrthogonalTiledMapRenderer(round.getMap(), spriteBatch);
        
        debugRenderer = new Box2DDebugRenderer();
        
        ///////////////////////NEW FOR ASSESSMENT 4////////////////////////////
        
        // Set up shaders
        ShaderProgram.pedantic = false;
        standardShader = null;
        dementedShader = new ShaderProgram(Gdx.files.internal("shaders/invert.vsh"), Gdx.files.internal("shaders/invert.fsh"));
//        System.out.println(shader.isCompiled() ? "shader compiled successfully" : shader.getLog());
        setShader(standardShader);
        ///////////////////////////////////////////////////////////////////////
    }

    @Override
    public void resize(int width, int height){
        super.resize(width, height);
        uiViewport.update(width, height, true);
        minimapViewport.update(width, height, false);
    }

    /**
     * Main game loop.
     *
     * @param delta how much time has passed since the last update
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        accumulator+=delta;
        while (accumulator>=step) {
            round.update(step);  // TODO(avinash): If round calls dispose, stop here.
            accumulator-=step;
        }
        Player player = round.getPlayer();
        
        float playerX = player.getX() + player.getWidth() / 2;
        float playerY = player.getY() + player.getHeight() / 2;
        
        
        // Centre the camera on the player.
        camera.position.set(
                Math.max(cameraMinX, Math.min(playerX, cameraMaxX)),
                Math.max(cameraMinY, Math.min(playerY, cameraMaxY)),
                0
        );
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        minimapCamera.position.set(
                Math.max(minimapCameraMinX, Math.min(playerX, minimapCameraMaxX)),
                Math.max(minimapCameraMinY, Math.min(playerY, minimapCameraMaxY)),
                0
        );
        minimapCamera.update();
        


        spriteBatch.begin();
        this.drawGame();
        spriteBatch.end();

//        this.drawDebug();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(new Matrix4(uiBatch.getProjectionMatrix()));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 0.6f);

        //Minimap underlay
        shapeRenderer.rect(minimapX-3, minimapY-3, minimapWidth+6, minimapHeight+6);

        shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.25f);

        //bottom right
        shapeRenderer.rect(1070, 3, 207, 120);

        //top left
        shapeRenderer.rect(5, 680, 400, 33);
        shapeRenderer.rect(5, 644, 130, 33);

        shapeRenderer.end();

        drawMiniMap();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        this.drawUI();


    }
    ///


    public void drawMiniMap() {
        spriteBatch.setColor(1, 1, 1, 0.7f);
        spriteBatch.begin();

        Vector3 screenPos = uiViewport.project(new Vector3(minimapX, minimapY, 0));
        // strange maths to accommodate non-uniform projections.
        Vector3 screenSize = uiViewport.project(
                    new Vector3(minimapX+minimapWidth, minimapX+minimapWidth, 0)
                ).sub(screenPos);

        minimapViewport.setScreenBounds(Math.round(screenPos.x),
                Math.round(screenPos.y),
                Math.round(screenSize.x),
                Math.round(screenSize.y));

        minimapViewport.apply();

        mapRenderer.setView(minimapCamera);
        drawMap();
        if (!DuckGame.session.noHitboxCheat)
        drawOverhang();


        Vector2 playerPos = round.getPlayer().getPosition();
        int width = Assets.minimapHead.getRegionWidth()*6;
        int height = Assets.minimapHead.getRegionHeight()*6;

        spriteBatch.draw(Assets.minimapHead, playerPos.x-width/2, playerPos.y-height/2, width, height);

        spriteBatch.end();
        spriteBatch.setColor(Color.WHITE);

        shapeRenderer.setProjectionMatrix(new Matrix4(spriteBatch.getProjectionMatrix()));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.9f, 0.2f, 0.2f, 0.7f);

        for (Entity entity : round.getEntities()) {
            if (entity instanceof Mob){
                Vector2 pos = entity.getCentre();
                shapeRenderer.circle(pos.x, pos.y, 10f);
            }
        }
        shapeRenderer.end();


    }

    private void drawMap() {
        for (TiledMapTileLayer layer : new TiledMapTileLayer[]{round.getBaseLayer(), round.getWaterLayer(), round.getBase2Layer(), round.getCollisionLayer()}) {
            if (layer != null) {
                mapRenderer.renderTileLayer(layer);
            }
        }

        // Render randomly-chosen obstacles layer.
        if (round.getObstaclesLayer() != null) {
            mapRenderer.renderTileLayer(round.getObstaclesLayer());
        }
    }

    private void drawOverhang() {
        // Render overhang layer (draws over the player).
        if (round.getOverhangLayer() != null) {
            mapRenderer.renderTileLayer(round.getOverhangLayer());
        }
    }

    /**
     * Draw the game level.
     */
    private void drawGame() {
        // Render base and collision layers.
        mapRenderer.setView(camera);
        drawMap();

        // Draw all entities.
        for (Entity entity : round.getEntities())
            entity.render(spriteBatch);
        
        if (!DuckGame.session.noHitboxCheat)
        drawOverhang();
    }

    /**
     * Draws a debug layer with helpful collision squares and stuff.
     */
    private void drawDebug() {
        debugMatrix = camera.combined.cpy().scl(PhysicsEntity.PIXELS_PER_METRE);
        debugRenderer.render(round.world, debugMatrix);
    }

    /**
     * Draw the static UI.
     */
    private void drawUI() {

        uiBatch.setProjectionMatrix(uiCamera.combined);
        uiViewport.apply();
        uiBatch.begin();

        Assets.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        Assets.font.draw(uiBatch, round.getObjective().getObjectiveString(), 10, 705);
        Assets.font.draw(uiBatch, "Score: " + round.getPlayer().getScore(), 10, 670);
        Assets.font.draw(uiBatch, Gdx.graphics.getFramesPerSecond() + " FPS", 10, 630);

        // Draw stamina bar (for flight);
        uiBatch.draw(Assets.staminaEmpty, 1080, 10);
        if (round.getPlayer().getFlyingTimer() > 0) {
            float remainingFlight = round.getPlayer().getFlyingTimer();
            float barFraction = Math.min(1,  (remainingFlight / Player.PLAYER_FLIGHT_TIME));
            Assets.staminaFull.setRegionWidth((int)Math.max(0f, barFraction*192));
        } else {
            Assets.staminaFull.setRegionWidth(0);
        }
        uiBatch.draw(Assets.staminaFull, 1080, 10);

        // Draw powerups.
        int i=0;
        for (Player.Pickup pickup : round.getPlayer().pickupMap.keySet()){
            TextureRegion texture = pickup.getTexture();
            float width = texture.getRegionWidth();
            float height = texture.getRegionHeight();
            uiBatch.draw(texture, 1080+(50*i++), 85, width*2, height*2);
        }

        for (int x = 0; x < round.getPlayer().getMaximumHealth(); x += 2) {
            TextureRegion heart;
            if(x+2 <= round.getPlayer().getCurrentHealth()) {
                heart = Assets.heartFull;
            }
            else if(x+1 <= round.getPlayer().getCurrentHealth()) {
                heart = Assets.heartHalf;
            }
            else {
                heart = Assets.heartEmpty;
            }
            uiBatch.draw(heart, x * 18 + 1080, 48);
        }

        uiBatch.end();
    }

    /**
     * Called to dispose libGDX objects used by this GameScreen.
     */
    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        debugRenderer.dispose();
        mapRenderer.dispose();
        spriteBatch.dispose();
        uiBatch.dispose();
    }

    private class MinimapViewport extends Viewport {
    }
    
    ///////////////////////NEW FOR ASSESSMENT 4////////////////////////////
    
    /**
     * Set the shader on the screen
     * 
     * @param shader The shader to use
     */
    public void setShader(ShaderProgram shader){
    	if (shader != mapRenderer.getBatch().getShader()){
    		mapRenderer.getBatch().setShader(shader);
        }
    }
    ///////////////////////////////////////////////////////////////////////
}
