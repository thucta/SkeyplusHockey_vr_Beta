package com.skyplus.hockey.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.config.Config;
import com.skyplus.hockey.config.Messsage;
import com.skyplus.hockey.network.GameClientInterface;
import com.skyplus.hockey.network.GameListener;
import com.skyplus.hockey.objects.BackgroundGame;
import com.skyplus.hockey.objects.Pandle;
import com.skyplus.hockey.objects.Puck;
import com.skyplus.hockey.objects.Room;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by TruongNN  on 4/24/2017.
 */


public class PlayStateNetWork extends State implements GameListener {


    private BackgroundGame background;
    private static int WIDTH_EDGE;
    private Pandle pandle_pink;
    private Pandle pandle_green;
    private Puck puck;

    private double WIDTH;
    private double HEIGHT;


    // map diem
    public static Map<Integer, Sprite> mapSpriteScore;

    /*
        xu ly game qua mang
     */
    private GameClientInterface gameClient;
    private JSONObject message;

    public PlayStateNetWork(GameStateManager gsm, GameClientInterface gameClient) {
        super(gsm);
        this.gameClient = gameClient;
        this.gameClient.setListener(this);
        initators();
    }


    private void initators() {
        // tao background
        createBackground();
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
        pandle_green.setPosition(Hockey.WITDH / 2, pandle_green.getHeight());
        puck = new Puck((int) cam.viewportWidth / 2, (int) cam.viewportHeight / 2, this.background.getMapEdge());

        WIDTH = (Config.SCREEN_MAIN.x / Hockey.WITDH);
        HEIGHT = (Config.SCREEN_MAIN.y / Hockey.HEIGHT);

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


        // neu pandle va cham voi puck thi pandle se sang len
        if (!gameClient.isServer()) {
            checkHitClient(pandle_green, puck);
            checkHitClient(pandle_pink, puck);
            puck.histEdge();
        } else {
            message = new JSONObject();

            message.put(Messsage.OP_MESSAGE, Messsage.MOVE_PUCK);
            message.put(Messsage.POINT_X, puck.getX() * WIDTH);
            message.put(Messsage.POINT_Y, puck.getY() * HEIGHT);
            gameClient.sendMessageUDP(message.toString());


            // neu nguoi choi la server thi se kiem tra xem puck co va cham voi pandle khong
            checkHitServer(pandle_green, puck);
            checkHitServer(pandle_pink, puck);

        }


    }

    private void exitGame() {
        Hockey.deviceAPI.showAlertDialogExitGame(this, "Exit game?");
    }


    @Override
    public void update(float dt) {

        handleInput();
        pandle_pink.update(dt);
        pandle_green.update(dt);
        if (gameClient.isServer()) {
            puck.update(dt);
        }
        goalScore();  // kiem tra xem co ghi duoc diem khong


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
        drawScores(sb);
        sb.end();
    }


    /*
    * Ham kiem tra xem cuoi cung cua update thi puck co va cham voi pandle neu co thi di chuyen puck ra xa
    *
    * */
    public void checkHitServer(Pandle pandle, Puck puck) {

        pandle.hits(puck);
        puck.hits(pandle);
        double distance = Math.sqrt(Vector2.dst2(puck.getX(), puck.getY(), pandle.getX(), pandle.getY()));
        if (Intersector.overlaps(pandle.getBounds(), puck.getBounds())) {
            Double x = (puck.getX() - pandle.getX()) * (puck.getWitdh() / 2 + pandle.getWitdh() / 2) / distance + pandle.getX();
            Double y = (puck.getY() - pandle.getY()) * (puck.getWitdh() / 2 + pandle.getWitdh() / 2) / distance + pandle.getY();
            puck.setPosition(x.floatValue(), y.floatValue());

        }


    }

    public void checkHitClient(Pandle pandle, Puck puck) {
        pandle.hits(puck);
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

        screenX = (int) Math.min(Math.max(screenX, pandle_pink.getWitdh() / 2 + WIDTH_EDGE - 1), Hockey.WITDH - pandle_pink.getWitdh() / 2 - WIDTH_EDGE - 1);
        screenY = (int) Math.min(Math.max(screenY, Hockey.HEIGHT / 2 + pandle_pink.getHeight() / 2), Hockey.HEIGHT - pandle_pink.getHeight() / 2 - WIDTH_EDGE - 1);

        message = new JSONObject();
        message.put(Messsage.OP_MESSAGE, Messsage.MOVE_PANDLE);
        message.put(Messsage.POINT_X, screenX * (Config.SCREEN_MAIN.x / Hockey.WITDH)); // dua ve chuan man hinh mac dinh la 480x800
        message.put(Messsage.POINT_Y, screenY * (Config.SCREEN_MAIN.y / Hockey.HEIGHT));
        gameClient.sendMessageUDP(message.toString());

        pandle_pink.move(screenX, screenY);
    }


    public void createBackground() {
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
        this.WIDTH_EDGE = background.get(Config.EDGE_RIGHT_TOP).getWidth();

    }


    /*
           Kiem tra xem score
     */
    public void goalScore() {
        // chi co server moi cap nhat diem
        if (!gameClient.isServer()) return;

        if (puck.getY() + puck.getHeight() / 2 < 0) {
            pandle_pink.setScore();

            message = new JSONObject();
            message.put(Messsage.OP_MESSAGE, Messsage.UPDATE_SCORE);
            message.put(Messsage.SCORE, Messsage.SCORE_PINK);
            gameClient.sendMessageUDP(message.toString());

            reLoad(1);


        } else if (puck.getY() + puck.getHeight() / 2 > Hockey.HEIGHT) {
            pandle_green.setScore();
            message = new JSONObject();
            message.put(Messsage.OP_MESSAGE, Messsage.UPDATE_SCORE);
            message.put(Messsage.SCORE, Messsage.SCORE_GREEN);
            gameClient.sendMessageUDP(message.toString());

            reLoad(2);

        }
    }

    private void reLoad(int flag) {
        pandle_pink.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT - pandle_pink.getHeight());
        pandle_green.reLoadGame(Hockey.WITDH / 2, pandle_pink.getHeight());
        // green ghi diem
        if (1 == flag) {
            puck.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT / 2 - pandle_pink.getHeight());
        } else { // pink ghi diem
            puck.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT / 2 + pandle_pink.getHeight());
        }
        Hockey.deviceAPI.vibRate(Config.miliSecond);

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
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        message = new JSONObject();
        message.put(Messsage.OP_MESSAGE, Messsage.PAUSE);
        gameClient.sendMessageTCP(message.toString());
    }

    @Override
    public void resume() {
        message = new JSONObject();
        message.put(Messsage.OP_MESSAGE, Messsage.RESUME);
        gameClient.sendMessageTCP(message.toString());
    }


    @Override
    public void dispose() {

    }

    /*
        Cac method lop mang
     */
    @Override
    public void createServer(String nameRoom) {

    }

    @Override
    public void connectServer(Room room) {

    }

    @Override
    public void backProgesDialog() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gsm.set(new JoinGameState(gsm));
            }
        }, 0.2f);
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {
        Hockey.deviceAPI.showAlertDialogDisconnected(this, "Player disconnected...");
        try {
            Hockey.deviceAPI.closeProgressDialog();
        } catch (Exception e) {

        }
    }

    @Override
    public void onConnectionFailed() {
        Gdx.app.error("dsad", "onConnectionFailed");
        try {
            gameClient.disconnect();
        } catch (Exception e) {

        }
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gsm.set(new MenuState(gsm));
                dispose();
            }
        }, 0.2f);
    }



    @Override
    public void onMessageReceived(String message) {
        try {
            JSONObject object = new JSONObject(message);

            String op = object.getString(Messsage.OP_MESSAGE);

            // pandle di chuyen
            if (Messsage.MOVE_PANDLE.equals(op)) {
                float x = (float) object.getDouble(Messsage.POINT_X);
                float y = (float) object.getDouble(Messsage.POINT_Y);
                x *= 1 / WIDTH;
                y *= 1 / HEIGHT;
                y = 2 * Hockey.HEIGHT / 2 - y;
//                Hockey.HEIGHT / 2 - (y - Hockey.HEIGHT / 2)
                pandle_green.move(x, y);
                // puck di chuyen
            } else if (Messsage.MOVE_PUCK.equals(op)) {
                float puckX = (float) object.getDouble(Messsage.POINT_X);
                float puckY = (float) object.getDouble(Messsage.POINT_Y);

                // chuyen doi ti le cac mang hinh
                puckX *= 1 / WIDTH;
                puckY *= 1 / HEIGHT;

                puckY = 2 * Hockey.HEIGHT / 2 - puckY; // doi xung y qua diem giua  xh = (xM + xN)/2 yh = (yM + yN)/2
                puck.update(puckX, puckY);

                // update diem
            } else if (Messsage.UPDATE_SCORE.equals(op)) {
                String score = object.getString(Messsage.SCORE);
                if (Messsage.SCORE_GREEN.equals(score)) {
                    pandle_pink.setScore();
                    reLoad(1);
                } else {
                    pandle_green.setScore();
                    reLoad(2);
                }

            }
        } catch (Exception e) {
            Gdx.app.error("message error", e + "");
        }
    }


}
