package org.nognog.freeSquare;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;

import static org.mockito.Mockito.mock;

/**
 * @author goshi 2014/12/27
 */
@SuppressWarnings("unused")
public class GdxUseTest {
	static {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		cfg.title = "Test"; //$NON-NLS-1$
		cfg.width = 2;
		cfg.height = 2;
		LwjglApplicationConfiguration.disableAudio = true;

		new LwjglApplication(new TestApplication(), cfg);
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = Gdx.gl;
		Gdx.gl30 = mock(GL30.class);
	}

	/**
	 * Fake application listener class
	 */
	private static class TestApplication extends ApplicationAdapter {

		public TestApplication() {
			// TODO Auto-generated constructor stub
		}

	}

}
