package com.oneoneone.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.oneoneone.game.Atomsly;

/**
 * Created by David on 18/07/2016.
 */
public class EnergyBand {
    private float velocity;
    private float positionResting;
    private float position;
    private boolean isRed;
    private Texture texture;

    public EnergyBand(float x) {
        isRed = x < Atomsly.WIDTH/2;
        if (isRed){
            texture = new Texture("red_field.png");
            positionResting = x - 100;
        } else {
            texture = new Texture("blue_field.png");
            positionResting = x + 100;
        }
        position = x;
        //velocity = positionResting-position;
    }

    public void update(float dt, float x){
        positionResting = x;
        float distance = positionResting - position;
        if(distance>0.01){
            velocity = distance;
        } else if (distance<0.01){
            velocity = distance;
        } else {velocity = 0;}
        velocity *= dt;
        position += velocity;
        velocity *= 1 / dt;
    }
    public float getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }
}