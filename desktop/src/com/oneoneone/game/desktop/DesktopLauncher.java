package com.oneoneone.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.oneoneone.game.BubbleMath;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 10*BubbleMath.WIDTH/5;
		config.height = 10*BubbleMath.HEIGHT/5;
		config.title = BubbleMath.TITLE;
		new LwjglApplication(new BubbleMath(), config);
	}
}
