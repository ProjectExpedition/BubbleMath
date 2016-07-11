package com.oneoneone.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by David on 9/07/2016.
 * States is an abstract class that is replaced by the inhereted class when called
 * Currently:
 *      MenuState ---> GameState TODO --> MenuState --> Next levelState
 */
public class GameStateManager {
    private Stack<State> states;

    public GameStateManager(){
        states = new Stack<State>();
    } //declares states
    public void push(State state){
        states.push(state);
    }
    public void pop(){
        states.pop();
    }
    public void get(State state){ //pushes argument into active state, i.e. makes PlayState active after MenuState
        states.pop();
        states.push(state);
    }
    public void update(float dt){
        states.peek().update(dt); //updates active state
    }
    public void render(SpriteBatch sb){
        states.peek().render(sb); //renders active state
    }
}
