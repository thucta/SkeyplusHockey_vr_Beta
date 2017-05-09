package com.skyplus.hockey.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.config.Config;

import java.util.Map;


/**
 * Created by TruongNN on 3/24/2017.
 */

public class Puck extends GameObject {

    private Sprite body;
    private Vector2 position;
    private Vector2d velocity;
    private Circle bounds;

    private Audio audio;

    private Map<String,BackgroundGame.Edge> listEdge;

    private int radius;
    private static float SPEED = 0.993f;
    private static float LIMIT = 20 ;
    private static float LIMIT_STOP = 0.3f;
    private static float F_EDGE = 1f;
    private static float RATE_WIDTH ;
    private static float RATE_HIEGHT ;

    private ParticleEffect effectEdge;


    public Puck(int x, int y, Map<String,BackgroundGame.Edge> listEdge) {
        this.listEdge = listEdge;
        position = new Vector2(x, y);
        initiators();

    }

    private void initiators() {
        velocity = new Vector2d(0, 0);
        body = new Sprite(new Texture(Hockey.PATCH + "puck.png"));

        // tru ban kinh cua puck de ve ngay tam duong tron
        body.setPosition(position.x - body.getWidth() / 2, position.y - body.getWidth() / 2);
        bounds = new Circle(position.x, position.y, body.getWidth() / 2);
        radius = (int) (body.getWidth() / 2);

        RATE_WIDTH = (Hockey.WITDH/ Config.SCREEN_MAIN.x);
        RATE_HIEGHT = (Hockey.HEIGHT/ Config.SCREEN_MAIN.y);

       audio = new Audio();

        Effect fx  = new Effect("fxgreenEdge");
        effectEdge = fx.create();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2d getVelocity() {

        return velocity;
    }

    public Vector2d vectorTemp = new Vector2d(0, 0);
    // Ham va cham voi pandles
    public Boolean hits(Pandle pandle) {
        if (Intersector.overlaps(getBounds(), pandle.getBounds())) {

            // nếu khônng sử dụng biến tạm thì sau khi kết thúc hàm giá trị của
            vectorTemp.x = pandle.getVelocity().x;
            vectorTemp.y = pandle.getVelocity().y;

//            Gdx.app.log("sau", velocity+" " );
            //xac ding huong va cham
            Vector2d direction = new Vector2d(position.x - pandle.getX(), position.y - pandle.getY());

            velocity = vectorTemp.proj(direction).plus(velocity.proj(direction).times(-1f)
                    .plus(velocity.proj(new Vector2d(direction.y, -direction.x)))).times(0.7f);

            /*
                 F = -F => a1m1 = - a2m2 (m1 = 2/3 m2)    (m1: khoi luong cua puck , m2 : khoi luong cua pandle)
                 => do giam van toc cua puck khi va cham vao cac dia
            */
//            velocity.x += velocity.x *-2/3;
//            velocity.y += velocity.y *-2/3;

            return true;
        }
        return false;
    }

    @Override
    public Boolean hits(Circle circle) {
        return null;
    }

    @Override
    public void move(float x, float y) {

    }


    // client dung
    public void histEdge(){


        for(String key : listEdge.keySet()){
            edge = listEdge.get(key);
            if(Intersector.overlaps(getBounds(),edge.getBound())){
                edge.setBody_light();
                audio.getEdgeHitSound().play();
            }
        }

    }

    // update trang thai cho opuck bao gom vi tri, body,...

    private BackgroundGame.Edge edge = null;
    @Override
    public void update(float delta) {
        // gioi han toc do
        velocityLimit();

        velocity.x *=  SPEED  ;
        velocity.y *= SPEED ;
        position.x += velocity.x * RATE_WIDTH; // chuyen doi ti le mang hinh
        position.y += velocity.y *RATE_HIEGHT;



        // va cham voi canh phai

        if (Intersector.overlaps(getBounds(), listEdge.get(Config.EDGE_RIGHT_TOP).getBound())) {
            // cộng 1 lực tác dụng ngược lại của edge
            edge = listEdge.get(Config.EDGE_RIGHT_TOP);
            velocity.x = Math.abs(velocity.x) + F_EDGE;
            position.x = radius + edge.getWitdh();


            effectEdge.setPosition(body.getX(), body.getY());
            effectEdge.reset();

            edge.setBody_light();
            audio.getEdgeHitSound().play();
        }

        else if  (Intersector.overlaps(getBounds(),listEdge.get(Config.EDGE_RIGHT_BOTTOM).getBound())){
            edge = listEdge.get(Config.EDGE_RIGHT_BOTTOM);
            velocity.x = Math.abs(velocity.x) + F_EDGE;
            position.x = radius + edge.getWitdh();

            effectEdge.setPosition(body.getX(), body.getY());
            effectEdge.reset();

            edge.setBody_light();
            audio.getEdgeHitSound().play();
        }

        // va cham voi canh trai
        else if (Intersector.overlaps(getBounds(),listEdge.get(Config.EDGE_LEFT_TOP).getBound())){
            edge = listEdge.get(Config.EDGE_LEFT_TOP);
            velocity.x = -(Math.abs(velocity.x)+F_EDGE);
            position.x = Hockey.WITDH - radius -edge.getWitdh() ;

            effectEdge.setPosition(body.getX(), body.getY());
            effectEdge.reset();

            edge.setBody_light();
            audio.getEdgeHitSound().play();
        }

        else if(Intersector.overlaps(getBounds(),listEdge.get(Config.EDGE_LEFT_BOTTOM).getBound())){
            edge = listEdge.get(Config.EDGE_LEFT_BOTTOM);
            velocity.x = -(Math.abs(velocity.x)+F_EDGE);
            position.x = Hockey.WITDH - radius - edge.getWitdh() ;

            effectEdge.setPosition(body.getX(), body.getY());
            effectEdge.reset();

            edge.setBody_light();
            audio.getEdgeHitSound().play();
        }


        // Va cham voi top
        if  (Intersector.overlaps(getBounds(),listEdge.get(Config.EDGE_TOP_RIGHT).getBound())) {

            edge = listEdge.get(Config.EDGE_TOP_RIGHT);
            // va cham vao cac goc cua edge
            if(position.y-getHeight()/2 < edge.getHeight() && (position.x - getWitdh()/4) > edge.getWitdh() ){
                velocity.x = Math.abs(velocity.x) ;

            }else{
                position.y = radius + edge.getHeight();
                velocity.y = Math.abs(velocity.y);


            }

            effectEdge.setPosition(body.getX(), body.getY());
            effectEdge.reset();

            edge.setBody_light();
            audio.getEdgeHitSound().play();

        }
        else if(Intersector.overlaps(getBounds(),listEdge.get(Config.EDGE_TOP_LEFT).getBound())){

            edge = listEdge.get(Config.EDGE_TOP_LEFT);
            if(position.y-getHeight()/2 < edge.getHeight() && (position.x + getWitdh()/4) < edge.getBody().getX()){
                velocity.x = -(Math.abs(velocity.x));
            }
            else {
                position.y = radius + edge.getHeight();
                velocity.y = Math.abs(velocity.y);

            }

            effectEdge.setPosition(body.getX(), body.getY());
            effectEdge.reset();

            edge.setBody_light();
            audio.getEdgeHitSound().play();
        }

        // va cham voi bottom
        else if (Intersector.overlaps(getBounds(),listEdge.get(Config.EDGE_BOTTOM_RIGHT).getBound())){
            edge = listEdge.get(Config.EDGE_BOTTOM_RIGHT);
            if( ( position.y+getHeight()/2 > Hockey.HEIGHT-edge.getHeight() ) && ( (position.x - getWitdh()/4) > edge.getWitdh() ) ){
                velocity.x = Math.abs(velocity.x) ;

            }else {
                velocity.y = -(Math.abs(velocity.y) + F_EDGE);
                position.y = Hockey.HEIGHT - radius - edge.getHeight();
            }

            effectEdge.setPosition(body.getX(), body.getY());
            effectEdge.reset();

            edge.setBody_light();
            audio.getEdgeHitSound().play();
        }
        else if ( Intersector.overlaps(getBounds(),listEdge.get(Config.EDGE_BOTTOM_LEFT).getBound())) {
            edge = listEdge.get(Config.EDGE_BOTTOM_LEFT);
            if(position.y+getHeight()/2 > edge.getHeight() && (position.x + getWitdh()/4) < edge.getBody().getX()){
                velocity.x = -(Math.abs(velocity.x));
            }
            else {

                velocity.y = -(Math.abs(velocity.y) + F_EDGE);
                position.y = Hockey.HEIGHT - radius - edge.getHeight();

            }

            effectEdge.setPosition(body.getX(), body.getY());
            effectEdge.reset();

            edge.setBody_light();
            audio.getEdgeHitSound().play();
        }
        effectEdge.update(delta);
    }
    public void drawEffect(SpriteBatch batch){
        effectEdge.draw(batch);
    }

    public void update(float x,float y){
        setPosition(x,y);
    }

    public void reLoadGame(float x,float y){
        setPosition(x,y);
        setVelocity(0,0);
    }


    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    @Override
    public float getX() {

        return this.position.x;
    }

    public Vector2 getPosition() {
        return this.position;
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



    public void setVelocity(float x ,float y) {
        this.velocity.y =y;
        this.velocity.x = x;
    }
    @Override
    public Circle getBounds() {
        bounds.set(position.x, position.y, body.getWidth() / 2);
        return bounds;
    }


    @Override
    public void draw(SpriteBatch batch) {
        body.setPosition(position.x - body.getWidth() / 2, position.y - body.getHeight() / 2);
        body.draw(batch);
    }


    // gioi han van toc cua puck
    public void velocityLimit() {
        velocity.x = Math.min(Math.max(velocity.x, -LIMIT), LIMIT);
        velocity.y = Math.min(Math.max(velocity.y, -LIMIT), LIMIT);

        if (velocity.x > -LIMIT_STOP && velocity.x < LIMIT_STOP) {
            velocity.x = 0;
        }
        if (velocity.y > -LIMIT_STOP && velocity.y < LIMIT_STOP) {
            velocity.y = 0;
        }
    }
}
