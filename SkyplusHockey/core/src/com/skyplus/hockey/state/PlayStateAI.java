package com.skyplus.hockey.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.config.Config;
import com.skyplus.hockey.network.GameClientInterface;
import com.skyplus.hockey.objects.AIPandle;
import com.skyplus.hockey.objects.Audio;
import com.skyplus.hockey.objects.BackgroundGame;
import com.skyplus.hockey.objects.CheckSoundMusic;
import com.skyplus.hockey.objects.Effect;
import com.skyplus.hockey.objects.HockeyPreferences;
import com.skyplus.hockey.objects.Pandle;
import com.skyplus.hockey.objects.Puck;
import com.skyplus.hockey.objects.Vector2d;

/*
import org.json.JSONObject;
*/

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sun.rmi.runtime.Log;


/**
 * Created by TruongNN  on 4/24/2017.
 */


public class PlayStateAI extends State implements Screen {


    private BackgroundGame background;



    private Pandle pandle_pink;
    private Pandle pandle_green;
    private AIPandle pandle_greenAI;
    private int bound = 0;
    private Puck puck;
    public static boolean movedball =false;
    public static int playmode;
    private Vector2d centerGoal;
    private boolean center=false;

    private ParticleEffect effectGood;
    private ParticleEffect effectPuck;
    private ParticleEffect effectGoal;

    private Sprite button_Resume,button_NewGame, button_Exit, button_Pause;
    private Sprite checksound, unchecksound, checkMusic,uncheckMusic, textSound, textMusic;
    private boolean GAME_PAUSED = false;
    private Sprite sprite;
    private Audio audio;


    // map diem
    public static Map<Integer, Sprite> mapSpriteScore;

    //win
    private Sprite button_Again, button_Result;
    private boolean flagWin = false;


    /*
        xu ly game qua mang
     */
    private GameClientInterface gameClient;

    public PlayStateAI(GameStateManager gsm, GameClientInterface gameClient){
        super(gsm);
        this.gameClient = gameClient;
        imitators();
    }

    public PlayStateAI(GameStateManager gsm,int playmode) {
        super(gsm);
        this.playmode = playmode;
        imitators();
        centerGoal = new Vector2d(Hockey.WITDH/2,Hockey.HEIGHT);

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
        button_Pause.setPosition(Hockey.WITDH - button_Pause.getWidth(), Hockey.HEIGHT / 2 - button_Pause.getHeight() / 2);

        button_Resume.setPosition(Hockey.WITDH / 2 - button_Resume.getWidth() / 2, Hockey.HEIGHT/4 - button_Resume.getHeight() / 2);
        button_NewGame.setPosition(Hockey.WITDH / 2 - button_NewGame.getWidth() / 2, Hockey.HEIGHT / 2 - button_NewGame.getHeight() / 2);
        button_Exit.setPosition(Hockey.WITDH / 2 - button_Exit.getWidth() / 2, Hockey.HEIGHT * 3 / 4 - button_Exit.getHeight() / 2);
        sprite = new Sprite(background.get(Config.BACKGROUND));

        //win
        button_Again = new Sprite(new Texture(Hockey.PATCH+"buttonAgain.png"));
        button_Again.setPosition(Hockey.WITDH/2-button_Again.getWidth()/2, Hockey.HEIGHT/2-button_Again.getHeight()/2);


        //scrore
        mapSpriteScore = new HashMap<Integer, Sprite>();
        for (int i = 0; i < 6; i++) {
            Sprite sprite = new Sprite(new Texture(Hockey.PATCH + i + ".png"));
            sprite.setSize(sprite.getWidth(), sprite.getHeight());
            mapSpriteScore.put(i, sprite);
        }

        pandle_pink = new Pandle(0, 0, new Texture(Hockey.PATCH + "pandle.png"), new Texture(Hockey.PATCH + "pandle_l.png"));
        pandle_pink.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT - pandle_pink.getHeight());
                pandle_green= new Pandle();
                pandle_greenAI = new AIPandle(0, 0, new Texture(Hockey.PATCH + "pandle_1.png"), new Texture(Hockey.PATCH + "pandle_l_1.png"),this.background);
                pandle_greenAI.setPosition(Hockey.WITDH / 2, pandle_greenAI.getHeight() );
                World world = createBackground(new Vector2(0,0),Hockey.HEIGHT,Hockey.WITDH);
                pandle_greenAI.setWorld(world);
                pandle_greenAI.setBodyworld(Hockey.HEIGHT,Hockey.WITDH);
                pandle_greenAI.setBackground(this.background);

        pandle_green=pandle_greenAI;

        puck = new Puck((int) cam.viewportWidth / 2, (int) cam.viewportHeight / 2, this.background.getMapEdge());

        //fx
        Effect fx = new Effect("fxRed");
        effectGood = fx.create();

        Effect fxEdge = new Effect("fxRedEdge");

        effectPuck = fxEdge.create();
        Effect fxGoal = new Effect("goal");
        effectGoal = fxGoal.create();
        effectGoal.setFlip(false,true);
        // co lien quan gi de bi null ko?koko ko
//        rotateBy(-50);
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

                if(GAME_PAUSED){
                    if(button_Resume.getBoundingRectangle().contains(screenX,screenY)){
                        resume();
                    }else if(button_NewGame.getBoundingRectangle().contains(screenX,screenY)){
                        gsm.set(new PlayStateAI(gsm,playmode));
                        dispose();
                    }else if(button_Exit.getBoundingRectangle().contains(screenX,screenY)){
                        gsm.set(new MenuState(gsm));
                        dispose();
                    }
                }else {
                    if(button_Pause.getBoundingRectangle().contains(screenX,screenY)){
                        pause();
                    }
                }
                if(flagWin){
                    if (button_Again.getBoundingRectangle().contains(screenX,screenY)) {
                        gsm.set(new PlayStateAI(gsm,PlayStateAI.playmode));
                        dispose();

                    }
                    else if (button_NewGame.getBoundingRectangle().contains(screenX,screenY)) {
                        gsm.set(new MenuState(gsm));
                        dispose();
                    }
                }

                if(screenY>Hockey.HEIGHT/2) {
                    move(screenX, screenY);
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
                if(screenY>Hockey.HEIGHT/2) {
                    move(screenX, screenY);
                }

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


        moveAI();

        checkHit(pandle_green,puck);
        checkHit(pandle_pink,puck);

    }




    @Override
    public void update(float dt) {
        handleInput();
        if(!GAME_PAUSED) {
            handleInput();
            pandle_pink.update(dt);
            pandle_green.update(dt);
            puck.update(dt);
            goalScore();  // kiem tra xem co ghi duoc diem khong
            effectGood.update(dt);
            effectPuck.update(dt);
            effectGoal.update(dt);
        }
    }


    /*
    * cac Ham trong render
    *
    * */

    @Override
    public void render(SpriteBatch sb) {
        if(!GAME_PAUSED){
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
            button_Pause.draw(sb);
        drawScores(sb);
            sb.end();
    }else {
            Gdx.gl.glClearColor(0,0,0,0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            sb.setProjectionMatrix(cam.combined);
            sb.begin();

            sprite.setAlpha(0.6f);

            puck.drawEffect(sb);
            effectGood.draw(sb);
            effectPuck.draw(sb);
            effectGoal.draw(sb);

            puck.draw(sb);
            pandle_pink.draw(sb);
            pandle_green.draw(sb);
            sprite.draw(sb);
            button_Resume.setFlip(false,true);
            button_Resume.draw(sb);
            button_NewGame.setFlip(false,true);
            button_NewGame.draw(sb);
            button_Exit.setFlip(false,true);
            button_Exit.draw(sb);
            sb.end();


        }
        if(flagWin){
            Gdx.gl.glClearColor(0, 0, 0, 0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            sb.setProjectionMatrix(cam.combined);
            sb.begin();
            sprite.setAlpha(0.6f);
            puck.drawEffect(sb);
            effectGood.draw(sb);
            effectPuck.draw(sb);
            effectGoal.draw(sb);

            puck.draw(sb);
            pandle_pink.draw(sb);
            pandle_green.draw(sb);
            sprite.draw(sb);

            button_Result.setFlip(false,true);
            button_Result.draw(sb);
            button_Again.setFlip(false,true);
            button_Again.draw(sb);
            button_NewGame.setFlip(false,true);
            button_NewGame.draw(sb);

            sb.end();
        }

    }






    public void moveAI()
    {
       /* AIPandle.setElapsed(0.1f);

//        Intersector.overlaps(puck.getBounds(),this.background.getMapEdge().get(Config.EDGE_RIGHT_TOP_LIGHT ).getBound());
        if(center)
        {
            checkmoveforAIpandle(Hockey.WITDH / 2, pandle_greenAI.getHeight());
           // pandle_greenAI.setElapsed(0.2f);
          //  pandle_green = pandle_greenAI;
            if((pandle_green.getX() == Hockey.WITDH/2)&&(pandle_green.getY()==pandle_green.getHeight()))
            {
             //   pandle_greenAI.setElapsed(0.15f);
               // pandle_green = pandle_greenAI;
                center = false;
            }
        }*/
        if(!movedball) {
            if(puck.getY()>Hockey.HEIGHT/2) {
               checkmoveforAIpandle(pandle_pink.getX()+pandle_pink.getWitdh()/2, Hockey.HEIGHT / 9);
            }
            else
            {
                checkmoveforAIpandle(pandle_pink.getX()+pandle_pink.getWitdh()/2,pandle_pink.getY()+pandle_pink.getHeight()/2);
                movedball = true;
            }
        }
        else {

            if(bound<=50)
            {
                switch (playmode) {
                    case 1: {
                        Random rn = new Random();
                        int range = (int) puck.getWitdh() / 2 + 1;
                        int randomNum = rn.nextInt(range);
                        if (puck.getY() > Hockey.HEIGHT / 2 +puck.getWitdh()/2) {
                            /*pandle_greenAI.setElapsed(AIPandle.elapsedhome);
                            pandle_green = pandle_greenAI;*/
                            if (puck.getY() < 100) {
                                checkmoveforAIpandle(puck.getX(), Hockey.HEIGHT / 9);
                            } else {
                                if (puck.getX() > Hockey.WITDH - 100) {
                                    checkmoveforAIpandle(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                } else {
                                    checkmoveforAIpandle(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                }
                            }
                        } else {
                            pandle_greenAI.setElapsed(AIPandle.elapsed);
                            pandle_green = pandle_greenAI;
                            checkmoveforAIpandle(puck.getX(), puck.getY() + randomNum);
                        }
                        if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
/*
                            AIPandle.setElapsed(0.2f);
*/

                            checkmoveforAIpandle(Hockey.WITDH / 2, pandle_greenAI.getHeight());

                        }
                        break;
                    }
                    case 2: {
                        if (puck.getY() > Hockey.HEIGHT / 2 + puck.getWitdh()/2) {
                          /*  pandle_greenAI.setElapsed(AIPandle.elapsedhome);
                            pandle_green = pandle_greenAI;*/
                            if (puck.getX() < 100) {
                                checkmoveforAIpandle(puck.getX() + 150, Hockey.HEIGHT / 9);
                            } else {
                                if (puck.getX() > Hockey.WITDH - 100) {
                                    checkmoveforAIpandle(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                } else {
                                    checkmoveforAIpandle(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                }
                            }
                        } else {
                            pandle_greenAI.setElapsed(AIPandle.elapsed);
                            pandle_green = pandle_greenAI;
                            pandle_green.move(puck.getX() + puck.getWitdh() / 2, puck.getY() + puck.getWitdh() / 2);
                            if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                                float a = (float) (Math.pow(puck.getX() - centerGoal.x, 2) / Math.pow(puck.getY() - centerGoal.y, 2) + 1);
                                float b = (float) (2 * (puck.getX() - centerGoal.x) / Math.pow(puck.getY() - centerGoal.y, 2) + 2 * puck.getX() * (puck.getX() - centerGoal.x) / (puck.getY() - centerGoal.y) - 2 * puck.getY());
                                float c = (float) (Math.pow(puck.getY() * centerGoal.x - puck.getX() * centerGoal.y, 2) / Math.pow(puck.getY() - centerGoal.y, 2) + Math.pow(puck.getX(), 2) + 2 * puck.getX() * (puck.getY() * centerGoal.x - puck.getY() * centerGoal.y) / (puck.getY() - centerGoal.y) + Math.pow(puck.getY(), 2) - Math.pow(puck.getWitdh() / 2 + pandle_green.getWitdh() / 2, 2));
                                float x = findx(a, b, c);
                                float y = (float) ((puck.getX() - centerGoal.x) * x - puck.getY() * centerGoal.x - puck.getX() * centerGoal.y) / (puck.getY() - centerGoal.x);
                                checkmoveforAIpandle(x, y);
                            }


                        }
                        if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
/*
                            AIPandle.setElapsed(0.2f);
*/
                            checkmoveforAIpandle(Hockey.WITDH / 2, pandle_greenAI.getHeight());


                        }
                        break;
                    }
                    case 3: {
                        if (puck.getY() > Hockey.HEIGHT / 2 + pandle_green.getWitdh()/2) {
                         /*   pandle_greenAI.setElapsed(AIPandle.elapsedhome);
                            pandle_green = pandle_greenAI;*/
                            if (puck.getX() < 100) {
                                checkmoveforAIpandle(puck.getX() + 150, Hockey.HEIGHT / 9);
                            } else {
                                if (puck.getX() > Hockey.WITDH - 100) {
                                    checkmoveforAIpandle(puck.getX() + puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                } else {
                                    checkmoveforAIpandle(puck.getX() + puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                }
                            }
                        } else {
                            pandle_greenAI.setElapsed(AIPandle.elapsed);
                            pandle_green = pandle_greenAI;
                            double tempx = Math.abs(((pandle_green.getY() - puck.getY()) * pandle_pink.getX() + (pandle_green.getX() - puck.getX()) * pandle_pink.getX()));
                            double tempy = Math.sqrt(Math.pow((pandle_green.getY() - puck.getY()), 2) + Math.pow(pandle_green.getX() - puck.getX(), 2));
                            if ((tempx / tempy) >= (pandle_pink.getWitdh() / 2 + puck.getWitdh() / 2)) {
                                checkmoveforAIpandle(puck.getX(), puck.getY());
                                if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                                    double x = Math.random();
                                    Vector2d temp = new Vector2d((float) (puck.getX() * Math.cos(x) - puck.getY() * Math.sin(x)), (float) (puck.getX() * Math.sin(x) + puck.getY() * Math.cos(x)));
                                    checkmoveforAIpandle(temp.x, temp.y);
                                }
                            } else {
                                checkmoveforAIpandle(puck.getX(), puck.getY());
                            }


                        }
                        if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
//                            AIPandle.setElapsed(0.2f);
                            checkmoveforAIpandle(Hockey.WITDH / 2, pandle_greenAI.getHeight());


                        }
                        break;
                    }
                    case 4: {
                        pandle_greenAI.setElapsed(0.15f);
                        if (puck.getY() > Hockey.HEIGHT / 2 + puck.getWitdh()) {
                            if (puck.getY() < 100) {
                                checkmoveforAIpandle(puck.getX(), Hockey.HEIGHT / 9);
                            } else {
                                if (puck.getX() > Hockey.WITDH - 100) {
                                    checkmoveforAIpandle(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                } else {
                                    checkmoveforAIpandle(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                }
                            }
                        }
                        else {

                       /* Random rn = new Random();
                        int range = (int) puck.getWitdh() / 2 + 1;
                        int randomNum = rn.nextInt(range);
                        if (puck.getY() > Hockey.HEIGHT / 2 + pandle_green.getWitdh()/2) {
                            pandle_greenAI.setElapsed(AIPandle.elapsedhome);
                            pandle_green = pandle_greenAI;
                            if (puck.getY() < 100) {
                                checkmoveforAIpandle(puck.getX(), Hockey.HEIGHT / 9);
                            } else {
                                if (puck.getX() > Hockey.WITDH - 100) {
                                    checkmoveforAIpandle(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                } else {
                                    checkmoveforAIpandle(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                }
                            }
                        } else {
                            pandle_greenAI.setElapsed(AIPandle.elapsed);
                            pandle_green = pandle_greenAI;
                            pandle_green.move(puck.getX(), puck.getY() + randomNum);
                        }*/
                            pandle_green.move(puck.getX() + puck.getWitdh() / 2, puck.getY() + puck.getWitdh() / 2);
                            if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                                float a = (float) (Math.pow(puck.getX() - centerGoal.x, 2) / Math.pow(puck.getY() - centerGoal.y, 2) + 1);
                                float b = (float) (2 * (puck.getX() - centerGoal.x) / Math.pow(puck.getY() - centerGoal.y, 2) + 2 * puck.getX() * (puck.getX() - centerGoal.x) / (puck.getY() - centerGoal.y) - 2 * puck.getY());
                                float c = (float) (Math.pow(puck.getY() * centerGoal.x - puck.getX() * centerGoal.y, 2) / Math.pow(puck.getY() - centerGoal.y, 2) + Math.pow(puck.getX(), 2) + 2 * puck.getX() * (puck.getY() * centerGoal.x - puck.getY() * centerGoal.y) / (puck.getY() - centerGoal.y) + Math.pow(puck.getY(), 2) - Math.pow(puck.getWitdh() / 2 + pandle_green.getWitdh() / 2, 2));
                                float x = findx(a, b, c);
                                float y = (float) ((puck.getX() - centerGoal.x) * x - puck.getY() * centerGoal.x - puck.getX() * centerGoal.y) / (puck.getY() - centerGoal.x);
                                double tempx = Math.abs((y - puck.getY()) * pandle_pink.getX() + (x - puck.getX()) * pandle_pink.getX());
                                double tempy = Math.sqrt(Math.pow((y - puck.getY()), 2) + Math.pow(x - puck.getX(), 2));
                                if ((tempx / tempy) >= (pandle_pink.getWitdh() / 2 + puck.getWitdh() / 2)) {
                                    checkmoveforAIpandle(puck.getX(), puck.getY());
                                    if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                                        double g = Math.random();
                                        Vector2d temp = new Vector2d((float) (puck.getX() * Math.cos(g) - puck.getY() * Math.sin(g)), (float) (puck.getX() * Math.sin(x) + puck.getY() * Math.cos(g)));
                                        x = temp.x;
                                        y = temp.y;
                                    }
                                }
                                checkmoveforAIpandle(x, y);
                            }
                        }
                        if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                            AIPandle.setElapsed(0.15f);
                            checkmoveforAIpandle(Hockey.WITDH / 2, pandle_greenAI.getHeight());


                        }
                        break;
                    }
                    default: {
                        checkHit(pandle_pink, puck);
                        checkHit(pandle_green, puck);

                    }
                }
            }
            if(Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())&&(Intersector.overlaps(puck.getBounds(),this.background.getMapEdge().get(Config.EDGE_RIGHT_TOP).getBound())))
            {
                bound++;

            }

            if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                center=true;
                bound++;
                /*boolean temp=false;
                for(String key : this.background.getMapEdge().keySet()) {
                    BackgroundGame.Edge edge = this.background.getMapEdge().get(key);
                    if(edge.getBody().getTexture()==edge.getBody_light())
                    {
                        temp = true;
                    }
                }*/


                if(bound > 50) {
                    checkmoveforAIpandle(Hockey.WITDH / 2, pandle_greenAI.getHeight());
                    bound = 80;
                }
            }
            else
            {
                if(bound>50) {

                    checkmoveforAIpandle(Hockey.WITDH / 2, pandle_greenAI.getHeight());
                    bound--;
                    puck.setPosition(puck.getX()+5, puck.getY()+10);
//                    if (pandle_green.getX() == Hockey.WITDH / 2)
//
//                    {
//                        bound =0;
//                    }
                    if(bound==51)
                    {
                        bound=0;
                    }
                }

            }
//            double distance = Math.sqrt(Vector2.dst2(puck.getX(), puck.getY(), pandle_green.getX(), pandle_green.getY())) + 5;
//            if(distance < (puck.getBounds().radius + pandle_green.getBounds().radius)) {
//                Double x = (pandle_green.getX() - puck.getX()) * (pandle_green.getWitdh() / 2 + puck.getWitdh() / 2) / distance + puck.getX();
//                Double y = (pandle_green.getY() - puck.getY()) * (pandle_green.getWitdh() / 2 + puck.getWitdh() / 2) / distance + puck.getY();
//                pandle_green.move(x.floatValue(), y.floatValue());
//            }
        }


    }
    /*
    * Ham kiem tra xem cuoi cung cua update thi puck co va cham voi pandle neu co thi di chuyen puck ra xa
    *
    * */
    public void checkHit(Pandle pandle, Puck puck) {


        pandle.hits(puck);
        puck.hits(pandle);

        double distance = Math.sqrt(Vector2.dst2(puck.getX(), puck.getY(), pandle.getX(), pandle.getY()));

        if (Intersector.overlaps(pandle.getBounds(), puck.getBounds())) {
            Double x = (puck.getX() - pandle.getX()) * (puck.getWitdh() / 2 + pandle.getWitdh() / 2) / distance + pandle.getX();
            Double y = (puck.getY() - pandle.getY()) * (puck.getWitdh() / 2 + pandle.getWitdh() / 2) / distance + pandle.getY();
            effectPuck.setPosition(x.floatValue(),y.floatValue());
            effectPuck.reset();
            puck.setPosition(x.floatValue(), y.floatValue());

        }


    }

    /*
        Giới hạn không cho di chuyển ra khởi màng hình, va di chuyen
    **/

    public void checkmoveforAIpandle(float screenX, float screenY) {
        if (screenY < Hockey.HEIGHT / 2 + pandle_green.getWitdh() / 2) {
            screenX = (int) Math.min(Math.max(screenX, pandle_green.getWitdh() / 2 + background.getMapEdge().get(Config.EDGE_RIGHT_TOP).getWitdh() - 1),
                    Hockey.WITDH - pandle_green.getWitdh() / 2 - (background.getMapEdge().get(Config.EDGE_LEFT_TOP).getWitdh() - 1));
            screenY = (int) Math.min(Math.max(screenY, pandle_green.getHeight() / 2 + background.getMapEdge().get(Config.EDGE_TOP_RIGHT).getHeight() - 1),
                    Hockey.HEIGHT / 2 - pandle_green.getHeight() / 2);
            double distance = Math.sqrt(Vector2.dst2(puck.getX(), puck.getY(), screenX, screenY)) +10;
           
            if (distance <= (puck.getBounds().radius + pandle_green.getBounds().radius)) {
                Double x = (screenX - puck.getX()) * (pandle_green.getWitdh() / 2 + puck.getWitdh() / 2) / distance + puck.getX();
                Double y = (screenY - puck.getY()) * (pandle_green.getWitdh() / 2 + puck.getWitdh() / 2) / distance + puck.getY();
                screenX = x.floatValue();
                screenY = y.floatValue();
            }
            if (distance==0)
            {
                screenX = - screenX;
                screenY = -screenY;
            }
            pandle_green.move(screenX, screenY);
        }

    }

    public void move(float screenX, float screenY) {

            // gioi han bounds khong cho chay ra khoi mang hinh

        screenX = (int) Math.min(Math.max(screenX, pandle_pink.getWitdh() / 2 + background.getMapEdge().get(Config.EDGE_RIGHT_BOTTOM).getWitdh() - 1),
                Hockey.WITDH - pandle_pink.getWitdh() / 2 - (background.getMapEdge().get(Config.EDGE_LEFT_BOTTOM).getWitdh() - 1));
        screenY = (int) Math.min(Math.max(screenY, Hockey.HEIGHT / 2 + pandle_pink.getHeight() / 2),
                Hockey.HEIGHT - pandle_pink.getHeight() / 2 - (background.getMapEdge().get(Config.EDGE_BOTTOM_RIGHT).getHeight() - 1));
        double distance = Math.sqrt(Vector2.dst2(puck.getX(), puck.getY(), screenX,screenY))+7;
        if (distance < (puck.getBounds().radius + pandle_pink.getBounds().radius) ) {

            Double x = (screenX - puck.getX()) * (pandle_pink.getWitdh() / 2 + puck.getWitdh() / 2) / distance + puck.getX();
            Double y = (screenY - puck.getY()) * (pandle_pink.getWitdh() / 2 + puck.getWitdh() / 2) / distance + puck.getY();screenX = x.floatValue();
            screenY = y.floatValue();
        }
        pandle_pink.move(screenX, screenY);

    }

   /* public void rotateBy(float amountInDegrees) {
        Array<ParticleEmitter> emitters = effect.getEmitters();
        for (int i = 0; i < emitters.size; i++) {
            ParticleEmitter.ScaledNumericValue val = emitters.get(i).getAngle();
            float amplitude = (val.getHighMax() - val.getHighMin()) / 2f;
            float h1 = amountInDegrees + amplitude;
            float h2 = amountInDegrees - amplitude;
            val.setHigh(h1, h2);
            val.setLow(amountInDegrees);
        }
    }*/


    /*
           Kiem tra xem score
     */
    public void goalScore() {

        if (puck.getY() + puck.getHeight() / 2 < 0) {
            pandle_pink.setScore();
            if (pandle_pink.getScore() == 5) {
                audio.getWin().play();
                createResult("win");
                flagWin = true;
            } else {
                reLoad();
                puck.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT / 2 - 100);
                effectGood.setPosition(Hockey.WITDH / 2, 10);
                effectGood.reset();
                effectGoal.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT / 3 * 2);
                effectGoal.reset();
                audio.getGoal().play();
            }
        } else if (puck.getY() - puck.getHeight() / 2 > Hockey.HEIGHT) {

            pandle_green.setScore();

            if (pandle_green.getScore() == 5) {
                audio.getLose().play();
                createResult("lose");
                flagWin = true;
            } else {
                reLoad();
                puck.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT / 2 + 100);
                effectGood.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT - 10);
                effectGood.reset();
                effectGoal.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT / 4);
                effectGoal.reset();
                audio.getGoal().play();
            }

        }
    }

    private void reLoad() {
        pandle_pink.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT - pandle_pink.getHeight());
        pandle_green.reLoadGame(Hockey.WITDH / 2, pandle_pink.getHeight());
        bound =0;
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
    public void pause() {GAME_PAUSED = true;

    }

    @Override
    public void resume() {GAME_PAUSED = false;

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    private float findx(float a, float b, float c)
    {
        float x=0,x1=0,delta =0;
        delta= (b*b)-(4*a*c);
        x= (float) ((-b+Math.sqrt(delta))/(2*a));
        x1= (float) ((-b-Math.sqrt(delta))/(2*a));
        if(x1>=x)
        {
            x=x1;
        }

        return x;
    }
    private World createBackground(Vector2 pointStart, int height, int width){
        //GROUND
        //body denfinition
        World world = new World(new Vector2(0,0),true);
        BodyDef bodyDef=new  BodyDef();
        FixtureDef fixtureDef=new FixtureDef();
        ChainShape groundShape=new ChainShape();
        for(int i=0;i<4;i++){
            bodyDef.type= BodyDef.BodyType.StaticBody;//32
            //01
            fixtureDef=new FixtureDef();
            groundShape=new ChainShape();
            switch(i){
                case 0:
                    bodyDef.position.set(0,0);
                    //ground shape
                    groundShape=new ChainShape();
                    groundShape.createChain(new Vector2[]{new Vector2(pointStart.x,pointStart.y),new Vector2(pointStart.x+width,pointStart.y)});
                    break;
                case 1:
                    bodyDef.position.set(0,0);
                    //ground shape
                    groundShape=new ChainShape();
                    groundShape.createChain(new Vector2[]{new Vector2(pointStart.x+width,pointStart.y),new Vector2(pointStart.x+width,pointStart.y+height)});
                    break;
                case 2:
                    bodyDef.position.set(0,0);
                    //ground shape
                    groundShape=new ChainShape();
                    groundShape.createChain(new Vector2[]{new Vector2(pointStart.x+width,pointStart.y+height),new Vector2(pointStart.x,pointStart.y+height)});
                    break;
                case 3:
                    bodyDef.position.set(0,0);
                    //ground shape
                    groundShape=new ChainShape();
                    groundShape.createChain(new Vector2[]{new Vector2(pointStart.x,pointStart.y+height),new Vector2(pointStart.x,pointStart.y)});
                    break;
            }

            //fixture definiton
            fixtureDef.shape=groundShape;
            fixtureDef.friction=0.25f;
            fixtureDef.restitution=0;
            world.createBody(bodyDef).createFixture(fixtureDef);
            groundShape.dispose();
        }
        return  world;
    }
    private void createResult(String kq) {
        button_Result = new Sprite(new Texture(Hockey.PATCH +kq+".png"));
        button_Result.setPosition(Hockey.WITDH/2 - button_Result.getWidth()/2, Hockey.HEIGHT/4- button_Result.getHeight()/2);
        button_NewGame.setPosition(Hockey.WITDH/2-button_NewGame.getWidth()/2, Hockey.HEIGHT*3/4-button_NewGame.getHeight()/2);
    }
}
