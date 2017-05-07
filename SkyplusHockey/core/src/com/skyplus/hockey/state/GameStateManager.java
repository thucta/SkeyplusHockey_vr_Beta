package com.skyplus.hockey.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by TruongNN  on 3/24/2017.
 */
public class GameStateManager {
    public Stack<State> states;

    public GameStateManager(){

        states = new Stack<State>();
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop();
    }

    public void set(State state){
        states.pop();
        states.push(state);
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }
    public void resize(int width, int height){
        states.peek().resize(width,height);
    }
}
