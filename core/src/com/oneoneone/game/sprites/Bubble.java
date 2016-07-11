package com.oneoneone.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.oneoneone.game.BubbleMath;
import com.oneoneone.game.states.PlayState;

import java.util.Random;

/**
 * Created by David on 9/07/2016.
 */
public class Bubble {
    private static final int RANGE = 100;       //number of possible numbers
    private static final int BLUESTARTX = 960;  //starting x coordinate for blue
    private static final int REDSTARTX = 320;   //starting x coordinate for red
    private static final int ALLSTARTY = 150;   //starting y coordinate for red and blue
    private static final int BUOYANCY = 1;      //velocity added each update to give effect of buoyancy
    private Texture texture;    //bubble texture (red or blue) stored here
    private Vector2 velocity;   //speed at which bubble moves
    private Vector2 position;   //position of bubble
    private int value;          //number represented on screen for bubble, -ve = red, +ve = blue
    private Circle bound;       //collision detection representation of bubble
    private boolean touched;

    public Bubble() {
        Random rand = new Random();
        value = (rand.nextInt()%RANGE) - (RANGE/2);
        velocity = new Vector2(0,5);

        if (value <= 0) {
            texture = new Texture("blue.png");
            position = new Vector2(BLUESTARTX,ALLSTARTY);
        } else if(value > 0){
            texture = new Texture("red.png");
            position = new Vector2(REDSTARTX,ALLSTARTY);
        }
        bound = new Circle(position.x + (texture.getWidth()/2),position.y+texture.getWidth()/2,texture.getWidth()/2);
    }

    public void update(float dt){ //dt = amount of time passed since last update
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
            position.y = BubbleMath.HEIGHT - 100;}//-100 because just HEIGHT moves bubble off screen
        if(position.x < 0){
            position.x = 0;}
        if(position.x > (BubbleMath.WIDTH - 100)){
            position.x = BubbleMath.WIDTH - 100;}
    }

    public Vector2 getPosition() {
        return position;
    }
    /* grab_bubble() is called on touch and directs bubbles within 50 units
     * towards the touch location. This is not working perfectly as yet.
     * Current issues (11/07/2016):
     * Screen size issues.
     * True Touch location offset from mouse click (on David's)
     */
    public void grab_bubble(){
        //the following retrieve the x and y coordinates of the current touch.
        float x_touch_location = PlayState.SCALEX*(Gdx.input.getX());
        float y_touch_location = PlayState.SCALEY*(PlayState.SCREEN_HEIGHT-Gdx.input.getY());

        //the following creates a pair of variables to check the difference between
        //the bubble position and the touch location.
        float x_touch_difference = Math.abs((position.x+texture.getWidth()/2)-x_touch_location);
        float y_touch_difference = Math.abs((position.y+texture.getWidth()/2)-y_touch_location);


        //Check to see if the bubbles are in range of the touch and if so
        //direct them to the touch location.
        if (x_touch_difference < 100){
            if(y_touch_difference < 100) {
                position.x = x_touch_location-texture.getWidth()/2; //set bubble to position of touch
                position.y = y_touch_location-texture.getWidth()/2; //offset texture.getWidth()/2 to centre bubble on touch (TODO needs better centre method for scaling)
                velocity.x = 0; //reset velocity
                velocity.y = 0;
            }
        }
    }
    public Circle getBound() {
        return bound;
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
}


