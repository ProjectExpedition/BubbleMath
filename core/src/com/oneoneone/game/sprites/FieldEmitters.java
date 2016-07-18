package com.oneoneone.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.oneoneone.game.Atomsly;

/**
 * Created by Rod on 18/07/2016.
 */
public class FieldEmitters {
    private static final String rfield = "rfield.particle";
    private static final String bfield = "bfield.particle";
    private String fieldColor;
    private ParticleEffect peg;
    private float posY;
    public FieldEmitters(float posX, boolean isRed) {
        if(isRed){
            fieldColor=rfield;
            posY=0;
        }else{
            fieldColor=bfield;
            posY= Atomsly.HEIGHT;
        }
        peg = new ParticleEffect();
        peg.load(Gdx.files.internal(fieldColor), Gdx.files.internal(""));
        peg.setPosition(posX,posY);
        peg.start();
    }

    public void update(float dt) {
        peg.update(dt);
    }

    public void draw(Batch spriteBatch){
        peg.draw(spriteBatch);
    }
    public boolean isComplete(){
        return peg.isComplete();
    }
    public void setPosition(float posX){
        peg.setPosition(posX,posY);
    }
}
