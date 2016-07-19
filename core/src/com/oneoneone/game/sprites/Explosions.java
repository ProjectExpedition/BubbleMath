package com.oneoneone.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 18/07/2016.
 */
public class Explosions {
    private static final String bsplosion="bsplosion.particle";
    private static final String rsplosion="rsplosion.particle";
    private static final String rbsplosion="rbsplosion.particle";
    private String emitter;
    private Vector2 emitterPosition;
    private ParticleEffect peg;

    public Explosions(Vector2 pos, boolean isRed, float scaleFactor){
        scaleFactor= (float) (Math.ceil(scaleFactor/7))/3f;
        emitterPosition=pos;
        if(isRed){
            emitter=rsplosion;
        }else{
            emitter=bsplosion;
        }
        peg=new ParticleEffect();
        peg.load(Gdx.files.internal(emitter),Gdx.files.internal(""));
        peg.setPosition(emitterPosition.x,emitterPosition.y);
        peg.scaleEffect(scaleFactor);
        peg.start();
    }
    public void update(float dt){
        peg.update(dt);
    }

    public static String getBsplosion() {
        return bsplosion;
    }

    public String getEmitter() {
        return emitter;
    }

    public Vector2 getEmitterPosition() {
        return emitterPosition;
    }

    public void draw(Batch spriteBatch){
        peg.draw(spriteBatch);
    }
    public boolean isComplete(){
        return peg.isComplete();
    }
}
