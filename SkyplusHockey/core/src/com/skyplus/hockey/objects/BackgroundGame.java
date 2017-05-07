package com.skyplus.hockey.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.config.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by NVTT on 4/19/2017.
 */

public class BackgroundGame {

    private Sprite bg;
    private Map<String,Edge> mapEdge;

    public BackgroundGame(){

    }
    public BackgroundGame(int widthScreen, int hieghtScreen, Map<String,Texture> backgroud) {

        bg = new Sprite(backgroud.get(Config.BACKGROUND));
        bg.setRegionWidth(widthScreen);
        bg.setRegionHeight(hieghtScreen);
        bg.setPosition(0, 0);
        mapEdge = new HashMap<String, Edge>();

        mapEdge.put(Config.EDGE_RIGHT_TOP, new Edge(0, 0, backgroud.get(Config.EDGE_RIGHT_TOP), backgroud.get(Config.EDGE_RIGHT_TOP_LIGHT)));
        mapEdge.put(Config.EDGE_RIGHT_BOTTOM, new Edge(0, Hockey.HEIGHT / 2, backgroud.get(Config.EDGE_RIGHT_BOTTOM), backgroud.get(Config.EDGE_RIGHT_BOTTOM_LIGHT)));
        mapEdge.put(Config.EDGE_LEFT_TOP, new Edge(widthScreen - backgroud.get(Config.EDGE_LEFT_TOP).getWidth(), 0, backgroud.get(Config.EDGE_LEFT_TOP), backgroud.get(Config.EDGE_LEFT_TOP_LIGHT)));
        mapEdge.put(Config.EDGE_LEFT_BOTTOM, new Edge(widthScreen - backgroud.get(Config.EDGE_LEFT_BOTTOM).getWidth(), Hockey.HEIGHT / 2, backgroud.get(Config.EDGE_LEFT_BOTTOM), backgroud.get(Config.EDGE_LEFT_BOTTOM_LIGHT)));
        mapEdge.put(Config.EDGE_TOP_RIGHT, new Edge(0, 0, backgroud.get(Config.EDGE_TOP_RIGHT), backgroud.get(Config.EDGE_TOP_RIGHT_LIGHT)));
        mapEdge.put(Config.EDGE_TOP_LEFT, new Edge(widthScreen - backgroud.get(Config.EDGE_TOP_LEFT).getWidth(), 0, backgroud.get(Config.EDGE_TOP_LEFT), backgroud.get(Config.EDGE_TOP_LEFT_LIGHT)));
        mapEdge.put(Config.EDGE_BOTTOM_RIGHT, new Edge(0, hieghtScreen - backgroud.get(Config.EDGE_BOTTOM_RIGHT).getHeight(), backgroud.get(Config.EDGE_BOTTOM_RIGHT), backgroud.get(Config.EDGE_BOTTOM_RIGHT_LIGHT)));
        mapEdge.put(Config.EDGE_BOTTOM_LEFT, new Edge(widthScreen - backgroud.get(Config.EDGE_BOTTOM_LEFT).getWidth(), hieghtScreen - backgroud.get(Config.EDGE_BOTTOM_LEFT).getHeight(), backgroud.get(Config.EDGE_BOTTOM_LEFT), backgroud.get(Config.EDGE_BOTTOM_LEFT_LIGHT)));


    }


    public Map<String, Edge> getMapEdge() {
        return mapEdge;
    }

    public void setMapEdge(Map<String, Edge> mapEdge) {
        this.mapEdge = mapEdge;
    }

    // ve backround va cac canh
    public void draw(SpriteBatch sb,Pandle pandle_pink,Pandle pandle_green,Puck puck  ){
        bg.draw(sb);
        Edge edge = new Edge();
        for(String key : mapEdge.keySet()){
                edge = mapEdge.get(key);
            if(System.currentTimeMillis()-edge.getTimer()>100){   // cho canh sang 200 mili giay
                edge.setBody(edge.getBody_dark());

            }
            edge.draw(sb);

        }
    }


    // cac canh cua background
    public  class Edge {
        // vi tri cua canh
        private int postionX,getPostionY;

        private Sprite body;
        private Texture body_dark;
        private Texture body_light;

        // bound cua canh
        private Rectangle bound;

        // set thoi gian sang cua moi canh khi co va cham
        private long timer = 0;

        public Edge() {

        }

        public Edge(int poistionX, int positionY, Texture body_dark, Texture body_light) {
            this.postionX = poistionX;
            this.getPostionY = positionY;
            this.body_dark = body_dark;
            this.body_light = body_light;
            this.body = new Sprite(body_dark);
            this.body.setPosition(poistionX,positionY);
            bound = new Rectangle(this.body.getX(),this.body.getY(),this.body.getWidth(),this.body.getHeight());
        }



        public Sprite getBody() {
            return body;
        }



        // ham set body sang toi, neu flagLigh  = true nghia la sang, co vat va cham nguoc lai khong
        public void setBody(Texture body) {

            setTimer(System.currentTimeMillis());
            this.body.setRegion(body);
        }

        public Texture getBody_dark() {
            return body_dark;
        }

        public void setBody_dark() {
            this.body.setRegion( this.body_dark); ;
            this.body_dark = body_dark;
        }

        public Texture getBody_light() {
            return body_light;
        }

        public void setBody_light() {
            setTimer(System.currentTimeMillis());
            this.body.setRegion(this.body_light);
        }
        public Rectangle getBound() {

            return bound;
        }
        public float getWitdh() {
            return body.getWidth();
        }

        public float getHeight() {
            return body.getHeight();
        }
        public void setBound(Rectangle bound) {
            bound.set(this.body.getX(),this.body.getY(),this.body.getWidth(),this.body.getHeight());
            this.bound = bound;
        }

        public  void draw(SpriteBatch sb){
            this.body.setPosition(postionX,getPostionY);
            this.body.draw(sb);
        }


        public long getTimer() {
            return timer;
        }

        public void setTimer(long timer) {
            this.timer = timer;
        }
    }

}
