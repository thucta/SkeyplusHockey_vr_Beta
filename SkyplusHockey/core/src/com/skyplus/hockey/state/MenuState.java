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
 * Created by TruongNN on 3/24/2017.
 */
public class MenuState extends State implements Screen{


    private Texture bg;
    private Sprite button_PC,button_PP,button_ST,button_Exit, button_About;

private  Audio audio;



    public MenuState(GameStateManager gsm) {
        super(gsm);
        Gdx.app.log("here","MenuState");
        bg = new Texture(Hockey.PATCH+"backgroundMenu.png");
        button_PC = new Sprite(new Texture(Hockey.PATCH+"buttonPC.png"));
        button_PP = new Sprite(new Texture(Hockey.PATCH+"buttonPP.png"));
        button_ST = new Sprite(new Texture(Hockey.PATCH+"buttonST.png"));
        button_Exit = new Sprite(new Texture(Hockey.PATCH+"buttonExit1.png"));
        button_About = new Sprite(new Texture(Hockey.PATCH+"buttonAbout.png"));
        float width = Hockey.WITDH/2-button_PP.getWidth()/2;

        button_PC.setPosition(width, Hockey.HEIGHT/5-button_PC.getHeight()/2);
        button_PP.setPosition(width, Hockey.HEIGHT/3-button_PP.getHeight()/2 );
        button_ST.setPosition(width, Hockey.HEIGHT/2-button_ST.getHeight()/2);
        button_About.setPosition(width, Hockey.HEIGHT*2/3-button_ST.getHeight()/2);
        button_Exit.setPosition(width, Hockey.HEIGHT*5/6-button_ST.getHeight()/2);

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
                if (button_PP.getBoundingRectangle().contains(screenX,screenY)) {
                    audio.getClick().play();
                    gsm.set(new PlayPPState(gsm));
                    dispose();
                }
                else if (button_PC.getBoundingRectangle().contains(screenX,screenY)) {
                    audio.getClick().play();
                    gsm.set(new ModeState(gsm));
                    dispose();
                }
                else if (button_ST.getBoundingRectangle().contains(screenX,screenY)) {
                    audio.getClick().play();
                    gsm.set(new SettingState(gsm));
                    dispose();
                }else if(button_About.getBoundingRectangle().contains(screenX,screenY)){
                    audio.getClick().play();

                }
                else if(button_Exit.getBoundingRectangle().contains(screenX,screenY)){
                    audio.getClick().play();
//                    gsm.set(new MenuState(gsm));
                    System.exit(0);
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
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg,0,0, Hockey.WITDH, Hockey.HEIGHT);
        button_PP.setFlip(false,true);
        button_PP.draw(sb);
        button_ST.setFlip(false,true);
        button_ST.draw(sb);
        button_PC.setFlip(false,true);
        button_PC.draw(sb);
        button_Exit.setFlip(false,true);
        button_Exit.draw(sb);
        button_About.setFlip(false,true);
        button_About.draw(sb);

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