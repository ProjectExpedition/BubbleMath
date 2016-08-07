package com.oneoneone.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.oneoneone.game.Atomsly;

/**
 * MenuState.java
 * Purpose: Handles the main menu screen
 *
 * @author David Hampton, Grace Poole, Roderick Lenz
 * @version 0.01 07/08/2016
 */
public class MenuState extends State {
    private Texture background;
    private Texture startBtn;
    private Vector2 startBtnPosition;
    private int btnRadius;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("bg.png");
        startBtn = new Texture("blue.png");
        btnRadius = startBtn.getWidth();
        startBtnPosition = new Vector2((Atomsly.WIDTH / 2) - (startBtn.getWidth() / 2), (Atomsly.HEIGHT / 2) - (btnRadius / 2));
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float x_touch_location = PlayState.X_SCALE_FACTOR * (Gdx.input.getX());
            float y_touch_location = PlayState.Y_SCALE_FACTOR * (PlayState.SCREEN_HEIGHT - Gdx.input.getY());

            //the following creates a pair of variables to check the difference between
            //the bubble position and the touch location.
            float x_touch_difference = (startBtnPosition.x + btnRadius / 2) - x_touch_location;
            float y_touch_difference = (startBtnPosition.y + btnRadius / 2) - y_touch_location;

            //the magnitude
            double touch_magnitude_difference = Math.hypot(x_touch_difference, y_touch_difference);
            if (touch_magnitude_difference < btnRadius / 2) {
                gsm.get(new PlayState(gsm));
                dispose();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0);
        sb.draw(startBtn, startBtnPosition.x, startBtnPosition.y, btnRadius, btnRadius);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        startBtn.dispose();
    }
}
