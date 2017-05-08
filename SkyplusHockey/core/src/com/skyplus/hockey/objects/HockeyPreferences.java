package com.skyplus.hockey.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by nguye on 06/05/2017.
 */

public class HockeyPreferences {

    private Preferences preferences;

    private static final String PREF_MUSIC = "music";
    private static final String PREF_SCORE= "score";

    public HockeyPreferences() {
    }

    public Preferences getPrefs() {
        if(preferences==null){
            preferences = Gdx.app.getPreferences("hockeyGamePrefs");
        }
        return preferences;
    }


    public  void setScore(int score){
        getPrefs().putInteger(PREF_SCORE, score);
        getPrefs().flush();
    }
    public void setMusic(Boolean music){
        getPrefs().putBoolean(PREF_MUSIC, music);
        getPrefs().flush();
    }


    public boolean getMusic() {
        return getPrefs().getBoolean(PREF_MUSIC,true);
    }
    public int getScore() {
        return getPrefs().getInteger(PREF_SCORE);
    }
}
