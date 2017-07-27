package com.jepsiandco.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PaintApp extends Game {
    SpriteBatch batch;
    BitmapFont font;

    GameScreen gameScreen;
	
	@Override
	public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
	    super.render();
	}
	
	@Override
	public void dispose () {
	    batch.dispose();
	    gameScreen.dispose();
	}
}
