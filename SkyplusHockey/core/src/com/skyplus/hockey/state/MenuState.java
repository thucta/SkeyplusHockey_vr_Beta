package com.skyplus.hockey.state;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyplus.hockey.Hockey;

/**
 * Created by TruongNN on 3/24/2017.
 */
public class MenuState extends State implements Screen {


    private Texture bg;
    private Sprite button_PC, button_PP, button_ST;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        bg = new Texture(Hockey.PATCH + "backGame.png");
        button_PC = new Sprite(new Texture(Hockey.PATCH + "buttonPC.png"));
        button_PP = new Sprite(new Texture(Hockey.PATCH + "buttonPP.png"));
        button_ST = new Sprite(new Texture(Hockey.PATCH + "buttonST.png"));

        button_PP.setPosition(Hockey.WITDH / 2 - button_PP.getWidth() / 2, Hockey.HEIGHT / 2 - button_PP.getHeight() * 3);
        button_PC.setPosition(Hockey.WITDH / 2 - button_PC.getWidth() / 2, Hockey.HEIGHT / 2 - button_PC.getHeight() / 2);
        button_ST.setPosition(Hockey.WITDH / 2 - button_ST.getWidth() / 2, Hockey.HEIGHT / 2 + button_ST.getHeight() * 2);

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

                clickButton(screenX, screenY);

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


    private void clickButton(int screenX, int screenY) {
        if (button_PP.getBoundingRectangle().contains(screenX, screenY)) {
            gsm.set(new PlayPPState(gsm));
            dispose();
        } else if (button_PC.getBoundingRectangle().contains(screenX, screenY)) {
            gsm.set(new ModeState(gsm));
            dispose();
        } else if (button_ST.getBoundingRectangle().contains(screenX, screenY)) {
            gsm.set(new SettingState(gsm));
            dispose();
        }
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
        button_PP.draw(sb);
        button_ST.draw(sb);
        button_PC.draw(sb);

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