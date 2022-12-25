package ru.myitschool.sungdx;

import static ru.myitschool.sungdx.MyGdx.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.myitschool.sungdx.MyGdx;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Sun GDX");
		config.setWindowedMode(SCR_WIDTH, SCR_HEIGHT);
		new Lwjgl3Application(new MyGdx(), config);
	}
}
