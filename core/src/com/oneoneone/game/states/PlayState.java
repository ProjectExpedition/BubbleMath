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
        //dtsum = TimeUtils.nanoTime();
        redSpawner = new Texture("red_spawner.png");
        blueSpawner = new Texture("blue_spawner.png");
        bg = new Texture("play_background.png");
        bubbles = new Array<Bubble>();
        bubbles.add(new Bubble());
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isTouched()) {
            for (Bubble bub: bubbles){
                bub.grab_bubble();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        dtsum = dtsum + dt;
        if (dtsum > 1) {
            bubbles.add(new Bubble());
            dtsum = 0;
        }
//        if (TimeUtils.timeSinceNanos(timer) > 1000000000) {
//            bubbles.add(new Bubble());
//            timer = TimeUtils.nanoTime();
//        }
        for (Bubble bub: bubbles){
            bub.update(dt);
//            for (Bubble bubb: bubbles)
//            if(bub.collides(bubb.getBound())){
//
//            }

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
