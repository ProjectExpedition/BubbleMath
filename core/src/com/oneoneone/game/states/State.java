package com.oneoneone.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.oneoneone.game.BubbleMath;

/**
 * Created by David on 9/07/2016.
 */
public abstract class State {
    protected OrthographicCamera cam;
    protected Vector3 mouse;
    protected GameStateManager gsm;
    protected State(GameStateManager gsm) {
        this.gsm = gsm;
        cam = new OrthographicCamera(BubbleMath.WIDTH, BubbleMath.HEIGHT);
        cam.setToOrtho(true, BubbleMath.WIDTH, BubbleMath.HEIGHT);
        mouse = new Vector3();
    }
    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
