package com.skyplus.hockey.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.config.Config;

/**
 * Created by nguye on 09/05/2017.
 */

public class CheckSoundMusic {
    private Sprite checksound, unchecksound, checkMusic,uncheckMusic, textSound, textMusic;
    private HockeyPreferences pref = new HockeyPreferences();
    public void createCheckSoundMusic(){


        checksound = new Sprite(new Texture(Hockey.PATCH+"check.png"));
        unchecksound = new Sprite(new Texture(Hockey.PATCH+"uncheck.png"));
        checksound.setPosition(Hockey.WITDH*4/5, Hockey.HEIGHT/6-checksound.getHeight()/2);
        unchecksound.setPosition(Hockey.WITDH*4/5, Hockey.HEIGHT/6-unchecksound.getHeight()/2);
        textSound = new Sprite(new Texture(Hockey.PATCH+"soundText.png"));
        textSound.setPosition(Hockey.WITDH/5-textSound.getHeight()/2, Hockey.HEIGHT/6-textSound.getHeight()/2);

        checkMusic = new Sprite(new Texture(Hockey.PATCH+"check.png"));
        uncheckMusic = new Sprite(new Texture(Hockey.PATCH+"uncheck.png"));
        checkMusic.setPosition(Hockey.WITDH*4/5, Hockey.HEIGHT/3-checkMusic.getHeight()/2);
        uncheckMusic.setPosition(Hockey.WITDH*4/5, Hockey.HEIGHT/3-uncheckMusic.getHeight()/2);

        textMusic = new Sprite(new Texture(Hockey.PATCH+"music.png"));
        textMusic.setPosition(Hockey.WITDH/5-textSound.getHeight()/2,Hockey.HEIGHT/3 - textSound.getHeight()/2);
    }
    public void clickCheckSoundMusic(int screenX, int screenY){
        if (checksound.getBoundingRectangle().contains(screenX,screenY)) {
            if(Hockey.flagCheck) {
                Hockey.flagCheck = false;
                pref.setSound(false);
            }else {
                Hockey.flagCheck = true;
                pref.setSound(true);
            }
        }
        if (checkMusic.getBoundingRectangle().contains(screenX, screenY)) {
            if(Hockey.flagCheckMusic) {
                Hockey.flagCheckMusic = false;
                pref.setMusic(false);
                Hockey.sound.pause();
            }else {
                Hockey.flagCheckMusic = true;
                pref.setMusic(true);
                Hockey.sound.play();
            }
        }
    }
    public void drawCheckSoundMusic(SpriteBatch sb){

        textSound.setFlip(false,true);
        textSound.draw(sb);
        textMusic.setFlip(false,true);
        textMusic.draw(sb);
        if(pref.getSound()){
            checksound.setFlip(false,true);
            checksound.draw(sb);
        }else {
            unchecksound.setFlip(false,true);
            unchecksound.draw(sb);
        }
        if(pref.getMusic()){
            checkMusic.setFlip(false,true);
            checkMusic.draw(sb);
        }else {
            uncheckMusic.setFlip(false,true);
            uncheckMusic.draw(sb);
        }

    }
}
