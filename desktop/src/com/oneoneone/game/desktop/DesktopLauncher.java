package com.oneoneone.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.oneoneone.game.Atomsly;

/**
 * DesktopLauncher.java
 * Purpose: Launches the desktop version of the game
 *
 * @author David Hampton, Grace Poole, Roderick Lenz
 * @version 0.01 07/08/2016
 */
public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 10 * Atomsly.WIDTH / 5;
        config.height = 10 * Atomsly.HEIGHT / 5;
        config.title = Atomsly.TITLE;
        new LwjglApplication(new Atomsly(), config);
    }
}
