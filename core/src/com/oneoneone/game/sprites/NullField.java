package com.oneoneone.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.oneoneone.game.Atomsly;

/**
 * NullField.java
 * Purpose: Positions where player can annihilate an atom
 *
 * @author David Hampton, Grace Poole, Roderick Lenz
 * @version 0.01 07/08/2016
 */
public class NullField {
    private Sprite sprite;
    private Vector2 position;

    public NullField(boolean isRed) {
        if (isRed) {
            sprite = new Sprite(new Texture("null_field.png"));
            position = new Vector2(0, 0);
        } else {
            sprite = new Sprite(new Texture("null_field.png"));
            position = new Vector2(Atomsly.WIDTH - sprite.getWidth(), 0);
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getPosition() {
        return position;
    }
}
