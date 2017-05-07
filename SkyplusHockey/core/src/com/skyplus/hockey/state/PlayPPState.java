package com.skyplus.hockey.state;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyplus.hockey.Hockey;

/**
 * Created by THUC UYEN on 29-Apr-17.
 */
public class PlayPPState extends State implements Screen {


    private Texture bg;
    private Sprite button_Local, button_Bluetooth;


    public PlayPPState(GameStateManager gsm) {
        super(gsm);
        bg = new Texture(Hockey.PATCH + "backGame.png");
        button_Local = new Sprite(new Texture(Hockey.PATCH + "buttonLocal.png"));
        button_Bluetooth = new Sprite(new Texture(Hockey.PATCH + "buttonBluetooth.png"));

        button_Local.setPosition(Hockey.WITDH / 2 - button_Local.getWidth() / 2, Hockey.HEIGHT / 3 - button_Local.getHeight() / 2);
        button_Bluetooth.setPosition(Hockey.WITDH / 2 - button_Bluetooth.getWidth() / 2, Hockey.HEIGHT * 2 / 3 - button_Bluetooth.getHeight() / 2);

    }

    @Override
    public void handleInput() {
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
               if (button_Local.getBoundingRectangle().contains(screenX, screenY)) {
                    gsm.set(new ModeState(gsm));
                    dispose();
                    dispose();
                } else if (button_Bluetooth.getBoundingRectangle().contains(screenX, screenY)) {
                    gsm.set(new JoinGameState(gsm));
//                    dispose();
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, 0, 0, Hockey.WITDH, Hockey.HEIGHT);
        button_Local.draw(sb);
        button_Bluetooth.draw(sb);
        sb.end();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bg.dispose();
    }


}