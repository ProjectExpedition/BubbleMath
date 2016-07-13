package com.oneoneone.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.oneoneone.game.BubbleMath;
import com.oneoneone.game.states.PlayState;

import java.util.Random;

/**
 * Created by David on 9/07/2016.
 */
public class Bubble {
    private static final int RANGE = 100;       //number of possible numbers
    private static final int BLUESTARTX = 1280;  //starting x coordinate for blue
    private static final int REDSTARTX = 0;   //starting x coordinate for red
    //private static final int ALLSTARTY = -200;   //starting y coordinate for red and blue
    private static final int BUOYANCY = 1;      //velocity added each update to give effect of buoyancy
    private Texture texture;    //bubble texture (red or blue) stored here
    private int bubbleScale;    //bubble size scale calculated in Bubble()
    private Sprite bubbleSprite;//sprite container for bubbles
    private Vector2 velocity;   //speed at which bubble moves
    private Vector2 position;   //position of bubble
    private boolean value;          //number represented on screen for bubble, -ve = red, +ve = blue
    private Circle bound;       //collision detection representation of bubble
    private float sumscaledt = 0;
    private int maxWidth;
    private float dt; //test for velocity drag
    float x_touch_location;
    float y_touch_location;
    private boolean touched;

    /** Bubble() creates an instance of a bubble sprite for the array.
     *  The colour is selected using a random Boolean value; true creates a
     *  blue bubble, false creates red.
     *  A random scale factor is then created to be attached to the bubble.
     */
    public Bubble(boolean isRed) {
        Random rand = new Random();
        touched = false;
        value = isRed;
        velocity = new Vector2(rand.nextInt(200),rand.nextInt(200));
        if (!isRed) {
            texture = new Texture("blue.png");
            position = new Vector2(BLUESTARTX,BubbleMath.HEIGHT);
        } else {
            texture = new Texture("red.png");
            position = new Vector2(REDSTARTX,0);
        }
        maxWidth = (int)Math.round(texture.getWidth() * (rand.nextFloat() * (1 - 0.4) + 0.4));//When using larger bubble sprite use height*rand.nextDouble()
        //maxWidth = rand.nextInt(5)*texture.getWidth();
        bubbleSprite = new Sprite(texture);
        bound = new Circle(position.x + (bubbleScale/2),position.y+bubbleScale/2,bubbleScale/2);
        //position.y = -bubbleScale; //so the bubble does not pop onto screen (turned off)
    }

    public void update(float dt){ //dt = amount of time passed since last update
        this.dt = dt;
        if(value) {
            velocity.add(0, -BUOYANCY);}
        else{
            velocity.add(0, BUOYANCY);
        }
        velocity.scl(dt);
        if (sumscaledt<=1){
            sumscaledt = sumscaledt + dt; //collect dt
            bubbleScale = Math.round(maxWidth*sumscaledt); //increase bubble scale
            bound.setRadius(bubbleScale/2);
        }else {
            position.add(velocity.x, velocity.y);
        }
        cornerCollision();
        velocity.scl(1/dt);
        bound.set(position,bubbleScale/2);
    }

    public boolean collision(Circle C){
        return bound.overlaps(C);
    }
    public void cornerCollision(){
        float offset = bubbleScale;
        if((position.y < 0)){
            position.y = 0;
            velocity.y = -velocity.y;}
        if(position.y > (BubbleMath.HEIGHT - offset)){
            velocity.y = -velocity.y;
            position.y = BubbleMath.HEIGHT - offset;
        }
        if(position.x < 0){
            position.x = 0;
            velocity.x = -velocity.x;}
        if(position.x > (BubbleMath.WIDTH - offset)){
            position.x = BubbleMath.WIDTH - offset;
            velocity.x = -velocity.x;
        }
    }

    public Vector2 getPosition() {
        return position;
    }
    /* grab_bubble() is called on touch and directs bubbles within 50 units
     * towards the touch location.
     */
    public void grab_bubble(int pointer){
        //the following retrieve the x and y coordinates of the current touch.
        float previous_x_touch = x_touch_location;
        float previous_y_touch = y_touch_location;
        x_touch_location = PlayState.SCALEX*(Gdx.input.getX(pointer));
        y_touch_location = PlayState.SCALEY*(PlayState.SCREEN_HEIGHT-Gdx.input.getY(pointer));

        //the following creates a pair of variables to check the difference between
        //the bubble position and the touch location.
        float x_touch_difference = (position.x+bubbleScale/2)-x_touch_location;
        float y_touch_difference = (position.y+bubbleScale/2)-y_touch_location;

        //the magnitude
        double touch_magnitude_difference = Math.hypot(x_touch_difference,y_touch_difference);

        //Check to see if the bubbles are in range of the touch and if so
        //direct them to the touch location.
        touched = false;
        if (touch_magnitude_difference <  bubbleScale/2){
            position.x = x_touch_location-(bubbleScale/2);//-x_touch_difference; //set bubble to position of touch
            position.y = y_touch_location-(bubbleScale/2);//-y_touch_difference; //offset texture.getWidth()/2 to centre bubble on touch (TODO needs better centre method for scaling)
            velocity.x = 0;
            velocity.y = 0;
            velocity.x = (x_touch_location-previous_x_touch)/dt; //reset velocity
            velocity.y = (y_touch_location-previous_y_touch)/dt;
            if (velocity.x >= 15/dt){
                velocity.x = 15f/dt;
            }
            if (velocity.y >= 15/dt){
                velocity.y = 15f/dt;
            }
            if (velocity.x <= -15/dt){
                velocity.x = -15f/dt;
            }
            if (velocity.y <= -15/dt){
                velocity.y = -15f/dt;
            }
            touched = true; //set touch to true TODO create "selected state" for bubble
        }
    }
    public Circle getBound() {
        return bound;
    }

    public void postCollisionVelocity(Vector2 newV){
//            newV.scl(10);
            velocity.set(newV);

    }

    public boolean getValue() {
        return value;
    }

    public Texture getBubble() {
        return texture;
    }

    public Sprite getBubbleSprite(){
        return bubbleSprite;
    }

    public int getBubbleScale(){
        return bubbleScale;
    }

    public Vector2 getVelocity(){
        return velocity;
    }
}


