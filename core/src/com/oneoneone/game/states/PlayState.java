package com.oneoneone.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.oneoneone.game.BubbleMath;
import com.oneoneone.game.sprites.Bubble;

/**
 * Created by David on 9/07/2016.
 */
public class PlayState extends State {
    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public static final float SCALEX = (float) BubbleMath.WIDTH / SCREEN_WIDTH;
    public static final float SCALEY = (float) BubbleMath.HEIGHT / SCREEN_HEIGHT;
    private Texture bg;
    private Texture redSpawner;
    private Texture blueSpawner;
    private Texture redSpawner2;
    private Texture blueSpawner2;
    private float dtsum = 0; //collects amount of time that has passed in game
    private Array<Bubble> bubbles; //Array Container of all bubbles

    /* PlayState(GameStateManager gsm) is called after Menu State
     * Allocates memory and calls constructors for all data members.
     */
    public PlayState(GameStateManager gsm) {
        super(gsm); //super = active state class
//        redSpawner = new Texture("red_spawner.png");
//        blueSpawner = new Texture("blue_spawner.png");
//        redSpawner2 = new Texture("red_spawner_2.png");
//        blueSpawner2 = new Texture("blue_spawner_2.png");
        bg = new Texture("bg.png");
        bubbles = new Array<Bubble>();
        bubbles.add(new Bubble()); //creates first bubble
    }

    /* handleInput() checks if the person has touched the screen
    *  A maximum of 2 touch pointers are counted
    */
    @Override
    protected void handleInput() {
        for (int i = 0; i < 2; i++) {       //initializes to count maximum of two touch pointers
            if (Gdx.input.isTouched(i)) {//multitouch i is the pointer number where 0 is the first touch and 1 is the second
                for (Bubble bub : bubbles) {
                    bub.grab_bubble(i);
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
        dtsum = dtsum + dt; //sums poll time
        if (dtsum > 1.5) { //if a certain number of poll times have passed spawn a bubble
            if (bubbles.get(0).getBound().radius==0&&bubbles.get(0).getBound().x==0&&
                    bubbles.get(0).getBound().y==0){
                bubbles.set(0,new Bubble());
            }else if (bubbles.size < 20) {
                bubbles.add(new Bubble()); //spawns bubble
            }
            dtsum = 0;//resets sum poll time
        }
        /** This control loop detects collisions and calculates the new velocity vector
         *  based on the size of the colliding bubbles. It was hard to write.
         **/
        Vector2 normal = new Vector2(); //allocate memory once to improve eff.
        Vector2 unitNormal=new Vector2();
        Vector2 unitTangent=new Vector2();
        Vector2 iVelocity=new Vector2();
        Vector2 kVelocity=new Vector2();
        Vector2 iVelocity_projection_normal=new Vector2();
        Vector2 kVelocity_projection_normal=new Vector2();
        Vector2 iVelocity_projection_tangent=new Vector2();
        Vector2 kVelocity_projection_tangent=new Vector2();
        Vector2 newiVelocity=new Vector2();
        float iMass,kMass;
        for (int i=0; i<bubbles.size; i++) {
            for (int k = 0; k < bubbles.size; k++) {
                if(i!=k) {
                    if(bubbles.get(i).collision(bubbles.get(k).getBound())){
                        normal.set(bubbles.get(i).getPosition());
                        normal.sub(bubbles.get(k).getPosition());
                        unitNormal.set(normal);
                        //unitNormal.nor();
                        unitTangent.set(-unitNormal.y,unitNormal.x);
                        iVelocity.set(bubbles.get(i).getVelocity());
                        kVelocity.set(bubbles.get(k).getVelocity());
                        iVelocity_projection_normal.set(unitNormal);
                        iVelocity_projection_normal.dot(iVelocity);
                        kVelocity_projection_normal.set(unitNormal);
                        kVelocity_projection_normal.dot(kVelocity);
                        iVelocity_projection_tangent.set(unitTangent);
                        iVelocity_projection_tangent.dot(iVelocity);
                        kVelocity_projection_tangent.set(unitTangent);
                        kVelocity_projection_tangent.dot(kVelocity);
                        iMass = (bubbles.get(i).getBubbleScale())/100f;
                        kMass = (bubbles.get(k).getBubbleScale())/100f;
                        newiVelocity.set(iVelocity_projection_normal);
                        newiVelocity.scl(iMass-kMass);
                        newiVelocity.add(kVelocity_projection_normal.scl(2*kMass));
                        newiVelocity.scl(1/(iMass+kMass));
                        Vector2 newkVelocity=new Vector2();
                        newkVelocity.set(kVelocity_projection_normal);
                        newkVelocity.scl(kMass-iMass);
                        newkVelocity.add(iVelocity_projection_normal.scl(2*iMass));
                        newkVelocity.scl(1/(iMass+kMass));
                        newiVelocity.dot(unitNormal);
                        newkVelocity.dot(unitNormal);
//                        iVelocity_projection_tangent.dot(unitTangent);
//                        kVelocity_projection_tangent.dot(unitTangent);
                        newiVelocity.add(iVelocity_projection_tangent);
                        newkVelocity.add(kVelocity_projection_tangent);
                        bubbles.get(i).postCollisionVelocity(newiVelocity);
                        bubbles.get(k).postCollisionVelocity(newkVelocity);
//                        bubbles.removeIndex(i);
//                        bubbles.removeIndex(k);
                    }
                }
            }
            bubbles.get(i).update(dt); //calculates position changes to bubble
        }
    }

    /* render(float dt) draws all sprites to SpriteBatch declared in BubbleMath
    * draws after all positions and conditions have been calculated in update in BubbleMath render
    */
    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0);
        //sb.draw(redSpawner2, BubbleMath.WIDTH / 4 - (blueSpawner2.getWidth() / 4), 0);
        //sb.draw(blueSpawner2, 3 * BubbleMath.WIDTH / 4 - (blueSpawner2.getWidth() / 4), 0);
        for (Bubble bub : bubbles) {
            sb.draw(bub.getBubbleSprite(), bub.getPosition().x, bub.getPosition().y,
                    bub.getBubbleScale(), bub.getBubbleScale());
        }
        //sb.draw(redSpawner, BubbleMath.WIDTH / 4 - (blueSpawner.getWidth() / 4), 0);
        //sb.draw(blueSpawner, 3 * BubbleMath.WIDTH / 4 - (blueSpawner.getWidth() / 4), 0);
        sb.end();
    }

    @Override
    public void dispose() {//TODO write a dispose method to avoid memory leaks

    }
}
