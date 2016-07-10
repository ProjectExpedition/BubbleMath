package com.oneoneone.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oneoneone.game.BubbleMath;

/**
 * Created by David on 9/07/2016.
 */
public class MenuState extends State {
    private Texture background;
    private Texture startBtn;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("start_menu_background.png");
        startBtn = new Texture("start_menu_start.png");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.get(new PlayState(gsm));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background,0,0);
        sb.draw(startBtn,(BubbleMath.WIDTH/2)-(startBtn.getWidth()/2),(BubbleMath.HEIGHT/2)-(startBtn.getHeight()/2));
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        startBtn.dispose();
    }
}
