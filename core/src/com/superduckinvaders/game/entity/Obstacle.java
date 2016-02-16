package com.superduckinvaders.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.superduckinvaders.game.Round;

public class Obstacle extends PhysicsEntity {

    public Obstacle(Round parent, float x, float y, float width, float height) {
        super(parent, x, y);
        this.width = width;
        this.height = height;
        createBody(BodyDef.BodyType.StaticBody, WORLD_BITS, ALL_BITS, NO_GROUP, false);
    }
    
    @Override
    public void render(SpriteBatch spriteBatch) {}

}
