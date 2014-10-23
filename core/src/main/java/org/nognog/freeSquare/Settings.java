package org.nognog.freeSquare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * @author goshi
 *
 */
public class Settings {

	private static final String preferenceName = "org.nognog.freeSquare"; //$NON-NLS-1$
	private static final Preferences PREFERENCES = Gdx.app.getPreferences(preferenceName);

	private static final String exampleKey = "example"; //$NON-NLS-1$

	/**
	 * @return サンプル値
	 */
	public static int getPreferenceExample() {
		return PREFERENCES.getInteger(exampleKey);
	}

	/**
	 * サンプル値を設定します。
	 * 
	 * @param value
	 */
	public static void setPreferenceExample(int value) {
		PREFERENCES.putInteger(exampleKey, value);
		PREFERENCES.flush();
	}
}
