package com.skyplus.hockey.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.config.Config;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by Thy on 5/6/2017.
 */

public class AIPandle extends Pandle {

    private Sprite body;
    private Texture body_dark;
    private Texture body_light;
    private Vector2 position;
    private Circle bounds;
    public int score = 0;
    /*
     Các biến di chuyển
   */
    public static double SPEED_WIDTH = 90f;
    public static double SPEED_HEIGHT = 95f;
    public static float elapsed = 0.15f;
    private Boolean moving = false;
    private Vector2 end = new Vector2(0, 0);
    private  Vector2d velocity ;
    // set độ sáng chậm khi va cham với puck
    private long timer = 0;



    private final float speed=500f;
    private final float TIMESTEP=1/60f;
    private final int VELOCITYINTERATIONS=8,POSITIONINTERATIONS=3;
    private Vector2 velocitytemp;
    private World world;
    private Body bodyworld;
    private Vector2 movement;
    private BackgroundGame background;


    public AIPandle(int x, int y, Texture body1, Texture body2,BackgroundGame background) {
        world = new World(new Vector2(0,0),true);
        movement = new Vector2();
        position = new Vector2(x, y);
        this.body_dark = body1;
        this.body_light = body2;
        body = new Sprite(body1);
        bounds = new Circle(position.x, position.y, getWitdh() / 2);
        velocity = new Vector2d(0, 0);
        SPEED_WIDTH *= (Hockey.WITDH/ Config.SCREEN_MAIN.x);
        SPEED_HEIGHT *= (Hockey.HEIGHT/Config.SCREEN_MAIN.y);
        this.background =background;

//        speed *= 2.4;
//        speed += speed *= (Hockey.WITDH * Config.SCREEN_MAIN.y)/ (Hockey.HEIGHT*Config.SCREEN_MAIN.x);
    }

    @Override
    public void draw(SpriteBatch batch) {
        body.setPosition(position.x - body.getWidth() / 2, position.y - body.getHeight() / 2);
        body.draw(batch);

    }

    public BackgroundGame getBackground() {
        return background;
    }

    public void setBackground(BackgroundGame background) {
        this.background = background;
    }


    @Override
    public void move(float x, float y) {
        if (y <= Hockey.HEIGHT / 2+getWitdh()/2) {
            x = (int) Math.min(Math.max(x, getWitdh() / 2 + background.getMapEdge().get(Config.EDGE_RIGHT_TOP).getWitdh() - 1),
                    Hockey.WITDH - getWitdh() / 2 - (background.getMapEdge().get(Config.EDGE_LEFT_TOP).getWitdh() - 1));

            y = (int) Math.min(Math.max(y, getHeight() / 2 + background.getMapEdge().get(Config.EDGE_TOP_RIGHT).getHeight() - 1),
                    Hockey.HEIGHT / 2 - getHeight() / 2);
            end.set(x, y);
        }
        else
        {
            end.set(position.x,position.y);
        }
        movement.set(-(end.x-position.x)* elapsed/1000,-(end.y-position.y)  * elapsed/1000);
        bodyworld.applyForceToCenter(movement,true);
        world.step(TIMESTEP, VELOCITYINTERATIONS, POSITIONINTERATIONS);
        setVelocitytemp( bodyworld.getLinearVelocity());
//        end.set(Hockey.WITDH/2,Hockey.HEIGHT/2);
        moving = true;
    }

    @Override
    public void update(float delta) {

        if (!moving) return;

        Vector2 start = new Vector2(position.x, position.y);

        // khoang cach giua 2 diem trong mat phang, chua lay can ban 2
        double distance = Vector2.dst2(start.x, start.y, end.x, end.y);
        double distance2 = Math.sqrt(distance);

        // xac dinh huong di
        Vector2 direction = new Vector2(end.x - start.x, end.y - start.y);
        direction.nor();
        double x = direction.x * SPEED_WIDTH * elapsed;
        double y = direction.y * SPEED_HEIGHT  * elapsed;

        setVelocity();  // set van toc cho pandle
        position.x += x;
        position.y += y;

        if (Vector2.dst2(start.x, start.y, position.x, position.y) >= distance) {
            position.set(end);
        }


    }

    public void reLoadGame(float x, float y) {
        setPosition(x, y);
        velocity.x = 0;
        velocity.y = 0;
    }

    @Override
    public Boolean hits(Circle circle) {

        return false;
    }


    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public float getY() {
        return position.y;
    }

    @Override
    public float getWitdh() {
        return body.getWidth();
    }

    @Override
    public float getHeight() {
        return body.getHeight();
    }

    @Override
    public Texture getTexture() {
        return body.getTexture();
    }

    @Override
    public Circle getBounds() {
        bounds.set(position.x, position.y, body.getWidth() / 2);
        return bounds;
    }

    public Boolean hits(Puck puck) {
        if (Intersector.overlaps(puck.getBounds(), getBounds())) {
            body.setTexture(body_light);
            timer = System.currentTimeMillis();
            if(velocitytemp.x<4)
            {
                Vector2 vector2 = new Vector2(2,20);
                setVelocitytemp(vector2);
                setVelocity();
            }
            return true;
        } else if (System.currentTimeMillis() - timer > 100) {
            body.setTexture(body_dark);
        }

        return false;
    }

    /*
    *
    * seter
    * geter
    * */
    public int getScore() {
        return score;
    }

    public void setScore() {
        this.score++;
        this.score = Math.min(Math.max(this.score, 0),9);
    }

    public Vector2d getVelocity() {
        return velocity;
    }

    public Texture getBody_dark() {
        return body_dark;
    }

    public void setBody_dark(Texture body_dark) {
        this.body_dark = body_dark;
    }

    public Texture getBody_light() {
        return body_light;
    }

    public void setBody_light(Texture body_light) {
        this.body_light = body_light;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        moving = false;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public float getSpeed() {
        return speed;
    }

    public float getTIMESTEP() {
        return TIMESTEP;
    }

    public int getVELOCITYINTERATIONS() {
        return VELOCITYINTERATIONS;
    }

    public int getPOSITIONINTERATIONS() {
        return POSITIONINTERATIONS;
    }

    public Vector2 getVelocitytemp() {
        return velocitytemp;
    }

    public void setVelocitytemp(Vector2 velocitytemp) {
        this.velocitytemp = velocitytemp;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void setVelocity() {
        velocity.set(Math.abs(velocitytemp.x),Math.abs(velocitytemp.y));
       /* velocity.set((end.x - position.x) * 3, (end.y - position.y) * 3);*/
    }

    public void setBody(Texture body) {
        this.body.setRegion(body);
    }

    public Body getBodyworld() {
        return bodyworld;
    }

    public void setBodyworld(int height, int width) {
       BodyDef bodyDef=new BodyDef();
       FixtureDef fixtureDef=new FixtureDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(height/2,getHeight());
       CircleShape shape=new CircleShape();
        shape.setRadius(getWitdh()/2);
        fixtureDef.friction=0.2f;
        fixtureDef.shape=shape;
        fixtureDef.restitution=0.7f;
        fixtureDef.density=3;
      bodyworld = world.createBody(bodyDef);
        bodyworld.createFixture(fixtureDef);


    }

    public static float getElapsed() {
        return elapsed;
    }

    public static void setElapsed(float elapsed) {
        AIPandle.elapsed = elapsed;
    }
}
