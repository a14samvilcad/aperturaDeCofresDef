package m08.uf3.drops;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import m08.uf3.drops.Drops;
import m08.uf3.drops.Utils.Settings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Drops");
		config.setWindowedMode(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new Drops(), config);
	}
}

