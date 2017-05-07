package com.skyplus.hockey.state;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.skyplus.hockey.Hockey;

/**
 * Created by THUC UYEN on 29-Apr-17.
 */
public class WinState extends State implements Screen{


    private Texture bg;
    private Sprite button_Again, button_NewGame;




    public WinState(GameStateManager gsm) {
        super(gsm);
        Gdx.app.log("here","WinState");
        bg = new Texture(Hockey.PATCH+"backGame.png");
        button_Again = new Sprite(new Texture(Hockey.PATCH+"buttonAgain.png"));
        button_NewGame = new Sprite(new Texture(Hockey.PATCH+"buttonNewGame.png"));
        button_Again.setPosition(Hockey.WITDH/2-button_Again.getWidth()/2,Hockey.HEIGHT/2-button_Again.getHeight()/2);
        button_NewGame.setPosition(Hockey.WITDH/2-button_NewGame.getWidth()/2,Hockey.HEIGHT/2+button_NewGame.getHeight()*2);

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
                if (button_Again.getBoundingRectangle().contains(screenX,screenY)) {
                    gsm.set(new ModeState(gsm));
                    dispose();
                }
                else if (button_NewGame.getBoundingRectangle().contains(screenX,screenY)) {
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
//        sb.draw(button_PP,cam.viewportWidth/2-button_PP.getWidth()/2,cam.viewportHeight/2-button_PP.getHeight()*3 );
        button_Again.draw(sb);
        button_NewGame.draw(sb);


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