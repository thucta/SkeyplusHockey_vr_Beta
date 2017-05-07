package com.skyplus.hockey.state;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyplus.hockey.Hockey;

/**
 * Created by NVTT on 4/27/2017.
 */

public class Loading  extends State {
    private Texture bg;

    protected Loading(GameStateManager gsm) {
        super(gsm);
        bg = new Texture(Hockey.PATCH+"backGame.png");


    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        sb.begin();;
        sb.draw(bg,0,0);
        sb.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }





}
