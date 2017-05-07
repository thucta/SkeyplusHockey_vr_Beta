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
public class ModeState extends State implements Screen{


    private Texture bg;
    private Sprite checkMode, button_Mode1, button_Mode2, button_Mode3, button_Mode4, button_OK;

    private float width,height;
    private int check;



    public ModeState(GameStateManager gsm) {
        super(gsm);
        bg = new Texture(Hockey.PATCH+"backGame.png");
        checkMode = new Sprite(new Texture(Hockey.PATCH+"checkMode.png"));
        button_Mode1 = new Sprite(new Texture(Hockey.PATCH+"mode1.png"));
        button_Mode2 = new Sprite(new Texture(Hockey.PATCH+"mode2.png"));
        button_Mode3 = new Sprite(new Texture(Hockey.PATCH+"mode3.png"));
        button_Mode4 = new Sprite(new Texture(Hockey.PATCH+"mode4.png"));
        button_OK = new Sprite(new Texture(Hockey.PATCH+"buttonOK.png"));


        height = 0;
        width = Hockey.WITDH/3-button_Mode1.getWidth()/2;

        button_Mode1.setPosition(width,Hockey.HEIGHT/6-button_Mode1.getHeight()/2 );
        button_Mode2.setPosition(width,Hockey.HEIGHT/3-button_Mode2.getHeight()/2);
        button_Mode3.setPosition(width,Hockey.HEIGHT/2-button_Mode3.getHeight()/2);
        button_Mode4.setPosition(width,Hockey.HEIGHT*2/3-button_Mode4.getHeight()/2);
        button_OK.setPosition(Hockey.WITDH/2-button_OK.getWidth()/2,Hockey.HEIGHT*5/6-button_OK.getHeight()/2);





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
                if (button_Mode1.getBoundingRectangle().contains(screenX,screenY)) {
                    height = Hockey.HEIGHT/6-button_Mode1.getHeight()/2;
                    check = 1;
                }
                else if (button_Mode2.getBoundingRectangle().contains(screenX,screenY)) {
                    height = Hockey.HEIGHT/3- button_Mode2.getHeight()/2;
                    check = 2;
                }
                else if (button_Mode3.getBoundingRectangle().contains(screenX,screenY)) {
                    height = Hockey.HEIGHT/2-button_Mode3.getHeight()/2;
                    check = 3;
                }
                else if (button_Mode4.getBoundingRectangle().contains(screenX,screenY)){
                    height = Hockey.HEIGHT*2/3-button_Mode4.getHeight()/2;
                    check = 4;
                }
                checkMode.setPosition(Hockey.WITDH*4/5,height);
                if(button_OK.getBoundingRectangle().contains(screenX,screenY)){
                    switch (check){
                        case 1:
                            gsm.set(new PlayStateAI(gsm,1));
                            dispose();
                            break;
                        case 2:
                            gsm.set(new PlayStateAI(gsm,2));
                            dispose();
                            break;
                        case 3:
                            gsm.set(new PlayStateAI(gsm,3));
                            dispose();
                            break;
                        case 4:
                            gsm.set(new PlayStateAI(gsm,4));
                            dispose();
                            break;
                        default:
                            break;
                    }
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
        sb.draw(bg,0,0, Hockey.WITDH, Hockey.HEIGHT);
        button_OK.draw(sb);
        button_Mode1.draw(sb);
        button_Mode2.draw(sb);
        button_Mode3.draw(sb);
        button_Mode4.draw(sb);

        if(height!=0){
            checkMode.draw(sb);
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