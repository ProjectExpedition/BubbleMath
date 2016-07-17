package com.oneoneone.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;
import com.oneoneone.game.Atomsly;
import com.oneoneone.game.sprites.Atom;

import java.util.Random;

import com.badlogic.gdx.InputAdapter;

/**
 * Created by David on 9/07/2016.
 */
public class PlayState extends State {
    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public static final float X_SCALE_FACTOR = (float) Atomsly.WIDTH / SCREEN_WIDTH;
    public static final float Y_SCALE_FACTOR = (float) Atomsly.HEIGHT / SCREEN_HEIGHT;
    private static final int FONT_SIZE = 72;
    private int sum = 0;
    private Texture background;
    private Texture redSpawner;
    private Texture blueSpawner;
    //    private Texture redSpawner2;
//    private Texture blueSpawner2;
    private float timeKeeper = 0; //collects amount of time that has passed in game
    //private BitmapFont font;
    private Array<Atom> redArray; //Array Container of all bubbles
    private Array<Atom> blueArray;
    BitmapFont font;
    private int goal;
    private int score = 0;
    private Random rand;

    /* PlayState(GameStateManager gsm) is called after Menu State
     * Allocates memory and calls constructors for all data members.
     */
    public PlayState(GameStateManager gsm) {
        super(gsm); //super = active state class
        buildFont();
        buildTextures();
        buildAtoms();
        rand = new Random();
        goal = rand.nextInt(50);
    }

    private void buildAtoms() {
        redArray = new Array<Atom>();
        blueArray = new Array<Atom>();
        redArray.add(new Atom(true)); //creates first bubble
        blueArray.add(new Atom(false));
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
        redSpawner = new Texture("spawn_red.png");
        blueSpawner = new Texture("spawn_blue.png");
//        redSpawner2 = new Texture("red_spawner_2.png");
//        blueSpawner2 = new Texture("blue_spawner_2.png");
        background = new Texture("bg.png");
    }

    /* handleInput() checks if the person has touched the screen
    *  A maximum of 2 touch pointers are counted
    */
    @Override
    protected void handleInput() {
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
        if (timeKeeper > 10) { //if a certain number of poll times have passed spawn a bubble
            if (redArray.size < 5) {
                redArray.add(new Atom(true));
            }
            if (blueArray.size < 5) {
                blueArray.add(new Atom(false)); //spawns bubble
            }
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
                        blueArray.removeIndex(k);
                        redArray.removeIndex(i);
                        i = i - 1; //resetting the iteration here means no bubbles are skipped on the this update
                        k = k - 1;
                    } else if (blueMass > redMass) { //removes the red and resizes the blue if the blue is larger
                        blueMass = blueMass - redMass;
                        blueArray.get(k).setSize(blueMass);
                        redArray.removeIndex(i);
                        i = i - 1;
                    } else { //removes the blue and resizes the red if red is bigger
                        redMass = redMass - blueMass;
                        redArray.get(i).setSize(redMass);
                        blueArray.removeIndex(k);
                        k = k - 1;
                    }
                }
                        /*normal.set(bubbles.get(i).getPosition());
                        normal.sub(bubbles.get(k).getPosition());
                        unitNormal.set(normal);
                        unitNormal.nor();
                        unitTangent.set(-unitNormal.y,unitNormal.x);
                        iVelocity.set(bubbles.get(i).getVelocity());
                        kVelocity.set(bubbles.get(k).getVelocity());
                        iVelocity_projection_normal=unitNormal.dot(iVelocity);
                        //iVelocity_projection_normal.dot(iVelocity);
                        kVelocity_projection_normal.set(unitNormal);
                        kVelocity_projection_normal.dot(kVelocity);
                        iVelocity_projection_tangent.set(unitTangent);
                        iVelocity_projection_tangent.dot(iVelocity);
                        kVelocity_projection_tangent.set(unitTangent);
                        kVelocity_projection_tangent.dot(kVelocity);
                        iMass = (bubbles.get(i).getSizeCurrent())/100f;
                        kMass = (bubbles.get(k).getSizeCurrent())/100f;
                        newiVelocity=iVelocity_projection_normal*(iMass-kMass);
                        newiVelocity.add(kVelocity_projection_normal.scl(2*kMass));
                        newiVelocity.scl(1/(iMass+kMass));
                        Vector2 newkVelocity=new Vector2();
                        newkVelocity.set(kVelocity_projection_normal);
                        newkVelocity.scl(kMass-iMass);
                        newkVelocity.add(iVelocity_projection_normal.scl(2*iMass));
                        newkVelocity.scl(1/(iMass+kMass));
                        newiVelocity.dot(unitNormal);
                        newkVelocity.dot(unitNormal);
                        iVelocity_projection_tangent.dot(unitTangent);
                        kVelocity_projection_tangent.dot(unitTangent);
                        newiVelocity.add(iVelocity_projection_tangent);
                        newkVelocity.add(kVelocity_projection_tangent);
                        bubbles.get(i).postCollisionVelocity(newiVelocity);
                        bubbles.get(k).postCollisionVelocity(newkVelocity);
                        bubbles.removeIndex(i);
                        bubbles.removeIndex(k);*/
            }
            //calculates position changes to bubble
        }
    }

    /* update(float dt) is called in GameStateManager (see states.peek().update(dt))
    * updates the mathematics of everything that happens; velocity, coordinates, inputs,
    * checking,etc
    */
    @Override
    public void update(float dt) {
        handleInput(); //calls first to see if screen has been touched before updating
        timeKeeper += dt; //sums poll time
        doSpawn();
        annihilate();
        sum = 0; //reset sum
        for (int i = 0; i < redArray.size; i++) {
            redArray.get(i).update(dt);
            sum += redArray.get(i).getAtomicNumber();
        }
        for (int i = 0; i < blueArray.size; i++) {
            blueArray.get(i).update(dt);
            sum += blueArray.get(i).getAtomicNumber();
        }
    }

    /* render(float dt) draws all sprites to SpriteBatch declared in Atomsly
    * draws after all positions and conditions have been calculated in update in Atomsly render
    */
    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0);
        font.setColor(com.badlogic.gdx.graphics.Color.GRAY);
        font.draw(sb, Integer.toString(sum), Atomsly.WIDTH / 2 - FONT_SIZE / 2, Atomsly.HEIGHT / 2 + FONT_SIZE / 2);
        font.draw(sb, "GOAL: " + goal, Atomsly.WIDTH / 2 - 4 * FONT_SIZE / 2, 3 * Atomsly.HEIGHT / 4 + FONT_SIZE / 2);
        font.draw(sb, Integer.toString(goal - sum), Atomsly.WIDTH / 2 - FONT_SIZE / 2, Atomsly.HEIGHT / 4 + FONT_SIZE / 2);
        font.setColor(Color.WHITE);
        font.draw(sb, Integer.toString(score), FONT_SIZE / 2, Atomsly.HEIGHT - FONT_SIZE / 2);
        //sb.draw(goal, Atomsly.WIDTH/2 - (goal.getWidth()/2), Atomsly.HEIGHT/2 - (goal.getHeight()/2));
        //sb.draw(redSpawner2, Atomsly.WIDTH / 4 - (blueSpawner2.getWidth() / 4), 0);
        //sb.draw(blueSpawner2, 3 * Atomsly.WIDTH / 4 - (blueSpawner2.getWidth() / 4), 0);
        font.setColor(com.badlogic.gdx.graphics.Color.RED);
        for (Atom bub : redArray) {
//            Sprite sprite = bub.getSprite();
//            sb.draw(sprite, bub.getPosition().x, bub.getPosition().y, sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
            sb.draw(bub.getSprite(), bub.getPosition().x, bub.getPosition().y,
                    bub.getSizeCurrent(), bub.getSizeCurrent());
            if (bub.getAtomicNumber() < 10) {
                font.draw(sb, Integer.toString(bub.getAtomicNumber()), bub.getPosition().x + bub.getSizeCurrent() / 2 - FONT_SIZE / 4, bub.getPosition().y + bub.getSizeCurrent() / 2 + FONT_SIZE / 2);
            } else {
                font.draw(sb, Integer.toString(bub.getAtomicNumber()), bub.getPosition().x + bub.getSizeCurrent() / 2 - FONT_SIZE / 2, bub.getPosition().y + bub.getSizeCurrent() / 2 + FONT_SIZE / 2);
            }
        }
        font.setColor(com.badlogic.gdx.graphics.Color.BLUE);
        for (Atom bub : blueArray) {
            sb.draw(bub.getSprite(), bub.getPosition().x, bub.getPosition().y,
                    bub.getSizeCurrent(), bub.getSizeCurrent());
            if (bub.getAtomicNumber() < 10) {
                font.draw(sb, Integer.toString(bub.getAtomicNumber()), bub.getPosition().x + bub.getSizeCurrent() / 2 - FONT_SIZE / 4, bub.getPosition().y + bub.getSizeCurrent() / 2 + FONT_SIZE / 2);
            } else {
                font.draw(sb, Integer.toString(bub.getAtomicNumber()), bub.getPosition().x + bub.getSizeCurrent() / 2 - FONT_SIZE / 2, bub.getPosition().y + bub.getSizeCurrent() / 2 + FONT_SIZE / 2);
            }
        }
        sb.draw(redSpawner, 0, 0);
        sb.draw(blueSpawner, Atomsly.WIDTH - (blueSpawner.getWidth()), Atomsly.HEIGHT - blueSpawner.getHeight());
        sb.end();
        if (sum == goal) {
            goal = rand.nextInt(50);
            score++;
            if (score == 3) {
                gsm.get(new MenuState(gsm));
                dispose();
            }

        }
    }

    @Override
    public void dispose() {//TODO write a dispose method to avoid memory leaks
        background.dispose();
        font.dispose();
        redArray.clear();
        blueArray.clear();
        redSpawner.dispose();
        blueSpawner.dispose();
    }

}
