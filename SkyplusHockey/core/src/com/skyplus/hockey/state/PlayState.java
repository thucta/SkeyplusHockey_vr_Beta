package com.skyplus.hockey.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.config.Config;
import com.skyplus.hockey.network.GameClientInterface;
import com.skyplus.hockey.objects.Audio;
import com.skyplus.hockey.objects.BackgroundGame;
import com.skyplus.hockey.objects.CheckSoundMusic;
import com.skyplus.hockey.objects.Effect;
import com.skyplus.hockey.objects.HockeyPreferences;
import com.skyplus.hockey.objects.Pandle;
import com.skyplus.hockey.objects.Puck;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by TruongNN  on 4/24/2017.
 */
public class PlayState extends State implements Screen {


    private BackgroundGame background;


    private Pandle pandle_pink;
    private Pandle pandle_green;
    private Puck puck;

    private ParticleEffect effectGood;
    private ParticleEffect effectPuck;
    private ParticleEffect effectGoal;
    private ParticleEffect effectGoal2;

    private Sprite button_Resume, button_NewGame, button_Exit, button_Pause;
    private boolean GAME_PAUSED = false;
    private Sprite sprite;
    private Audio audio;
    // map diem
    public static Map<Integer, Sprite> mapSpriteScore;

    /*
        xu ly game qua mang
     */
    private GameClientInterface gameClient;
    private JSONObject message;

    public PlayState(GameStateManager gsm, GameClientInterface gameClient) {
        super(gsm);
        this.gameClient = gameClient;
        imitators();
    }

    public PlayState(GameStateManager gsm) {
        super(gsm);
        imitators();
    }

    private void imitators() {
        HashMap<String, Texture> background = new HashMap<String, Texture>();
        background.put(Config.BACKGROUND, new Texture(Hockey.PATCH + "backGame.png"));

        background.put(Config.EDGE_RIGHT_TOP, new Texture(Hockey.PATCH + "bg_right.png"));
        background.put(Config.EDGE_RIGHT_TOP_LIGHT, new Texture(Hockey.PATCH + "bg_right_l.png"));

        background.put(Config.EDGE_RIGHT_BOTTOM, new Texture(Hockey.PATCH + "bg_right.png"));
        background.put(Config.EDGE_RIGHT_BOTTOM_LIGHT, new Texture(Hockey.PATCH + "bg_right_l.png"));

        background.put(Config.EDGE_LEFT_TOP, new Texture(Hockey.PATCH + "bg_left.png"));
        background.put(Config.EDGE_LEFT_TOP_LIGHT, new Texture(Hockey.PATCH + "bg_left_l.png"));

        background.put(Config.EDGE_LEFT_BOTTOM, new Texture(Hockey.PATCH + "bg_left.png"));
        background.put(Config.EDGE_LEFT_BOTTOM_LIGHT, new Texture(Hockey.PATCH + "bg_left_l.png"));


        background.put(Config.EDGE_TOP_RIGHT, new Texture(Hockey.PATCH + "bg_top.png"));
        background.put(Config.EDGE_TOP_RIGHT_LIGHT, new Texture(Hockey.PATCH + "bg_top_l.png"));
        background.put(Config.EDGE_TOP_LEFT, new Texture(Hockey.PATCH + "bg_top.png"));
        background.put(Config.EDGE_TOP_LEFT_LIGHT, new Texture(Hockey.PATCH + "bg_top_l.png"));

        background.put(Config.EDGE_BOTTOM_RIGHT, new Texture(Hockey.PATCH + "bg_bottom.png"));
        background.put(Config.EDGE_BOTTOM_RIGHT_LIGHT, new Texture(Hockey.PATCH + "bg_bottom_l.png"));
        background.put(Config.EDGE_BOTTOM_LEFT, new Texture(Hockey.PATCH + "bg_bottom.png"));
        background.put(Config.EDGE_BOTTOM_LEFT_LIGHT, new Texture(Hockey.PATCH + "bg_bottom_l.png"));

        this.background = new BackgroundGame(Hockey.WITDH, Hockey.HEIGHT, background);
        //pause
        button_Pause = new Sprite(new Texture(Hockey.PATCH + "buttonPause.png"));
        button_Resume = new Sprite(new Texture(Hockey.PATCH + "buttonResume.png"));
        button_NewGame = new Sprite(new Texture(Hockey.PATCH + "buttonNewGame.png"));
        button_Exit = new Sprite(new Texture(Hockey.PATCH + "buttonExit.png"));
        button_Pause.setPosition(Hockey.WITDH - button_Pause.getWidth()-this.background.getMapEdge().get(Config.EDGE_LEFT_BOTTOM).getWitdh()- 5*(Hockey.WITDH/Config.SCREEN_MAIN.x) , Hockey.HEIGHT / 2 - button_Pause.getHeight() / 2);

        button_Resume.setPosition(Hockey.WITDH / 2 - button_Resume.getWidth() / 2, Hockey.HEIGHT/2 - button_Resume.getHeight() / 2);
        button_NewGame.setPosition(Hockey.WITDH / 2 - button_NewGame.getWidth() / 2, Hockey.HEIGHT *2/ 3 - button_NewGame.getHeight() / 2);
        button_Exit.setPosition(Hockey.WITDH / 2 - button_Exit.getWidth() / 2, Hockey.HEIGHT * 5 / 6 - button_Exit.getHeight() / 2);



        sprite = new Sprite(background.get(Config.BACKGROUND));
        sprite.setFlip(false,true);
        //scrore
        mapSpriteScore = new HashMap<Integer, Sprite>();
        for (int i = 0; i < 6; i++) {
            Sprite sprite = new Sprite(new Texture(Hockey.PATCH + i + ".png"));
            mapSpriteScore.put(i, sprite);
        }

        pandle_pink = new Pandle(0, 0, new Texture(Hockey.PATCH + "pandle.png"), new Texture(Hockey.PATCH + "pandle_l.png"));
        pandle_pink.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT - pandle_pink.getHeight());
        pandle_green = new Pandle(0, 0, new Texture(Hockey.PATCH + "pandle_1.png"), new Texture(Hockey.PATCH + "pandle_l_1.png"));
        pandle_green.setPosition(Hockey.WITDH / 2, pandle_green.getHeight());
        puck = new Puck((int) cam.viewportWidth / 2, (int) cam.viewportHeight / 2, this.background.getMapEdge());

        //fx
        Effect fx = new Effect("fxRed");
        effectGood = fx.create();
        Effect fxEdge = new Effect("fxRedEdge");
        effectPuck = fxEdge.create();
        Effect fxGoal = new Effect("goal");
        effectGoal = fxGoal.create();
        effectGoal.setFlip(false,true);


        Effect fxGoal2 = new Effect("goal");
        effectGoal2 = fxGoal2.create();
        effectGoal2.setFlip(true,false);
        //ve hieu ung//
//        rotateBy(-50);
 Hockey.scaleParticleEffect(2.4f,effectGood);

        //audio

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
                if (GAME_PAUSED) {
                    if (button_Resume.getBoundingRectangle().contains(screenX, screenY)) {
                        audio.getClick().play();
                        resume();
                    } else if (button_NewGame.getBoundingRectangle().contains(screenX, screenY)) {
                        audio.getClick().play();
                        gsm.set(new PlayState(gsm));
                        dispose();
                    } else if (button_Exit.getBoundingRectangle().contains(screenX, screenY)) {
                        audio.getClick().play();
                        gsm.set(new MenuState(gsm));
                        dispose();
                    }
                } else {
                //    move(screenX, screenY);
                    if (button_Pause.getBoundingRectangle().contains(screenX, screenY)) {

                        audio.getClick().play();
                        pause();
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

                // ham di chuyen
                move(screenX, screenY);

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


       checkHit(pandle_green, puck, background);
       checkHit(pandle_pink, puck, background);

    }


    @Override
    public void update(float dt) {

        handleInput();
        if (!GAME_PAUSED) {
            pandle_pink.update(dt);
            pandle_green.update(dt);
            puck.update(dt);
            goalScore();  // kiem tra xem co ghi duoc diem khong
            effectGood.update(dt);
            effectPuck.update(dt);
            effectGoal.update(dt);
            effectGoal2.update(dt);
        }

    }


    /*
    * cac Ham trong render
    *
    * */

    @Override
    public void render(SpriteBatch sb) {
        if (!GAME_PAUSED) {
            sb.setProjectionMatrix(cam.combined);
            sb.begin();
            background.draw(sb, pandle_pink, pandle_green, puck);
            puck.draw(sb);
            pandle_pink.draw(sb);
            pandle_green.draw(sb);

            puck.drawEffect(sb);
            effectGood.draw(sb);
            effectPuck.draw(sb);
            effectGoal.draw(sb);
            effectGoal2.draw(sb);
            button_Pause.draw(sb);
            drawScores(sb);

        } else {

            sb.setProjectionMatrix(cam.combined);
            sb.begin();

            sprite.setAlpha(0.9f);

            puck.drawEffect(sb);
            effectGood.draw(sb);
            effectPuck.draw(sb);
            effectGoal.draw(sb);
            effectGoal2.draw(sb);
            puck.draw(sb);
            pandle_pink.draw(sb);
            pandle_green.draw(sb);
            sprite.draw(sb);
            button_Resume.setFlip(false, true);
            button_Resume.draw(sb);
            button_NewGame.setFlip(false, true);
            button_NewGame.draw(sb);
            button_Exit.setFlip(false, true);
            button_Exit.draw(sb);



        }
        sb.end();
    }


    /*
    * Ham kiem tra xem cuoi cung cua update thi puck co va cham voi pandle neu co thi di chuyen puck ra xa
    *
    * */
    public void checkHit(Pandle pandle, Puck puck, BackgroundGame background) {


        pandle.hits(puck);
        puck.hits(pandle);

        double distance = Math.sqrt(Vector2.dst2(puck.getX(), puck.getY(), pandle.getX(), pandle.getY()));

        if (Intersector.overlaps(pandle.getBounds(), puck.getBounds())) {
            Double x = (puck.getX() - pandle.getX()) * (puck.getWitdh() / 2 + pandle.getWitdh() / 2) / distance + pandle.getX();
            Double y = (puck.getY() - pandle.getY()) * (puck.getWitdh() / 2 + pandle.getWitdh() / 2) / distance + pandle.getY();

            effectPuck.setPosition(x.floatValue(), y.floatValue());
            effectPuck.reset();

            puck.setPosition(x.floatValue(), y.floatValue());

        }


    }

    /*
        Giới hạn không cho di chuyển ra khởi màng hình, va di chuyen
    **/
    public void move(float screenX, float screenY) {

        if (screenY < Hockey.HEIGHT / 2) {
            screenX = (int) Math.min(Math.max(screenX, pandle_green.getWitdh() / 2 + background.getMapEdge().get(Config.EDGE_RIGHT_TOP).getWitdh() - 1),
                    Hockey.WITDH - pandle_green.getWitdh() / 2 - (background.getMapEdge().get(Config.EDGE_LEFT_TOP).getWitdh() - 1));
            screenY = (int) Math.min(Math.max(screenY, pandle_green.getHeight() / 2 + background.getMapEdge().get(Config.EDGE_TOP_RIGHT).getHeight() - 1),
                    Hockey.HEIGHT / 2 - pandle_green.getHeight() / 2);
//            double distance = Math.sqrt(Vector2.dst2(puck.getX(), puck.getY(), screenX,screenY))+2;
//
//            if (distance < (puck.getBounds().radius + pandle_green.getBounds().radius) ) {
//                Double x = (screenX - puck.getX()) * (pandle_green.getWitdh() / 2 + puck.getWitdh() / 2) / distance + puck.getX();
//                Double y = (screenY - puck.getY()) * (pandle_green.getWitdh() / 2 + puck.getWitdh() / 2) / distance + puck.getY();
//
//               screenX = x.floatValue();
//               screenY = y.floatValue();
//            }

           pandle_green.move(screenX, screenY);
        } else {
            // gioi han bounds khong cho chay ra khoi mang hinh
            screenX = (int) Math.min(Math.max(screenX, pandle_pink.getWitdh() / 2 + background.getMapEdge().get(Config.EDGE_RIGHT_BOTTOM).getWitdh() - 1),
                    Hockey.WITDH - pandle_pink.getWitdh() / 2 - (background.getMapEdge().get(Config.EDGE_LEFT_BOTTOM).getWitdh() - 1));
            screenY = (int) Math.min(Math.max(screenY, Hockey.HEIGHT / 2 + pandle_pink.getHeight() / 2),
                    Hockey.HEIGHT - pandle_pink.getHeight() / 2 - (background.getMapEdge().get(Config.EDGE_BOTTOM_RIGHT).getHeight() - 1));
//            double distance = Math.sqrt(Vector2.dst2(puck.getX(), puck.getY(), screenX,screenY))+2;
//            if (distance < (puck.getBounds().radius + pandle_green.getBounds().radius) ) {
//
//               Double x = (screenX - puck.getX()) * (pandle_pink.getWitdh() / 2 + puck.getWitdh() / 2) / distance + puck.getX();
//                Double y = (screenY - puck.getY()) * (pandle_pink.getWitdh() / 2 + puck.getWitdh() / 2) / distance + puck.getY();screenX = x.floatValue();
//               screenY = y.floatValue();
//            }
            pandle_pink.move(screenX, screenY);

        }
    }

    /*
           Kiem tra xem score
     */
    public void goalScore() {

        if (puck.getY() + puck.getHeight() / 2 < 0) {
            pandle_pink.setScore();
            if (pandle_pink.getScore() == 5) {
                audio.getWin().play();
                gsm.set(new WinState(gsm, "lose","win"));
                dispose();
            } else {
                reLoad();
                puck.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT / 2 - 100);
                effectGood.setPosition(Hockey.WITDH / 2, 10);
                effectGood.reset();


                effectGoal.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT / 3 * 2);
                effectGoal.reset();

                audio.getGoal().play();
                Hockey.deviceAPI.vibRate(Config.miliSecond);
            }

        } else if (puck.getY() - puck.getHeight() / 2 > Hockey.HEIGHT) {

            pandle_green.setScore();
            if (pandle_green.getScore() == 5) {
                audio.getLose().play();
                gsm.set(new WinState(gsm, "win","lose"));

            } else {
                reLoad();
                puck.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT / 2 + 100);
                effectGood.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT - 10);
                effectGood.reset();
                effectGoal2.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT / 4);
                effectGoal2.reset();
                audio.getGoal().play();
                Hockey.deviceAPI.vibRate(Config.miliSecond);
            }
        }
    }

    private void reLoad() {
        pandle_pink.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT - pandle_pink.getHeight());
        pandle_green.reLoadGame(Hockey.WITDH / 2, pandle_pink.getHeight());
    }

    public void drawScores(SpriteBatch sb) {

        Sprite sprite;

        sprite = mapSpriteScore.get(pandle_green.getScore());
        sprite.setPosition(Hockey.WITDH - 100*(Hockey.WITDH/Config.SCREEN_MAIN.x), Hockey.HEIGHT / 2 - (100*(Hockey.HEIGHT/Config.SCREEN_MAIN.y)));
        sprite.draw(sb);

        sprite = mapSpriteScore.get(pandle_pink.getScore());
        sprite.setPosition(Hockey.WITDH - 100 *(Hockey.WITDH/Config.SCREEN_MAIN.x), Hockey.HEIGHT / 2 + (100*(Hockey.HEIGHT/Config.SCREEN_MAIN.y) - sprite.getHeight()));
        sprite.draw(sb);
    }

    public Pandle getPandle_pink() {
        return pandle_pink;
    }

    public void setPandle_pink(Pandle pandle_pink) {
        this.pandle_pink = pandle_pink;
    }

    public Pandle getPandle_green() {
        return pandle_green;
    }

    public void setPandle_green(Pandle pandle_green) {
        this.pandle_green = pandle_green;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
//       gamePort.update(width,height);
    }

    @Override
    public void pause() {
        GAME_PAUSED = true;
    }

    @Override
    public void resume() {
       this.audio = new Audio();
        GAME_PAUSED = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
