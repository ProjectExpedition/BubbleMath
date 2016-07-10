package com.oneoneone.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.oneoneone.game.BubbleMath;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = BubbleMath.WIDTH;
		config.height = BubbleMath.HEIGHT;
		config.title = BubbleMath.TITLE;
		new LwjglApplication(new BubbleMath(), config);
	}
}
