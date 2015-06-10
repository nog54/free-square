/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog;

import java.lang.Thread.UncaughtExceptionHandler;

import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.nognog.freeSquare.persist.PersistManager;
import org.nognog.freeSquare.square2d.CombineSquare2d;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

@SuppressWarnings("all")
public class GdxTestRunner extends BlockJUnit4ClassRunner {
	
	public GdxTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	volatile boolean finished = false;

	@Override
	public void run(final RunNotifier notifier) {
		System.out.println("---------------------------------------------------");
		System.out.println(this);
		System.out.println("Starting run " + getDescription());
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1;
		config.height = 1;
		config.title = "Test";
		config.useGL30 = false;
		config.forceExit = false;
		config.disableAudio = true;

		final GdxTestRunner runner = this;
		LwjglApplication app = null;

		try {
			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(Thread t, Throwable e) {
					runner.finished = true;
					EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
					testNotifier.addFailure(e);
				}
			});

			/*
			 * Gdx.gl was not getting nulled out between tests. It may not be
			 * the direct cause, but there was no OpenGL context available after
			 * a few runs; null out Gdx.gl and it works again.
			 * 
			 * The 'correct' solution here is to use a new classloader for every
			 * test class, but this works for now.
			 */

			Gdx.gl = null;
			System.out.println("Gdx.gl: " + Gdx.gl);
			System.out.println("Thread in TestRunner: " + Thread.currentThread());
			app = new LwjglApplication(new ApplicationListener() {

				@Override
				public void resume() {
				}

				@Override
				public void resize(int width, int height) {
				}

				@Override
				public void render() {
					if (!finished) {
						System.out.println("Running " + runner.getDescription().getDisplayName());

						try {
							System.out.println("Thread in Render: " + Thread.currentThread());
							runner.actualRun(notifier);
						} catch (Throwable t) {
							System.out.println("Throwable: " + t.getMessage());
						} finally {
							System.out.println("Finally " + runner.getDescription().getDisplayName());
							runner.finished = true;
						}
					}
				}

				@Override
				public void pause() {
				}

				@Override
				public void dispose() {
					System.out.println("Disposing");
				}

				@Override
				public void create() {
					Gdx.app.getGraphics().setDisplayMode(0, 0, false);
				}
			}, config);

			while (!finished) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("Interrupted");
					e.printStackTrace();
				}
			}
		} catch (Throwable t) {
			System.out.println("b0rk");
			System.out.println(t.getMessage());
		} finally {
			if (app != null) {
				app.stop();
			}
		}

		System.out.println("Ending run " + getDescription());
	}

	public void actualRun(RunNotifier notifier) {
		super.run(notifier);
	}

}