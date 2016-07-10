package com.oneoneone.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.oneoneone.game.BubbleMath;

import java.util.Random;

/**
 * Created by David on 9/07/2016.
 */
public class Bubble {
    private static final int RANGE = 100;
    private static final int REDSTARTX = 960;
    private static final int BLUESTARTX = 320;
    private static final int ALLSTARTY = 150;
    private static final int BUOYANCY = 1;

    private Texture texture;
    private Vector2 velocity;
    private Vector2 position;
    private Random rand;
    private int value;
    private Circle bound;

    public Bubble() {
        rand = new Random();
        value = (rand.nextInt()%RANGE) - (RANGE/2);
        velocity = new Vector2(0,5);

        if (value <= 0) {
            texture = new Texture("blue.png");
            position = new Vector2(REDSTARTX,ALLSTARTY);
        } else if(value >0){
            texture = new Texture("red.png");
            position = new Vector2(BLUESTARTX,ALLSTARTY);
        }
        bound = new Circle(position.x + (texture.getWidth()/2),position.y+texture.getWidth()/2,texture.getWidth()/2);
    }

    public void update(float dt){
        if(position.y > 0) {
            velocity.add(0, BUOYANCY);
        }
        velocity.scl(dt);
        position.add(velocity.x, velocity.y);
        cornerCollision();
        velocity.scl(1/dt);
        bound.setPosition(position);
    }
    public void cornerCollision(){
        if(position.y < 0){
            position.y = 0;}
        if(position.y > (BubbleMath.HEIGHT - 100)){
            position.y = BubbleMath.HEIGHT - 100;}          //-100 because just HEIGHT moves bubble off screen
        if(position.x < 0){
            position.x = 0;}
        if(position.x > (BubbleMath.WIDTH - 100)){
            position.x = BubbleMath.WIDTH - 100;}
    }

    public Vector2 getPosition() {
        return position;
    }

//    public boolean collides(Array<Bubble> bubbles){
//        int sum = 0;
//        for (Bubble bub: bubbles){
//            if (bound.overlaps(bub.getBound()));
//        ++sum;
//        }
//        return ;
//    }

    public int getValue() {
        return value;
    }

    public Texture getBubble() {
        return texture;
    }

    /* grab_bubble() is called on touch and directs bubbles within 200 units
     * towards the touch location. This is not working perfectly as yet.
     * Current issues (10/07/2016):
     * Bubbles stuck at top of screen, and grabbing from outside bubble range.
     * Screen size issues.
     */
    public void grab_bubble(){
        //the following retrieve the x and y coordinates of the current touch.
        float x_touch_location = Gdx.input.getX()-50;
        float y_touch_location = BubbleMath.HEIGHT-Gdx.input.getY()-50;

        //the following creates a pair of variables to check the difference between
        //the bubble position and the touch location.
        float x_touch_difference = Math.abs(position.x-x_touch_location);
        float y_touch_difference = Math.abs(position.y-y_touch_location);

        //Check to see if the bubbles are in range of the touch and if so
        //direct them to the touch location.
        if (x_touch_difference < 50){
            if(y_touch_difference < 50) {
                velocity.x = 100*(x_touch_location - position.x);
                velocity.y = 100*(y_touch_location - position.y);
            }
        }//this is a test. This test is finished.
    }
//    public void walk(){
//
//        velocity.y = rand.nextInt()%500;
//        velocity.x = rand.nextInt()%500;
//    }
//
    public Circle getBound() {
        return bound;
    }
}

