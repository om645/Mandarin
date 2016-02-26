package com.superduckinvaders.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.assets.Assets;

/**
 * Represents a projectile.
 */
public class Projectile extends PhysicsEntity {

    /**
     * The owner of this Projectile (i.e. the Entity that fired it).
     */
    private PhysicsEntity owner;

    /**
     * How much damage this Projectile does to what it hits.
     */
    private int damage;

    /**
     * Initialises this Projectile.
     *
     * @param parent   the round this Projectile belongs to
     * @param pos      the initial position
     * @param velocity the projectile velocity
     * @param damage   how much damage the projectile deals
     * @param owner    the owner of the projectile (i.e. the one who fired it)
     */
    public Projectile(Round parent, Vector2 pos, Vector2 velocity, int damage, PhysicsEntity owner) {
        super(parent, pos);
        
        this.width = Assets.projectile.getRegionWidth();
        this.height = Assets.projectile.getRegionHeight();
        
        this.damage = damage;
        this.owner = owner;

        createDynamicBody(PROJECTILE_BITS, (short) ~owner.categoryBits, NO_GROUP, false);
        body.setBullet(true);
        setVelocity(velocity);
    }

    /**
     * Set the owner of the projectile to a new PhysicsEntity.
     * @param owner the new owner.
     */
    public void setOwner(PhysicsEntity owner) {
        this.owner = owner;
        setMaskBits((short) ~owner.categoryBits);
    }

    /**
     * @return the current projectile owner.
     */
    public PhysicsEntity getOwner() {
        return owner;
    }
    
    @Override
    public void beginCollision(PhysicsEntity other, Contact contact){
        parent.createParticle(getCentre(), 0.6f, Assets.explosionAnimation);
        removed = true;
        if (other instanceof TheCharacter && other != owner) {
            ((TheCharacter) other).damage(damage);
        }
    }

    /**
     * Updates the state of this Projectile.
     *
     * @param delta how much time has passed since the last update
     */
    @Override
    public void update(float delta) {
    }

    /**
     * Renders this Projectile.
     *
     * @param spriteBatch the sprite batch on which to render
     */
    @Override
    public void render(SpriteBatch spriteBatch) {
        Vector2 pos = getPosition();
        spriteBatch.draw(Assets.projectile, pos.x, pos.y);
    }
}
