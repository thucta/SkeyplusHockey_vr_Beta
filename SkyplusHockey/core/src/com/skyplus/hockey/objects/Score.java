package com.skyplus.hockey.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by NVTT on 4/27/2017.
 */

public class Score {
    private Matrix4 mx4Font;
    private BitmapFont font;
    private Sprite spriteFont;
    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    private String score;
    public  Score(int x,int y, String score){
        this.score = score;
        mx4Font = new Matrix4();

        font = new BitmapFont(Gdx.files.internal("Font/fontMain.fnt"), Gdx.files.internal("Font/fontMain.png"),true);
        
        spriteFont = new Sprite(font.getRegion());
        mx4Font.setToRotation(new Vector3(200, 200,0), 180);
    }

    public void draw(SpriteBatch sb,String score){
        spriteFont.setRotation(180);
        spriteFont.draw(sb);
    }
}
