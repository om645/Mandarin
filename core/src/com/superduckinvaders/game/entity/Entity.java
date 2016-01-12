package com.superduckinvaders.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.superduckinvaders.game.round.Round;

/**
 * Represents an object in the game.
 */
public abstract class Entity {

	/**
	 * The round that this Entity is in.
	 */
	protected Round parent;

	/**
	 * The x and y coordinates of this Entity.
	 */
	protected double x, y;

	/**
	 * The x and y velocity of this MobileEntity in pixels per second.
	 */
	protected double velocityX = 0, velocityY = 0;

	/**
	 * Whether or not to remove this Entity on the next frame.
	 */
	protected boolean removed = false;

	/**
	 * Initialises this Entity with zero initial coordinates.
	 */
	public Entity(Round parent) {
		this(parent, 0, 0);
	}

	/**
	 * Initialises this Entity with the specified initial coordinates.
	 *
	 * @param x the initial x coordinate
	 * @param y the initial y coordinate
	 */
	public Entity(Round parent, double x, double y) {
		this.parent = parent;
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x coordinate of this Entity
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y coordinate of this Entity
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the x velocity of this MobileEntity in pixels per second
	 */
	public double getVelocityX() {
		return velocityX;
	}

	/**
	 * @return the y coordinate of this MobileEntity in pixels per second
	 */
	public double getVelocityY() {
		return velocityY;
	}

	/**
	 * Returns true if the specified rectangle intersects this Entity.
	 *
	 * @param x      the x coordinate of the rectangle's bottom left corner
	 * @param y      the y coordinate of the rectangle's bottom left corner
	 * @param width  the width of the rectangle
	 * @param height the height of the rectangle
	 * @return whether the specified rectangle intersects this Entity
	 */
	public boolean intersects(double x, double y, int width, int height) {
		return this.x < x + width && this.x + getWidth() > x && this.y < y + height && this.y + getHeight() > y;
	}

	/**
	 * @return the width of this Entity
	 */
	public abstract int getWidth();

	/**
	 * @return the height of this Entity
	 */
	public abstract int getHeight();

	/**
	 * @return whether this Entity has been removed
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * Ensures that this MobileEntity stays within the map area.
	 */
	protected void checkBounds() {
		if (x < 0) {
			x = 0;
		} else if (x > parent.getMapWidth() - getWidth()) {
			x = parent.getMapWidth() - getWidth();
		}

		if (y < 0) {
			y = 0;
		} else if (y > parent.getMapHeight() - getHeight()) {
			y = parent.getMapHeight() - getHeight();
		}
	}

	/**
	 * Gets whether the specified x delta will cause a collision on the left or right.
	 *
	 * @param deltaX the x delta
	 * @return whether a collision would occur on the left or right
	 */
	protected boolean collidesX(double deltaX) {
		return collidesLeft(deltaX) || collidesRight(deltaX);
	}

	/**
	 * Gets whether the specified y delta will cause a collision on the bottom or top.
	 *
	 * @param deltaY the y delta
	 * @return whether a collision would occur on the bottom or top
	 */
	protected boolean collidesY(double deltaY) {
		return collidesBottom(deltaY) || collidesTop(deltaY);
	}

	/**
	 * Gets whether the specified x delta will cause a collision on the left.
	 *
	 * @param deltaX the x delta
	 * @return whether a collision would occur on the left
	 */
	private boolean collidesLeft(double deltaX) {
		// N.B. this code can be greatly simplified if the player texture is smaller than the tile size.
		for (int i = (int) y; i < y + getHeight(); i++) {
			if (parent.isTileBlocked((int) Math.floor(x + deltaX), i)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets whether the specified x delta will cause a collision on the right.
	 *
	 * @param deltaX the x delta
	 * @return whether a collision would occur on the right
	 */
	private boolean collidesRight(double deltaX) {
		for (int i = (int) y; i < y + getHeight(); i++) {
			if (parent.isTileBlocked((int) Math.ceil(x + getWidth() - 1 + deltaX), i)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets whether the specified y delta will cause a collision on the bottom.
	 *
	 * @param deltaY the y delta
	 * @return whether a collision would occur on the bottom
	 */
	private boolean collidesBottom(double deltaY) {
		for (int i = (int) x; i < x + getWidth(); i++) {
			if (parent.isTileBlocked(i, (int) Math.floor(y + deltaY))) {
				return true;
			}
		}

		return false;
	}


	/**
	 * Gets whether the specified y delta will cause a collision on the top.
	 *
	 * @param deltaY the y delta
	 * @return whether a collision would occur on the top
	 */
	private boolean collidesTop(double deltaY) {
		for (int i = (int) x; i < x + getWidth(); i++) {
			if (parent.isTileBlocked(i, (int) Math.ceil(y + getHeight() - 1 + deltaY))) {
				return true;
			}
		}

		return false;
	}

	public void update(float delta) {
		double deltaX = velocityX * delta;
		double deltaY = velocityY * delta;

		if (collidesX(deltaX)) {
			deltaX = 0;
		}

		if (collidesY(deltaY)) {
			deltaY = 0;
		}

		x += deltaX;
		y += deltaY;

		checkBounds();
	}

	public abstract void render(SpriteBatch spriteBatch);
}
