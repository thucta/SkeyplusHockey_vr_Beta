package com.skyplus.hockey;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyplus.hockey.objects.DeviceAPI;
import com.skyplus.hockey.objects.HockeyPreferences;
import com.skyplus.hockey.state.GameStateManager;
import com.skyplus.hockey.state.MenuState;


/**
 * Created by TruongNN  on 3/24/2017.
 */

public class Hockey extends Game {

	public static  int WITDH =0;
	public static  int HEIGHT = 0;
	public static  String TITLE = "Skyplus Hockey";
	public static String PATCH="";
	public  SpriteBatch batch;
	private GameStateManager gms;
	public static boolean flagCheck = true;
    public static boolean flagCheckMusic = true;
	public static Sound sound;
	public static DeviceAPI deviceAPI;

	private HockeyPreferences pref =new HockeyPreferences();

	public Hockey(DeviceAPI deviceAPI){
		this.deviceAPI = deviceAPI;
	}
	public Hockey(){

	}


	@Override
	public void create () {
		batch = new SpriteBatch();
		gms = new GameStateManager();

		WITDH = Gdx.app.getGraphics().getWidth();
		HEIGHT = Gdx.app.getGraphics().getHeight();

		// dua vao kich thuoc mang hinh set duong dan den thu muc hinh anh phu hop
		PATCH=WITDH+"x"+HEIGHT+"/";
		Gdx.input.setCatchBackKey(true);

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Hockey.sound = Gdx.audio.newSound(Gdx.files.internal("mysound.mp3"));

		if(pref.getMusic()){
			Hockey.sound.stop();
			Hockey.sound.play();
			Hockey.sound.loop();
		}
		gms.push(new MenuState(gms));
	}


	/*
	*  vong lap cua game
	*
	* */
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gms.update(Gdx.graphics.getDeltaTime());
		gms.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}

	@Override
	public void resize(int width, int height) {
		gms.resize(width,height);
	}

	public void setPatch(){
			PATCH = WITDH+"x"+HEIGHT;
	}

	@Override
	public void pause() {
		gms.pause();
	}

	@Override
	public void resume() {
		gms.resume();
	}

	public static void scaleParticleEffect(float pScale, ParticleEffect effect){

		float scaling = effect.getEmitters().get(0).getScale().getHighMax();
		effect.getEmitters().get(0).getScale().setHigh(scaling * pScale);

		scaling = effect.getEmitters().get(0).getScale().getLowMax();
		effect.getEmitters().get(0).getScale().setLow(scaling * pScale);

		scaling = effect.getEmitters().get(0).getVelocity().getHighMax();
		effect.getEmitters().get(0).getVelocity().setHigh(scaling * pScale);

		scaling = effect.getEmitters().get(0).getVelocity().getLowMax();
		effect.getEmitters().get(0).getVelocity().setLow(scaling * pScale);
	}
}
