package com.superduckinvaders.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

/**
 * Responsible for loading game assets.
 */
public class Assets {

    /**
     * The texture for the icon that represents the player on the minimap.
     */
    public static TextureRegion minimapHead;

    /**
     *  Player texture sets for normal and flying.
     */
    public static TextureSet playerNormal, playerGun, playerSwimming, playerFlying, playerSaber;
    public static TextureSet playerStaticAttackGun, playerStaticAttackSaber;
    public static TextureSet playerWalkingAttackGun, playerWalkingAttackSaber;

    public static TextureSet boss;

    /**
     *  Bad guy texture set.
     */
    public static TextureSet rangedBadGuyNormal, badGuyNormal,infectedBadGuyNormal;

    /**
     *  Texture for Projectile.
     */
    public static TextureRegion projectile;
    
    /**
     *  Textures for Hearts
     */
    public static TextureRegion heartFull, heartHalf, heartEmpty;

    /**
     *  Textures for stamina.
     */
    public static TextureRegion staminaFull, staminaEmpty;

    /**
     *  Animation for explosion.
     */
    public static Animation explosionAnimation;

    /**
     *  Tile maps for levels.
     */
    public static TiledMap levelOneMap, levelTwoMap, levelThreeMap, levelFourMap,
            levelFiveMap, levelSixMap, levelSevenMap, levelEightMap;

    public static TiledMap[] maps;
    /**
     *  The font for the UI.
     */
    public static BitmapFont font;

    /**
     * The texture for the button.
     */
    public static TextureRegion button;

    /**
     *  Textures for floor items.
     */
    public static TextureRegion floorItemGun, floorItemSaber, floorItemSpeed, floorItemInvulnerable, floorItemScore, floorItemFireRate, floorItemHeart;

    /**
     *  Texture for objective flag.
     */
    public static TextureRegion flag;

    /**
     *  Texture for the game logo.
     */
    public static TextureRegion logo;

    public static Sound gunShot;
    public static Music menuTheme, swimming;

    /**
     * Responsible for loading maps.
     */
    private static TmxMapLoader mapLoader = new TmxMapLoader();

    /**
     * Loads all assets.
     */
    public static void load() {
        loadPlayerTextureSets();
        loadBadGuyTextureSet();
        loadFloorItems();
        loadSFX();

        minimapHead = new TextureRegion(loadTexture("textures/minimap_head.png"));
        projectile  = new TextureRegion(loadTexture("textures/projectile.png")  );

        explosionAnimation = loadAnimation("textures/explosion.png", 2, 0.3f);

        levelOneMap = loadTiledMap("maps/map.tmx");
        levelTwoMap = loadTiledMap("maps/James.tmx");
        levelThreeMap = loadTiledMap("maps/Halifax.tmx");
        levelFourMap = loadTiledMap("maps/QuietPlace.tmx");
        levelFiveMap = loadTiledMap("maps/bridges.tmx");
        levelSixMap = loadTiledMap("maps/Library.tmx");
        levelSevenMap = loadTiledMap("maps/HesEast.tmx");
        levelEightMap = loadTiledMap("maps/Compsci.tmx");

        maps = new TiledMap[]{
                Assets.levelOneMap,
                Assets.levelTwoMap,
                Assets.levelThreeMap,
                Assets.levelFourMap,
                Assets.levelFiveMap,
                Assets.levelSixMap,
                Assets.levelSevenMap,
                Assets.levelEightMap
        };

        font = loadFont("Lato-Regular.ttf");

        TextureRegion[] hearts = TextureRegion.split(loadTexture("textures/hearts.png"), 32, 28)[0];
        heartFull  = hearts[0];
        heartHalf  = hearts[1];
        heartEmpty = hearts[2];

        TextureRegion[][] stamina = TextureRegion.split(loadTexture("textures/stamina.png"), 192, 28);
        staminaFull  = stamina[0][0];
        staminaEmpty = stamina[1][0];

        button = new TextureRegion(loadTexture("textures/button.png"));

        flag = new TextureRegion(loadTexture("textures/flag.png"));
        logo = new TextureRegion(loadTexture("textures/logo.png"));
    }

    private static void loadSFX(){
        gunShot = Gdx.audio.newSound(Gdx.files.internal("Gun.mp3"));
        menuTheme = Gdx.audio.newMusic(Gdx.files.internal("MenuTheme.ogg"));
        swimming = Gdx.audio.newMusic(Gdx.files.internal("swimming.mp3"));
        //MenuTheme.ogg is credited to SIMG, originally name Passionate.
    }

    /**
     * Loads assets relating to the player in the normal state.
     * If you change the player texture size, be sure to change the values here.
     */
    private static void loadPlayerTextureSets() {
        // Load idle texture map.
        TextureRegion[][] idleAll = TextureRegion.split(loadTexture("textures/player_idle_all.png"), 28, 18);

        TextureRegion[] idle         = idleAll[0];
        TextureRegion[] idleSwimming = idleAll[1];
        TextureRegion[] idleGun      = idleAll[2];
        TextureRegion[] idleSaber    = idleAll[3];

        // Load walking animations.
        Animation[] baseWalks     = loadAnimations("textures/player_walk_base_all.png",    28, 18, 0.2f);
        Animation[] gunWalks      = loadAnimations("textures/player_walk_gun_all.png",     28, 18, 0.2f);
        Animation[] saberWalks    = loadAnimations("textures/player_walk_saber_all.png",   28, 18, 0.08f);
        Animation[] flyingWalks   = loadAnimations("textures/player_flying_all.png",       28, 18, 0.2f);
        Animation[] swimmingWalks = loadAnimations("textures/player_swimming_all.png",     28, 18, 0.2f);
        Animation[] SaberAttacks  = loadAnimations("textures/player_walk_attack_saber_all.png", 28, 18, 0.08f);
        Animation[] SaberAttacksStatic  = loadAnimations("textures/player_static_attack_saber_all.png", 28, 18, 0.08f);
        Animation[] GunAttacks  = loadAnimations("textures/player_walk_attack_gun_all.png", 28, 18, 0.08f);
        Animation[] GunAttacksStatic  = loadAnimations("textures/player_static_attack_gun_all.png", 28, 18, 0.08f);

        playerNormal      = new TextureSet(idle,         baseWalks    );
        playerFlying      = new TextureSet(idle,         flyingWalks  );
        playerSwimming    = new TextureSet(idleSwimming, swimmingWalks);
        playerGun         = new TextureSet(idleGun,      gunWalks     );
        playerSaber       = new TextureSet(idleSaber,    saberWalks   );
        playerStaticAttackSaber = new TextureSet(SaberAttacksStatic);
        playerStaticAttackGun   = new TextureSet(GunAttacksStatic);
        playerWalkingAttackSaber = new TextureSet(SaberAttacks);
        playerWalkingAttackGun   = new TextureSet(GunAttacks);
    }

    /**
     * Loads the textures from the bad guy textures file.
     */
    private static void loadBadGuyTextureSet() {
        // Load idle texture map.
        TextureRegion[] idle = TextureRegion.split(loadTexture("textures/badguy_idle.png"), 21, 24)[0];

        // Load walking animations.
        Animation walkingFront = loadAnimation("textures/badguy_walking_front.png", 4, 0.2f);
        Animation walkingBack  = loadAnimation("textures/badguy_walking_back.png", 4, 0.2f);
        Animation walkingLeft  = loadAnimation("textures/badguy_walking_left.png", 4, 0.2f);
        Animation walkingRight = loadAnimation("textures/badguy_walking_right.png", 4, 0.2f);

        // Load idle texture map.
        TextureRegion[] rangedIdle = TextureRegion.split(loadTexture("textures/ranged_badguy_idle.png"), 21, 24)[0];

        // Load walking animations.
        Animation rangedWalkingFront = loadAnimation("textures/ranged_badguy_walking_front.png", 4, 0.2f);
        Animation rangedWalkingBack  = loadAnimation("textures/ranged_badguy_walking_back.png", 4, 0.2f);
        Animation rangedWalkingLeft  = loadAnimation("textures/ranged_badguy_walking_left.png", 4, 0.2f);
        Animation rangedWalkingRight = loadAnimation("textures/ranged_badguy_walking_right.png", 4, 0.2f);
        
        //Load idle texture map.
        TextureRegion[] infectedIdle = TextureRegion.split(loadTexture("textures/infected_badguy_idle.png"), 21, 24)[0];
        
        // Load walking animations.
        Animation infectedWalkingFront = loadAnimation("textures/infected_badguy_walking_front.png", 4, 0.2f);
        Animation infectedWalkingBack  = loadAnimation("textures/infected_badguy_walking_back.png", 4, 0.2f);
        Animation infectedWalkingLeft  = loadAnimation("textures/infected_badguy_walking_left.png", 4, 0.2f);
        Animation infectedWalkingRight = loadAnimation("textures/infected_badguy_walking_right.png", 4, 0.2f);

        badGuyNormal       = new TextureSet(idle[0], idle[1], idle[2], idle[3], walkingFront, walkingBack, walkingLeft, walkingRight);
        rangedBadGuyNormal = new TextureSet(rangedIdle[0], rangedIdle[1], rangedIdle[2], rangedIdle[3], rangedWalkingFront, rangedWalkingBack, rangedWalkingLeft, rangedWalkingRight);
        infectedBadGuyNormal = new TextureSet(infectedIdle[0],infectedIdle[1],infectedIdle[2],infectedIdle[3],infectedWalkingFront,infectedWalkingBack,infectedWalkingLeft,infectedWalkingRight);

        TextureRegion[] idleBoss = TextureRegion.split(loadTexture("textures/ranged_mechaboss_idle.png"), 42, 48)[0];

        boss = new TextureSet(idleBoss);
    }

    /**
     * Loads the texture from the floor items file.
     */
    public static void loadFloorItems() {
        TextureRegion[] items = TextureRegion.split(loadTexture("textures/floor_items.png"), 15, 15)[0];

        floorItemGun          = items[0];
        floorItemSaber        = items[1];
        floorItemSpeed        = items[2];
        floorItemInvulnerable = items[3];
        floorItemScore        = items[4];
        floorItemFireRate     = items[5];
        floorItemHeart        = items[6];
    }

    /**
     * Loads the texture from the specified file.
     *
     * @param file the file to load from
     * @return the texture
     */
    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    /**
     * Loads the tile map from the specified file.
     *
     * @param file the file to load from
     * @return the tile map
     */
    public static TiledMap loadTiledMap(String file) {
        return mapLoader.load(file);
    }

    /**
     * Loads the animation from the specified file.
     *
     * @param file          the file to load from
     * @param count         how many frames are in the file
     * @param frameDuration how long each frame should be shown for in seconds
     * @return the animation
     */
    public static Animation loadAnimation(String file, int count, float frameDuration) {
        Texture texture = loadTexture(file);
        int frameWidth = texture.getWidth()/count;
        Array<TextureRegion> keyFrames = new Array<>();

        for (int i = 0; i < count; i++) {
            keyFrames.add(new TextureRegion(texture, i * frameWidth, 0, frameWidth, texture.getHeight()));
        }

        return new Animation(frameDuration, keyFrames);
    }

    /**
     * Loads a set of animations from the specified file.
     *
     * @param file          the file to load from
     * @param frameWidth    the width of an individual frame
     * @param frameHeight   the height of an individual frame
     * @param frameDuration the time (in seconds) each frame spends on screen.
     * @return the animation
     */
    public static Animation[] loadAnimations(String file, int frameWidth, int frameHeight, float frameDuration) {
        Texture texture = loadTexture(file);

        TextureRegion[][] frames = TextureRegion.split(
                texture,
                frameWidth, frameHeight);
        int count = frames.length;

        Animation[] animations = new Animation[count];
        for (int i = 0; i < count; i++) {
            animations[i] = new Animation(frameDuration, new Array<>(frames[i]));
        }
        return animations;
    }

    /**
     * Loads the bitmap font from the specified files.
     *
     * @param fontFile the file containing information about the glyphs stored on the image file
     * @return the bitmap font
     */
    public static BitmapFont loadFont(String fontFile) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontFile));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 23;
        font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }


}
