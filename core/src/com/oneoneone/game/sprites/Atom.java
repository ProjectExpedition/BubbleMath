package com.oneoneone.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.oneoneone.game.Atomsly;
import com.oneoneone.game.states.PlayState;

import java.util.Random;

/**
 * Created by David on 9/07/2016.
 */
public class Atom {
    private static final int RANGE = 20;            //number of atoms
    private static final int BLUESTARTX = 1280;     //starting x coordinate for blue
    private static final int REDSTARTX = 0;         //starting x coordinate for red
    private static final int BUOYANCY = 1;          //velocity added each update to give effect of buoyancy
    private int atomicNumber;           //random "mass" of the bubble used to generate number in bubble and size
    private int sizeCurrent;            //current bubble size calculated in update()
    private float scaleFactor = 0;      //the scaling factor used to reach final size
    private int sizeFinal;              //final bubble size reached in update()
    private float dt;                   //poll time of current (or last) update
    private boolean isRed;              //is it red
    float x_touch_location;             //set as data members to calculate velocity after touch
    float y_touch_location;
    private Sprite sprite;          //sprite container for bubbles
    private Vector2 velocity;       //speed at which bubble moves
    private Vector2 position;       //position of bubble
    private Circle circleBound;     //collision detection representation of bubble
    public boolean is_grabbed = false;
    public int grabbed_by;

    /**
     * Atom() creates an instance of a bubble sprite for the array.
     * The colour is selected using a random Boolean isRed; true creates a
     * blue bubble, false creates red.
     * A random scale factor is then created to be attached to the bubble.
     */
    public Atom(boolean isRed) {
        this.isRed = isRed;
        Random rand = new Random();
        velocity = new Vector2(rand.nextInt(150), rand.nextInt(150));//set random velocity vector
        sprite = new Sprite(buildTexture(isRed));
        //sprite.setOriginCenter(); //for rotation
        setSize(rand.nextInt(RANGE) + 1);
        circleBound = new Circle(position.x + (sizeCurrent / 2), position.y + sizeCurrent / 2, sizeCurrent / 2);
    }

    public Texture buildTexture(boolean isRed) {
        Texture texture;
        if (isRed) {
            texture = new Texture("red.png");
            position = new Vector2(REDSTARTX, 0);
        } else {
            texture = new Texture("blue.png");
            position = new Vector2(BLUESTARTX, Atomsly.HEIGHT);
        }
        return texture;
    }

    public void update(float dt) { //dt = amount of time passed since last update
        this.dt = dt;
        if (isRed) {
            velocity.add(-BUOYANCY, 0);
        } else {
            velocity.add(BUOYANCY, 0);
        }
        velocity.scl(dt);
        if ((scaleFactor <= 1)) {
            scaleFactor += dt; //collect dt
            sizeCurrent = Math.round(sizeFinal * scaleFactor); //increase bubble scale
            circleBound.setRadius(sizeCurrent / 2);
        } else {
            position.add(velocity.x, velocity.y);
            circleBound.set(position, sizeCurrent / 2);
        }
        cornerCollision(); //detect collision after coordinates updated
        velocity.scl(1 / dt);
    }

    public Vector2 getPosition() {
        return position;
    }

    /* grabBubble() is called on touch and directs bubbles within 50 units
     * towards the touch location.
     */

    public void grabBubble(int pointer) {
        //the following retrieve the x and y coordinates of the current touch.
        x_touch_location = PlayState.X_SCALE_FACTOR * (Gdx.input.getX(pointer));
        y_touch_location = PlayState.Y_SCALE_FACTOR * (PlayState.SCREEN_HEIGHT - Gdx.input.getY(pointer));
        //the following creates a pair of variables to check the difference between
        //the bubble position and the touch location.
        float x_touch_difference = (position.x + sizeCurrent / 2) - x_touch_location;
        float y_touch_difference = (position.y + sizeCurrent / 2) - y_touch_location;

        if (Math.hypot(x_touch_difference, y_touch_difference) < sizeCurrent / 2) {
            is_grabbed = true;
            grabbed_by = pointer;
        }
    }
/*            position.x = x_touch_location - (sizeCurrent / 2);//-x_touch_difference; //set bubble to position of touch
            position.y = y_touch_location - (sizeCurrent / 2);//-y_touch_difference;
            //velocity.x = 0;
            //velocity.y = 0;
            velocity.x = (Gdx.input.getDeltaX(pointer)) / dt; //reset velocity
            velocity.y = (Gdx.input.getDeltaY(pointer)) / dt;
            if (velocity.x >= 15 / dt) {
                velocity.x = 15f / dt;
            }
            if (velocity.y >= 15 / dt) {
                velocity.y = 15f / dt;
            }
            if (velocity.x <= -15 / dt) {
                velocity.x = -15f / dt;
            }
            if (velocity.y <= -15 / dt) {
                velocity.y = -15f / dt;
            }
            //Check to see if the bubbles are in range of the touch and if so
            //direct them to the touch location.
//        if (Math.hypot(x_touch_difference, y_touch_difference) < sizeCurrent / 2) {
//            setThrowVelocity(x_touch_difference, y_touch_difference);
        }
    }*/

    public void dragBubble(float x, float y, int pointer) {
        position.x = PlayState.X_SCALE_FACTOR * x - (sizeCurrent / 2);//-x_touch_difference; //set bubble to position of touch
        position.y = PlayState.Y_SCALE_FACTOR * (PlayState.SCREEN_HEIGHT - y) - (sizeCurrent / 2);//-y_touch_difference; //offset texture.getWidth()/2 to centre bubble on touch (TODO needs better centre method for scaling)
        velocity.set((Gdx.input.getDeltaX(pointer) / dt), (Gdx.input.getDeltaY(pointer) / dt));
        if (velocity.x >= 15 / dt) {
            velocity.x = 15f / dt;
        }
        if (velocity.y >= 15 / dt) {
            velocity.y = 15f / dt;
        }
        if (velocity.x <= -15 / dt) {
            velocity.x = -15f / dt;
        }
        if (velocity.y <= -15 / dt) {
            velocity.y = -15f / dt;
        }

    }

    public void releaseBubble(int pointer) {
        if (grabbed_by == pointer) {
            is_grabbed = false;
        }
    }

    public Circle getCircleBound() {
        return circleBound;
    }

    /**
     * Work in progress; resizes surviving bubble after collision.
     * I want to implement a change in vector and velocity based
     * on the change in mass (i.e. dE=dMc^2->v=sqrt(2E/m), etc.
     */
    public void setSize(int newMass) {
        //scaleFactor = -atomicNumber/newMass;
        atomicNumber = newMass;
        sizeFinal = (int) Math.round(sprite.getWidth() * (0.4 + (0.6 * atomicNumber) / RANGE));
        sizeFinal = sizeFinal * 250 / 570; //ratio to get large atom (570) to normal atom (250)
        sizeCurrent = sizeFinal;
        //        velocity.set(newV);
    }

    public void cornerCollision() {
        if ((position.y < 0)) {
            position.y = 0;
            velocity.y = -velocity.y;
        }
        if (position.y > (Atomsly.HEIGHT - sizeCurrent)) {
            velocity.y = -velocity.y;
            position.y = Atomsly.HEIGHT - sizeCurrent;
        }
        if (position.x < 0) {
            position.x = 0;
            velocity.x = -velocity.x;
        }
        if (position.x > (Atomsly.WIDTH - sizeCurrent)) {
            position.x = Atomsly.WIDTH - sizeCurrent;
            velocity.x = -velocity.x;
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getSizeCurrent() {
        return sizeCurrent;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }
}


