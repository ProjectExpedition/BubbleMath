package com.oneoneone.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.oneoneone.game.Atomsly;

/**
 * EnergyBand.java
 * Purpose: Currently handles the field emitter sprites
 * todo: integrate EnergyBand.java and FieldEmitters.java
 *
 * @author David Hampton, Grace Poole, Roderick Lenz
 * @version 0.01 07/08/2016
 */
public class EnergyBand {
    private float velocity;
    private float positionMoveTo;
    private float position;
    private boolean isRed;
    private Texture texture;

    public EnergyBand(float x) {
        isRed = x < Atomsly.WIDTH / 2;
        if (isRed) {
            texture = new Texture("red_field.png");
            positionMoveTo = x;
        } else {
            texture = new Texture("blue_field.png");
            positionMoveTo = x;
        }
        position = x;
    }

    public void update(float dt) {
        float distance = positionMoveTo - position;
        if (distance > 0) {
            velocity = distance;
        } else if (distance < 0) {
            velocity = distance;
        } else {
            velocity = 0;
        }
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

    public void setPosition(float position) {
        this.positionMoveTo = position;
    }
}