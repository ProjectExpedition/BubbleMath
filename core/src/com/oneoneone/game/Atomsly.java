package com.oneoneone.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oneoneone.game.states.GameStateManager;
import com.oneoneone.game.states.MenuState;

public class Atomsly extends ApplicationAdapter{

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final java.lang.String TITLE = "Atom Math";
	private GameStateManager gsm;
	private SpriteBatch batch;
	private OrthographicCamera cam;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		cam = new OrthographicCamera(Atomsly.WIDTH, Atomsly.HEIGHT);
		cam.setToOrtho(false, Atomsly.WIDTH, Atomsly.HEIGHT);
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
