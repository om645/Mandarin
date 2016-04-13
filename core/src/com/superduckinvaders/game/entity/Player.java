package com.superduckinvaders.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.assets.TextureSet;
import com.superduckinvaders.game.screen.GameScreen;

import java.util.EnumMap;
import java.util.Map;
import java.io.Console;
import java.lang.Character;

/**
 * Represents the player of the game.
 */
public class Player extends TheCharacter {

	
    /**
     * TEXTURE_OFFSET is a quick way to make certain animations less jumpy.
     */
    public static final Vector2 TEXTURE_OFFSET = new Vector2(-8, 0);

    /**
     * Player's maximum health.
     */
    public static final int PLAYER_HEALTH = 8;

    /**
     * Player's standard movement speed in pixels per second.
     */
    public static final float PLAYER_SPEED = 16f;

    /**
     * How much the Player's score increases should be multiplied by if they have the score multiplier powerup.
     */
    public static final float PLAYER_SCORE_MULTIPLIER = 5f;

    /**
     * How much the Player's speed should be multiplied by if they have the super speed powerup.
     */
    public static final float PLAYER_SUPER_SPEED_MULTIPLIER = 3f;

    /**
     * How much the Player's speed should be multiplied by if they are flying.
     */
    public static final float PLAYER_FLIGHT_SPEED_MULTIPLIER = 2f;

    /**
     * How much the Player's speed should be multiplied by if they are swimming.
     */
    public static final float PLAYER_SWIMMING_SPEED_MULTIPLIER = 1.5f;

    /**
     * How much the Player's attack rate should be multiplied by if they have the rate of fire powerup.
     */
    public static final float PLAYER_RANGED_ATTACK_MULTIPLIER = 5f;

    /**
     * How long the Player can fly for, in seconds.
     */
    public static final float PLAYER_FLIGHT_TIME = 1f;

    /**
     * The player's current state.
     */
    public State state = State.DEFAULT;

    /**
     * The time spent in the attack animation. This is used to render the correct frame of attackAnimation.
     */
    private float attackAnimationTimer = 0f;

    /**
     * The attack animation
     */
    private Animation attackAnimation;

    /**
     * Player's current score.
     */
    private int points = 0;

    /**
     * A map of pickups the player currently has, where the values are the time left for each one.
     */
    public EnumMap<Pickup, Float> pickupMap = new EnumMap<>(Pickup.class);

    /**
     * Shows if a player is flying. If less than 0, player is flying for -flyingTimer seconds. If less than PLAYER_FLIGHT_COOLDOWN, flying is on cooldown.
     */
    private float flyingTimer = 5;

    /**
     * Keeps track of the water blocks encountered, to know when to stop swimming (when this variable is 0).
     */
    public int waterBlockCount = 0;

    /**
     * The weapon currently being used.
     */
    protected Pickup currentWeapon = Pickup.GUN;
    

    /**
     * Demention timer.
     */
    public float dementionTimer = 0;

    /**
     * Initialises this Player at the specified coordinates and with the specified initial health.
     *
     * @param parent the round this Player belongs to
     * @param x      the initial x coordinate
     * @param y      the initial y coordinate
     */
    public Player(Round parent, float x, float y) {
        super(parent, x, y, PLAYER_HEALTH);
        enemyBits = MOB_BITS | PROJECTILE_BITS;
        MELEE_RANGE = 40f;
        MELEE_ATTACK_COOLDOWN = 0.2f;
        STUNNED_DURATION = 0f;
        createDynamicBody(PLAYER_BITS, ALL_BITS, NO_GROUP, false);
        if(DuckGame.session.shootingCheat)   pickupMap.put(Pickup.RATE_OF_FIRE, Float.POSITIVE_INFINITY);
    }

    /**
     * Increases the Player's score by the specified amount.
     *
     * @param amount the amount to increase the score by
     */
    public void addScore(int amount) {
        if (hasPickup(Pickup.SCORE_MULTIPLIER)) amount *= PLAYER_SCORE_MULTIPLIER;
        points += amount;
    }

    /**
     * Gets the Player's current score.
     *
     * @return the current score
     */
    public int getScore() {
        return points;
    }

    /**
     * Gets the Player's current flying timer.
     *
     * @return the current flying timer
     */
    public float getFlyingTimer() {
        return flyingTimer;
    }

    /**
     * @return whether the player is flying
     */
    public boolean isFlying() {
        return flyingTimer > 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }

    /**
     * @return whether the player is swimming
     */
    public boolean isSwimming() {
        return waterBlockCount > 0 && !isFlying();
    }

    /**
     * @return the width of this Player
     */
    @Override
    public float getWidth() {
        return 12;
    }

    /**
     * @return the height of this Player
     */
    @Override
    public float getHeight() {
        return 18;
    }

    /**
     * Damages the Player, taking into account the possibility of invulnerability.
     *
     * @param health the number of points to damage
     */
    @Override
    public void damage(int health) {
        // Only apply damage if we don't have the invulnerability powerup.
        if (!pickupMap.containsKey(Pickup.INVULNERABLE)) {
            super.damage(health);
        }
    }

    /**
     * Give a pickup to the player for a certain amount of time.
     * @param pickup   the pickup
     * @param duration the amount of time the pickup applies.
     */
    public void givePickup(Pickup pickup, float duration){
    	if (pickupMap.containsKey(pickup))
    	{
    		if (pickupMap.get(pickup)!=Float.POSITIVE_INFINITY)
    		pickupMap.put(pickup, duration);
    	}
    	else pickupMap.put(pickup, duration);
    }

    /**
     * @param pickup the pickup to test for.
     * @return whether the player has the given pickup.
     */
    public boolean hasPickup(Pickup pickup){
        return pickupMap.containsKey(pickup);
    }

    @Override
    protected boolean meleeAttack(Vector2 direction, int damage,boolean infected) {
        if (super.meleeAttack(direction, damage,infected)) {
            currentWeapon = Pickup.LIGHTSABER;
            setAttackAnimation((stateTime > 0 ? Assets.playerWalkingAttackSaber :Assets.playerStaticAttackSaber)
                    .getAnimation(facing));
            return true;
        }
        return false;
    }

    @Override
    protected boolean rangedAttack(Vector2 direction, int damage) {
        if (super.rangedAttack(direction, damage)) {
            currentWeapon = Pickup.GUN;
            setAttackAnimation((stateTime > 0 ? Assets.playerWalkingAttackGun : Assets.playerStaticAttackGun)
                    .getAnimation(facing));
            return true;
        }
        return false;
    }

    /**
     * Starts the attack animation.
     * @param animation the attack animation.
     */
    private void setAttackAnimation(Animation animation) {
        this.attackAnimationTimer = 0f;
        this.attackAnimation = animation;
    }

    /**
     * @return the frame of the attack animation we are currently in.
     */
    private TextureRegion getAttackAnimationFrame() {
        if (attackAnimation == null || attackAnimation.isAnimationFinished(attackAnimationTimer)){
            return null;
        }
        else {
            return attackAnimation.getKeyFrame(attackAnimationTimer);
        }
    }

    /**
     * Updates the state of this Player.
     *
     * @param delta how much time has passed since the last update
     */
    @Override
    public void update(float delta) {
        attackAnimationTimer += delta;
        
        if (this instanceof Player && ((Player)this).dementionTimer>0)
        {
        	if (((Player)this).dementionTimer-delta<0) {
        		((Player)this).dementionTimer=0;
            	parent.gameScreen.setShader(parent.gameScreen.standardShader);
        	} else {
        		((Player)this).dementionTimer-=delta;
        	}
        }

        if (isFlying()){
            state = State.FLYING;
        }
        else if (isSwimming()){

            boolean  isPlaying = Assets.swimming.isPlaying();
            if (isPlaying == false){
                Assets.swimming.play();



            }
            state = State.SWIMMING;
        }else if (currentWeapon == Pickup.GUN && hasPickup(Pickup.GUN)) {
            state = State.HOLDING_GUN;
        } else if (currentWeapon == Pickup.LIGHTSABER && hasPickup(Pickup.LIGHTSABER)) {
            state = State.HOLDING_SABER;
        } else {
            state = State.DEFAULT;
        }

        if (hasPickup(Pickup.RATE_OF_FIRE)){
            rangedAttackTimer+=delta*(PLAYER_RANGED_ATTACK_MULTIPLIER-1);
        }

        if (hasPickup(Pickup.HEALTH)) {
            heal(2);
        }


        // Decrement pickup timer.
        for (Map.Entry<Pickup, Float> entry : pickupMap.entrySet()){
            float value = entry.getValue();
            if (value <= 0) {
                pickupMap.remove(entry.getKey());
            }
            else {
                entry.setValue(value - delta);
            }
        }


        if (! isFlying() && !isSwimming()) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && hasPickup(Pickup.LIGHTSABER)) {
                Vector3 target = parent.unproject(Gdx.input.getX(), Gdx.input.getY());
                meleeAttack(vectorTo(new Vector2(target.x, target.y)), 1,false);
            }
            else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && hasPickup(Pickup.GUN)) {
                Vector3 target = parent.unproject(Gdx.input.getX(), Gdx.input.getY());
                rangedAttack(vectorTo(new Vector2(target.x, target.y)), 1);
            }
        }

        // Press space to start flying, but only if flying isn't cooling down and we're moving.
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (flyingTimer > 0){
                flyingTimer -= delta;
            }
        }
        else {
            flyingTimer = Math.min((flyingTimer+(delta*0.2f)), PLAYER_FLIGHT_TIME);
        }


        // Left/right movement.
        
        Vector2 targetVelocity = new Vector2();
        
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        	if (getPosition().x - getWidth() > 0){
        		targetVelocity.x = -1f; // Was +=, change back?
        	} else {
        		targetVelocity.x = 0f;
        	}
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        	if (getPosition().x + 2*getWidth() < parent.getMapWidth()){
                targetVelocity.x = 1f; // Was +=, change back?
        	} else {
        		targetVelocity.x = 0f;
        	}
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        	if (getPosition().y + getHeight()*1.5 < parent.getMapHeight()){
        		targetVelocity.y = 1f;
        	} else {
        		targetVelocity.y = 0f;
        	}
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        	if (getPosition().y - getHeight()/2 > 0){
        		targetVelocity.y = -1f;
        	} else {
        		targetVelocity.y = 0f;
        	}
        }
        
        // Demented mode
        if (dementionTimer>0) // TODO Currently it is possible to leave the edge of the screen in demented mode because the checking is based on the key you press and this just reverses it.
        {
        	targetVelocity.x=-targetVelocity.x;
        	targetVelocity.y=-targetVelocity.y;
        }
        
        // Calculate speed at which to move the player.
        float speed = PLAYER_SPEED * (hasPickup(Pickup.SUPER_SPEED) ? PLAYER_SUPER_SPEED_MULTIPLIER : 1);

        if (state == State.FLYING){
            speed *= PLAYER_FLIGHT_SPEED_MULTIPLIER;
        }
        else if (state == State.SWIMMING){
            speed *= PLAYER_SWIMMING_SPEED_MULTIPLIER;
        }
        
        targetVelocity.setLength(speed);
        setVelocity(targetVelocity, state == State.SWIMMING ? 1f : 4f);
        

        // Update movement.
        super.update(delta);
    }


    /**
     * Renders this Player.
     *
     * @param spriteBatch the sprite batch on which to render
     */
    @Override
    public void render(SpriteBatch spriteBatch) {
        Vector2 pos = getPosition().add(TEXTURE_OFFSET);
        TextureRegion attackTexture = getAttackAnimationFrame();
        if (attackTexture != null){
            spriteBatch.draw(attackTexture, pos.x, pos.y);
        }
        else {
            TextureSet textureSet = state.getTextureSet();
            spriteBatch.draw(textureSet.getTexture(facing, stateTime), pos.x, pos.y);
        }
    }

    /**
     * The various positions and ways we can show our player.
     */
    public enum State {
        DEFAULT       (Assets.playerNormal),
        HOLDING_GUN   (Assets.playerGun),
        HOLDING_SABER (Assets.playerSaber),
        SWIMMING      (Assets.playerSwimming),
        FLYING        (Assets.playerFlying);

        private final TextureSet textureSet;

        State (TextureSet textureSet){
            this.textureSet = textureSet;
        }

        public TextureSet getTextureSet(){
            return this.textureSet;
        }
    }

    /**
     * Available pickups.
     */
    public enum Pickup {
        GUN               (Assets.floorItemGun,          Float.POSITIVE_INFINITY),
        LIGHTSABER        (Assets.floorItemSaber,        Float.POSITIVE_INFINITY),
        SCORE_MULTIPLIER  (Assets.floorItemScore,        30                     ),
        SUPER_SPEED       (Assets.floorItemSpeed,        10                     ),
        RATE_OF_FIRE      (Assets.floorItemFireRate,     60                     ),
        HEALTH            (Assets.floorItemHeart,        0                      ),
        INVULNERABLE      (Assets.floorItemInvulnerable, 10                     );

    
        private final TextureRegion texture;
        private final float duration;

        Pickup(TextureRegion texture, float duration){
            this.texture = texture;
            this.duration = duration;
        }
        
        public TextureRegion getTexture() {
            return texture;
        }
        public float getDuration() {
            return duration;
        }

        public static Pickup random(){
            float random = MathUtils.random();
            Pickup pickup = null;

            if (random < 0.05) {
                pickup = Pickup.SCORE_MULTIPLIER;
            } else if (random >= 0.05 && random < 0.1) {
                pickup = Pickup.INVULNERABLE;
            } else if (random >= 0.1 && random < 0.15) {
                pickup = Pickup.SUPER_SPEED;
            } else if (random >= 0.15 && random < 0.2) {
                pickup = Pickup.RATE_OF_FIRE;
            } else {
                pickup = Pickup.HEALTH;
            }

            return pickup;
        }
    }
    
    // Remove collision detection with cheat
    @Override
    public void preSolve(PhysicsEntity other, Contact contact, Manifold manifold) {
    	if (DuckGame.session.noHitboxCheat){
    		contact.setEnabled(false);
    	}
    }
}
