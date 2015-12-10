package com.dasilveira.webslinger.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dasilveira.webslinger.WebSlinger;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Web Slinger v1.0";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new WebSlinger(), config);
	}
}
