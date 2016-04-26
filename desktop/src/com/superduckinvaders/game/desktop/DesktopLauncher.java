/**
 * URL for executable: https://4d45b16264c555b3f8e66ac055f749f7a2451836-www.googledrive.com/host/0B4aXpJk4iCmSMVNXODg5bVVuYkU/SuperDuckInvadersMandarin.zip
 */

package com.superduckinvaders.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superduckinvaders.game.DuckGame;

/**
 * Desktop launcher for Super Duck Invaders.
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.resizable = true;
		config.title = "SUPER DUCK INVADERS! - Team Mandarin";
		new LwjglApplication(new DuckGame(), config);
	}
}
