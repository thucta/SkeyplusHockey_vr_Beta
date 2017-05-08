package com.skyplus.hockey.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.utils.Array;

/**
 * Created by nguye on 05/05/2017.
 */

public class Effect {

    Array<ParticleEmitter> emitters;

    private String fxFileName;

    public Effect(String fxFileName) {
      this.fxFileName = fxFileName;
    }


    public ParticleEffect create(){
          ParticleEffect effect;
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal(fxFileName), Gdx.files.internal(""));
        // emitters = effect.getEmitters();
        effect.setPosition(-100, -100);
        effect.start();
        return effect;
    }


    public String getFxFileName() {
        return fxFileName;
    }

    public void setFxFileName(String fxFileName) {
        this.fxFileName = fxFileName;
    }

    public Array<ParticleEmitter> getEmitters() {
        return emitters;
    }

    public void setEmitters(Array<ParticleEmitter> emitters) {
        this.emitters = emitters;
    }
}
