package com.oneoneone.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.oneoneone.game.BubbleMath;
import com.oneoneone.game.sprites.Bubble;

/**
 * Created by David on 9/07/2016.
 */
public class PlayState extends State{
    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public static final float SCALEX = (float)BubbleMath.WIDTH/SCREEN_WIDTH;
    public static final float SCALEY = (float)BubbleMath.HEIGHT/SCREEN_HEIGHT; //(float) used so int division isnt used
    private static final int MAX_BUBBLES = 10;
    private Bubble bubble;
    private Texture bg;
    private Texture redSpawner;
    private Texture blueSpawner;
    private float dtsum = 0;
    private Array<Bubble> bubbles;
    private Vector2 touch_location;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bubble = new Bubble();
        redSpawner = new Texture("red_spawner.png");
        blueSpawner = new Texture("blue_spawner.png");
        bg = new Texture("play_background.png");
        bubbles = new Array<Bubble>();
        bubbles.add(new Bubble());
    }

    @Override
    protected void handleInput() {
        for(int i = 0; i<2; i++) {
            if (Gdx.input.isTouched(i)) {
                for (Bubble bub : bubbles) {
                    bub.grab_bubble(i);
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        dtsum = dtsum + dt;
        if (dtsum > 2) {
            bubbles.add(new Bubble());
            dtsum = 0;
        }
        for (Bubble bub: bubbles){
            for (int i=0;i>bubbles.size; i++) {
                bub.collides(bubbles.get(i).getBound());
            }
            bub.update(dt);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg,0,0);

        for (Bubble bub: bubbles){
            sb.draw(bub.getBubble(),bub.getPosition().x,bub.getPosition().y);
        }
        sb.draw(redSpawner, BubbleMath.WIDTH/4-(blueSpawner.getWidth()/4),0);
        sb.draw(blueSpawner, 3*BubbleMath.WIDTH/4-(blueSpawner.getWidth()/4),0);
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
