package com.skyplus.hockey.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.skyplus.hockey.Hockey;

/**
 * Created by TruongNN on 3/24/2017.
 */
public abstract class State {
    protected GameStateManager gsm;
    protected OrthographicCamera cam;

    protected State(GameStateManager gsm){
        this.gsm = gsm;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, Hockey.WITDH, Hockey.HEIGHT);
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void resize(int width, int height);
    public abstract void dispose();
    public abstract void pause();
    public abstract void resume();

}
