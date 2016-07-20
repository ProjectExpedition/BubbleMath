package com.oneoneone.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;
import com.oneoneone.game.Atomsly;
import com.oneoneone.game.sprites.Atom;

import java.util.Random;

import com.badlogic.gdx.InputAdapter;
import com.oneoneone.game.sprites.EnergyBand;
import com.oneoneone.game.sprites.Explosions;
import com.oneoneone.game.sprites.FieldEmitters;

/**
 * Created by David on 9/07/2016.
 */
public class PlayState extends State {
    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public static final float X_SCALE_FACTOR = (float) Atomsly.WIDTH / SCREEN_WIDTH;
    public static final float Y_SCALE_FACTOR = (float) Atomsly.HEIGHT / SCREEN_HEIGHT;
    private static final int FONT_SIZE = 72;
    private int sumRed = 0;
    private int sumBlue = 0;
    private Texture background;
    private Texture redSpawner;
    private Texture blueSpawner;
    private Texture redSpawner2;
    private Texture blueSpawner2;
    private float timeKeeper = 0; //collects amount of time that has passed in game
    private Array<Atom> redArray; //Array Container of all bubbles
    private Array<Atom> blueArray;
    private Array<Explosions> explosions;
    private EnergyBand redEnergyBand;
    private EnergyBand blueEnergyBand;
    private FieldEmitters redField;
    private FieldEmitters blueField;
    private BitmapFont font;
    private int goal;
    private int score = 0;
    private Random rand;
    public int grabLoop = 0;
    private float sumdt = 0;
    private Sound boom=Gdx.audio.newSound(Gdx.files.internal("boom.wav"));
//    private Sound boom2=Gdx.audio.newSound(Gdx.files.internal("boom2.wav"));
    private Sound boom3=Gdx.audio.newSound(Gdx.files.internal("boom3.wav"));
    private Sound boom4=Gdx.audio.newSound(Gdx.files.internal("boom4.wav"));
    private Sound boom5=Gdx.audio.newSound(Gdx.files.internal("boom5.wav"));
    private Sound fieldSound=Gdx.audio.newSound(Gdx.files.internal("field.wav"));

//    private ShapeRenderer SR;

    public PlayState(GameStateManager gsm) {
        /* PlayState(GameStateManager gsm) is called after Menu State
        * Allocates memory and calls constructors for all data members.
        */
        super(gsm); //super = active state class
        buildFont();
        buildTextures();
        buildObjects();
        fieldSound.loop(0.5f);
        rand = new Random();
        goal = rand.nextInt(100 - 10) + 10;
//        SR = new ShapeRenderer();
    }

    private void buildObjects() {
        redArray = new Array<Atom>();
        blueArray = new Array<Atom>();
        explosions = new Array<Explosions>();

        redEnergyBand = new EnergyBand((Atomsly.WIDTH / 2) - 30);
        blueEnergyBand = new EnergyBand((Atomsly.WIDTH / 2) + 30);
        redField = new FieldEmitters(redEnergyBand.getPosition(),true);
        blueField = new FieldEmitters(blueEnergyBand.getPosition(),false);
    }

    private void buildFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        //parameter.characters = "13";
        font = generator.generateFont(parameter);
        generator.dispose(); //no longer needed
    }

    private void buildTextures() {
        redSpawner = new Texture("RPipe.png");
        blueSpawner = new Texture("BPipe.png");
        redSpawner2 = new Texture("RHole.png");
        blueSpawner2 = new Texture("Bhole.png");
        background = new Texture("bg.png");
    }

    @Override
    protected void handleInput() {
        /* handleInput() checks if the person has touched the screen
        *  A maximum of 2 touch pointers are counted
        */
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
//                for (int i = 0; i <= 1; i++) {       //initializes to count maximum of two touch pointers
                if (pointer < 2) {//multitouch i is the pointer number where 0 is the first touch and 1 is the second
                    for (Atom bub : redArray) {
                        bub.grabBubble(pointer);
                    }
                    for (Atom bub : blueArray) {
                        bub.grabBubble(pointer);
                    }
                }
                return true; // return true to indicate the event was handled
            }

            @Override
            public boolean touchUp(int x, int y, int pointer, int button) {
                for (Atom bub : redArray) {
                    bub.releaseBubble(pointer);
                }
                for (Atom bub : blueArray) {
                    bub.releaseBubble(pointer);
                }
                return true; // return true to indicate the event was handled
            }

            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                if (pointer < 2) {
                    for (Atom bub : redArray) {
                        if (bub.is_grabbed && bub.grabbed_by == pointer) {
                            bub.dragBubble(x, y, pointer);
                        }
                    }
                    for (Atom bub : blueArray) {
                        if (bub.is_grabbed && bub.grabbed_by == pointer) {
                            bub.dragBubble(x, y, pointer);
                        }
                    }
                }
                return false;
            }
        });
    }

    private void doSpawn() {
        if ((timeKeeper > 8) || (redArray.size + blueArray.size == 0) && (timeKeeper > 1)) { //if a certain number of poll times have passed spawn a bubble
            //if (redArray.size < 3) {
            redArray.add(new Atom(true, redEnergyBand.getPosition()));
            //}
            //if (blueArray.size < 3) {
            blueArray.add(new Atom(false, blueEnergyBand.getPosition())); //spawns bubble
            //}
            timeKeeper = 0;//resets sum poll time
        }
    }

    private void annihilate() {
        //        Vector2 normal = new Vector2(); //allocate memory once to improve eff.
//        Vector2 unitNormal = new Vector2();
//        Vector2 unitTangent = new Vector2();
//        Vector2 iVelocity = new Vector2();
//        Vector2 kVelocity = new Vector2();
//        float iVelocity_projection_normal;
//        Vector2 kVelocity_projection_normal = new Vector2();
//        Vector2 iVelocity_projection_tangent = new Vector2();
//        Vector2 kVelocity_projection_tangent = new Vector2();
//        float newiVelocity;
//        float iMass, kMass;
//        int redOffset, blueOffset;
        boolean collision;
        int blueMass, redMass;
        for (int i = 0; i < redArray.size; i++) {
            for (int k = 0; k < blueArray.size; k++) {
                try {
                    collision = redArray.get(i).getCircleBound().overlaps((blueArray.get(k).getCircleBound()));
                } catch (Exception iob) { //catch if index is out of bounds
                    break; //leave loop TODO not sure if this skips some collisions
                }
                if (collision) {
                    blueMass = blueArray.get(k).getAtomicNumber();
                    redMass = redArray.get(i).getAtomicNumber();
                    if (blueMass == redMass) {  //this clause removes both bubbles if they're equal mass
                        explosions.add(new Explosions(blueArray.get(k).getPosition(), false,blueMass));
                        explosions.add(new Explosions(redArray.get(i).getPosition(), true,redMass));
                        blueArray.removeIndex(k);
                        redArray.removeIndex(i);
                        i = i - 1; //resetting the iteration here means no bubbles are skipped on the this update
                        k = k - 1;
                        playBoom(redMass);
                        playBoom(blueMass);
                    } else if (blueMass > redMass) { //removes the red and resizes the blue if the blue is larger
                        blueMass = blueMass - redMass;
                        explosions.add(new Explosions(redArray.get(i).getPosition(), true, redMass));
                        blueArray.get(k).setSize(blueMass);
                        redArray.removeIndex(i);
                        i = i - 1;
                        playBoom(redMass);
                    } else { //removes the blue and resizes the red if red is bigger
                        redMass = redMass - blueMass;
                        explosions.add(new Explosions(blueArray.get(k).getPosition(), false,blueMass));
                        redArray.get(i).setSize(redMass);
                        blueArray.removeIndex(k);
                        k = k - 1;
                        playBoom(blueMass);
                    }
                }
                /*collision detection stuff is here*/
//                        normal.set(bubbles.get(i).getPosition());
//                        normal.sub(bubbles.get(k).getPosition());
//                        unitNormal.set(normal);
//                        unitNormal.nor();
//                        unitTangent.set(-unitNormal.y,unitNormal.x);
//                        iVelocity.set(bubbles.get(i).getVelocity());
//                        kVelocity.set(bubbles.get(k).getVelocity());
//                        iVelocity_projection_normal=unitNormal.dot(iVelocity);
//                        //iVelocity_projection_normal.dot(iVelocity);
//                        kVelocity_projection_normal.set(unitNormal);
//                        kVelocity_projection_normal.dot(kVelocity);
//                        iVelocity_projection_tangent.set(unitTangent);
//                        iVelocity_projection_tangent.dot(iVelocity);
//                        kVelocity_projection_tangent.set(unitTangent);
//                        kVelocity_projection_tangent.dot(kVelocity);
//                        iMass = (bubbles.get(i).getSizeCurrent())/100f;
//                        kMass = (bubbles.get(k).getSizeCurrent())/100f;
//                        newiVelocity=iVelocity_projection_normal*(iMass-kMass);
//                        newiVelocity.add(kVelocity_projection_normal.scl(2*kMass));
//                        newiVelocity.scl(1/(iMass+kMass));
//                        Vector2 newkVelocity=new Vector2();
//                        newkVelocity.set(kVelocity_projection_normal);
//                        newkVelocity.scl(kMass-iMass);
//                        newkVelocity.add(iVelocity_projection_normal.scl(2*iMass));
//                        newkVelocity.scl(1/(iMass+kMass));
//                        newiVelocity.dot(unitNormal);
//                        newkVelocity.dot(unitNormal);
//                        iVelocity_projection_tangent.dot(unitTangent);
//                        kVelocity_projection_tangent.dot(unitTangent);
//                        newiVelocity.add(iVelocity_projection_tangent);
//                        newkVelocity.add(kVelocity_projection_tangent);
//                        bubbles.get(i).postCollisionVelocity(newiVelocity);
//                        bubbles.get(k).postCollisionVelocity(newkVelocity);
//                        bubbles.removeIndex(i);
//                        bubbles.removeIndex(k);
            }
        }
    }
    private void playBoom(float volume){
        volume= (float) (0.4 + (0.6 * volume) / Atom.getRANGE());
        boom5.play(volume);
    }

    @Override
    public void update(float dt) {
        /* update(float dt) is called in GameStateManager (see states.peek().update(dt))
        * updates the mathematics of everything that happens; velocity, coordinates, inputs,
        * checking,etc
        */
        handleInput(); //calls first to see if screen has been touched before updating
        timeKeeper += dt; //sums poll time
        doSpawn();
        annihilate();
        findSum();
        updateField(dt);
        redEnergyBand.update(dt);
        blueEnergyBand.update(dt);
        redField.update(dt);
        blueField.update(dt);
        for (int i = 0; i < explosions.size; i++) {
            if (explosions.get(i).isComplete()) {
                explosions.removeIndex(i);
            }
        }
        for (Explosions peg : explosions) {
            peg.update(dt);
        }
        for (int i = 0; i < redArray.size; i++) {
            redArray.get(i).update(dt, redEnergyBand.getPosition());
        }
        for (int i = 0; i < blueArray.size; i++) {
            blueArray.get(i).update(dt, blueEnergyBand.getPosition());
        }
    }

    private void findSum() {
        sumRed = 0; //reset sum
        for (int i = 0; i < redArray.size; i++) {
            sumRed += redArray.get(i).getAtomicNumber();
        }
        sumBlue = 0;
        for (int i = 0; i < blueArray.size; i++) {
            sumBlue += blueArray.get(i).getAtomicNumber();
        }
    }

    private void updateField(float dt) {
        sumdt += dt;
        float constraint = sumdt / 100;
        float Rprop = 1f - sumRed / ((4f - constraint) * (float) goal);
        float Bprop = 1f + sumBlue / ((4f - constraint) * (float) goal);
        redEnergyBand.setPosition((((float) Atomsly.WIDTH - 200) / 2f) * Rprop);
        blueEnergyBand.setPosition((((float) Atomsly.WIDTH + 200) / 2f) * Bprop);
        redField.setPosition(redEnergyBand.getPosition());
        blueField.setPosition(blueEnergyBand.getPosition());
        //redEnergyBandMoveToPosition = (((float)Atomsly.WIDTH-200)/2f) * Rprop;
        //blueEnergyBandMoveToPosition = (((float)Atomsly.WIDTH+200)/2f) * Bprop;
    }

    @Override
    public void render(SpriteBatch sb) {
        /* render(float dt) draws all sprites to SpriteBatch declared in Atomsly
        * draws after all positions and conditions have been calculated in update in Atomsly render
        */
        if (/*(score == 1) ||*/ (redEnergyBand.getPosition() <= 0) || (blueEnergyBand.getPosition() >= Atomsly.WIDTH)) {
            fieldSound.stop();
            gsm.get(new MenuState(gsm));
            dispose();
        }
        sb.begin();
        sb.draw(background, 0, 0);
        font.setColor(com.badlogic.gdx.graphics.Color.GRAY);
        font.draw(sb, Integer.toString(sumRed + sumBlue), Atomsly.WIDTH / 2 - FONT_SIZE / 2, Atomsly.HEIGHT / 2 + FONT_SIZE / 2);
        font.draw(sb, "GOAL: " + goal, Atomsly.WIDTH / 2 - 4 * FONT_SIZE / 2, 3 * Atomsly.HEIGHT / 4 + FONT_SIZE / 2);
        font.draw(sb, Integer.toString(goal - (sumRed + sumBlue)), Atomsly.WIDTH / 2 - FONT_SIZE / 2, Atomsly.HEIGHT / 4 + FONT_SIZE / 2);
        font.setColor(Color.WHITE);
        font.draw(sb, Integer.toString(score), FONT_SIZE / 2, Atomsly.HEIGHT - FONT_SIZE / 2);
        font.draw(sb,"FPS: " + Integer.toString(Gdx.graphics.getFramesPerSecond()), 50f, 50f);
        font.setColor(com.badlogic.gdx.graphics.Color.RED);
        sb.draw(redSpawner2, redEnergyBand.getPosition() - redSpawner2.getWidth() / 2, -70);
        sb.draw(blueSpawner2, blueEnergyBand.getPosition() - blueSpawner2.getWidth() / 2, 70 + Atomsly.HEIGHT - blueSpawner.getHeight());
        redField.draw(sb);
        blueField.draw(sb);
//        sb.draw(redEnergyBand.getTexture(), redEnergyBand.getPosition() - redEnergyBand.getTexture().getWidth() / 2, 0);//redEnergyBandMoveToPosition - (float) redBand.getWidth()/2f, 0);
//        sb.draw(blueEnergyBand.getTexture(), blueEnergyBand.getPosition() - blueEnergyBand.getTexture().getWidth() / 2, 0); //blueEnergyBandMoveToPosition - (float) blueBand.getWidth()/2f, 0);

        for (Atom bub : redArray) {
//            Sprite sprite = bub.getSprite();
//            sb.draw(sprite, bub.getPosition().x, bub.getPosition().y, sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
            sb.draw(bub.getSprite(), bub.getPosition().x - bub.getSizeCurrent() / 2, bub.getPosition().y - bub.getSizeCurrent() / 2,
                    bub.getSizeCurrent(), bub.getSizeCurrent());

            if (bub.getAtomicNumber() < 10) {
                font.draw(sb, Integer.toString(bub.getAtomicNumber()), bub.getPosition().x - FONT_SIZE / 4, bub.getPosition().y + FONT_SIZE / 2);
            } else {
                font.draw(sb, Integer.toString(bub.getAtomicNumber()), bub.getPosition().x - FONT_SIZE / 2, bub.getPosition().y + FONT_SIZE / 2);
            }
        }
        font.setColor(com.badlogic.gdx.graphics.Color.BLUE);
        for (Atom bub : blueArray) {
            sb.draw(bub.getSprite(), bub.getPosition().x - bub.getSizeCurrent() / 2, bub.getPosition().y - bub.getSizeCurrent() / 2,
                    bub.getSizeCurrent(), bub.getSizeCurrent());

            if (bub.getAtomicNumber() < 10) {
                font.draw(sb, Integer.toString(bub.getAtomicNumber()), bub.getPosition().x - FONT_SIZE / 4, bub.getPosition().y + FONT_SIZE / 2);
            } else {
                font.draw(sb, Integer.toString(bub.getAtomicNumber()), bub.getPosition().x - FONT_SIZE / 2, bub.getPosition().y + FONT_SIZE / 2);
            }
        }
        sb.draw(redSpawner, redEnergyBand.getPosition() - redSpawner.getWidth() / 2, -70);
        sb.draw(blueSpawner, blueEnergyBand.getPosition() - blueSpawner.getWidth() / 2, 70 + Atomsly.HEIGHT - blueSpawner.getHeight());

//        SR.setColor(Color.BLACK);
//        SR.begin(ShapeRenderer.ShapeType.Line);
//        for (Atom bub : redArray) {
//            SR.circle(bub.getCircleBound().x,bub.getCircleBound().y,bub.getCircleBound().radius);
//        }
//        for (Atom bub : blueArray) {
//            SR.circle(bub.getCircleBound().x,bub.getCircleBound().y,bub.getCircleBound().radius);
//        }
//        SR.end();
        for (Explosions peg : explosions) {
            peg.draw(sb);
        }
        sb.end();
        if (sumRed + sumBlue == goal) {
            goal = rand.nextInt(50 - 10) + 10;
            score++;
            if (/*(score == 3) || */(redEnergyBand.getPosition() <= 0) || (blueEnergyBand.getPosition() >= Atomsly.WIDTH)) {
                gsm.get(new MenuState(gsm));
                dispose();
            }
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
        redArray.clear();
        blueArray.clear();
        explosions.clear();
        redField.dispose();
        blueField.dispose();
        redSpawner.dispose();
        blueSpawner.dispose();
        boom.dispose();
//        boom2.dispose();
        boom3.dispose();
        boom4.dispose();
//        boom5.dispose();
        fieldSound.dispose();
    }
}
