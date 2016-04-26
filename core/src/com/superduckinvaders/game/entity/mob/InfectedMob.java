//////////////////////////NEW FOR ASSESSMENT 4 /////////////////////////
package com.superduckinvaders.game.entity.mob;

import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.assets.Assets;

/**
 * Chases the player until it gets close enough to cause damage.
 * Ever seen Shawn of the Dead? Good movie, right?
 */
public class InfectedMob extends MeleeMob {
    /**
     * Create a ZombieMob
     * @param parent the round parent
     * @param x      the starting x position
     * @param y      the starting y position
     */
    public InfectedMob (Round parent, float x, float y) {
        super(parent, x, y, 8, Assets.infectedBadGuyNormal, 7);
        infecting = true;
    }
}
///////////////////////////////////////////////////