package com.superduckinvaders.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.entity.TheCharacter;
import com.superduckinvaders.game.util.LwjglTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Test the Character class's special methods.
 */
@RunWith(LwjglTestRunner.class)
public class CharacterTest {
    private class MyCharacter extends TheCharacter {
        MyCharacter(Round round, int x, int y, int maxHealth) {
            super(round, x, y, maxHealth);
        }

        @Override
        public void render(SpriteBatch spriteBatch) {}
    }

    private Round round;

    @Before
    public void setUp() {
        round = mock(Round.class);
    }

    @Test
    public void CanAccessMaxHealth() {
        MyCharacter ch = new MyCharacter(round, 10, 10, 50);
        assertEquals(ch.getMaximumHealth(), 50);
    }

    @Test
    public void CanAccessCurrentHealth() {
        MyCharacter ch = new MyCharacter(round, 10, 10, 25);
        assertEquals(ch.getCurrentHealth(), 25);
    }

    @Test
    public void CanBeDamaged() {
        MyCharacter ch = new MyCharacter(round, 10, 10, 50);
        ch.damage(15);
        assertEquals(ch.getCurrentHealth(), 35);
    }

    @Test
    public void CanBeKilled() {
        MyCharacter ch = new MyCharacter(round, 10, 10, 50);
        ch.damage(55);
        assertTrue(ch.isDead());
    }

    @Test
    public void IsHealable() {
        MyCharacter ch = new MyCharacter(round, 10, 10, 50);
        ch.damage(30);
        ch.heal(22);
        assertEquals(ch.getCurrentHealth(), 42);
    }

    @Test
    public void CannotBeHealedPastMax() {
        MyCharacter ch = new MyCharacter(round, 10, 10, 50);
        ch.damage(10);
        ch.heal(25);
        assertEquals(ch.getCurrentHealth(), ch.getMaximumHealth());
    }
}
