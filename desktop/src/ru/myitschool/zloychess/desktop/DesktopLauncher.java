package ru.myitschool.zloychess.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.myitschool.zloychess.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Main.SCR_WIDTH;
		config.height = Main.SCR_HEIGHT;
		new LwjglApplication(new Main(), config);
	}
}
