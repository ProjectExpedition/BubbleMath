package com.oneoneone.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.oneoneone.game.BubbleMath;
import com.oneoneone.game.sprites.Bubble;

/**
 * Created by David on 9/07/2016.
 */
public class PlayState extends State {
    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public static final float X_SCALE_FACTOR = (float) BubbleMath.WIDTH / SCREEN_WIDTH;
    public static final float Y_SCALE_FACTOR = (float) BubbleMath.HEIGHT / SCREEN_HEIGHT;
    private Texture background;
    private Texture redSpawner;
    private Texture blueSpawner;
    private Texture goal;
//    private Texture redSpawner2;
//    private Texture blueSpawner2;
    private float timeKeeper = 0; //collects amount of time that has passed in game
    private BitmapFont font;
    private Array<Bubble> redArray; //Array Container of all bubbles
    private Array<Bubble> blueArray;

    /* PlayState(GameStateManager gsm) is called after Menu State
     * Allocates memory and calls constructors for all data members.
     */
    public PlayState(GameStateManager gsm) {
        super(gsm); //super = active state class
        //FileHandle filename = "DestructoBeamBB-200.fnt";
        font = new BitmapFont();
        redSpawner = new Texture("Rs_placeholder.png");
        blueSpawner = new Texture("Bs_placeholder.png");
        goal = new Texture("goal.png");
//        redSpawner2 = new Texture("red_spawner_2.png");
//        blueSpawner2 = new Texture("blue_spawner_2.png");
        background = new Texture("bg.jpg");
        redArray = new Array<Bubble>();
        blueArray = new Array<Bubble>();
        redArray.add(new Bubble(true)); //creates first bubble
        blueArray.add(new Bubble(false));
    }

    /* handleInput() checks if the person has touched the screen
    *  A maximum of 2 touch pointers are counted
    */
    @Override
    protected void handleInput() {
        for (int i = 0; i < 2; i++) {       //initializes to count maximum of two touch pointers
            if (Gdx.input.isTouched(i)) {//multitouch i is the pointer number where 0 is the first touch and 1 is the second
                for (Bubble bub : redArray) {
                    bub.grabBubble(i);
                }
                for (Bubble bub : blueArray) {
                    bub.grabBubble(i);
                }
            }
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
        if (timeKeeper > 0.1) { //if a certain number of poll times have passed spawn a bubble
//            if (redArray.get(0).getCircleBound().radius == 0 && redArray.get(0).getCircleBound().x == 0 && redArray.get(0).getCircleBound().y == 0) {
//                redArray.set(0, new Bubble(true));
//                blueArray.set(0, new Bubble(false));
//            } //ctrl + / to bring back, taken out because it was chucking an error and
            if (redArray.size < 20) {
                redArray.add(new Bubble(true));
            }
            if (blueArray.size < 20){
                blueArray.add(new Bubble(false)); //spawns bubble
            }
            timeKeeper = 0;//resets sum poll time
        }
        /** This control loop detects collisions and calculates the new velocity vector
         *  based on the size of the colliding bubbles. It was hard to write.
         **/
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
        int blueMass,redMass;
        //int[] redIndex = new int[10] , blueIndex = new int[10];
        for (int i = 0; i < redArray.size; i++) {   //I just prefer for loops, they're tidier than while
            /* I've moved the array update(dt) calls to separate loops.
             * Having them here causes index out of bounds errors when
             * a bubble is removed from one array and not the other.
             */
            for (int k = 0; k < blueArray.size; k++) {
                try {collision = redArray.get(i).getCircleBound().overlaps((blueArray.get(k).getCircleBound()));}
                catch (Exception iob){ //catch if index is out of bounds
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
                        blueArray.get(k).setNewSize(blueMass);
                        redArray.removeIndex(i);
                        i = i - 1;
                    } else { //removes the blue and resizes the red if red is bigger
                        redMass = redMass - blueMass;
                        redArray.get(i).setNewSize(redMass);
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
        /* The arrays need to be updated separately now as they
         * will be different sizes post collision
         */
        for(int i=0; i<redArray.size; i++){
            redArray.get(i).update(dt);
        }
        for(int i=0; i<blueArray.size; i++){
            blueArray.get(i).update(dt);
        }
    }

    /* render(float dt) draws all sprites to SpriteBatch declared in BubbleMath
    * draws after all positions and conditions have been calculated in update in BubbleMath render
    */
    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0);
        //font.setScale(200); //not working for some reason
        font.draw(sb, "13", BubbleMath.WIDTH/2, BubbleMath.HEIGHT/2);
        sb.draw(goal, BubbleMath.WIDTH/2 - (goal.getWidth()/2), BubbleMath.HEIGHT/2 - (goal.getHeight()/2));
        //sb.draw(redSpawner2, BubbleMath.WIDTH / 4 - (blueSpawner2.getWidth() / 4), 0);
        //sb.draw(blueSpawner2, 3 * BubbleMath.WIDTH / 4 - (blueSpawner2.getWidth() / 4), 0);
        for (Bubble bub : redArray) {
            sb.draw(bub.getSprite(), bub.getPosition().x, bub.getPosition().y,
                    bub.getSizeCurrent(), bub.getSizeCurrent());
        }
        for (Bubble bub : blueArray) {
            sb.draw(bub.getSprite(), bub.getPosition().x, bub.getPosition().y,
                    bub.getSizeCurrent(), bub.getSizeCurrent());
        }
        sb.draw(redSpawner, 0, 0);
        sb.draw(blueSpawner, BubbleMath.WIDTH - (blueSpawner.getWidth()), BubbleMath.HEIGHT - blueSpawner.getHeight());
        sb.end();
    }

    @Override
    public void dispose() {//TODO write a dispose method to avoid memory leaks

    }

}
