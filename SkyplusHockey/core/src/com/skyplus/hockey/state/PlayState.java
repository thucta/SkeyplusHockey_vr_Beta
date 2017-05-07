package com.skyplus.hockey.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.config.Config;
import com.skyplus.hockey.network.GameClientInterface;
import com.skyplus.hockey.objects.BackgroundGame;
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

    private ParticleEffect effect;
    Array<ParticleEmitter> emitters;

    // map diem
    public static Map<Integer, Sprite> mapSpriteScore;

    /*
        xu ly game qua mang
     */
    private GameClientInterface gameClient;
    private JSONObject message;

    public PlayState(GameStateManager gsm, GameClientInterface gameClient){
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

        background.put(Config.EDGE_LEFT_TOP, new Texture(Hockey.PATCH + "bg_right.png"));
        background.put(Config.EDGE_LEFT_TOP_LIGHT, new Texture(Hockey.PATCH + "bg_right_l.png"));

        background.put(Config.EDGE_LEFT_BOTTOM, new Texture(Hockey.PATCH + "bg_right.png"));
        background.put(Config.EDGE_LEFT_BOTTOM_LIGHT, new Texture(Hockey.PATCH + "bg_right_l.png"));


        background.put(Config.EDGE_TOP_RIGHT, new Texture(Hockey.PATCH + "bg_top.png"));
        background.put(Config.EDGE_TOP_RIGHT_LIGHT, new Texture(Hockey.PATCH + "bg_top_light.png"));
        background.put(Config.EDGE_TOP_LEFT, new Texture(Hockey.PATCH + "bg_top.png"));
        background.put(Config.EDGE_TOP_LEFT_LIGHT, new Texture(Hockey.PATCH + "bg_top_light.png"));

        background.put(Config.EDGE_BOTTOM_RIGHT, new Texture(Hockey.PATCH + "bg_top.png"));
        background.put(Config.EDGE_BOTTOM_RIGHT_LIGHT, new Texture(Hockey.PATCH + "bg_top_light.png"));
        background.put(Config.EDGE_BOTTOM_LEFT, new Texture(Hockey.PATCH + "bg_top.png"));
        background.put(Config.EDGE_BOTTOM_LEFT_LIGHT, new Texture(Hockey.PATCH + "bg_top_light.png"));

        this.background = new BackgroundGame(Hockey.WITDH, Hockey.HEIGHT, background);

        //scrore
        mapSpriteScore = new HashMap<Integer, Sprite>();
        for (int i = 0; i < 10; i++) {
            Sprite sprite = new Sprite(new Texture(Hockey.PATCH + i + ".png"));
            sprite.setSize(sprite.getWidth(), sprite.getHeight());
            mapSpriteScore.put(i, sprite);
        }

        pandle_pink = new Pandle(0, 0, new Texture(Hockey.PATCH + "pandle.png"), new Texture(Hockey.PATCH + "pandle_l.png"));
        pandle_pink.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT - pandle_pink.getHeight());
        pandle_green = new Pandle(0, 0, new Texture(Hockey.PATCH + "pandle_1.png"), new Texture(Hockey.PATCH + "pandle_l_1.png"));
        pandle_green.setPosition(Hockey.WITDH / 2, pandle_green.getHeight() );
        puck = new Puck((int) cam.viewportWidth / 2, (int) cam.viewportHeight / 2, this.background.getMapEdge());

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("ex.p"), Gdx.files.internal(""));
        emitters = effect.getEmitters();
        effect.setPosition(-100, -100);
        effect.start();
//        rotateBy(-50);

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

                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {

                // ham di chuyen
                move(screenX,screenY);

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
        pandle_pink.update(dt);
        pandle_green.update(dt);
        puck.update(dt);
        goalScore();  // kiem tra xem co ghi duoc diem khong
        effect.update(dt);

    }


    /*
    * cac Ham trong render
    *
    * */

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        background.draw(sb, pandle_pink, pandle_green, puck);
        puck.draw(sb);
        pandle_pink.draw(sb);
        pandle_green.draw(sb);
        effect.draw(sb);
        drawScores(sb);
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
            pandle_green.move(screenX, screenY);

        } else {
            // gioi han bounds khong cho chay ra khoi mang hinh
            screenX = (int) Math.min(Math.max(screenX, pandle_pink.getWitdh() / 2 + background.getMapEdge().get(Config.EDGE_RIGHT_BOTTOM).getWitdh() - 1),
                    Hockey.WITDH - pandle_pink.getWitdh() / 2 - (background.getMapEdge().get(Config.EDGE_LEFT_BOTTOM).getWitdh() - 1));

            screenY = (int) Math.min(Math.max(screenY, Hockey.HEIGHT / 2 + pandle_pink.getHeight() / 2),
                    Hockey.HEIGHT - pandle_pink.getHeight() / 2 - (background.getMapEdge().get(Config.EDGE_BOTTOM_RIGHT).getHeight() - 1));



            pandle_pink.move(screenX, screenY);
        }
    }

    public void rotateBy(float amountInDegrees) {
        Array<ParticleEmitter> emitters = effect.getEmitters();
        for (int i = 0; i < emitters.size; i++) {
            ParticleEmitter.ScaledNumericValue val = emitters.get(i).getAngle();
            float amplitude = (val.getHighMax() - val.getHighMin()) / 2f;
            float h1 = amountInDegrees + amplitude;
            float h2 = amountInDegrees - amplitude;
            val.setHigh(h1, h2);
            val.setLow(amountInDegrees);
        }
    }


    /*
           Kiem tra xem score
     */
    public void goalScore() {

        if (puck.getY() + puck.getHeight() / 2 < 0) {
            pandle_pink.setScore();

            reLoad();
            puck.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT / 2 - 100);
            effect.setPosition(Hockey.WITDH / 2, 10);

            effect.reset();

        } else if (puck.getY() - puck.getHeight() / 2 > Hockey.HEIGHT) {

            pandle_green.setScore();
            reLoad();
            puck.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT / 2 + 100);
            effect.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT - 10);
            effect.reset();

        }
    }

    private void reLoad() {
        pandle_pink.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT - pandle_pink.getHeight());
        pandle_green.reLoadGame(Hockey.WITDH / 2, pandle_pink.getHeight());
    }

    public void drawScores(SpriteBatch sb) {

        Sprite sprite;

        sprite = mapSpriteScore.get(pandle_green.getScore());
        sprite.setPosition(Hockey.WITDH - 100, Hockey.HEIGHT / 2 - 100);
        sprite.draw(sb);

        sprite = mapSpriteScore.get(pandle_pink.getScore());
        sprite.setPosition(Hockey.WITDH - 100, Hockey.HEIGHT / 2 + (100 - sprite.getHeight()));
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

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
