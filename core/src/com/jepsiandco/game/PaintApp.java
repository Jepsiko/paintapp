package com.jepsiandco.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class PaintApp extends ApplicationAdapter {
	private Texture dropImage, bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	
	@Override
	public void create () {
	    // Load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture("droplet.png");
		bucketImage = new Texture("bucket.png");

		// Load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // Start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void dispose () {
	}
}
