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
public class PlayState extends State {
    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public static final float SCALEX = (float) BubbleMath.WIDTH / SCREEN_WIDTH;
    public static final float SCALEY = (float) BubbleMath.HEIGHT / SCREEN_HEIGHT;
    private Texture bg;
    private Texture redSpawner;
    private Texture blueSpawner;
    private float dtsum = 0; //collects amount of time that has passed in game
    private Array<Bubble> bubbles; //Array Container of all bubbles

    /* PlayState(GameStateManager gsm) is called after Menu State
     * Allocates memory and calls constructors for all data members.
     */
    public PlayState(GameStateManager gsm) {
        super(gsm); //super = active state class
        redSpawner = new Texture("red_spawner.png");
        blueSpawner = new Texture("blue_spawner.png");
        bg = new Texture("play_background.png");
        bubbles = new Array<Bubble>();
        bubbles.add(new Bubble()); //creates first bubble
    }

    /* handleInput() checks if the person has touched the screen
    *  A maximum of 2 touch pointers are counted
    */
    @Override
    protected void handleInput() {
        for (int i = 0; i < 2; i++) {       //initializes to count maximum of two touch pointers
            if (Gdx.input.isTouched(i)) {//multitouch i is the pointer number where 0 is the first touch and 1 is the second
                for (Bubble bub : bubbles) {
                    bub.grab_bubble(i);
                }
            }
        }
    }

    /* update(float dt) is called in GameStateManager (see states.peek().update(dt))
    * updates the mathematics of everything that happens; velocity, coordinates, inputs,
    * checking,etc
    */
    @Override
    public void update(float dt) {
        handleInput(); //calls first to see if screen has been touched before updating
        dtsum = dtsum + dt; //sums poll time
        if (dtsum > 2) { //if a certain number of poll times have passed spawn a bubble
            bubbles.add(new Bubble()); //spawns bubble
            dtsum = 0; //resets sum poll time
        }
        for (Bubble bub : bubbles) {//attempt at brute force collision detection TODO Collision Detection
            for (int i = 0; i > bubbles.size; i++) {
                bub.collides(bubbles.get(i).getBound());//inserts circle bound of all bubbles into current bubble
            }
            bub.update(dt); //calculates position changes to bubble
        }
    }

    /* render(float dt) draws all sprites to SpriteBatch declared in BubbleMath
    * draws after all positions and conditions have been calculated in update in BubbleMath render
    */
    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0);
        for (Bubble bub : bubbles) {
            sb.draw(bub.getBubbleSprite(), bub.getPosition().x, bub.getPosition().y, bub.getBubbleScale(), bub.getBubbleScale());
        }
        sb.draw(redSpawner, BubbleMath.WIDTH / 4 - (blueSpawner.getWidth() / 4), 0);
        sb.draw(blueSpawner, 3 * BubbleMath.WIDTH / 4 - (blueSpawner.getWidth() / 4), 0);
        sb.end();
    }

    @Override
    public void dispose() {//TODO write a dispose method to avoid memory leaks

    }
}
