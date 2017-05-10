package com.skyplus.hockey.state;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.objects.Audio;

/**
 * Created by THUC UYEN on 29-Apr-17.
 */
public class PlayPPState extends State implements Screen{


    private Texture bg;
    private Sprite button_Local,button_Bluetooth, button_Exit;
    private  Audio audio;
    
    public PlayPPState(GameStateManager gsm) {
        super(gsm);
        Gdx.app.log("here","MenuState");
        bg = new Texture(Hockey.PATCH+"backgroundMenu.png");
        button_Local = new Sprite(new Texture(Hockey.PATCH+"buttonLocal.png"));
        button_Bluetooth = new Sprite(new Texture(Hockey.PATCH+"buttonInter.png"));
        button_Exit = new Sprite(new Texture(Hockey.PATCH+"buttonExit.png"));


        float witdh = Hockey.WITDH/2-button_Local.getWidth()/2;
        button_Local.setPosition(witdh, Hockey.HEIGHT/4-button_Local.getHeight()/2);
        button_Bluetooth.setPosition(witdh, Hockey.HEIGHT/2-button_Bluetooth.getHeight()/2);
        button_Exit.setPosition(witdh, Hockey.HEIGHT*3/4- button_Exit.getHeight()/2);
audio = new Audio();
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
                if (button_Local.getBoundingRectangle().contains(screenX,screenY)) {
                    audio.getClick().play();
                    gsm.set(new PlayState(gsm));
                    dispose();
                }
                else if (button_Bluetooth.getBoundingRectangle().contains(screenX,screenY)) {
                    audio.getClick().play();
                    gsm.set(new JoinGameState(gsm));
                    dispose();
                }
                else if(button_Exit.getBoundingRectangle().contains(screenX,screenY)){
                    audio.getClick().play();
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
        button_Local.setFlip(false,true);
        button_Local.draw(sb);
        button_Bluetooth.setFlip(false,true);
        button_Bluetooth.draw(sb);
        button_Exit.setFlip(false,true);
        button_Exit.draw(sb);

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