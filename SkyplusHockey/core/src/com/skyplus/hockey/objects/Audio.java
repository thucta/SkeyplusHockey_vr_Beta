package com.skyplus.hockey.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by nguye on 08/05/2017.
 */

public class Audio {
    private Sound edgeHitSound;
    private Sound pandleHit;
    private Sound click;
    private Sound win;
    private Sound lose;
    private Sound goal;
    private HockeyPreferences pref ;

public Audio(){

    pref = new HockeyPreferences();
    if(pref.getSound()){
    edgeHitSound = Gdx.audio.newSound(Gdx.files.internal("hit_paddle1.mp3"));
    pandleHit = Gdx.audio.newSound(Gdx.files.internal("puck_hit1.mp3"));
    click = Gdx.audio.newSound(Gdx.files.internal("menu_select.mp3"));
    win = Gdx.audio.newSound(Gdx.files.internal("winner.mp3"));
    lose = Gdx.audio.newSound(Gdx.files.internal("loser.mp3"));
    goal = Gdx.audio.newSound(Gdx.files.internal("goal.mp3"));
    }else {
        edgeHitSound = Gdx.audio.newSound(Gdx.files.internal("silence.mp3"));
        pandleHit = Gdx.audio.newSound(Gdx.files.internal("silence.mp3"));
        click = Gdx.audio.newSound(Gdx.files.internal("silence.mp3"));
        win = Gdx.audio.newSound(Gdx.files.internal("silence.mp3"));
        lose = Gdx.audio.newSound(Gdx.files.internal("silence.mp3"));
        goal = Gdx.audio.newSound(Gdx.files.internal("silence.mp3"));
    }

}


    public Sound getEdgeHitSound() {
        return edgeHitSound;
    }

    public void setEdgeHitSound(Sound edgeHitSound) {
        this.edgeHitSound = edgeHitSound;
    }

    public Sound getPandleHit() {
        return pandleHit;
    }

    public void setPandleHit(Sound pandleHit) {
        this.pandleHit = pandleHit;
    }

    public Sound getClick() {
        return click;
    }

    public void setClick(Sound click) {
        this.click = click;
    }

    public Sound getWin() {
        return win;
    }

    public void setWin(Sound win) {
        this.win = win;
    }

    public Sound getLose() {
        return lose;
    }

    public void setLose(Sound lose) {
        this.lose = lose;
    }

    public Sound getGoal() {
        return goal;
    }

    public void setGoal(Sound goal) {
        this.goal = goal;
    }
}
