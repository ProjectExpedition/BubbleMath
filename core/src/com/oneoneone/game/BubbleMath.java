package com.oneoneone.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oneoneone.game.states.GameStateManager;
import com.oneoneone.game.states.MenuState;

public class BubbleMath extends ApplicationAdapter {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final java.lang.String TITLE = "Bubble Math";
	private GameStateManager gsm;
	private SpriteBatch batch;
	private OrthographicCamera cam;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		cam = new OrthographicCamera(BubbleMath.WIDTH, BubbleMath.HEIGHT);
		cam.setToOrtho(false, BubbleMath.WIDTH, BubbleMath.HEIGHT);
		batch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
