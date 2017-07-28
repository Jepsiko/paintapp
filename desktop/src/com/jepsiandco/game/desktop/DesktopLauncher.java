package com.jepsiandco.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jepsiandco.game.PaintApp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "PaintApp";
		config.width = 480;
		config.height = 800;

		new LwjglApplication(new PaintApp(), config);
	}
}
