package com.jepsiandco.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class PaintApp extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;

    // Assets
	private Texture dropImage, bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	// Shapes
    private Rectangle bucket;
	
	@Override
	public void create () {

        // Create the camera and batch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

	    // Load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture("droplet.png");
		bucketImage = new Texture("bucket.png");

		// Load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // Start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        // Create the bucket
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;
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
