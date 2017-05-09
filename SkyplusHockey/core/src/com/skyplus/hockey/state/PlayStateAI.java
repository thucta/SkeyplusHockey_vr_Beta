package com.skyplus.hockey.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.config.Config;
import com.skyplus.hockey.network.GameClientInterface;
import com.skyplus.hockey.objects.AIPandle;
import com.skyplus.hockey.objects.BackgroundGame;
import com.skyplus.hockey.objects.Effect;
import com.skyplus.hockey.objects.Pandle;
import com.skyplus.hockey.objects.Puck;
import com.skyplus.hockey.objects.Vector2d;

/*
import org.json.JSONObject;
*/

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


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

    private ParticleEffect effectGood;
    private ParticleEffect effectPuck;

    private Sprite button_Resume,button_NewGame, button_Exit, button_Pause;
    private boolean GAME_PAUSED = false;
    private Sprite sprite;
    // map diem
    public static Map<Integer, Sprite> mapSpriteScore;

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


        //pause
        button_Pause = new Sprite(new Texture(Hockey.PATCH+"buttonPause.png"));
        button_Resume = new Sprite(new Texture(Hockey.PATCH+"buttonResume.png"));
        button_NewGame = new Sprite(new Texture(Hockey.PATCH+"buttonNewGame.png"));
        button_Exit = new Sprite(new Texture(Hockey.PATCH+"buttonExit.png"));
        button_Pause.setPosition(Hockey.WITDH- button_Pause.getWidth(), Hockey.HEIGHT/2-button_Pause.getHeight()/2);
        button_Resume.setPosition(Hockey.WITDH/ 2 - button_Resume.getWidth() / 2, Hockey.HEIGHT/ 4 - button_Resume.getHeight() / 3);
        button_NewGame.setPosition(Hockey.WITDH / 2 - button_NewGame.getWidth() / 2, Hockey.HEIGHT/ 2 - button_NewGame.getHeight() / 2);
        button_Exit.setPosition(Hockey.WITDH/ 2 - button_Exit.getWidth() / 2, Hockey.HEIGHT *3/ 4 - button_Exit.getHeight() / 2);
        sprite = new Sprite(background.get(Config.BACKGROUND));


        //scrore
        mapSpriteScore = new HashMap<Integer, Sprite>();
        for (int i = 0; i < 10; i++) {
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
            if(playmode==4)
            {
            pandle_greenAI.setElapsed(0.0003f);
            }
        pandle_green=pandle_greenAI;

        puck = new Puck((int) cam.viewportWidth / 2, (int) cam.viewportHeight / 2, this.background.getMapEdge());

        //fx
        Effect fx = new Effect("fxRed");
        effectGood = fx.create();

        Effect fxEdge = new Effect("fxRedEdge");
        effectPuck = fxEdge.create();
        // co lien quan gi de bi null ko?koko ko
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

        checkHit(pandle_green,puck,background);
        checkHit(pandle_pink,puck,background);

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
            button_Pause.draw(sb);
        drawScores(sb);
    }else {
            Gdx.gl.glClearColor(0,0,0,0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            sb.setProjectionMatrix(cam.combined);
            sb.begin();

            sprite.setAlpha(0.6f);

            puck.drawEffect(sb);
            effectGood.draw(sb);
            effectPuck.draw(sb);

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

        }
        sb.end();
    }






    public void moveAI()
    {


        if(!movedball) {
            if(puck.getY()>Hockey.HEIGHT/2) {
                pandle_green.move(pandle_pink.getX()+pandle_pink.getWitdh()/2, Hockey.HEIGHT / 9);
            }
            else
            {
                pandle_green.move(pandle_pink.getX()+pandle_pink.getWitdh()/2,pandle_pink.getY()+pandle_pink.getHeight()/2);
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
                        if (puck.getY() > Hockey.HEIGHT / 2 + pandle_green.getWitdh()) {
                            if (puck.getY() < 100) {
                                pandle_green.move(puck.getX(), Hockey.HEIGHT / 9);
                            } else {
                                if (puck.getX() > Hockey.WITDH - 100) {
                                    pandle_green.move(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                } else {
                                    pandle_green.move(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                }
                            }
                        } else {
                            pandle_green.move(puck.getX(), puck.getY() + randomNum);
                        }
                        if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                            pandle_green.move(puck.getX() + 1000, puck.getY());

                        }
                        break;
                    }
                    case 2: {
                        if (puck.getY() > Hockey.HEIGHT / 2 + pandle_green.getWitdh()) {
                            if (puck.getX() < 100) {
                                pandle_green.move(puck.getX() + 150, Hockey.HEIGHT / 9);
                            } else {
                                if (puck.getX() > Hockey.WITDH - 100) {
                                    pandle_green.move(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                } else {
                                    pandle_green.move(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                }
                            }
                        } else {
                            pandle_green.move(puck.getX() + puck.getWitdh() / 2, puck.getY() + puck.getWitdh() / 2);
                            if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                                float a = (float) (Math.pow(puck.getX() - centerGoal.x, 2) / Math.pow(puck.getY() - centerGoal.y, 2) + 1);
                                float b = (float) (2 * (puck.getX() - centerGoal.x) / Math.pow(puck.getY() - centerGoal.y, 2) + 2 * puck.getX() * (puck.getX() - centerGoal.x) / (puck.getY() - centerGoal.y) - 2 * puck.getY());
                                float c = (float) (Math.pow(puck.getY() * centerGoal.x - puck.getX() * centerGoal.y, 2) / Math.pow(puck.getY() - centerGoal.y, 2) + Math.pow(puck.getX(), 2) + 2 * puck.getX() * (puck.getY() * centerGoal.x - puck.getY() * centerGoal.y) / (puck.getY() - centerGoal.y) + Math.pow(puck.getY(), 2) - Math.pow(puck.getWitdh() / 2 + pandle_green.getWitdh() / 2, 2));
                                float x = findx(a, b, c);
                                float y = (float) ((puck.getX() - centerGoal.x) * x - puck.getY() * centerGoal.x - puck.getX() * centerGoal.y) / (puck.getY() - centerGoal.x);
                                pandle_green.move(x, y);
                            }


                        }
                        if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                            pandle_green.move(puck.getX() + 1000, puck.getY());

                        }
                        break;
                    }
                    case 3: {
                        if (puck.getY() > Hockey.HEIGHT / 2 + pandle_green.getWitdh()) {
                            if (puck.getX() < 100) {
                                pandle_green.move(puck.getX() + 150, Hockey.HEIGHT / 9);
                            } else {
                                if (puck.getX() > Hockey.WITDH - 100) {
                                    pandle_green.move(puck.getX() + puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                } else {
                                    pandle_green.move(puck.getX() + puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                }
                            }
                        } else {
                            double tempx = Math.abs(((pandle_green.getY() - puck.getY()) * pandle_pink.getX() + (pandle_green.getX() - puck.getX()) * pandle_pink.getX()));
                            double tempy = Math.sqrt(Math.pow((pandle_green.getY() - puck.getY()), 2) + Math.pow(pandle_green.getX() - puck.getX(), 2));
                            if ((tempx / tempy) >= (pandle_pink.getWitdh() / 2 + puck.getWitdh() / 2)) {
                                pandle_green.move(puck.getX(), puck.getY());
                                if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                                    double x = Math.random();
                                    Vector2d temp = new Vector2d((float) (puck.getX() * Math.cos(x) - puck.getY() * Math.sin(x)), (float) (puck.getX() * Math.sin(x) + puck.getY() * Math.cos(x)));
                                    pandle_green.move(temp.x, temp.y);
                                }
                            } else {
                                pandle_green.move(puck.getX(), puck.getY());
                            }


                        }
                        if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                            pandle_green.move(puck.getX() + 1000, puck.getY());

                        }
                        break;
                    }
                    case 4: {
                        Random rn = new Random();
                        int range = (int) puck.getWitdh() / 2 + 1;
                        int randomNum = rn.nextInt(range);
                        if (puck.getY() > Hockey.HEIGHT / 2 + pandle_green.getWitdh()) {
                            if (puck.getY() < 100) {
                                pandle_green.move(puck.getX(), Hockey.HEIGHT / 9);
                            } else {
                                if (puck.getX() > Hockey.WITDH - 100) {
                                    pandle_green.move(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                } else {
                                    pandle_green.move(puck.getX() - puck.getWitdh() / 2, Hockey.HEIGHT / 9);
                                }
                            }
                        } else {
                            pandle_green.move(puck.getX(), puck.getY() + randomNum);
                        }
                        if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                            pandle_green.move(puck.getX() + 1000, puck.getY() + randomNum);

                        }
                        break;
                    }
                    default: {
                        checkHit(pandle_pink, puck, background);
                        checkHit(pandle_green, puck, background);

                    }
                }
            }
            if(Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())&&Intersector.overlaps(puck.getBounds(),this.background.getMapEdge().get(Config.EDGE_RIGHT_TOP).getBound()))
            {
                bound++;
            }
            if(Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())&&Intersector.overlaps(puck.getBounds(),this.background.getMapEdge().get(Config.EDGE_LEFT_TOP).getBound()))
            {
                bound++;
            }
            if (Intersector.overlaps(pandle_green.getBounds(), puck.getBounds())) {
                bound++;
                if(bound > 50) {
                    pandle_green.move(Hockey.WITDH / 2, pandle_greenAI.getHeight());
                    bound = 70;
                }
            }
            else
            {
                if(bound>50) {

                    pandle_green.move(Hockey.WITDH / 2, pandle_greenAI.getHeight());
                    bound--;
                    puck.setPosition(puck.getX()+2, puck.getY()+5);
                    if (pandle_green.getX() == Hockey.WITDH / 2)

                    {
                        puck.setPosition(puck.getX() + puck.getWitdh()*2, puck.getY() + puck.getWitdh()*2);
                        bound =0;
                    }
                    if(bound==51)
                    {
                        bound=0;
                    }
                }

            }
        }

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
            effectPuck.setPosition(x.floatValue(),y.floatValue());
            effectPuck.reset();
            puck.setPosition(x.floatValue(), y.floatValue());

        }


    }

    /*
        Giới hạn không cho di chuyển ra khởi màng hình, va di chuyen
    **/
    public void move(float screenX, float screenY) {

            // gioi han bounds khong cho chay ra khoi mang hinh
            screenX = (int) Math.min(Math.max(screenX, pandle_pink.getWitdh() / 2 + background.getMapEdge().get(Config.EDGE_RIGHT_BOTTOM).getWitdh() - 1),
                    Hockey.WITDH - pandle_pink.getWitdh() / 2 - (background.getMapEdge().get(Config.EDGE_LEFT_BOTTOM).getWitdh() - 1));

            screenY = (int) Math.min(Math.max(screenY, Hockey.HEIGHT / 2 + pandle_pink.getHeight() / 2),
                    Hockey.HEIGHT - pandle_pink.getHeight() / 2 - (background.getMapEdge().get(Config.EDGE_BOTTOM_RIGHT).getHeight() - 1));

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

            reLoad();
            puck.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT / 2 - 100);
            effectGood.setPosition(Hockey.WITDH / 2, 10);
            effectGood.reset();

        } else if (puck.getY() - puck.getHeight() / 2 > Hockey.HEIGHT) {

            pandle_green.setScore();
            reLoad();
            puck.reLoadGame(Hockey.WITDH / 2, Hockey.HEIGHT / 2 + 100);
            effectGood.setPosition(Hockey.WITDH / 2, Hockey.HEIGHT - 10);
            effectGood.reset();


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
}
