package com.skyplus.hockey.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.objects.CheckSoundMusic;
import com.skyplus.hockey.objects.HockeyPreferences;

/**
 * Created by THUC UYEN on 20-Apr-17.
 */

public class SettingState extends State implements Screen {
    private Texture bg;
    private Sprite check, uncheck, button_ST;
    private CheckSoundMusic checkSoundMusic = new CheckSoundMusic();
    private HockeyPreferences pref = new HockeyPreferences();

    public SettingState(GameStateManager gsm) {
        super(gsm);
        Gdx.app.log("here","SettingState");
        bg = new Texture(Hockey.PATCH + "backgroundMenu.png");
    checkSoundMusic.createCheckSoundMusic();
        check = new Sprite(new Texture(Hockey.PATCH+"check.png"));
        uncheck = new Sprite(new Texture(Hockey.PATCH+"uncheck.png"));
        check.setPosition(Hockey.WITDH*4/5, Hockey.HEIGHT/3-check.getHeight()/2);
        uncheck.setPosition(Hockey.WITDH*4/5, Hockey.HEIGHT/3-uncheck.getHeight()/2);

        button_ST = new Sprite(new Texture(Hockey.PATCH+"buttonExit.png"));
        button_ST.rotate(360);
        button_ST.setPosition(Hockey.WITDH/2-button_ST.getWidth()/2, Hockey.HEIGHT*3/4-button_ST.getHeight()/2);

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
                checkSoundMusic.clickCheckSoundMusic(screenX,screenY);
                if(button_ST.getBoundingRectangle().contains(screenX, screenY)){
                    gsm.set(new MenuState(gsm));
                    dispose();
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
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg,0,0, Hockey.WITDH, Hockey.HEIGHT);
        button_ST.setFlip(false,true);
        button_ST.draw(sb);
checkSoundMusic.drawCheckSoundMusic(sb);

        if(pref.getMusic()){
            check.setFlip(false,true);
            check.draw(sb);
        }else {
            uncheck.setFlip(false,true);
            uncheck.draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
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
}
