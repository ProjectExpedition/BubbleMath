package com.oneoneone.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.oneoneone.game.Atomsly;
import com.oneoneone.game.states.PlayState;

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
    private float bandMovesTo;
    private float position;
    private boolean isRed;

    public EnergyBand(float x) {
        isRed = x < Atomsly.WIDTH / 2;
        if (isRed) {
            bandMovesTo = x;
        } else {
            bandMovesTo = x;
        }
        position = x;
    }

    public void update(float dt) {
        float distance = bandMovesTo - position;
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

    public void setMoveToPosition(boolean isRed, float timeConstraint) {
        if (isRed) {
            bandMovesTo = (1 - timeConstraint) * (Atomsly.WIDTH / 2 - PlayState.BAND_OFFSET);
        } else {
            bandMovesTo = (1 + timeConstraint)*(Atomsly.WIDTH / 2 + PlayState.BAND_OFFSET);
        }
    }
}