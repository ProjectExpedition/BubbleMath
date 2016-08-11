package com.oneoneone.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.oneoneone.game.Atomsly;
import com.oneoneone.game.states.PlayState;

import java.util.Random;

/**
 * Atom.java
 * Purpose: Creates and manages Atom objects in game
 *
 * @author David Hampton, Grace Poole, Roderick Lenz
 * @version 0.01 07/08/2016
 */
public class Atom {
    private static final int RANGE = 5;            //number of atoms
    //    private static final int BLUESTARTX = 1280;     //starting x coordinate for blue
    //    private static final int REDSTARTX = 0;         //starting x coordinate for red
    //    private static final int BUOYANCY = 1;          //velocity added each update to give effect of buoyancy
    private int atomicNumber;           //random "mass" of the bubble used to generate number in bubble and size
    private int sizeCurrent = 0;        //current bubble size calculated in update()
    private float scaleFactor = 0;      //the scaling factor used to reach final size
    private int sizeFinal;              //final bubble size reached in update()
    private float dt;                   //poll time of current (or last) update
    private boolean isRed;              //is it red
    private boolean toRemove;
    float x_touch_location;             //set as data members to calculate velocity after touch
    float y_touch_location;
    private Sprite sprite;          //sprite container for bubbles
    private Vector2 velocity;       //speed at which bubble moves
    private Vector2 position;       //position of bubble
    private Circle circleBound;     //collision detection representation of bubble
    public boolean is_grabbed;
    public int grabbed_by;
    private Array<Sprite> shells;

    public Atom(boolean isRed, float x) {
        /**
         * Atom() creates an instance of a bubble sprite for the array.
         *
         * @param isRed true or false check, false is blue
         * @param x     x coordinate of the emitter when atom is created
         */
        //Check if atom is red or blue and intializes variables for the bubble
        this.isRed = isRed;
        Random rand = new Random();
        velocity = new Vector2((rand.nextInt(2 * 15) - 15), (rand.nextInt(40) + 80));//set random velocity vector
        sprite = new Sprite(buildTexture(x));
        sprite.setOriginCenter();
        setSize(rand.nextInt(RANGE) + 1);
        //WIP implementation of shells
        //int i = 0;
//        shells = new Array<Sprite>();
//        while (i <= atomicNumber){
//            shells.add(sprite);
//        }

        //set boundary for collision detection etc.
        circleBound = new Circle(position.x / PlayState.X_SCALE_FACTOR, position.y / PlayState.X_SCALE_FACTOR, (float) sizeCurrent / 2f);
    }

    public Texture buildTexture(float x) {
        /**
         * Assigns the texture file for the atom, blue or red, and creates the postion vector.
         * The y coordinate is set the top of the screen for blue and bottom for red
         *
         * @param x     x coordinate of the emitter at the time
         * @return texture file name
         */
        Texture texture;
        if (isRed) {
            texture = new Texture("red.png");
            position = new Vector2(x, 0);
        } else {
            texture = new Texture("blue.png");
            position = new Vector2(x, Atomsly.HEIGHT);
        }
        return texture;
    }

    public void update(float dt, float xMoveTo) { //dt = amount of time passed since last update
        /**
         * Called by PlayState.java to recalculate the positions and sizes atoms each tick
         *
         * @param dt      the change in time
         * @param xMoveTo the x coordinate of the field emitter
         */
        if (!is_grabbed){
        this.dt = dt;
        float distance = (xMoveTo) - circleBound.x * PlayState.X_SCALE_FACTOR + 30; //30*2 = sze of beam
        if (distance > 0) {
            velocity.add(distance / 30, 0);
        } else if (distance < 0) {
            velocity.add(distance / 30, 0);
        } else {
            velocity.set(0, 0);
        }
        velocity.scl(dt);
        if ((scaleFactor <= 1)) {
            sizeCurrent = Math.round(sizeFinal * scaleFactor); //increase bubble scale
            scaleFactor += dt / 2; //collect dt
        }
        position.add(velocity.x, velocity.y);
        velocity.scl(1 / dt);
        }
        circleBound.set((position.x) / PlayState.X_SCALE_FACTOR, (position.y) / PlayState.Y_SCALE_FACTOR, sizeCurrent / PlayState.X_SCALE_FACTOR / 2f);
        cornerCollision(); //detect collision after coordinates updated
    }

    public Vector2 getPosition() {
        /**
         * Gets the postion of an atom
         *
         * @return atom's position vector
         */
        return position;
    }

    public void grabBubble(int pointer) {
        /**
         * checks the position of an atom relative to a touch location
         *
         * @param pointer the pointer being check against
         */
        //the following retrieve the x and y coordinates of the current touch.
        x_touch_location = PlayState.X_SCALE_FACTOR * (Gdx.input.getX(pointer));
        y_touch_location = PlayState.Y_SCALE_FACTOR * (PlayState.SCREEN_HEIGHT - Gdx.input.getY(pointer));
        //the following creates a pair of variables to check the difference between
        //the bubble position and the touch location.
        float x_touch_difference = (position.x) - x_touch_location;
        float y_touch_difference = (position.y) - y_touch_location;

        if (Math.hypot(x_touch_difference, y_touch_difference) < sizeCurrent / 2) {
            is_grabbed = true;
            grabbed_by = pointer;
        }
    }

    public void dragBubble(float x, float y, int pointer) {
        /**
         * drags a bubble when a drag event is detected
         *
         * @param x       x coordinate of drag event
         * @param y       y coordinate of drag event
         * @param pointer pointer being dragged unused atm
         */
        position.x = PlayState.X_SCALE_FACTOR * x;
        position.y = PlayState.Y_SCALE_FACTOR * (PlayState.SCREEN_HEIGHT - y);
        velocity.set(0, 0);
//        velocity.set((Gdx.input.getDeltaX(pointer) / dt), (Gdx.input.getDeltaY(pointer) / dt));
//        if (velocity.x >= 15 / dt) {
//            velocity.x = 15f / dt;
//        }
//        if (velocity.y >= 15 / dt) {
//            velocity.y = 15f / dt;
//        }
//        if (velocity.x <= -15 / dt) {
//            velocity.x = -15f / dt;
//        }
//        if (velocity.y <= -15 / dt) {
//            velocity.y = -15f / dt;
//        }

    }

    public void releaseBubble(int pointer) {
        /**
         * resets the is_grabbed state when a touch is released
         *
         * @param pointer the pointer being checked
         */
        if (grabbed_by == pointer) {
            is_grabbed = false;
        }
    }

    public Circle getCircleBound() {
        /**
         * Gets the circle bound of the atom for collision detection
         *
         * @return the bound of the atom
         */return circleBound;
    }

    public void setSize(int newMass) {
        /**
         * Sets the size of the current Atom to a new value
         *
         * @param newMass new mass of the atom
         */
        atomicNumber = newMass;
        sizeFinal = (int) Math.round(sprite.getWidth() * (0.4 + (0.6 * atomicNumber) / RANGE));
        sizeFinal = sizeFinal * 200 / 570; //ratio to get large atom (570) to normal atom (250)
        sizeCurrent = sizeFinal;
    }

    public static int getRANGE() {
        /**
         * Gets the current bubble value range
         *
         * @return RANGE the current range value
         */
        return RANGE;
    }

    private void cornerCollision() {
        /**
         * Detects collisions with the top and bottom of the screen
         */
        if ((position.y < sizeCurrent / 2)) {
            position.y = sizeCurrent / 2;
            velocity.y = -velocity.y;
        }
        if (position.y > (Atomsly.HEIGHT - sizeCurrent / 2)) {
            velocity.y = -velocity.y;
            position.y = Atomsly.HEIGHT - sizeCurrent / 2;
        }
    }

    public boolean NullFieldCollision(){
        /**
         * Detects collisions with the NullFIeld
         */
        float NULL_FIELD_WIDTH = 65;
        if (position.x < (sizeCurrent / 2) + NULL_FIELD_WIDTH) {
            return true;
        }
        if (position.x > (Atomsly.WIDTH - sizeCurrent / 2) - NULL_FIELD_WIDTH) {
            return true;
        }
        return false;
    }

    public Sprite getSprite() {
        /**
         * Gets the sprite for the current Atom
         *
         * @return sprite
         */
        return sprite;
    }

    public int getSizeCurrent() {
        /**
         * Gets the size of the current atom
         *
         * @return sizeCurrent
         */
        return sizeCurrent;
    }

    public int getAtomicNumber() {
        /**
         * Gets the number value of the bubble
         *
         * @return atomicNumber integer value
         */
        return atomicNumber;
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public void setToRemove(boolean toRemove) {
        /**
         * Sets the atom to be deleted
         *
         * @return atomicNumber integer value
         */
        if (!this.toRemove){ //if () here to ignore a false overwrite after set to true
            this.toRemove = toRemove;
        }
    }
}