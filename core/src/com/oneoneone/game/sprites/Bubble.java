package com.oneoneone.game.sprites;

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

        if (value <= 0) {//red
            texture = new Texture("blue.png");
            position = new Vector2(REDSTARTX,ALLSTARTY);
        } else if(value >0){ //blue
            texture = new Texture("red.png");
            position = new Vector2(BLUESTARTX,ALLSTARTY);
        }
        bound = new Circle(position.x,position.y,texture.getWidth()/2);
    }

    public void update(float dt){
        if(position.y > 0) {
            velocity.add(0, BUOYANCY);
        }
        velocity.scl(dt);
        position.add(velocity.x, velocity.y);
        cornerCollision();
        velocity.scl(1/dt);
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

    public boolean collides(Circle C){
        return C.overlaps(bound);
    }

    public int getValue() {
        return value;
    }

    public Texture getBubble() {
        return texture;
    }
    public void walk(){
        velocity.y = rand.nextInt()%500;
        velocity.x = rand.nextInt()%500;
    }

    public Circle getBound() {
        return bound;
    }
}

