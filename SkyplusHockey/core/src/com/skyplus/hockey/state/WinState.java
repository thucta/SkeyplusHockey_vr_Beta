package com.skyplus.hockey.state;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyplus.hockey.Hockey;

/**
 * Created by THUC UYEN on 29-Apr-17.
 */
public class WinState extends State implements Screen{


    private Texture bg;
    private Sprite button_Again, button_NewGame, button_Result, button_Result2;
    private String kq2;




    public WinState(GameStateManager gsm, String kq, String kq2) {
        super(gsm);
        Gdx.app.log("here","WinState");
        bg = new Texture(Hockey.PATCH+"backGame.png");
        this.kq2=kq2;
        button_Again = new Sprite(new Texture(Hockey.PATCH+"buttonAgain.png"));
        button_NewGame = new Sprite(new Texture(Hockey.PATCH+"buttonNewGame.png"));
        button_Result = new Sprite(new Texture(Hockey.PATCH +kq+".png"));
        if(!kq2.equals("null")){
            button_Result2 = new Sprite(new Texture(Hockey.PATCH +kq2+".png"));
            button_Result.setPosition(Hockey.WITDH/2 - button_Result.getWidth()/2, Hockey.HEIGHT/5- button_Result.getHeight()/2);
            button_Again.setPosition(Hockey.WITDH/2-button_Again.getWidth()/2, Hockey.HEIGHT*2/5-button_Again.getHeight()/2);
            button_NewGame.setPosition(Hockey.WITDH/2-button_NewGame.getWidth()/2, Hockey.HEIGHT*3/5-button_NewGame.getHeight()/2);
            button_Result2.setPosition(Hockey.WITDH/2 - button_Result2.getWidth()/2, Hockey.HEIGHT*4/5- button_Result2.getHeight()/2);


        }else {
            button_Result.setPosition(Hockey.WITDH/2 - button_Result.getWidth()/2, Hockey.HEIGHT/4- button_Result.getHeight()/2);
            button_Again.setPosition(Hockey.WITDH/2-button_Again.getWidth()/2, Hockey.HEIGHT/2-button_Again.getHeight()/2);
            button_NewGame.setPosition(Hockey.WITDH/2-button_NewGame.getWidth()/2, Hockey.HEIGHT*3/4-button_NewGame.getHeight()/2);
        }


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
                    if(kq2.equals("null")){
                        gsm.set(new PlayStateAI(gsm,PlayStateAI.playmode));
                        dispose();
                    }else {
                        gsm.set(new PlayState(gsm));
                        dispose();
                    }

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
        button_Result.setFlip(false,true);
        button_Result.draw(sb);
        button_Again.setFlip(false,true);
        button_Again.draw(sb);
        button_NewGame.setFlip(false,true);
        button_NewGame.draw(sb);
        if(!kq2.equals("null")){
            button_Result2.setFlip(false,true);
            button_Result2.draw(sb);
        }


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