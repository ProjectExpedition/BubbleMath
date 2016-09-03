package com.oneoneone.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
    private final Texture BACKGROUND_IMAGE = new Texture("bg.png");
    private final Texture START_BUTTON_IMAGE = new Texture("blue.png");
    private Vector2 startButtonPosition;
    private int startButtonRadius;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        startButtonRadius = START_BUTTON_IMAGE.getWidth()/2;
        startButtonPosition = new Vector2((Atomsly.WIDTH / 2) - (START_BUTTON_IMAGE.getWidth() / 2), (Atomsly.HEIGHT / 2) - startButtonRadius);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            //Create variables for touch co-ordinates
            float xTouchLocation;
            float yTouchLocation;
            float xTouchDifference;
            float yTouchDifference;

            //Get touch data
            xTouchLocation = PlayState.X_SCALE_FACTOR * (Gdx.input.getX());
            yTouchLocation = PlayState.Y_SCALE_FACTOR * (PlayState.SCREEN_HEIGHT - Gdx.input.getY());

            //Compare touch date to button co-ordinates
            xTouchDifference = (startButtonPosition.x + startButtonRadius) - xTouchLocation;
            yTouchDifference = (startButtonPosition.y + startButtonRadius) - yTouchLocation;
            double touch_magnitude_difference = Math.hypot(xTouchDifference, yTouchDifference);

            //Start game if button is pressed
            if (touch_magnitude_difference < startButtonRadius) {
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
        sb.draw(BACKGROUND_IMAGE, 0, 0);
        sb.draw(START_BUTTON_IMAGE, startButtonPosition.x, startButtonPosition.y, startButtonRadius, startButtonRadius);
        sb.end();
    }

    @Override
    public void dispose() {
        BACKGROUND_IMAGE.dispose();
        START_BUTTON_IMAGE.dispose();
    }
}
